package com.cqmaple.ai.foolrobot.common;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by Maple on 2015/4/26.
 */
public class ResponseMsg {
    public static int SUCCESS=1;
    public static int ERROR=0;
    /**
     * 消息
     */
    private String msg;
    /**
     * 0 error
     * 1 ok
     */
    private int state=1;
    /**
     * 数据
     */
    private Map<String,Object> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject jsonObject=new  JSONObject();
        jsonObject.put("msg",this.msg);
        jsonObject.put("state",this.state);
        jsonObject.put("data",this.data);
        return  jsonObject.toJSONString();
    }
}
