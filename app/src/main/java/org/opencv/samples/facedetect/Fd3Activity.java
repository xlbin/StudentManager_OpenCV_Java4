package org.opencv.samples.facedetect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bin.studentmanager.R;
import com.bin.studentmanager.utils.FileUtils;
import com.bin.studentmanager.utils.ImageUtils;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.NORM_MINMAX;
import static org.opencv.core.CvType.CV_8UC1;

public class Fd3Activity extends Activity implements CvCameraViewListener2 {
    List<Bitmap> faceList = new ArrayList<>();

    //人数目
    static int num1 = 0001;
    //同一个人的人脸数
    static int num2 = 01;
    private Thread newThread; //声明一个子线程
    long time=0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 1:
                    Bundle b = msg.getData();
                    b.getLong("time");
                    if(time%10 == 0) {
                        byte[] bytes = b.getByteArray("bytes");
                        Bitmap bitmap = ImageUtils.bytesToBitmap(bytes);
                        Mat mat = new Mat();
                        Utils.bitmapToMat(bitmap, mat);
                        Rect[] r = (Rect[]) b.getSerializable("faces");
                        saveImage(r, mat);
                    }
                    break;
            }
        }
    };

    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private MenuItem               mItemFace50;
    private MenuItem               mItemFace40;
    private MenuItem               mItemFace30;
    private MenuItem               mItemFace20;
    private MenuItem               mItemType;

    private Mat                    mRgba;
    private Mat                    mGray;
    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 1;

    private CameraBridgeViewBase   mOpenCvCameraView;

    private boolean isGettingFace = true;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_profileface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Fd3Activity() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.face_detect_surface_view);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        // 抓取一张人脸
        Button bn_get_face = (Button) findViewById(R.id.bn_get_face);
        // 抓取一张人脸
        bn_get_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isGettingFace) {
//                    isGettingFace = false;
//                } else {
//                    isGettingFace = true;
//                }
                num1++;
                num2=1;
            }
        });
        Button complete = findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("faces", (Serializable)faceList); //将计算的值回传回去
                setResult(2, intent);
                finish();
            }
        });
        Button switch_camera = (Button) findViewById(R.id.switch_camera);
        // 切换摄像头（如果有多个）
        switch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换摄像头
                boolean isSwitched = switchCamera();
                convert = (convert+1)%2;
                Toast.makeText(getApplicationContext(), isSwitched ? "摄像头切换成功" : "摄像头切换失败", Toast.LENGTH_SHORT).show();
            }
        });

//        newThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //这里写入子线程需要做的工作
//                while(true) {
//                    Log.d("asd", "asd");
//                }
//            }
//        });
//        newThread.start(); //启动线程
    }


    // 记录切换摄像头点击次数
    private int mCameraSwitchCount = 0;
    /**
     * 切换摄像头
     *
     * @return 切换摄像头是否成功
     */
    public boolean switchCamera() {
        // 摄像头总数
        int numberOfCameras = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            numberOfCameras = Camera.getNumberOfCameras();
        }
        // 2个及以上摄像头
        if (1 < numberOfCameras) {
            // 设备没有摄像头
            int index = ++mCameraSwitchCount % numberOfCameras;
            mOpenCvCameraView.disableView();
            mOpenCvCameraView.setCameraIndex(index);
            mOpenCvCameraView.enableView();
            return true;
        }
        return false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
//        System.loadLibrary("opencv_java3");
        setDetectorType(NATIVE_DETECTOR);
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    static int convert = 0;
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        Imgproc.equalizeHist(mGray, mGray);
        Core.normalize(mGray, mGray, 0, 255, NORM_MINMAX, CV_8UC1);

//        Imgproc.threshold(mGray, mGray, 123, 255, Imgproc.THRESH_TRUNC);


        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null)
                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize*1.2), new Size());
        }
        else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null)
                mNativeDetector.detect(mGray, faces);
        }
        else {
            Log.e(TAG, "Detection method is not selected!");
        }

        time = System.currentTimeMillis()/100;
        Rect[] facesArray = faces.toArray();
        if(facesArray.length > 0) {
            if(time%10 == 0) {
                saveImage(facesArray, mGray);
                num2++;
            }
//        saveImage(facesArray);
//            Message message = new Message();
//            Bundle b = new Bundle();
////            time = System.currentTimeMillis();
////            Log.d("asd", ""+time);
//            Bitmap bitmap = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(mRgba,bitmap);
//            byte[] bytes = ImageUtils.bitmapToByte(bitmap);
//            b.putSerializable("faces", (Serializable) facesArray);
//            b.putByteArray("bytes", bytes);
//            b.putLong("time", time);
//            message.setData(b);
//            message.what = 1;
//            handler.sendMessage(message);
        }
        for (int i = 0; i < facesArray.length; i++) {
//            Point center = new Point((facesArray[i].br().x + facesArray[i].tl().x)/2, (facesArray[i].tl().y + facesArray[i].br().y)/2);
//            double []tl = {2*facesArray[i].tl().x - center.x, 2*facesArray[i].tl().y - center.y};
//            facesArray[i].br().x = 2*facesArray[i].br().x - center.x;
//            facesArray[i].br().y = 2*facesArray[i].br().y - center.y;
//            double []br = {2*facesArray[i].br().x - center.x, 2*facesArray[i].br().y - center.y};
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
        }
//        isGettingFace = false;
//        Core.flip(mRgba, mRgba, 1);
        return mRgba;
    }

    private void saveImage(Rect[] facesArray, Mat mat) {
        for (int i = 0; i < facesArray.length; i++) {
            if (isGettingFace) {
                // 把检测到的人脸重新定义大小后保存成文件
                Mat sub = mat.submat(facesArray[i]);
                Imgproc.resize(sub, sub, new Size(100, 100));
                Bitmap bitmap = Bitmap.createBitmap(sub.cols(), sub.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(sub, bitmap);
                faceList.add(bitmap);
                FileUtils.saveImageToGallery(this, bitmap, num1+i*100, num2);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemFace50 = menu.add("Face size 50%");
        mItemFace40 = menu.add("Face size 40%");
        mItemFace30 = menu.add("Face size 30%");
        mItemFace20 = menu.add("Face size 20%");
        mItemType   = menu.add(mDetectorName[mDetectorType]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemFace50)
            setMinFaceSize(0.5f);
        else if (item == mItemFace40)
            setMinFaceSize(0.4f);
        else if (item == mItemFace30)
            setMinFaceSize(0.3f);
        else if (item == mItemFace20)
            setMinFaceSize(0.2f);
        else if (item == mItemType) {
            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[tmpDetectorType]);
            setDetectorType(tmpDetectorType);
        }
        return true;
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }



}
