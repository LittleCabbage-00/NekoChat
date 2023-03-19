package com.myneko.dev_operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/*
 * 读取文件字符内容
 */
public class ReaderFileString {
	//读取账号和密码
		public static ArrayList<String>  readerFilestr(String path) throws IOException{
			BufferedReader  reader = null;
			try {
				if (new File(path).exists()) {
					ArrayList<String> userinfo=new ArrayList<>();
					  //实例化一个BufferedReader缓存对象
			    	  reader=new BufferedReader(new FileReader(path));
			    	 //读取数据
			    	  String line=null;
			    	  while((line=reader.readLine())!=null){
			    		  userinfo.add(line);
			    	  }
			    	  reader.close();
			    	  return userinfo;
				}
				
				return null;
		    	  
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				  if (reader!=null) {
					  reader.close();
					}
				WriteErrorLog.SaveError("在public ArrayList<String> readerFilestr(String path)中出现异常,异常信息:"+e.toString());
				return null;
			}
		}
}
