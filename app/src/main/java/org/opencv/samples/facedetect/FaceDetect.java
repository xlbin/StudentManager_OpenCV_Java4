package org.opencv.samples.facedetect;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bin.studentmanager.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
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

/**
 * Created by xiaolang on 2018/3/8.
 */

public class FaceDetect {

    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    private static final int        JAVA_DETECTOR       = 0;
    private static final int        NATIVE_DETECTOR     = 1;
    private final Context context;
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;
    private BaseLoaderCallback mLoaderCallback;

    private Context getContext() {
        return context;
    }

    public FaceDetect(Context context) {
        Log.i(TAG, "Instantiated new " + this.getClass());
        this.context = context;
        mLoaderCallback = new BaseLoaderCallback(context) {
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
                            InputStream is = getContext().getResources().openRawResource(R.raw.lbpcascade_frontalface);
                            File cascadeDir = getContext().getDir("cascade", Context.MODE_PRIVATE);
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

                    } break;
                    default:
                    {
                        super.onManagerConnected(status);
                    } break;
                }
            }
        };
        loadOpencv();
        setDetectorType(JAVA_DETECTOR);
    }
    //照片人脸检查
    public Mat getMat(Bitmap bitmap) {
        Mat pRgba = new Mat();
        Mat pGray = new Mat();
        Utils.bitmapToMat(bitmap, pRgba);
        Imgproc.cvtColor(pRgba, pGray, Imgproc.COLOR_BGR2GRAY);
        if (mAbsoluteFaceSize == 0) {
            int height = pGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null)
                mJavaDetector.detectMultiScale(pGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null)
                mNativeDetector.detect(pGray, faces);
        }
        else {
            Log.e(TAG, "Detection method is not selected!");
        }

        Rect[] facesArray = faces.toArray();
        Log.d(TAG, "Get "+facesArray.length+" faces");
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(pRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);

        return pRgba;
    }
    private void loadOpencv() {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
//        System.load("opencv_java3");
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

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    /**
     * train LBPHFaceRecognizer
     *
     * @param csvFile
     * @param separator
     */
    public void trainRecognizer(String csvFile, String trainFile, int trainingType, char separator) {
        jniRecognizerTraining(csvFile, trainFile, trainingType, separator);

    }

    /**
     *
     * @param csvFile the csvFile which will be read
     * @param trainFile the training file as yaml or xml file whose path is path of trainFile
     */
    private native void jniRecognizerTraining(String csvFile, String trainFile, int trainingType, char separator);
}