 package com.whollyframework.util.client.useragent;
 
 public enum BrowserType
 {
   WEB_BROWSER("Browser"), 
 
   MOBILE_BROWSER("Browser (mobile)"), 
 
   TEXT_BROWSER("Browser (text only)"), 
 
   EMAIL_CLIENT("Email Client"), 
 
   ROBOT("Robot"), 
 
   TOOL("Downloading tool"), 
   UNKNOWN("unknown");
 
   private String name;
 
   private BrowserType(String name) { this.name = name; }
 
   public String getName()
   {
     return this.name;
   }
 }

/* Location:           E:\迅雷下载\UserAgentUtils-1.6.jar
 * Qualified Name:     nl.bitwalker.useragentutils.BrowserType
 * JD-Core Version:    0.6.0
 */