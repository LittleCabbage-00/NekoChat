package com.myneko.dev_operation;

import java.text.SimpleDateFormat;
import java.util.Date;
public class WriteErrorLog {
	    //编写错误日志
		public static void SaveError(String errorStr){
			 SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间 
		        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记  
		        Date date = new Date();// 获取当前时间 
			String error=sdf.format(date)+":"+errorStr;
			SaveFileString.SaveFileStr(error,"","SoftwareError.txt",true);
		}
}
