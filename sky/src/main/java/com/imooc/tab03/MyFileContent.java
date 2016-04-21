package com.imooc.tab03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MyFileContent {
	
	public static ArrayList<String> readFileByLines(String fileName) {
		ArrayList<String> readStringList=new ArrayList<String>();
		//String writeString = "";
		File file = new File(fileName);
		BufferedReader reader = null;
		//int line = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = "";
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				//System.out.println("line " + line + ": " + tempString);
				 //line++;
				 
				// ???
				//findLuanMa(tempString);
				readStringList.add(tempString);

			}
			//System.out.println("line:" + line);
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return readStringList;
	}

	public static void writeToFile(String filePath,String contentString){
		FileOutputStream fos = null;
		File file=new File(filePath);
		try {
			fos = new FileOutputStream(file);
			fos.write(contentString.getBytes());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				//
			}
		}
	}
}
