package com.cqmaple.ai.foolrobot.tools;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ranchaowen on 15/7/12.
 */
public class ConnectHelper {
    private static final String BDZD_URL="http://zhidao.baidu.com/search?word=";
    public static String doGet(String url) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient=HttpClients.createDefault();

        HttpGet httpGet=new HttpGet();
        httpGet.setHeader("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Accept-Charset", "gbk,utf-8;q=0.7,*;q=0.7");
        httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.77 Safari/537.1");
        httpGet.setURI(new URI(url));
        CloseableHttpResponse httpResponse=  httpClient.execute(httpGet);
        HttpEntity httpEntity= httpResponse.getEntity();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpEntity.getContent(),"GB2312"));
        StringBuffer result=new StringBuffer("");
        String line=""; //每次读取一行数据
        while((line=bufferedReader.readLine()) != null){
            result.append(line+"\n");
            //System.out.println(new String(line.getBytes(),"GBK"));
        }
        //System.out.println(result.toString());
        //输出服务器返回的内容
        return result.toString();
    }
    public static String postFY(String chinese) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient=HttpClients.createDefault();

        HttpPost httpPost=new HttpPost();
        httpPost.setHeader("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpPost.setHeader("Accept-Charset", "gbk,utf-8;q=0.7,*;q=0.7");
        httpPost.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setHeader("Host", "fanyi.baidu.com");
        httpPost.setHeader("Referer", "http://fanyi.baidu.com/");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.77 Safari/537.1");
        httpPost.setURI(new URI("http://fanyi.baidu.com/v2transapi"));
        List <NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("from", "zh"));
        params.add(new BasicNameValuePair("query", chinese));
        params.add(new BasicNameValuePair("to", "en"));
        params.add(new BasicNameValuePair("simple_means_flag", "3"));
        params.add(new BasicNameValuePair("transtype", "trans"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse httpResponse=  httpClient.execute(httpPost);
        HttpEntity httpEntity= httpResponse.getEntity();
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpEntity.getContent(),"GB2312"));
        StringBuffer result=new StringBuffer("");
        String line=""; //每次读取一行数据
        while((line=bufferedReader.readLine()) != null){
            result.append(line+"\n");
            //System.out.println(new String(line.getBytes(),"GBK"));
        }
        //System.out.println(result.toString());
        //输出服务器返回的内容
        return result.toString();
    }

    public static String BDZD(String askMsg){
        try {
            return doGet(BDZD_URL+askMsg);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<QuestionDTO> getPageQA(String html){
        List<QuestionDTO> results= new ArrayList<QuestionDTO>();
        Document doc = Jsoup.parse(html);
        Element element=doc.getElementById("wgt-list");
        Elements elements=element.getElementsByTag("dl");
        for(Element e:elements) {

            Elements es = e.getElementsByTag("dt");
            String question = es.get(0).text();

            Elements answer = e.getElementsByClass("answer");
            String answerStr = answer.get(0).text();

            Elements explain = e.getElementsByClass("explain");
            Elements spans = explain.get(0).getElementsByTag("span");
            String time = null;
            String who = null;
            String how = null;
            String moreHref = null;
            String sart = null;
            for (int i = 0; i < spans.size(); i++) {
                Element inf = spans.get(i);
                switch (i) {
                    case 0:
                        time = inf.text();
                        break;
                    case 1:
                        who = inf.text();
                        break;
                    case 2:
                        try {
                            moreHref = inf.getElementsByTag("a").get(0).attr("href");
                            how = inf.text();
                        }catch (Exception e1){

                        }
                        break;
                    case 3:
                        sart = inf.text();
                        break;
                }
            }
            QuestionDTO questionDTO = new QuestionDTO(question, answerStr, time, who, how, moreHref, sart);
            results.add(questionDTO);
        }
        return results;
    }
    public static QuestionDTO getBestAnswer(List<QuestionDTO> list){
        int i=0;
        QuestionDTO result=list.get(0);
        for (QuestionDTO questionDTO:list) {
            int q=Integer.valueOf(questionDTO.getSart());
            if(q>i){
                result=questionDTO;
                i=q;
            }
        }
        return result;
    }
    public static String chan2en(String chinese) throws IOException, URISyntaxException {
        String results= doGet("http://fanyi.baidu.com/v2transapi?from=zh&query=" + URLEncoder.encode(chinese) + "&simple_means_flag=3&to=en&transtype=trans");
        JSONObject jsonObject=JSONObject.parseObject(results);

        JSONObject dict=jsonObject.getJSONObject("trans_result");
        JSONArray fyc=dict.getJSONArray("data");
        if(fyc!=null){
            results=fyc.getJSONObject(0).getString("dst");
        }
        return  results;
    }

    public static void main(String args[]) throws URISyntaxException, IOException {
            ConnectHelper connectHelper=new ConnectHelper();

//        String html=connectHelper.BDZD("开心网");
////            for (QuestionDTO questionDTO:getPageQA(html)) {
////                System.out.println("==================");
////                System.out.println("问题:"+questionDTO.getQuestion());
////                System.out.println("==================");
////                System.out.println("答案:"+questionDTO.getAnswer());
////                System.out.println("=======END===========");
////            }
//        QuestionDTO questionDTO=getBestAnswer(getPageQA(html));
//        System.out.println("===最佳答案为:===============");
//        System.out.println("问题:"+questionDTO.getQuestion());
//        System.out.println("==================");
//        System.out.println("答案:"+questionDTO.getAnswer());
//        System.out.println("=======END===========");

//            System.out.println("==================");
//            System.out.println("问题:"+question);
//            System.out.println("==================");
//            System.out.println("答案:"+answerStr);
//            System.out.println("==================");
//            System.out.println("info: 于"+time+" 由 "+who+" 等回答 共"+how+" 点赞"+sart);
//            System.out.println("更多信息 地址:"+moreHref);
//            System.out.println("=======END===========");
//            System.out.println();
//            System.out.println();



    }

}
