package com.cqmaple.ai.foolrobot.controller;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by ranchaowen on 15-4-6.
 */
@RequestMapping("/talk")
@Controller
public class TalkController {
    @RequestMapping("/home")
    private String home(){
        return "talk/home";
    }
    @RequestMapping("/say")
    @ResponseBody
    private String say(String question){
        if(question==null||"".equals(question)){
                return "显然我不知道你在说什么";
        }
        List<Term> terms=ToAnalysis.parse(question);
        for (Term term:terms){
            System.out.println("===>"+term.toString());
        }
        return "";
    }

}
