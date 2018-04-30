package com.bin.studentmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xiaolang on 2018/3/24.
 */

public class FileUtils {
    private static final String FILE_FOLDER =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "faces" + File.separator + "data";
    private static final String FILE_CSV = "about_data.csv";

    public static void saveCSV(String filename) {
//        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "faces";
//        File file = new File(storePath);
//        File file=new File("E:\\faces\\db_PEAL");
        File file = new File(filename);
        //打开faces目录
        String faces = filename+ File.separator + "db_PEAL";
        File mFacesFile = new File(faces);
        File[] files=mFacesFile.listFiles();

//        File csv = context.getDir("csv", Context.MODE_PRIVATE);
//        File mCsvFile = new File(csv, "at.txt");
        // 首先保存图片
        String csv = filename+ File.separator+"at.txt";
        File mCsvFile = new File(csv);
        if (!mCsvFile.exists()) {
            try {
                mCsvFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
//            FileOutputStream os = new FileOutputStream(mCsvFile);
            FileOutputStream os = new FileOutputStream(mCsvFile);
            String s;
            for(File f : files) {
                s = f.getAbsolutePath()+";"+f.getName().substring(0, 4)+"\n";
                os.write(s.getBytes());
            }
            Log.d("test", os.toString());
            os.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> refreshFileList(String strPath, ArrayList<String> wechats) {
        String filename;//文件名
        String suf;//文件后缀
        File dir = new File(strPath);//文件夹dir
        File[] files = dir.listFiles();//文件夹下的所有文件或文件夹

        if (files == null)
            return null;

        for (int i = 0; i < files.length; i++) {

            if (files[i].isDirectory())
            {
                System.out.println("---" + files[i].getAbsolutePath());
                refreshFileList(files[i].getAbsolutePath(), wechats);//递归文件夹！！！

            }
            else {
                filename = files[i].getName();
                int j = filename.lastIndexOf(".");
                suf = filename.substring(j+1);//得到文件后缀


                if(suf.equalsIgnoreCase("amr"))//判断是不是msml后缀的文件
                {
                    String strFileName = files[i].getAbsolutePath().toLowerCase();

                    wechats.add(files[i].getAbsolutePath());//对于文件才把它的路径加到filelist中
                }
            }

        }
        return wechats;
    }
    static int numbers = 000000;
    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "faces//db_PEAL";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "1041"+String.format("%02d",numbers) + ".jpg"; File file = new File(appDir, fileName);
        numbers++;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            // MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            // 保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } return false;
    }


    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp, int n1, int n2) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "faces//db_PEAL";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = String.format("%04d",n1)+String.format("%02d",n2) + ".jpg"; File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            // MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            // 保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } return false;
    }

}
