package com.imooc.tab03;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CameraFragmentMrliuTwoGood extends Fragment {
    private static final String TAG = "CameraFragmentMrliuTwo";
    public static final int RESULT_OK=-1;
    public static final String EXTRA_OK_PHOTOFILENAME_STRING = "extra_ok_photoFileName_string";

    private SurfaceView mSurfaceView;
    private View mBgFrame;
//    private ImageView mCountImageView;
    private TextView mCountText;
    private Button mPhotoButton;
    private Button mCancleButton;
    private Button mOKButton;

    private boolean mPhotoTaked;
    private String mPhotoFilePath = new String();

    private Camera mCamera;

    private Camera.PictureCallback mJpegCallBack = new Camera.PictureCallback() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void onPictureTaken(byte[] data, Camera camera) {
            Date photoDate = new Date();
            String shortName = new SimpleDateFormat("yyyyMMddHHmmss").format(photoDate) +"_"+AppConfig.NOW_LONGITUDE +"_"+AppConfig.NOW_LATITUDE+ ".jpeg";
            mPhotoFilePath = AppConfig.APP_FOLDER + shortName;
            String photoTime=new SimpleDateFormat("HH:mm:ss").format(photoDate);

            FileOutputStream fos = null;
//            File pictureFile = new File(mPhotoPathArray[mPhotoCount]);
            File pictureFile = MyFileSystem.createFileSuccessful(getActivity(), mPhotoFilePath);

            /*以下三行为侯哥的代码*/
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=1;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);


            if(bitmap.getWidth()>bitmap.getHeight()){
                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                bitmap = Bitmap.createBitmap(bitmap ,0,0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
            try{
                    fos = new FileOutputStream(pictureFile);
                } catch (FileNotFoundException e) {
                    Log.e(TAG,e.getMessage());
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }else{
                try {
                    fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                } catch (Exception e) {
                    Log.e(TAG,e.getMessage());
                } finally {
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }
            }



//            ContentValues values = new ContentValues();
//            values.put("photo" + mPhotoCount + "Time", photoTime);
//            values.put("photo" + mPhotoCount + "Path", mPhotoPathArray[mPhotoCount]);
//
//            int taskId = CurrentUser.getSingleton().getSelectedTaskId();
//
//            mDatabase.update("tasks", values, "taskId=?", new String[]{"" + taskId});
//            values.clear();
//
//            mPhotoCount++;

            mPhotoTaked=true;
            resetView();
        }

    };


    public static void compressImageFromFile(String srcPath ){



    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        mPhotoCount = getActivity().getIntent().getIntExtra(EXTRA_PHOTO_COUNT, 0);
        mPhotoTaked = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_camera_mrliu_two_good, container, false);

        mBgFrame=v.findViewById(R.id.bg_frame);
        mBgFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        Log.d("点击屏幕", "hahahah");
                    }
                });
            }
        });

        mSurfaceView = (SurfaceView) v.findViewById(R.id.surfaceview_camera);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException exception) {
                    Log.e(TAG, "设置预览失败", exception);
                }
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                if (mCamera == null) {
                    return;
                }
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(0, info);//0是默认的第一个相机

                int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 180;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 270;
                        break;
                }
                int result;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    result = (info.orientation + degrees) % 360;
                    result = (360 - result) % 360; // compensate the mirror
                } else { // back-facing
                    result = (info.orientation - degrees + 360) % 360;
                }

                Camera.Parameters parameters = mCamera.getParameters();
                Size previewSize = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                parameters.setRotation(result);
                Log.d("预览尺寸", previewSize.width + "*" + previewSize.height);

                String previewSizesString = parameters.get("preview-size-values");
                Log.d("预览全尺寸", previewSizesString);

                Size pictureSize = getMatchedSupportedSize(parameters.getSupportedPictureSizes(), previewSize);
                Log.d("拍照尺寸", pictureSize.width + "*" + pictureSize.height);
                parameters.setPictureSize(pictureSize.width, pictureSize.height);

                String pictureSizesString = parameters.get("picture-size-values");
                Log.d("拍照全尺寸", pictureSizesString);

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦

                mCamera.setParameters(parameters);
                mCamera.setDisplayOrientation(result);

                try {
                    mCamera.startPreview();
                    mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
                } catch (Exception e) {
                    Log.e(TAG, "启动预览失败", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }

        });

        mPhotoButton = (Button) v.findViewById(R.id.button_takephoto);
        mPhotoButton.setVisibility(View.VISIBLE);
        mPhotoButton.setEnabled(true);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(null, null, mJpegCallBack);
                }
            }
        });

        mCancleButton = (Button)v.findViewById(R.id.button_cancle);
        mCancleButton.setVisibility(View.INVISIBLE);
        mCancleButton.setEnabled(false);
        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPhotoCount--;
                mPhotoTaked=false;
