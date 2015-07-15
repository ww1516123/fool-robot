package com.cqmaple.ai.foolrobot.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ranchaowen on 15/7/14.
 */
public class LanguageHelper {
    /**
     * 验证所有字都是中文
     * @param str
     * @return
     */
    public static  boolean isChinese(String str){
        if(str==null||str.equals("")){
            return false;
        }
        boolean isGB2312=false;
        int count = 0;
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count==str.length();
    }
}
