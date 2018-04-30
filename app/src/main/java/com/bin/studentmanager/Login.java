package com.bin.studentmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.samples.facedetect.Fd2Activity;
import org.opencv.samples.facedetect.FdActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaolang on 2018/1/31.
 */

public class Login extends AppCompatActivity {
    private EditText mUser; // 帐号编辑框
    private EditText mPassword; // 密码编辑框
    private Cursor cursor;
    private StudentDao dao;
    Student student;    //存储学生个人信息

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.student_login_layout);

        mUser = findViewById(R.id.login_user_edit);
        mPassword = findViewById(R.id.login_passwd_edit);
    }

    //主界面
    public void login_mainweixin(View v) {

//        测试用
        if (true) {
            Intent intent = new Intent();
            intent.setClass(Login.this, Test.class);
//            intent.putExtra("student", student);
            startActivity(intent);
            this.finish();
            return;
        }


        if ("m".equals(mUser.getText().toString()) && "123".equals(mPassword.getText().toString())) {
            //管理员登陆
            Intent intent = new Intent();
            intent.setClass(Login.this, ManagerActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
            this.finish();
        } else if("test".equals(mUser.getText().toString())) {
            Intent intent = new Intent();
            intent.setClass(Login.this, Fd2Activity.class);
            startActivity(intent);
            this.finish();
        } else if(judgeStudent()) {
            //学生登陆
            Intent intent = new Intent();
            intent.putExtra("student", student);
            intent.setClass(this, FdActivity.class);
            this.startActivity(intent);
            this.finish();
        } else if ("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString())) {
            //账号或密码为空
            new AlertDialog.Builder(Login.this)
//                    .setIcon(getResources().getDrawable(R.drawable.login_error_icon))//过时
                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.login_error_icon, null))
                    .setTitle("登录错误")
                    .setMessage("帐号或者密码不能为空，\n请输入后再登录！")
                    .create()//为什么不使用create()函数也可以弹出对话框？
                    .show();

        } else {
            new AlertDialog.Builder(Login.this)
                    .setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.login_error_icon, null))
                    .setTitle("登录错误")
                    .setMessage("帐号或者密码不正确，\n请检查后重新输入！")
                    .create()
                    .show();
        }

        //登录按钮
    	/*
      	Intent intent = new Intent();
		intent.setClass(Login.this,Whatsnew.class);
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
		this.finish();*/
    }

    //返回
    public void login_back(View v) {     //标题栏 返回按钮
        this.finish();
    }

    //忘记密码
    public void login_pw(View v) {
        Uri uri = Uri.parse("http://3g.qq.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        //Intent intent = new Intent();
        //intent.setClass(Login.this,Whatsnew.class);
        //this.finish();
    }

    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void load() {

        StudentDBHelper studentDBHelper = new StudentDBHelper(
                Login.this);
        SQLiteDatabase database = studentDBHelper.getReadableDatabase();
        dao = new StudentDao(new StudentDBHelper(this));
        cursor = database.query(TableContanst.STUDENT_TABLE, null, null, null,
                null, null, TableContanst.StudentColumns.MODIFY_TIME + " desc");
//        startManagingCursor(cursor);  //使用会报错
    }

    public boolean judgeStudent() {
        //获取学生账号信息（包括ID和密码）并判断是否学生
        boolean isStudent = false;  //判断是否学生
        load();
        List<Map<String, Object>> list = dao.getAllStudents();
        Long[] IDS = new Long[list.size()];
        Long ID;
        int no = 0;
        for(Map<String, Object> map : list) {
            ID = (Long) map.get(TableContanst.StudentColumns.ID);
            Log.e("asd", ID.toString() + " " +mUser.getText().toString().trim());
            //判断是否为该id的学生
            try {
                if (ID == Long.parseLong(mUser.getText().toString().trim())) {
                    isStudent = true;
//                    String name = (String)map.get(TableContanst.StudentColumns.NAME);
                    String name = (String)map.get(TableContanst.StudentColumns.NAME);
                    int age = (int)map.get(TableContanst.StudentColumns.AGE);
                    String sex = (String)map.get(TableContanst.StudentColumns.SEX);
                    String likes = (String)map.get(TableContanst.StudentColumns.LIKES);
                    String phone_number = (String)map.get(TableContanst.StudentColumns.PHONE_NUMBER);
                    String train_date = (String)map.get(TableContanst.StudentColumns.TRAIN_DATE);
                    String modify_time = (String)map.get(TableContanst.StudentColumns.MODIFY_TIME);
                    byte[] face = (byte[])map.get(TableContanst.StudentColumns.FACE_INFO);
                    student = new Student(ID, name, age, sex, likes, phone_number, train_date, modify_time, face);
                    Log.e("asd",","+name+","+sex+","+face.toString());
                    break;
                } else {
                    isStudent = false;
                }
            } catch (Exception e) {
                Log.e("asd", "Exception: "+e.toString());
            }
        }
        cursor.close();
        dao.closeDB();
        return isStudent;
    }

}