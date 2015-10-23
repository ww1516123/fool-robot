package com.cqmaple.ai.foolrobot.controller;

/**
 * Created by Maple on 2015/10/22.
 */
import java.util.ArrayList;
import java.util.List;

import com.cqmaple.ai.foolrobot.tools.RedisHelper;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;


@ContextConfiguration(locations = {"classpath*:spring-context-application.xml"})
public class RedisTest extends AbstractJUnit4SpringContextTests {
        @Autowired
        RedisHelper redisHelper;
        @Test
        public void testSet(){
            redisHelper.set("n2ame","张三");
        }
}