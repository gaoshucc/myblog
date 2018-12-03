package com.funnycode.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.funnycode.myblog.ds.LoginType;
import com.funnycode.myblog.pojo.Note;
import com.funnycode.myblog.pojo.NoteType;
import com.funnycode.myblog.pojo.User;
import com.funnycode.myblog.service.NoteService;
import com.funnycode.myblog.service.UserService;
import com.funnycode.myblog.shiro.common.UserToken;
import com.funnycode.myblog.utils.VerifyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gaoshucc
 * @create 2018-11-11 10:05
 */
@Controller
@RequestMapping(value = "/user", produces = "text/html;charset=UTF-8")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String User_LoginType = LoginType.USER.toString();

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    /**
     * 默认跳转到登录页面
     */
    @RequestMapping("/")
    public String getIndex(){
        return "redirect:loginpage";
    }

    /**
     * 判断用户是否已存在
     */
    @PostMapping("/userexists")
    public void userExists(HttpServletResponse response, String username){
        String exists = userService.userexists(username);
        try {
            response.getWriter().write(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断昵称是否已存在
     */
    @PostMapping("/nicknameexists")
    public void nicknameExists(HttpServletResponse response, String nickname){
        String exists = userService.nicknameexists(nickname);
        try {
            response.getWriter().write(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成验证码
     */
    @RequestMapping("/valicode")
    public void generateValicode(HttpServletResponse response,HttpSession session){
        logger.info("接收到更新验证码请求了");
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        session.setAttribute("valiCode",objs[0]);

        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检验验证码是否正确
     */
    @RequestMapping("/checkValicode")
    public void checkValicode(HttpServletResponse response, HttpSession session, String valicode){
        //获取之前存放在session中的验证码
        String imageCode = (String) session.getAttribute("valiCode");
        logger.info("进入验证码验证方法了");
        try {
            if (!imageCode.toLowerCase().equals(valicode.toLowerCase())) {
                response.getWriter().write("false");
                logger.info("验证码错误");
            } else {
                response.getWriter().write("true");
                logger.info("验证码" + valicode);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    /**
     * 注册
     */
    @PostMapping("/regist")
    public String regist(User user){
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        String newPassword = new SimpleHash("MD5", user.getPassword(), salt, 512).toHex();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User register = new User(user.getUsername(), newPassword, user.getNickname(), sdf.format(new Date()));
        userService.regist(register);

        return "redirect:loginpage";
    }

    @RequestMapping("/loginpage")
    public String loginpage(){
        logger.info("进入loginpage了");

        return "user/login";
    }

    @RequestMapping("/homepage")
    public String homepage(){
        return "user/home";
    }

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(Model model, String username, String password, boolean rememberMe) {
        logger.info("進入用户login方法了");
        String errorMsg = "";
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UserToken userToken = new UserToken(username, password, rememberMe, User_LoginType);
        // 执行认证登陆
        try{
            subject.login(userToken);
            PrincipalCollection principals = subject.getPrincipals();
            User user = (User) principals.getPrimaryPrincipal();
            //更新最后一次登录时间
            String lastLogintime = userService.updateUserLastLogintimeByUsername(user);
            user.setLastLogintime(lastLogintime);
            subject.getSession().setAttribute("loginUser", user.getUserId());
            logger.info(user.getUsername() + ":" + user.getNickname());

            return "redirect:homepage";
        }catch (UnknownAccountException e){
            errorMsg = "当前账号不存在";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "user/login";
        }catch (IncorrectCredentialsException e1){
            errorMsg = "当前密码不正确";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "user/login";
        }catch (Exception ex){
            errorMsg = "账号信息不正确";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "user/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        logger.info("当前用户已退出登录");

        return "redirect:loginpage";
    }

    @GetMapping("/userDetail")
    @ResponseBody
    public String getUserDetail(HttpSession session){
        logger.info("后台接收到请求了");
        String userId = (String) session.getAttribute("loginUser");
        User loginUser = userService.getUserByUserId(userId);
        String loginUserJson =  JSON.toJSONString(loginUser);

        return loginUserJson;
    }

    /**
     * 我的账号
     */
    @GetMapping("/myAccount")
    public String myAccount(){

        return "user/account";
    }

    /**
     * 优质手记
     */
    @RequestMapping("/noteArticle")
    public String noteArticle(){
        return "user/noteArticle";
    }

    @GetMapping("/findNoteCate")
    @ResponseBody
    public String findNoteCate(){
        List<NoteType> noteCate = noteService.findNoteCate();
        if(noteCate != null && noteCate.size() > 0){
            String noteCateJson = JSON.toJSONString(noteCate, SerializerFeature.DisableCircularReferenceDetect);
            return noteCateJson;
        }

        return "";
    }

    @GetMapping("/findAllNotes")
    @ResponseBody
    public String findAllNotes(){
        List<Note> noteList = noteService.findAllNotes();
        if(noteList != null && noteList.size() > 0){
            String notes = JSON.toJSONString(noteList, SerializerFeature.DisableCircularReferenceDetect);
            logger.info(notes);
            return notes;
        }

        return "";
    }

    /**
     * 书写手记
     */
    @RequestMapping("/noting")
    public String takeNote(){
        return "user/noting";
    }

    /**
     * 发布手记
     */
    @PostMapping("/publish")
    public String publish(HttpSession session, String title, Integer typeId, String markdownDoc, Model model){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userId = (String) session.getAttribute("loginUser");
        NoteType noteType = new NoteType();
        noteType.setTypeId(typeId);
        Note note = new Note(userId,title,noteType,markdownDoc);
        note.setCreateTime(sdf.format(new Date()));
        boolean publishSucc =  userService.saveNote(note,userId);

        if(publishSucc){
            return "redirect:myNote";
        }else{
            model.addAttribute("failed","发布失败");
            return "user/noteArticle";
        }
    }

    /**
     * 删除手记
     */
    @GetMapping("/deleteNote")
    @ResponseBody
    public String deleteNoteByNoteId(String noteId){
        logger.info("noteId:"+noteId);
        boolean delSuccess = noteService.deleteNoteByNoteId(noteId);

        return delSuccess?"1":"0";
    }

    @GetMapping("/recycleNote")
    @ResponseBody
    public String recycleNoteByNoteId(String noteId){
        logger.info("noteId:"+noteId);
        boolean recycleSuccess = noteService.recycleNoteByNoteId(noteId);

        return recycleSuccess?"1":"0";
    }

    @GetMapping("/completelyDelNote")
    @ResponseBody
    public String completelyDelNoteByNoteId(String noteId){
        logger.info("noteId:"+noteId);
        boolean completelyDelSuccess = noteService.completelyDelNoteByNoteId(noteId);

        return completelyDelSuccess?"1":"0";
    }

    /**
     * 跳转到“我的手记”页面
     */
    @RequestMapping("/myNote")
    public String myNote(){
        return "user/myNote";
    }

    /**
     * 查找我的手记
     *
     * @param
     * @return List<Note>的Json字符串
     */
    @RequestMapping("/findMyNotes")
    @ResponseBody
    public String findMyNotesByUserId(HttpSession session){
        String userId = (String) session.getAttribute("loginUser");
        if(userId != null){
            List<Note> noteList = noteService.findMyNotesByUserId(userId);
            if(noteList != null && noteList.size() > 0){
                String notes = JSON.toJSONString(noteList, SerializerFeature.DisableCircularReferenceDetect);
                logger.info(notes);
                return notes;
            }
        }

        return "";
    }

    /**
     * 查找“我的手记”回收站
     *
     * @param
     * @return List<Note>的Json字符串
     */
    @GetMapping("/noteRecycleBin")
    @ResponseBody
    public String findNoteRecycleBin(HttpSession session){
        String userId = (String) session.getAttribute("loginUser");
        if(userId != null){
            List<Note> noteList = noteService.findNoteRecycleBinByUserId(userId);
            if(noteList != null && noteList.size() > 0){
                String notes = JSON.toJSONString(noteList, SerializerFeature.DisableCircularReferenceDetect);
                logger.info(notes);
                return notes;
            }
        }

        return "";
    }

    /**
     * 阅读某一篇手记
     */
    @GetMapping("/note")
    public String toNote(Model model, String noteId){
        String noteHtml = noteService.findNoteHtmlByNoteId(noteId);
        model.addAttribute("noteId",noteId);
        model.addAttribute("noteHtml", noteHtml);

        return "user/note";
    }

    @GetMapping("/readNote")
    @ResponseBody
    public String readNote(String noteId){
        logger.info("noteId:"+noteId);
        Note note = noteService.findNoteByNoteId(noteId);
        String noteJson = JSON.toJSONString(note, SerializerFeature.DisableCircularReferenceDetect);

        return noteJson;
    }

    /**
     * 精品问答
     */
    @RequestMapping("/questArticle")
    public String questArticle(){
        return "user/questArticle";
    }
}
