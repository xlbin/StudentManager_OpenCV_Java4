package com.bin.studentmanager.Menbers;

import android.os.Environment;

import java.io.File;

/**
 * Created by bin
 */

public class FileConstants {
    //主要人脸数据存放目录
    public static final String MainFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "faces";
    //训练的人脸库目录
    public static final String DB_PEAL_FOLDER = "db_PEAL";
    //csv文件名字
    public static final String CSV_NAME = "at.txt";
    //训练好的模型文件名
    public static String LBPH_NAME = "MyFaceLBPHModel.xml";
    public static String EIGEN_NAME = "MyFacePCAModel.xml";
    public static String FISHER_NAME = "MyFaceFisherModel.xml";

}
