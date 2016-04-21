package com.imooc.tab03;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.imooc.tab03.CameraActivity;
import com.imooc.tab03.picture.CropImageActivity;
import com.imooc.tab03.picture.UploadPicActivity;
import com.imooc.tab03.util.ImageContainer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShotFragment extends Fragment
{
	private static final String TAG = "MainActivity";
	public static final int NONE = 0;
	public static final int CAPTURE_PICTURE = 1;// 调用相机拍照
	public static final int PICK_PICTURE = 2; // 从相册中选择图片
	public static final int RESULT_PICTURE = 3;// 剪切返回结果
	private Button photoButton;
	public static final String SD_IMAGES_PATH = "/sdcard/com.future.handlepicture/images/";
	public static final String DATA_IMAGES_PATH = "/data/data/com.future.handlepicture/images/";

	private String mImageDir = null;
	private String mImageName = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView =  inflater.inflate(R.layout.tab01, container, false);
		photoButton = (Button)rootView.findViewById(R.id.btn_photo);
		photoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				capturePicture();
			}
		});
		return rootView;
	}
	private void capturePicture() {
		mImageDir = getImageDir();
		mImageName = getImageName();
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), mImageName)));
		Intent intent = new Intent(getActivity(),CameraActivity.class);
		intent.putExtra("image_dir", mImageDir);
		intent.putExtra("image_name",  mImageName);
		startActivityForResult(intent, 1);
	}
	private String getImageDir() {
		String storagePath = null;
		String sdStatus = Environment.getExternalStorageState();
		if (sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			storagePath = SD_IMAGES_PATH;
		} else {
			storagePath = DATA_IMAGES_PATH;
		}

		return storagePath;
	}

	private String getImageName() {
		return new SimpleDateFormat("yyyymmddhhMMssSSS").format(new Date()) + ".png";
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;

		switch (requestCode) {
			case CAPTURE_PICTURE:
				// 设置文件保存路径这里放在跟目录下
				//File picture = new File(Environment.getExternalStorageDirectory() + "/" + mImageName);
//			File picture = new File(mImageDir+ mImageName);
//			startPhotoZoom(Uri.fromFile(picture));
				String image_name = data.getStringExtra("image_name");
				cutPicture(image_name);
				break;
			case PICK_PICTURE:
				if (data == null) {
					return;
				}

				ContentResolver resolver = getActivity().getContentResolver();
				Bitmap bmp = null;
				try {
					Uri uri = data.getData();
					bmp = MediaStore.Images.Media.getBitmap(resolver,uri);

					if (bmp != null) {
						ImageContainer.instance().putBitmap2Container(uri.toString(), bmp);
						cutPicture(uri.toString());
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//startPhotoZoom(data.getData());
				break;
			case RESULT_PICTURE:
				String result_image_name = data.getStringExtra("result_image_name");
				uploadPic(result_image_name);
				break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	private void uploadPic(String key){
		Intent intent = new Intent(getActivity(),UploadPicActivity.class);
		intent.putExtra("upload_image_key", key);
		startActivity(intent);
	}
	private void cutPicture(String key) {
		Intent intent = new Intent(getActivity(),CropImageActivity.class);
		intent.putExtra("image_key", key);
		startActivityForResult(intent, RESULT_PICTURE);
	}
}
