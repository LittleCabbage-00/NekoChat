package com.myneko.dev_operation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/*
 *保存文件字符内容
 */
public class SaveFileString {
	   //保存文件字符内容
		public static void SaveFileStr(String str1, String str2,String path,boolean apend) {
			try {
				if (!new File(path).exists()) {
					new File(path).createNewFile();
				}
				//创建文件写入流
				FileWriter objwrite=new FileWriter(path,apend);
				if (str1.trim().isEmpty()||str2.trim().isEmpty()) {
					if (!str1.trim().isEmpty()) {
						objwrite.write(str1);	
						objwrite.write("\r\n");
					}
					if (!str2.trim().isEmpty()) {
						objwrite.write(str2);
						objwrite.write("\r\n");
					}
				}else if (!str1.trim().isEmpty()&&!str2.trim().isEmpty()) {
					objwrite.write(str1);
					objwrite.write("\r\n");
					objwrite.write(str2);
				}
				
				objwrite.close();
			} catch (IOException e) {
			   WriteErrorLog.SaveError("在public void SaveFileStr(String str1, String str2,String path,boolean apend)中出现异常,异常信息:"+e.toString());
				e.printStackTrace();
			}
		}
}
