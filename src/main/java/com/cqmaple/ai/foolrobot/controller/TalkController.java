package com.cqmaple.ai.foolrobot.controller;

import com.alibaba.fastjson.JSONArray;
import com.cqmaple.ai.foolrobot.common.DuplicateException;
import com.cqmaple.ai.foolrobot.common.ResponseMsg;
import com.cqmaple.ai.foolrobot.controller.vo.WordTypeVo;
import com.cqmaple.ai.foolrobot.dao.WordTypeDao;
import com.cqmaple.ai.foolrobot.model.WordType;
import com.cqmaple.ai.foolrobot.service.WordService;
import com.cqmaple.ai.foolrobot.service.WordTypeService;
import com.cqmaple.ai.foolrobot.tools.AutoSaveWordThread;
import com.cqmaple.ai.foolrobot.tools.ConnectHelper;
import com.cqmaple.ai.foolrobot.tools.QuestionDTO;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ranchaowen on 15-4-6.
 */
@RequestMapping("/talk")
@Controller
public class TalkController {
    @Autowired
    private WordTypeService wordTypeService;
    @Autowired
    private WordService wordService;

    @RequestMapping("/home")
    private String home(){
        return "talk/home";
    }


    @RequestMapping("/question")
    @ResponseBody
    private String question(String question){
        ResponseMsg responseMsg=new ResponseMsg();
        if(question==null||"".equals(question)){
            responseMsg.setMsg("显然我不知道你在说什么");
            responseMsg.setState(responseMsg.ERROR);
            return responseMsg.toString();
        }
        String html=ConnectHelper.BDZD(question);
        List<QuestionDTO> questionDTOs=ConnectHelper.getPageQA(html);
        QuestionDTO questionDTO=ConnectHelper.getBestAnswer(questionDTOs);
        AutoSaveWordThread wordThread=new AutoSaveWordThread(this.wordService,questionDTOs,question);
        //启动后台线程进行后台处理
        Thread thread=new Thread(wordThread);
        thread.start();

        responseMsg.setMsg(questionDTO.getAnswer());
        return responseMsg.toString();
    }

    @RequestMapping("/say")
    @ResponseBody
    private String say(String question){
        ResponseMsg responseMsg=new ResponseMsg();
        if(question==null||"".equals(question)){

            responseMsg.setMsg("显然我不知道你在说什么");
            responseMsg.setState(responseMsg.ERROR);
            return responseMsg.toString();
        }
        List<Term> terms=ToAnalysis.parse(question);
        for (Term term:terms){
            System.out.println("===>"+term.toString());
        }
        return "";
    }
    @RequestMapping("/addWordType")
    @ResponseBody
    private String addWordType(WordTypeVo wordTypeVo){
        WordType wordType=new WordType();
        ResponseMsg responseMsg=new ResponseMsg();
        try {
            BeanUtils.copyProperties(wordType,wordTypeVo);
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
            responseMsg.setMsg("已存在该类型");
            responseMsg.setState(responseMsg.ERROR);
            e.printStackTrace();
        }
        return responseMsg.toString();
    }
    @RequestMapping(value = "/findByName")
    @ResponseBody
    private String findTypeByName(String name){
       WordType wordType= wordTypeService.findByName(name);
       WordTypeVo wordTypeVo=new WordTypeVo(wordType);
        ResponseMsg responseMsg=new ResponseMsg();
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("",wordTypeVo);
        responseMsg.setData(result);
        return  responseMsg.toString();
    }
    @RequestMapping(value="/allWordType")
    @ResponseBody
    private String all(){
       List<WordType> allWordType=wordTypeService.findAll();
        JSONArray array=new JSONArray();
        array.addAll(allWordType);
        return array.toJSONString();
    }
    @RequestMapping(value="/data")
    @ResponseBody
    private String getTestJson(String callback){
        String json="{\"total\":100,\"rows\":[{\"productid\":\"FI-SW-01_1\",\"unitcost\"\n" +
                ":10.00,\"status\":\"P_1\",\"listprice\":36.50,\"attr1\":\"Large_1\",\"itemid\":\"EST-1_1\"},{\"productid\":\"FI-SW-01_2\"\n" +
                ",\"unitcost\":10.00,\"status\":\"P_2\",\"listprice\":36.50,\"attr1\":\"Large_2\",\"itemid\":\"EST-1_2\"},{\"productid\"\n" +
                ":\"FI-SW-01_3\",\"unitcost\":10.00,\"status\":\"P_3\",\"listprice\":36.50,\"attr1\":\"Large_3\",\"itemid\":\"EST-1_3\"\n" +
                "},{\"productid\":\"FI-SW-01_4\",\"unitcost\":10.00,\"status\":\"P_4\",\"listprice\":36.50,\"attr1\":\"Large_4\",\"itemid\"\n" +
                ":\"EST-1_4\"},{\"productid\":\"FI-SW-01_5\",\"unitcost\":10.00,\"status\":\"P_5\",\"listprice\":36.50,\"attr1\":\"Large_5\"\n" +
                ",\"itemid\":\"EST-1_5\"},{\"productid\":\"FI-SW-01_6\",\"unitcost\":10.00,\"status\":\"P_6\",\"listprice\":36.50,\"attr1\"\n" +
                ":\"Large_6\",\"itemid\":\"EST-1_6\"},{\"productid\":\"FI-SW-01_7\",\"unitcost\":10.00,\"status\":\"P_7\",\"listprice\"\n" +
                ":36.50,\"attr1\":\"Large_7\",\"itemid\":\"EST-1_7\"},{\"productid\":\"FI-SW-01_8\",\"unitcost\":10.00,\"status\":\"P_8\"\n" +
                ",\"listprice\":36.50,\"attr1\":\"Large_8\",\"itemid\":\"EST-1_8\"},{\"productid\":\"FI-SW-01_9\",\"unitcost\":10.00,\"status\"\n" +
                ":\"P_9\",\"listprice\":36.50,\"attr1\":\"Large_9\",\"itemid\":\"EST-1_9\"},{\"productid\":\"FI-SW-01_10\",\"unitcost\"\n" +
                ":10.00,\"status\":\"P_10\",\"listprice\":36.50,\"attr1\":\"Large_10\",\"itemid\":\"EST-1_10\"}]}";
        return callback+"("+json+")";
    }



    @RequestMapping(value="/tree")
    @ResponseBody
    private String getTree(String callback){
        String json="[\n" +
                "            { id:1,pId:0, text:\"随意勾选 1\", open:true},\n" +
                "            { id:11, pId:1, text:\"随意勾选 1-1\", open:true},\n" +
                "            { id:111, pId:11, text:\"disabled 1-1-1\"},\n" +
                "            { id:112, pId:11, text:\"随意勾选 1-1-2\"},\n" +
                "            { id:12, pId:1, text:\"disabled 1-2\",open:true},\n" +
                "            { id:121, pId:12, text:\"disabled 1-2-1\"},\n" +
                "            { id:122, pId:12, text:\"disabled 1-2-2\"},\n" +
                "            { id:2, pId:0, text:\"随意勾选 2\", open:true},\n" +
                "            { id:21, pId:2, text:\"随意勾选 2-1\"},\n" +
                "            { id:22, pId:2, text:\"随意勾选 2-2\", open:true},\n" +
                "            { id:221, pId:22, text:\"随意勾选 2-2-1\"},\n" +
                "            { id:222, pId:22, text:\"随意勾选 2-2-2\"},\n" +
                "            { id:23, pId:2, text:\"随意勾选 2-3\"}\n" +
                "        ]";
        return callback+"("+json+")";
    }

}
