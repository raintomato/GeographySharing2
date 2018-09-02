package com.example.lenovo.geographysharing.Home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/12/30.
 */

public class HomePicAdapter extends PagerAdapter {

    private Context context;
    private Activity activity;
    private List <Equipment> myEquipmentList = new ArrayList<>();
    private  Bitmap tempBitmap = null;

//    private int[] mDes = new int[]{
//            R.string.a_name,
//            R.string.b_name,
//            R.string.c_name,
//            R.string.d_name,
//            R.string.e_name,
//    };
//    private Bitmap[] mImg = new Bitmap[]{
//           null
//    };



    public HomePicAdapter(Activity activity, List <Equipment> myEquipmentList) {
        context = activity;
        this.myEquipmentList = myEquipmentList;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.home_pic_item, null);
        view.setOnClickListener(new View.OnClickListener() {//轮播图的点击事件，跳转到相应的详情页面。
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        AlbumDetailActivity.launchAlbumDetailActivity((Activity)context,(Equipment) myEquipmentList.get(position));
                        break;
                }

            }
        });
        final TextView textView = (TextView) view.findViewById(R.id.tv_dec);
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        /**
         * 7-11
         */
        try {
            textView.setText(myEquipmentList.get(position).getName().toString());
            imageView.setImageResource(R.drawable.loading);
        }catch (Exception e){
            e.printStackTrace();
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageView.setImageBitmap(tempBitmap);
                container.addView(view);
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    tempBitmap = JsonDataUtil.getImage(myEquipmentList.get(position).getPicture().getImageURL());
                }catch (Exception e){
                    e.printStackTrace();
                }
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        });
        thread.start();
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return myEquipmentList.size()<5? myEquipmentList.size():5;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
