package com.imooc.tab03;

import java.io.File;

/**
 * Created by hougr on 15/11/26.
 */
public class AppConfig {
    //配置，不变
    public static final String APP_FOLDER = MyFileSystem.sSDRootPath + "LaoHanSocket"+ File.separator;
    public static String NEW_FILE_PATH = "";

    public static double NOW_LONGITUDE = 0;
    public static double NOW_LATITUDE = 0;


    /* 服务器地址 */
//	public final static String SERVER_HOST_IP = "10.103.242.79";
//    public final static String SERVER_HOST_IP = "192.168.2.1";
    public final static String SERVER_HOST_IP = "10.103.242.79";

    /* 服务器端口 */
    public final static int SERVER_HOST_PORT = 9400;

}
