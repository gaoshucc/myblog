package com.funnycode.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gaoshucc
 * @create 2018-11-20 21:14
 */
@Controller
@RequestMapping(value = "/visitor", produces = "text/html;charset=UTF-8")
public class VisitorController {

    /**
     * 默认跳转到登录页面
     */
    @RequestMapping("/")
    public String getIndex(){
        return "user/login";
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET)
    @ResponseBody
    public String login() {
        return "欢迎进入，您的身份是游客";
    }

}
