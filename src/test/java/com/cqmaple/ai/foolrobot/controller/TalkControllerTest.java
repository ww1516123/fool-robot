package com.cqmaple.ai.foolrobot.controller;


import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;

import java.util.List;

/**
 * Created by ranchaowen on 15-4-6.
 */

public class TalkControllerTest {

    @Before
    public void before(){
        System.out.println("before is executed once.");
    }
    @AfterClass
    public static void test5(){
        System.out.println("test5 @AfterClass:after class is executed once.");
    }

    @BeforeClass
    public static void test6(){
        System.out.println("test6 @BeforeClass: before class is executed once.");
    }
    @Test
    public void say(){
        String str="你知道这个世界上最大的国家是?";
        List<Term> terms= ToAnalysis.parse(str);
        for (Term term:terms){
            System.out.println("===>"+term.toString());
        }
    }


}
