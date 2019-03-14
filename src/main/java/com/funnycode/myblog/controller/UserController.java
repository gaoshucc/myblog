package com.funnycode.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.funnycode.myblog.ds.LoginType;
import com.funnycode.myblog.pojo.PO.*;
import com.funnycode.myblog.pojo.VO.*;
import com.funnycode.myblog.service.NoteService;
import com.funnycode.myblog.service.QuestionService;
import com.funnycode.myblog.service.UserService;
import com.funnycode.myblog.shiro.common.UserToken;
import com.funnycode.myblog.utils.LoginUserUtil;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author gaoshucc
 * @create 2018-11-11 10:05
 */
@Controller
@RequestMapping(value = "/user", produces = "text/html;charset=UTF-8")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String User_LoginType = LoginType.USER.toString();

    /*@Value("${web.project-name}")
    public static String PRO_NAMESPACE;*/

    @Value("${web.profile-path}")
    public String PROFILE_PATH;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private QuestionService questionService;

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
            //todo 不稳定，偶尔会有异常，需要改进
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

    @GetMapping("/whetherTheLogin")
    @ResponseBody
    public String whetherTheLogin(HttpSession session){
        JSONObject jsonObject = new JSONObject();
        User loginUser = LoginUserUtil.findLoginUser(session);
        if(loginUser != null){
            jsonObject.put("loginStatus","1");
        }else{
            jsonObject.put("loginStatus","0");
        }

        return JSON.toJSONString(jsonObject);
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
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("rememberMe", rememberMe?"checked":null);
            return "user/login";
        }catch (IncorrectCredentialsException e1){
            errorMsg = "当前密码不正确";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("rememberMe", rememberMe?"checked":null);
            return "user/login";
        }catch (Exception ex){
            errorMsg = "账号信息不正确";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("rememberMe", rememberMe?"checked":null);
            return "user/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        subject.getSession().removeAttribute("loginUser");
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

    @GetMapping("/editableAccountInfo")
    @ResponseBody
    public String findEditableAccountInfo(HttpSession session){
        String userId = (String) session.getAttribute("loginUser");
        User editableUserInfo = userService.findEditableUserInfo(userId);
        List<Position> positions = userService.findAllPosition();
        EditableUserInfoVO editableUserInfoVO = new EditableUserInfoVO(editableUserInfo, positions);
        String editableUserInfoVOJson = JSON.toJSONString(editableUserInfoVO);

        return editableUserInfoVOJson;
    }
    /**
     * 更新用户信息
     */
    @PostMapping("/submitEditedAccountInfo")
    public String submitEditedAccountInfo(HttpSession session, @RequestParam(value = "editProfile",required = false) MultipartFile profile, @RequestParam("editNickname")String nickname, @RequestParam("gender")Integer gender, @RequestParam("editPosition")Integer position, @RequestParam("editMotto")String motto){
        logger.info("请求到达submitEditedAccountInfo，图片为" + profile);
        User editUser;
        String filename = "";
        String userId = LoginUserUtil.findLoginUserId(session);

        if(!profile.isEmpty()){
            //todo 修改头像名字的时候，要保证唯一性
            filename = profile.getOriginalFilename();
            File newProfile = new File(PROFILE_PATH + filename);
            if(!newProfile.getParentFile().exists()){
                newProfile.getParentFile().mkdir();
            }
            try {
                profile.transferTo(newProfile);
                editUser = new User(userId, filename, nickname, gender, position, motto);
            } catch (IOException e) {
                logger.info("图片上传出现异常");
                return "0";
            }
        }else{
            editUser = new User(userId, nickname, gender, position, motto);
        }
        boolean updateUserInfoSuccess = userService.updateUserInfo(editUser);

        return "user/account";
    }

    /**
     * 获得作者信息（包括手记、问答）
     */
    @GetMapping("/authorDetail")
    @ResponseBody
    public String findAuthorDetail(String authorId){
        User user = userService.findAuthorByAuthorId(authorId);
        Integer noteCount = noteService.findNoteCountByAuthorId(authorId);
        Integer answerCount = questionService.findAnswerCountByAuthorId(authorId);
        AuthorVO authorVO = new AuthorVO(user.getUserId(),user.getNickname(),user.getPosition().getPosition(),user.getProfilePath(),noteCount,answerCount);

        return JSON.toJSONString(authorVO, SerializerFeature.DisableCircularReferenceDetect);
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

    @GetMapping("/findNotesByTypeId")
    @ResponseBody
    public String findNotesNoteTypeId(Integer noteTypeId){
        List<Note> noteList = noteService.findNotesNoteTypeId(noteTypeId);
        if(noteList != null && noteList.size() > 0){
            String notes = JSON.toJSONString(noteList, SerializerFeature.DisableCircularReferenceDetect);
            logger.info(notes);
            return notes;
        }

        return "";
    }

    @GetMapping("/findAllNotesLimit")
    @ResponseBody
    public String findAllNotesLimit(){
        List<Note> noteList = noteService.findAllNotesLimit(0, 6);
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
     * 查找我的提问
     *
     * @param
     * @return List<Question>的Json字符串
     */
    @RequestMapping("/findMyQuestions")
    @ResponseBody
    public String findMyQuestionsByUserId(HttpSession session){
        String userId = (String) session.getAttribute("loginUser");
        if(userId != null){
            //todo  继续完成我的提问部分内容
            List<Question> questionList = questionService.findMyQuestionsByUserId(userId);
            if(questionList != null && questionList.size() > 0){
                String questions = JSON.toJSONString(questionList, SerializerFeature.DisableCircularReferenceDetect);
                logger.info(questions);
                return questions;
            }
        }

        return "";
    }

    /**
     * 查找“我的问题”回收站
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

    @GetMapping("/completelyDelQuestion")
    @ResponseBody
    public String completelyDelQuestionByQuestionId(String questionId){
        logger.info("questionId:"+questionId);
        boolean completelyDelSuccess = questionService.completelyDelQuestionByQuestionId(questionId);

        return completelyDelSuccess?"1":"0";
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

    @GetMapping("/findLikeCount")
    @ResponseBody
    public String findLikeCount(String noteId){
        JSONObject count = new JSONObject();
        Integer likeCount = noteService.findLikeCount(noteId);
        count.put("likeCount",likeCount);

        return JSON.toJSONString(count);
    }
    /**
     * 点赞
     */
    @PostMapping("/likeNote")
    @ResponseBody
    public String likeNOte(HttpSession session,String noteId){
        JSONObject success = new JSONObject();
        Boolean hasLike = userService.hasLike(LoginUserUtil.findLoginUserId(session),noteId);
        if(hasLike){
            Boolean cancelLikeSuccess = userService.cancelLikeNote(LoginUserUtil.findLoginUserId(session),noteId);
            if(cancelLikeSuccess){
                success.put("like","0");
            }else{
                success.put("like","1");
            }
            return JSON.toJSONString(success);
        }else {
            Boolean likeSuccess = userService.likeNote(LoginUserUtil.findLoginUserId(session),noteId);
            if(likeSuccess){
                success.put("like","1");
            }else{
                success.put("like","0");
            }
            return JSON.toJSONString(success);
        }
    }

    @PostMapping("/whetherLike")
    @ResponseBody
    public String whetherLike(HttpSession session,String noteId){
        JSONObject hasLike = new JSONObject();
        Boolean whetherLike = userService.hasLike(LoginUserUtil.findLoginUserId(session),noteId);
        if(whetherLike){
            hasLike.put("like","1");
        }else{
            hasLike.put("like","0");
        }
        return JSON.toJSONString(hasLike);
    }
    /**
     * 收藏
     */
    @PostMapping("/collectNote")
    @ResponseBody
    public String collectNote(HttpSession session,String noteId){
        JSONObject success = new JSONObject();
        Boolean hasCollect = userService.hasCollect(LoginUserUtil.findLoginUserId(session),noteId);
        if(hasCollect){
            Boolean cancelCollectSuccess = userService.cancelCollectNote(LoginUserUtil.findLoginUserId(session),noteId);
            if(cancelCollectSuccess){
                success.put("favorite","0");
            }else{
                success.put("favorite","1");
            }
            return JSON.toJSONString(success);
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Boolean collectSuccess = userService.collectNote(LoginUserUtil.findLoginUserId(session),noteId,sdf.format(new Date()));
            if(collectSuccess){
                success.put("favorite","1");
            }else{
                success.put("favorite","0");
            }
            return JSON.toJSONString(success);
        }
    }

    @PostMapping("/whetherCollect")
    @ResponseBody
    public String whetherCollect(HttpSession session,String noteId){
        JSONObject hasCollect = new JSONObject();
        Boolean whetherCollect = userService.hasCollect(LoginUserUtil.findLoginUserId(session),noteId);
        if(whetherCollect){
            hasCollect.put("favorite","1");
        }else{
            hasCollect.put("favorite","0");
        }
        return JSON.toJSONString(hasCollect);
    }
    /**
     * 获取收藏的文章（按时间排序前6个）
     */
    @GetMapping("/findFavoritesLimit")
    @ResponseBody
    public String findFavoritesLimit(HttpSession session){
        List<FavoriteVO> favoriteList = userService.findFavoritesLimitByUserId(LoginUserUtil.findLoginUserId(session), 0, 6);
        if(favoriteList.size() > 0){
            logger.info(JSON.toJSONString(favoriteList));
            return JSON.toJSONString(favoriteList);
        }else{
            return "";
        }
    }

    /**
     * 查找我的收藏
     */
    @GetMapping("/findFavorites")
    @ResponseBody
    public String findFavorites(HttpSession session){
        List<FavoritesDetailVO> favoritesDetailVOS = userService.findFavoritesByUserId(LoginUserUtil.findLoginUserId(session));

        return JSON.toJSONString(favoritesDetailVOS,SerializerFeature.DisableCircularReferenceDetect);
    }

    @GetMapping("/myFavoritesCount")
    @ResponseBody
    public String myFavoritesCount(HttpSession session){
        JSONObject jsonObject = new JSONObject();
        Integer count = userService.findMyFavoritesCount(LoginUserUtil.findLoginUserId(session));
        jsonObject.put("count",count);

        return JSON.toJSONString(jsonObject);
    }

    /**
     * “我的收藏”页面
     */
    @GetMapping("/myFavorites")
    public String myFavorites(){
        return "user/favorites";
    }
    /**
     * 查找评论
     */
    @GetMapping("/findComments")
    @ResponseBody
    public String findComments(String noteId){
        List<Comment> commentList = noteService.findCommends(noteId);
        if(commentList!=null && commentList.size()>0){
            String commentListJson = JSON.toJSONString(commentList, SerializerFeature.DisableCircularReferenceDetect);
            return commentListJson;
        }
        return "";
    }

    @PostMapping("/submitComment")
    @ResponseBody
    public String submitComment(HttpSession session, String noteId, String commentContent){
        logger.info("noteId:" + noteId + " | " + "commentContent:" + commentContent);
        String userId = (String) session.getAttribute("loginUser");
        Comment comment = new Comment(userId, noteId, commentContent);
        boolean saveCommentSuccess = noteService.saveComment(comment);

        return saveCommentSuccess?"true":"false";
    }
    /**
     * 提交回复
     */
    @PostMapping("/submitReply")
    @ResponseBody
    public String submitReply(HttpSession session, String noteId, String byReplyId, String commentContent){
        logger.info("noteId:" + noteId + " | " + "byReplyId:" + byReplyId + " | " + "commentContent:" + commentContent);
        String userId = (String) session.getAttribute("loginUser");
        Comment comment = new Comment(userId, noteId, byReplyId, commentContent);
        boolean saveCommentSuccess = noteService.saveComment(comment);

        return saveCommentSuccess?"true":"false";
    }

    /**
     * 精品问答
     */
    @RequestMapping("/questArticle")
    public String questArticle(){
        return "user/questArticle";
    }

    @GetMapping("/findQuestCate")
    @ResponseBody
    public String findQuestCate(){
        List<QuestType> questCate = questionService.findQuestCate();
        if(questCate != null && questCate.size() > 0){
            String questCateJson = JSON.toJSONString(questCate, SerializerFeature.DisableCircularReferenceDetect);
            return questCateJson;
        }

        return "";
    }

    /**
     * 获得所有的提问
     */
    @GetMapping("/findAllQuestions")
    @ResponseBody
    public String findAllQuestions(){
        List<Question> questList = questionService.findAllQuestions();
        if(questList != null && questList.size() > 0){
            String questions = JSON.toJSONString(questList, SerializerFeature.DisableCircularReferenceDetect);
            logger.info(questions);
            return questions;
        }

        return "";
    }

    /**
     * 获得所有的提问
     */
    @GetMapping("/findQuestionsByTypeId")
    @ResponseBody
    public String findQuestionsByTypeId(Integer questTypeId){
        List<Question> questList = questionService.findQuestionsByTypeId(questTypeId);
        if(questList != null && questList.size() > 0){
            String questions = JSON.toJSONString(questList, SerializerFeature.DisableCircularReferenceDetect);
            logger.info(questions);
            return questions;
        }

        return "";
    }

    /**
     * 获得首页显示的提问
     */
    @GetMapping("/findAllQuestionsLimit")
    @ResponseBody
    public String findAllQuestionsLimit(){
        List<Question> questList = questionService.findAllQuestionsLimit(0, 6);
        if(questList != null && questList.size() > 0){
            String questions = JSON.toJSONString(questList, SerializerFeature.DisableCircularReferenceDetect);
            logger.info(questions);
            return questions;
        }

        return "";
    }

    /**
     * 跳转到“我的手记”页面
     */
    @RequestMapping("/myQuestion")
    public String myQuestion(){
        return "user/myQuestion";
    }

    @PostMapping("/askQuestion")
    public String askQuestion(HttpSession session, Model model, String questionTitle, Integer typeId, String questMarkdownDoc){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userId = (String) session.getAttribute("loginUser");
        QuestType questType = new QuestType();
        questType.setTypeId(typeId);
        Question question = new Question(userId,questionTitle,questType,questMarkdownDoc);
        question.setCreateTime(sdf.format(new Date()));
        boolean askSuccess =  questionService.saveQuestion(question,userId);

        if(askSuccess){
            return "redirect:questArticle";
        }else{
            model.addAttribute("failed","发布失败");
            return "user/noteArticle";
        }
    }

    /**
     * 查找“我的手记”回收站
     *
     * @param
     * @return List<Note>的Json字符串
     */
    @GetMapping("/questionRecycleBin")
    @ResponseBody
    public String findQuestionRecycleBin(HttpSession session){
        String userId = (String) session.getAttribute("loginUser");
        if(userId != null){
            List<Question> questionList = questionService.findQuestionRecycleBinByUserId(userId);
            if(questionList != null && questionList.size() > 0){
                String questions = JSON.toJSONString(questionList, SerializerFeature.DisableCircularReferenceDetect);
                logger.info(questions);
                return questions;
            }
        }

        return "";
    }

    /**
     * 删除手记
     */
    @GetMapping("/deleteQuestion")
    @ResponseBody
    public String deleteQuestionByQuestionId(String questionId){
        logger.info("questionId:"+questionId);
        boolean delSuccess = questionService.deleteQuestionByQuestionId(questionId);

        return delSuccess?"1":"0";
    }

    @GetMapping("/recycleQuestion")
    @ResponseBody
    public String recycleQuestionByQuestionId(String questionId){
        logger.info("questionId:"+questionId);
        boolean recycleSuccess = questionService.recycleQuestionByQuestionId(questionId);

        return recycleSuccess?"1":"0";
    }

    /**
     * 阅读某一篇提问
     */
    @GetMapping("/question")
    public String toQuestion(Model model, String questId){
        String question = questionService.findQuestionContentByQuestId(questId);
        model.addAttribute("questId",questId);
        model.addAttribute("question", question);

        return "user/question";
    }
    /**
     * 获得提问详细信息
     */
    @GetMapping("/readQuestion")
    @ResponseBody
    public String readQuestion(String questId){
        logger.info("questId:"+questId);
        Question question = questionService.findQuestionByQuestId(questId);
        String questionJson = JSON.toJSONString(question, SerializerFeature.DisableCircularReferenceDetect);

        return questionJson;
    }
    /**
     * 回答问题
     */
    @PostMapping("/submitAnswer")
    @ResponseBody
    public String submitAnswer(HttpSession session, String questId, String answerContent){
        logger.info("questId:" + questId + " | " + "answerContent:" + answerContent);
        String userId = (String) session.getAttribute("loginUser");
        Answer answer = new Answer(userId, questId, answerContent);
        boolean saveAnswerSuccess = questionService.saveAnswer(answer);

        return saveAnswerSuccess?"true":"false";
    }
    /**
     * 查找该问题的所有回答
     */
    @GetMapping("/findAnswers")
    @ResponseBody
    public String findAnswers(String questId){
        List<Answer> answerList = questionService.findAnswers(questId);
        if(answerList!=null && answerList.size()>0){
            String answerListJson = JSON.toJSONString(answerList, SerializerFeature.DisableCircularReferenceDetect);
            return answerListJson;
        }
        return "";
    }
    /**
     * 提交问题回复
     */
    @PostMapping("/submitAnswerReply")
    @ResponseBody
    public String submitAnswerReply(HttpSession session, String questId, String byReplyId, String answerContent){
        logger.info("questId:" + questId + " | " + "byReplyId:" + byReplyId + " | " + "commentContent:" + answerContent);
        String userId = (String) session.getAttribute("loginUser");
        Answer answer = new Answer(userId, questId, byReplyId, answerContent);
        boolean saveAnswerSuccess = questionService.saveAnswer(answer);

        return saveAnswerSuccess?"true":"false";
    }

    /**
     * 是否已关注
     */
    @GetMapping("/hasAttention")
    @ResponseBody
    public String hasAttention(HttpSession session, String attentionId){
        JSONObject jsonObject = new JSONObject();
        Integer attentionStatus = 0;
        //不是用户本人时
        if(!LoginUserUtil.findLoginUserId(session).equals(attentionId)){
            attentionStatus = userService.findAttentionStatusById(LoginUserUtil.findLoginUserId(session),attentionId);
            if(attentionStatus != null){
                if(attentionStatus == 1 || attentionStatus == 3){
                    jsonObject.put("status","1");
                }else {
                    jsonObject.put("status","0");
                }
            }else{
                Integer reverseAttentionStatus = userService.findAttentionStatusById(attentionId,LoginUserUtil.findLoginUserId(session));
                if(reverseAttentionStatus == null || reverseAttentionStatus == 0 || reverseAttentionStatus == 1){
                    jsonObject.put("status","0");
                }else{
                    jsonObject.put("status","1");
                }
            }
        }

        return JSON.toJSONString(jsonObject);
    }
    /**
     * 关注ta
     */
    @RequestMapping("/payAttentionTo")
    @ResponseBody
    public String payAttentionToOther(HttpSession session, @RequestParam("attentionId")String attentionId){
        JSONObject jsonObject = new JSONObject();
        Boolean attentionSuccess = false;
        //如果关注的人是自己，则关注操作不可进行
        if(!LoginUserUtil.findLoginUserId(session).equals(attentionId)){
            attentionSuccess = userService.updateAttentionStatus(LoginUserUtil.findLoginUserId(session),attentionId);
        }
        if(attentionSuccess){
            //表示操作成功
            jsonObject.put("status","1");
        }else {
            //表示操作失败
            jsonObject.put("status","0");
        }

        return JSON.toJSONString(jsonObject);
    }

    @GetMapping("/findFolloweeList")
    @ResponseBody
    public String findFolloweeList(HttpSession session){
        Set<FolloweeVO> followee = userService.findFolloweeList(LoginUserUtil.findLoginUserId(session));

        return JSON.toJSONString(followee);
    }
}
