package com.bin.studentmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CameraOpenActivity extends AppCompatActivity{

    private ImageView iv;
    private Bitmap bitmap;
    private byte[] face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_open);
        iv = (ImageView) findViewById(R.id.iv);

    }

    public void selectImage(View view) {
        // 激活系统图库的应用 选择一张照片
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData(); // 图片的uri路径
            bitmap = null;
            if (uri != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            face = os.toByteArray();
            Bitmap bmpout = BitmapFactory.decodeByteArray(face, 0, face.length);
//            Toast.makeText(getApplicationContext(), face.toString(), Toast.LENGTH_SHORT).show();
//            initValues.put(T_BLOB, os.toByteArray());//以字节形式保存
            iv.setImageBitmap(bmpout);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}