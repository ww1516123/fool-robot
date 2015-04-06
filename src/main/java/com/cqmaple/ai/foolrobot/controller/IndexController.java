package com.cqmaple.ai.foolrobot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Maple on 2015/4/5.
 */
@RequestMapping("/index")
@Controller
public class IndexController {

    @RequestMapping("/hello")
    private String hello(){
        return  "/index/hello";
    }

}
