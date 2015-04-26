package com.cqmaple.ai.foolrobot.common;

/**
 * Created by ranchaowen on 15-4-25.
 */
public class DuplicateException extends  Exception {
    private Class dupclass;
    private String filedName;
    public DuplicateException(){

    }
    public DuplicateException(Class c,String filedName){
            this.dupclass=c;
            this.filedName=filedName;
    }

    @Override
    public String toString() {
        return "DuplicateException{" +
                "dupclass=" + dupclass +
                ", filedName='" + filedName + '\'' +
                '}';
    }
}
