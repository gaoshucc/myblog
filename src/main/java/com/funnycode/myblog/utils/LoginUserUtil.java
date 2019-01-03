package com.funnycode.myblog.utils;

import com.funnycode.myblog.pojo.PO.User;
import com.funnycode.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

/**
 * @author gaoshucc
 * @create 2018-12-16 13:40
 */
@Component
public class LoginUserUtil {
    @Autowired
    public UserService service;

    private static UserService userService;

    @PostConstruct
    private void init(){
        userService = service;
    }

    public static String findLoginUserId(HttpSession session){
        String userId = (String) session.getAttribute("loginUser");

        return userId;
    }

    public static User findLoginUser(HttpSession session){
        User loginUser = null;
        String userId;
        try {
            userId = (String) session.getAttribute("loginUser");
            System.out.println("userId:"+userId);
            if(userId != null){
                System.out.println("userId不为空");
                loginUser = userService.getUserByUserId(userId);
                System.out.println("user："+loginUser);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return loginUser;
    }
}
