package com.cqmaple.ai.foolrobot.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ranchaowen on 15/7/17.
 */
public class HtmlContents {
    private List<String> htmls;

    public HtmlContents() {
        this.htmls = new ArrayList<String>();
    }

    public synchronized List<String> getHtmls() {
        return htmls;
    }

    public synchronized List<String> getByCount(int i){
        List<String> htmlcounts = new ArrayList<String>();
        if(htmls.size()>=i){
            for (int j = 0; j < i; j++) {
                htmlcounts.add(htmls.get(j));

            }
            for (int j = 0; j <i ; j++) {
                htmls.remove(0);
            }
        }

        return  htmlcounts;
    }
}
