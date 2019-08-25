package com.example.hsc.irunning.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.main.bean.LeftMenu;

import java.util.List;


/**
 * 左边菜单适配器
 *
 * @author Diviner
 * @date 2018-1-23 下午1:30:33
 */
public class LeftMenuAdapter extends ArrayAdapter<LeftMenu> {

    private int mResourceId;

    /**
     * 适配器的构造方法
     *
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public LeftMenuAdapter(Context context, int textViewResourceId,
                           List<LeftMenu> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftMenu li = getItem(position);// 取到当前项

        View view;// 声明一个视图存储器
        ViewHolder vh;

        if (convertView == null) {
            /*
             * 如果没有视图则加载一个视图
             */
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            vh = new ViewHolder();
            /*
             * 初始化控件
             */
            //			vh.mImageView = (ImageView) view
            //					.findViewById(R.id.id_left_item_img);
            //			vh.mTextView = (TextView) view.findViewById(R.id.id_left_item_tv);

            view.setTag(vh);
        } else {
            /*
             * 如果用视图的时候
             */
            view = convertView;// 直接把当前视图赋值给View
            vh = (ViewHolder) view.getTag();
        }
        vh.mImageView.setImageResource(li.getmImgId());
        vh.mTextView.setText(li.getmName());

        return view;
    }

    /**
     * 组装数据
     *
     * @author Diviner
     * @date 2018-1-23 下午1:30:46
     */
    class ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
    }
}
