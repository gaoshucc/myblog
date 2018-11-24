package com.funnycode.myblog.controller;

import com.funnycode.myblog.ds.LoginType;
import com.funnycode.myblog.pojo.Admin;
import com.funnycode.myblog.pojo.User;
import com.funnycode.myblog.service.AdminService;
import com.funnycode.myblog.shiro.common.UserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author gaoshucc
 * @create 2018-11-20 21:14
 */
@Controller
@RequestMapping(value = "/admin",produces = "text/html;charset=UTF-8")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String Admin_LoginType = LoginType.ADMIN.toString();

    @Autowired
    private AdminService adminService;

    /**
     * 判断用户是否已存在
     */
    @PostMapping("/adminexists")
    public void userExists(HttpServletResponse response, String adminname){
        String exists = adminService.adminexists(adminname);
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
        String exists = adminService.nicknameexists(nickname);
        try {
            response.getWriter().write(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录页面
     */
    @RequestMapping("/loginpage")
    public String loginpage(){
        return "admin/login";
    }

    /**
     * 主页面
     */
    @RequestMapping("/homepage")
    public String homepage(){
        return "admin/home";
    }

    /**
     * 注册
     */
    @PostMapping("/regist")
    public String regist(Admin admin){
        logger.info("进入regist方法了");
        ByteSource salt = ByteSource.Util.bytes(admin.getAdminname());
        String newPassword = new SimpleHash("MD5", admin.getPassword(), salt, 512).toHex();
        Admin register = new Admin(admin.getAdminname(), newPassword, admin.getNickname());
        adminService.regist(register);

        return "redirect:loginpage";
    }

    /**
     * 登陆
     *
     * @param adminname 管理员账号
     * @param password 管理员密码
     */
    @PostMapping(value = "/login")
    public String login(Model model, String adminname, String password, boolean rememberMe) {
        logger.info("進入管理员login了");
        String errorMsg = "";
        Subject subject = SecurityUtils.getSubject();
        UserToken userToken = new UserToken(adminname, password, rememberMe, Admin_LoginType);
        try{
            subject.login(userToken);

            return "redirect:homepage";
        }catch (UnknownAccountException e){
            errorMsg = "当前账号不存在";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "admin/login";
        }catch (IncorrectCredentialsException e1){
            errorMsg = "当前密码不正确";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "admin/login";
        }catch (Exception ex){
            errorMsg = "账号信息不正确";
            logger.info(errorMsg);
            model.addAttribute("errorMsg", errorMsg);
            return "admin/login";
        }
    }

    /**
     * 管理员注销
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        logger.info("管理员已退出登录");

        return "redirect:loginpage";
    }
}
