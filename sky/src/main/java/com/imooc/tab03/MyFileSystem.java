package com.imooc.tab03;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class MyFileSystem {
	public static String sSDRootPath = Environment.getExternalStorageDirectory().getPath()+ File.separator;
	private static String TAG = "MyFileSystem";
	public static ArrayList<String> getFilePathList(String dirPath) {
		ArrayList<String> filePathList=new ArrayList<String>();
		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();
		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().charAt(0)=='.'){
				Log.d(TAG,"该文件是隐藏文件");
			}else if (files[i].isDirectory()) {
				ArrayList<String> subPathList=getFilePathList(files[i].getAbsolutePath());
				filePathList.addAll(subPathList);
			} else {
				String strFileName = files[i].getAbsolutePath();
				System.out.println(strFileName);
				filePathList.add(strFileName);
			}
		}
		return filePathList;

	}
	
	public static ArrayList<String> getParentMeFormatList(String filePath){
		ArrayList<String> parentMeFormatList=new ArrayList<String>();
		String parentPath = "";

		if(new File(filePath).isDirectory()){
			String[] layerArray = filePath.split("/");
			int layer = layerArray.length;
			for(int i=0;i<layer-1;i++){
				if(layerArray[i] != ""){
					parentPath += layerArray[i] + "/";
				}
			}
			parentMeFormatList.add(0, parentPath);
			parentMeFormatList.add(1, layerArray[layer - 1]);
			parentMeFormatList.add(2, "/");
		} else {
			String[] formatArray = filePath.split("\\.");
			String[] layerArray=formatArray[0].split("/");
			int layer = layerArray.length;
			for(int i=0;i<layer-1;i++){
				parentPath+=layerArray[i]+"/";
			}
			parentMeFormatList.add(parentPath);
			parentMeFormatList.add(layerArray[layer - 1]);
			parentMeFormatList.add(formatArray[1]);
		}

		return parentMeFormatList;
	}

	public static File createFileSuccessful(Context context, String filePath){
		ArrayList<String> parentMeFormatList = getParentMeFormatList(filePath);
		String dirPath = parentMeFormatList.get(0);
		File dirFile = new File(dirPath);
		if(!dirFile.exists()||!dirFile.isDirectory()){
			if(!dirFile.mkdirs()){
				DebugHelper.logD("该目录创建失败："+dirPath);
				return null;
			}
		}
		File newFile =new File(filePath);
		return newFile;
	}
}
