package com.bin.studentmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bin.studentmanager.dao.ListMsgAdapter;
import com.bin.studentmanager.utils.FileUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.samples.facedetect.DetectionBasedTracker;
import org.opencv.samples.facedetect.FaceDetect;
import org.opencv.samples.facedetect.Fd3Activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test extends AppCompatActivity {

    private Button button;
    private Button button2;
    private ImageView imageView;
    FaceDetect faceDetect;
//    private String trainingFile = getApplicationContext().getFilesDir().getPath() + "MyFaceLBPHModel.xml";
    private String trainingFile = "asd";
    private ListView listView;
    private ListMsgAdapter listMsgAdapter;
    private List<Map<String, Object>> listItems;
    private final static int REQUESTCODE = 2; // 返回的结果码
    private List<Bitmap> faceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        faceDetect = new FaceDetect(this);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        button2 = findViewById(R.id.button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                faceDetect.trainRecognizer("asd", trainingFile, 2, ';');
                Intent intent = new Intent(Test.this, Fd3Activity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
        imageView = findViewById(R.id.imageView);
        listView = (ListView) findViewById(R.id.list_message);
        listItems = getListItems();
        listMsgAdapter = new ListMsgAdapter(this, listItems);    //创建适配器
        listView.setAdapter(listMsgAdapter);

        //保存在保存csv在faces
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "faces";
        FileUtils.saveCSV(filename);
        Mat mat = new Mat();
        int i = DetectionBasedTracker.FacePredict(filename + File.separator +"at.txt", mat);
        //删除
        Log.d("asd", "识别第"+i);
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, bitmap);
        imageView.setImageBitmap(bitmap);
    }

    public void selectImage() {
        // 激活系统图库的应用 选择一张照片
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 2) {
                if (requestCode == REQUESTCODE) {
//                    faceList = (ArrayList<Bitmap>)data.getSerializableExtra("faces");
//
//                    imageView.setImageBitmap((Bitmap)faceList.get(0));
                    String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "faces";
//                    int i = DetectionBasedTracker.FacePredict(filename + File.separator +"at.txt");
//                    Log.d("asd", "识别第"+i);
                }
            } else {
                Uri uri = data.getData(); // 图片的uri路径
                Bitmap bitmap = null;
                if (uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Mat mat = faceDetect.getMat(bitmap);
                Utils.matToBitmap(mat, bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    /**
     * 初始化商品信息
     */
    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("", "");
//            map.put("image", imgeIDs[i]);                //图片资源
            map.put("title", "五月五"+i);              //物品标题
//            map.put("info", goodsNames[i]);     //物品名称
//            map.put("detail", goodsDetails[i]); //物品详情
            listItems.add(map);
        }
        return listItems;
    }
}
