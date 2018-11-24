package com.funnycode.myblog.controller;

import com.funnycode.myblog.ds.LoginType;
import com.funnycode.myblog.pojo.User;
import com.funnycode.myblog.service.UserService;
import com.funnycode.myblog.shiro.common.UserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

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
        System.out.println("进入loginpage了");

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

}
