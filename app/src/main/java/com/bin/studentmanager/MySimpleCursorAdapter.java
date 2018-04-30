package com.bin.studentmanager;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;

/**
 * Created by xiaolang on 2018/2/11.
 */

class MySimpleCursorAdapter extends SimpleCursorAdapter {
    private Cursor _cursor;
    private Context _context;
    public MySimpleCursorAdapter(Context context, int layout, Cursor c,
                                 String[] from, int[] to) {
        super(context, layout, c, from, to);
        _cursor = c;
        _context = context;
    }
    public void bindView(View view, Context context, Cursor cursor) {
//        ImageView imageView = (ImageView) view.findViewById(R.id.img);
//        Log.e("asd",  "MySimpleCursorAdapter"+_cursor.getInt(cursor.getColumnIndex(TableContanst.StudentColumns.AGE)));
//        ByteArrayInputStream stream = new ByteArrayInputStream(
//                _cursor.getBlob(cursor.getColumnIndex(TableContanst.StudentColumns.FACE_INFO)));
//        imageView.setImageDrawable(Drawable.createFromStream(stream, "img"));
        super.bindView(view, context, cursor);
    }
}