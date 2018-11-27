package com.funnycode.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.funnycode.myblog.ds.LoginType;
import com.funnycode.myblog.pojo.User;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

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

    /**
     * 默认跳转到登录页面
     */
    @RequestMapping("/")
    public String getIndex(){
        return "user/login";
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
        User register = new User(user.getUsername(), newPassword, user.getNickname());
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
    public String login(HttpSession session, Model model, String username, String password, boolean rememberMe) {
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
    public String getUserDetail(String userId){
        logger.info("后台接收到" + userId +"请求了");
        User loginUser = userService.getUserByUserId(userId);
        String loginUserJson =  JSON.toJSONString(loginUser);

        return loginUserJson;
    }

    @GetMapping("/myAccount")
    public String myAccount(String id){
        logger.info(id);

        return "user/account";
    }

}
