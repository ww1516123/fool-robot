package com.cqmaple.ai.foolrobot.controller;

import com.alibaba.fastjson.JSONArray;
import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.common.ResponseMsg;
import com.cqmaple.ai.foolrobot.controller.vo.WordTypeVo;
import com.cqmaple.ai.foolrobot.dao.WordTypeDao;
import com.cqmaple.ai.foolrobot.model.WordType;
import com.cqmaple.ai.foolrobot.service.WordTypeService;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by ranchaowen on 15-4-6.
 */
@RequestMapping("/talk")
@Controller
public class TalkController {
    @Autowired
    private WordTypeService wordTypeService;

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
    @RequestMapping("/addWordType")
    @ResponseBody
    private ResponseMsg addWordType(WordTypeVo wordTypeVo){
        WordType wordType=new WordType();
        ResponseMsg responseMsg=new ResponseMsg();
        try {
            BeanUtils.copyProperties(wordTypeVo,wordType);
            wordTypeService.save(wordType);
            responseMsg.setMsg("保存成功");
        } catch (IllegalAccessException e) {
            responseMsg.setMsg(e.getMessage());
            responseMsg.setState(responseMsg.ERROR);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            responseMsg.setMsg(e.getMessage());
            responseMsg.setState(responseMsg.ERROR);
            e.printStackTrace();
        } catch (DuplicateException e){
            responseMsg.setMsg(e.getMessage());
            responseMsg.setState(responseMsg.ERROR);
            e.printStackTrace();
        }
        return responseMsg;
    }

    @RequestMapping(value="/allWordType",produces = "text/plain;charset=UTF-8")
    @ResponseBody
    private String all(){
       List<WordType> allWordType=wordTypeService.findAll();
        JSONArray array=new JSONArray();
        array.addAll(allWordType);
        return array.toJSONString();
    }

}
