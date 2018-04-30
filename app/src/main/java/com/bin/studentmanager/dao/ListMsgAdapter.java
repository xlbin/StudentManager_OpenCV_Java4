package com.bin.studentmanager.dao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.studentmanager.R;

import java.util.List;
import java.util.Map;

public class ListMsgAdapter extends BaseAdapter {
    private Context context;   //运行上下文
    private List<Map<String, Object>> listItems;    //评论回复集合
    private LayoutInflater listContainer;     //视图容器

    //自定义控件集合
    public final class ListItemView {
        public ImageView img_icon;
        public TextView text_type, text_title, text_time, text_content;
        public ImageView img_right;
    }

    public ListMsgAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);    //创建视图容器并设置上下文
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_message_item布局文件的视图
            convertView = listContainer.inflate(R.layout.list_message_item, null);
            //获取控件对象
            listItemView.img_icon = (ImageView) convertView.findViewById(R.id.img_msg_icon);
            listItemView.text_type = (TextView) convertView.findViewById(R.id.text_type);
            listItemView.text_title = (TextView) convertView.findViewById(R.id.text_title);
            listItemView.text_time = (TextView) convertView.findViewById(R.id.text_time);
            listItemView.text_content = (TextView) convertView.findViewById(R.id.text_content);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        //设置文字和图片
//        listItemView.img_icon.setBackgroundResource((Integer) listItems.get(
//                position).get("image"));
        listItemView.text_title.setText((String) listItems.get(position)
                .get("title"));
//        listItemView.info.setText((String) listItems.get(position).get("info"));
        return convertView;
    }
}