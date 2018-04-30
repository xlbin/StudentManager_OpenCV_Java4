package com.bin.studentmanager;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by xiaolang on 2018/2/12.
 */

public class MyViewBinder implements SimpleCursorAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        int viewID = view.getId();
        if(viewID == R.id.img) {
            ImageView contactProfile = (ImageView) view;
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(TableContanst.StudentColumns.FACE_INFO));
            if(imageBytes != null ){
                // Pic image from database
                contactProfile.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            }else {
                // If image not found in database , assign a default image
//                contactProfile.setBackgroundResource(R.drawable.bubble_a);
            }
        } else if(viewID == R.id.tv_stu_id) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getLong(cursor.getColumnIndex(TableContanst.StudentColumns.ID))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_name) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getString(cursor.getColumnIndex(TableContanst.StudentColumns.NAME))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_age) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getInt(cursor.getColumnIndex(TableContanst.StudentColumns.AGE))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_sex) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getString(cursor.getColumnIndex(TableContanst.StudentColumns.SEX))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_likes) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getString(cursor.getColumnIndex(TableContanst.StudentColumns.LIKES))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_phone) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getString(cursor.getColumnIndex(TableContanst.StudentColumns.PHONE_NUMBER))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_traindate) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getString(cursor.getColumnIndex(TableContanst.StudentColumns.TRAIN_DATE))+"";
            tv.setText(s);
        } else if(viewID == R.id.tv_stu_modifyDateTime) {
            TextView tv = (TextView) view;
            String s;
            s = cursor.getString(cursor.getColumnIndex(TableContanst.StudentColumns.MODIFY_TIME))+"";
            tv.setText(s);
        }

        return true;
    }
}
