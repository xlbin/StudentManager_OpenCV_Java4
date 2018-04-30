package com.bin.studentmanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bin.studentmanager.utils.ImageUtils;

import java.util.List;

public class ShowStudnetActivity2 extends AppCompatActivity {

    private static final String TAG = "TestSQLite";
    private Button addStudent;
    private Cursor cursor;
    private MySimpleCursorAdapter adapter;
    private ListView listView;
    private List<Long> list;
    private RelativeLayout relativeLayout;
    private Button searchButton;
    private Button selectButton;
    private Button deleteButton;
    private Button selectAllButton;
    private Button cancleButton;
    private LinearLayout layout;
    private StudentDao dao;
    private Student student;
    private Boolean isDeleteList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info_own);
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

    public void edit(View view) {
        Intent intent = getIntent();
        student = (Student)getIntent().getSerializableExtra("student");
        Intent intent2 = new Intent(ShowStudnetActivity2.this,AddStudentActivity.class);
        intent.putExtra("student", student);
        startActivityForResult(intent2, 0);
        ShowStudnetActivity2.this.finish();
    }

    @Override
    public void onBackPressed() {
        ExitDialog dialog=new ExitDialog(ShowStudnetActivity2.this);
        dialog.show();
    }
}
