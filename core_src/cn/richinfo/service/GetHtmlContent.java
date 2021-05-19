package cn.richinfo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GetHtmlContent {
	/**
	    * 读取一个网页全部内容
	    */
	    public static String getOneHtml(final String htmlurl) throws IOException
	    {
	    URL url;
	    String temp;
	    final StringBuffer sb = new StringBuffer();
	    try
	    {
	       url = new URL(htmlurl);
	       String urlEncode="GB2312";
	       if(url.toString().indexOf("prize")>=0)
	    	   urlEncode = "GB2312";
	       final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), urlEncode));// 读取网页全部内容
	       
	       while ((temp = in.readLine()) != null)
	       {
	        sb.append(temp);
	       }
	       in.close();
	    }
	    catch (final MalformedURLException me)
	    {
	       System.out.println("你输入的URL格式有问题！请重新输入");
	       me.getMessage();
	       throw me;
	    }
	    catch (final IOException e)
	    {
	       e.printStackTrace();
	       throw e;
	    }
	    return sb.toString();
	    }
}