//
//                ContentValues values = new ContentValues();
//                values.put("photo" + mPhotoCount + "Time", "");
//                values.put("photo" + mPhotoCount + "Path", "");
//                int taskId = CurrentUser.getSingleton().getSelectedTaskId();
//                mDatabase.update("tasks", values, "taskId=?", new String[]{"" + taskId});
//                values.clear();

                resetView();

                new File(mPhotoFilePath).delete();
            }
        });

        mOKButton =(Button)v.findViewById(R.id.button_ok);
        mOKButton.setVisibility(View.INVISIBLE);
        mOKButton.setEnabled(false);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {

//                ContentValues values = new ContentValues();
//                values.put("finished", 1);
//                int taskId = CurrentUser.getSingleton().getSelectedTaskId();
//                mDatabase.update("tasks", values, "taskId=?", new String[]{"" + taskId});
//                values.clear();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_OK_PHOTOFILENAME_STRING, mPhotoFilePath);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

//        mCountImageView =(ImageView)v.findViewById(R.id.count_imageview);
        mCountText=(TextView)v.findViewById(R.id.count_textView);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {//为什么要在这里增加打开指定相机。相当于初始化。
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    private Size getMatchedSupportedSize(List<Size> sizes, Size previewSize) {
        Size bestSize = sizes.get(0);
        boolean has1080=false;

        double bili=(double)previewSize.width/previewSize.height;
        for (Size s : sizes) {
//            Log.d("尺寸组合：","width"+s.width+"height"+s.height);
            if(Math.abs((double) s.width / s.height - bili)<0.005){
                int smallOne=(s.width-s.height>0)?s.height:s.width;
                if(smallOne==1080){
                    bestSize=s;
                    has1080=true;
                    break;
                }
            }
        }
        if(!has1080){
            for (Size s : sizes) {
                if(Math.abs((double) s.width / s.height - bili)<0.005){
                    int smallOne=(s.width-s.height>0)?s.height:s.width;
                    if(smallOne>900&&smallOne<1200){
                        bestSize=s;
                        break;
                    }
                }
            }
        }

        return bestSize;
    }

    private void resetView(){

        if (!mPhotoTaked){
//            mCountImageView.setImageResource(R.drawable.white1);
            mCountText.setText("");
            mPhotoButton.setEnabled(true);
            mPhotoButton.setVisibility(View.VISIBLE);
            mCancleButton.setEnabled(false);
            mCancleButton.setVisibility(View.INVISIBLE);
            mOKButton.setEnabled(false);
            mOKButton.setVisibility(View.INVISIBLE);

            try {
                mCamera.startPreview();
            } catch (Exception e) {
                Log.e(TAG, "启动预览失败", e);
                mCamera.release();
                mCamera = null;
            }
        }else {
            mCountText.setText("不满意可重新拍照");
            mPhotoButton.setEnabled(false);
            mPhotoButton.setVisibility(View.INVISIBLE);
            mCancleButton.setEnabled(true);
            mCancleButton.setVisibility(View.VISIBLE);
            mOKButton.setEnabled(true);
            mOKButton.setVisibility(View.VISIBLE);
        }
    }
}
