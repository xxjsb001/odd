package com.vtradex.wms.server.test;

import java.util.regex.*;
//以“com/7655/zh-cn/preview/”为开始、以“preview”为结束的字符串（如果查到多个搜索结果,则以最后一个为准）
//要返回的字符串是有着特定长度的,永远都是跟“com/7655/zh-cn/preview/49785790f0aba048e55a0289f377edb4/preview”
//和“com/7655/zh-cn/preview/fce7a93a77f42dd754dcf9d81f115cb8/preview”有着相同的长度
public class RegexTest {
  private static String ONE = "49785790f0aba048e55a0289f377edb4";
  private static String PATTERN = "(com/7655/zh-cn/preview/)(\\w{" + ONE.length() + "})(/preview)";
  
  public static void main(String [] args) {
      String dist = null;
      Pattern p = Pattern.compile(RegexTest.PATTERN);
      String input = 
      "com/7655/zh-cn/preview/11111111111/preview"  
    		  +"com/7655/zh-cn/preview/49785790f0aba048e55a0289f377edb4/preview"
    		  +"com/7655/zh-cn/preview/22222222222222222222222222222222/preview"  
    		  ;
      Matcher m = p.matcher(input);
      while(m.find()) {
          String g = m.group();
         // System.out.println(g);
          dist = g;
      }
      System.out.println(dist);
  }
}
