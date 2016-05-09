package com.imooc.tab03;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.imooc.tab03.base.BaseActivity;

public class MainActivity extends BaseActivity implements OnClickListener
{
	private static final String TAG = "MainActivity";
	private Button photoButton;

	private LocationClient mLocationClient;
	private double mLatitude;
	private double mLongitude;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		initEvents();
	}
	@Override
	public void initViews()
	{
		photoButton = (Button)findViewById(R.id.btn_photo);
		mLocationClient = new LocationClient(this);
	}
	@Override
	protected void initEvents()
	{
		photoButton.setOnClickListener(this);
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
	}



	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_photo:
				Intent intent = new Intent(MainActivity.this,CameraActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//关闭所有Activity
		ActivityCollector.finishAll();
	}
}
