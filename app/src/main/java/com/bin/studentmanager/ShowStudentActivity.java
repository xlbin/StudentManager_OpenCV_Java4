package com.bin.studentmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.studentmanager.utils.ImageUtils;

/**
 * Created by xiaolang on 2018/2/20.
 */

public class ShowStudentActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info);
        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra(TableContanst.STUDENT_TABLE);
        ((TextView)findViewById(R.id.tv_info_id)).setText(student.getId()+"");
        ((TextView)findViewById(R.id.tv_info_name)).setText(student.getName());
        ((TextView)findViewById(R.id.tv_info_age)).setText(student.getAge()+"");
        ((TextView)findViewById(R.id.tv_info_sex)).setText(student.getSex());
        ((TextView)findViewById(R.id.tv_info_likes)).setText(student.getLike());
        ((TextView)findViewById(R.id.tv_info_train_date)).setText(student.getTrainDate());
        ((TextView)findViewById(R.id.tv_info_phone)).setText(student.getPhoneNumber());
        byte[] bytes = student.getFaceInfo();
        Log.e("asd", bytes.toString());
        Bitmap bitmap = ImageUtils.bytesToBitmap(bytes);
        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
    }
    public void goBack(View view) {
        finish();
    }
}