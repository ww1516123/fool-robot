package com.cqmaple.ai.foolrobot.controller;

import com.alibaba.fastjson.JSONArray;
import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.common.ResponseMsg;
import com.cqmaple.ai.foolrobot.controller.vo.WordTypeVo;
import com.cqmaple.ai.foolrobot.controller.vo.WordVo;
import com.cqmaple.ai.foolrobot.model.WordType;
import com.cqmaple.ai.foolrobot.model.Words;
import com.cqmaple.ai.foolrobot.service.WordService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by ranchaowen on 15/7/12.
 */
@RequestMapping("/word")
@Controller
public class WordController {
    @Autowired
    private WordService wordService;

    @RequestMapping("/")
    public String home(){
        return "word/list";
    }

    @RequestMapping("/add")
    @ResponseBody
    private String addWordType(WordVo wordVo){
        Words words=new Words();
        ResponseMsg responseMsg=new ResponseMsg();
        try {
            BeanUtils.copyProperties(words, wordVo);
            wordService.save(words);
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
            responseMsg.setMsg("已存在该词语");
            responseMsg.setState(responseMsg.ERROR);
            e.printStackTrace();
        }
        return responseMsg.toString();
    }
    @RequestMapping(value="/all")
    @ResponseBody
    private String all(int page,int size){
        PageRequest request=new PageRequest(page,size);
        Page<Words> alls=wordService.findAll(request);
        JSONArray array=new JSONArray();
        array.addAll(alls.getContent());
        return "{\"total\":"+alls.getTotalPages()+",\"data\":"+array.toJSONString()+"}";
    }
}
