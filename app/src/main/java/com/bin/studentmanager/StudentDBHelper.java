package com.bin.studentmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xiaolang on 2018/2/17.
 */

class StudentDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "StudentDBHelper";
    public static final String DB_NAME = "student_manager.db";
    public static final int VERSION = 1;    //构造方法
    public StudentDBHelper(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public StudentDBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate");
        db.execSQL("create table "
                + TableContanst.STUDENT_TABLE                 + "(_id Integer primary key AUTOINCREMENT,"
                + "name char,age integer, sex char, likes char, phone_number char,train_date date, "
                + "modify_time DATETIME, face_picture BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
