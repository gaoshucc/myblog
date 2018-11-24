package com.funnycode.myblog.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常类
 *
 * @author gaoshucc
 * @create 2018-11-20 21:35
 */
@ControllerAdvice
public class GlobalExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(AuthorizationException.class)
    public String authorizationExceptionHandler(Exception e){
        logger.info("进入异常控制器了");

        return "403";
    }
}
