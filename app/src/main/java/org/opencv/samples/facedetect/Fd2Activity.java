package org.opencv.samples.facedetect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.studentmanager.R;
import com.bin.studentmanager.utils.FaceUtil;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
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

public class Fd2Activity extends Activity implements CvCameraViewListener2 {

    //按钮组件
    private static final String FACE1 = "face1";
    private static final String FACE2 = "face2";
    private static boolean isGettingFace = false;
    private double cmp;
    private Bitmap mBitmapFace1;
    private Bitmap mBitmapFace2;
    private ImageView mImageViewFace1;
    private ImageView mImageViewFace2;
    private TextView mCmpPic;
    private Button bn_get_face;

    private static final String TAG                 = "OCVSample::Activity";
    private static final Scalar FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private MenuItem mItemFace50;
    private MenuItem mItemFace40;
    private MenuItem mItemFace30;
    private MenuItem mItemFace20;
    private MenuItem mItemType;

    private Mat mRgba;
    private Mat mGray;
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
//                    System.loadLibrary("face_detect");
                    System.loadLibrary("detection_based_tracker");
//                    System.loadLibrary("opencv_core.so");
//                    System.loadLibrary("opencv_imgproc.so");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
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

    public Fd2Activity() {
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

        // 显示的View
        mImageViewFace1 = (ImageView) findViewById(R.id.face1);
        mImageViewFace2 = (ImageView) findViewById(R.id.face2);
        mCmpPic = (TextView) findViewById(R.id.text_view);
        bn_get_face = (Button) findViewById(R.id.bn_get_face);
        // 抓取一张人脸
        bn_get_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGettingFace = true;

            }
        });
        Button switch_camera = (Button) findViewById(R.id.switch_camera);
        // 切换摄像头（如果有多个）
        switch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换摄像头
                boolean isSwitched = switchCamera();
                Toast.makeText(getApplicationContext(), isSwitched ? "摄像头切换成功" : "摄像头切换失败", Toast.LENGTH_SHORT).show();
            }
        });
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

        //加载opencv
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        System.loadLibrary("opencv_java3");
//        System.loadLibrary("detection_based_tracker");


        //导入学生头像
//        Intent intent = getIntent();
//        Student student = (Student) intent.getSerializableExtra("student");
//        student.getFaceInfo();
//        Bitmap bitmap = ImageUtils.bytesToBitmap(student.getFaceInfo());
//        onPhoto(bitmap);
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

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
//            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

//        MatOfRect faces = new MatOfRect();

//        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null) {
                MatOfRect faces = new MatOfRect();

                mJavaDetector.detectMultiScale(mGray, // 要检查的灰度图像
                        faces, // 检测到的人脸
                        1.1, // 表示在前后两次相继的扫描中，搜索窗口的比例系数。默认为1.1即每次搜索窗口依次扩大10%;
                        4, // 默认是3 控制误检测，表示默认几次重叠检测到人脸，才认为人脸存在
                        4 // 返回一张最大的人脸（无效？）
                                | 2
                                | 8
                                | 1, //CV_HAAR_DO_CANNY_PRUNING ,// CV_HAAR_SCALE_IMAGE, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize),
                        new Size(mGray.width(), mGray.height()));

                final Rect[] facesArray = faces.toArray();
                for (Rect aFacesArray : facesArray) {
                    Imgproc.rectangle(mRgba, aFacesArray.tl(), aFacesArray.br(), FACE_RECT_COLOR, 3);
                    renewsUI(mRgba, aFacesArray);
                }
            }

//        Core.flip(mRgba, mRgba, 1);

        return mRgba;
    }

    public void renewsUI(Mat mat, Rect rect) {
        if (isGettingFace) {
            if (null == mBitmapFace1 || null != mBitmapFace2) {
                mBitmapFace1 = null;
                mBitmapFace2 = null;

                // 保存人脸信息并显示
                FaceUtil.saveImage(this, mat, rect, FACE1);
                mBitmapFace1 = FaceUtil.getImage(this, FACE1);
                cmp = 0.0d;
            } else {
                FaceUtil.saveImage(this, mat, rect, FACE2);
                mBitmapFace2 = FaceUtil.getImage(this, FACE2);

                // 计算相似度
//                cmp = FaceUtil.compare(this, FACE1, FACE2);
                Log.i(TAG, "onFace: 相似度 : " + cmp);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null == mBitmapFace1) {
                        mImageViewFace1.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        mImageViewFace1.setImageBitmap(mBitmapFace1);
                    }
                    if (null == mBitmapFace2) {
                        mImageViewFace2.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        mImageViewFace2.setImageBitmap(mBitmapFace2);
                    }
                    mCmpPic.setText(String.format("相似度 :  %.2f", cmp) + "%");
                }
            });

            isGettingFace = false;
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
