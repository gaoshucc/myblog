package com.funnycode.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公用的Controller
 *
 * @author gaoshucc
 * @create 2018-11-20 22:52
 */
@Controller
@RequestMapping(produces = "text/html;charset=UTF-8")
public class BasicController {

    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);
    /**
     * 默认跳转到登录页面
     */
    @RequestMapping("/")
    public String getIndex(){
        return "user/login";
    }

    @RequestMapping("/unauth")
    public String unauth(){
        logger.info("权限不足");

        return "403";
    }

    @RequestMapping("/resource")
    public String resource(){
        logger.info("进入resource页面了");

        return "user/resource";
    }
}
