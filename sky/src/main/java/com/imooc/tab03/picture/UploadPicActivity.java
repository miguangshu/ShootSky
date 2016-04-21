/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imooc.tab03.picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.imooc.tab03.R;
import com.imooc.tab03.util.ImageContainer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// ----------------------------------------------------------------------

public class UploadPicActivity extends Activity implements View.OnClickListener{
	private ImageView uploadPic;
	private Bitmap mBitmap;
	private String image_key;
	private static String TAG = "UploadPicActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_upload);
		image_key = this.getIntent().getStringExtra("upload_image_key");
		uploadPic = (ImageView)findViewById(R.id.id_upload_pic);
		mBitmap = ImageContainer.instance().getBitmapFromContainer(image_key);
		uploadPic.setImageBitmap(mBitmap);
	}
 	@Override
	public  void onClick(View v){

	}

}





