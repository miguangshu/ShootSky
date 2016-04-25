package com.imooc.tab03;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.imooc.tab03.CameraActivity;
import com.imooc.tab03.network.FdfTransfer;
import com.imooc.tab03.network.FileDataFrame;
import com.imooc.tab03.picture.CropImageActivity;
import com.imooc.tab03.picture.UploadPicActivity;
import com.imooc.tab03.util.BasicDataTypeTransfer;
import com.imooc.tab03.util.ImageContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ShotFragment extends Fragment
{
	private static final String TAG = "MainActivity";
	private Button photoButton;

	private LocationClient mLocationClient;
	private double mLatitude;
	private double mLongitude;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView =  inflater.inflate(R.layout.tab01, container, false);
		photoButton = (Button)rootView.findViewById(R.id.btn_photo);
		photoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),CameraActivity.class);
				startActivity(intent);
			}
		});

		mLocationClient = new LocationClient(getActivity().getApplicationContext());
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				// map view 销毁后不在处理新接收的位置
				if (bdLocation == null)
					return;

				mLatitude = bdLocation.getLatitude();
				mLongitude = bdLocation.getLongitude();
				AppConfig.NOW_LONGITUDE = String.valueOf(mLongitude);
				AppConfig.NOW_LATITUDE = String.valueOf(mLatitude);


			}
		});
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//混合模式
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//仅GPS
//        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//仅基站
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1200);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		return rootView;
	}
}
