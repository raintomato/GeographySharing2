package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.widget.VectorEnabledTintResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.CompressImageUtil;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/1.
 */

public class DetailsPicAdapter extends PagerAdapter {

    private Context context;
    private int lunbotuCount;//轮播图数量
    Equipment equipment;
    List<Image> images = new ArrayList<>();
    Image image[] = null;

    /**
     *
     * @param activity
     * @param equipment 传入的一个equipment对象
     */
    public DetailsPicAdapter(Activity activity, Equipment equipment) {

        this.lunbotuCount=equipment.getImageCount();//equipment对象有几张图片,作轮播图使用。
        this.equipment = equipment;
//        images.addAll(equipment.getPicture().getImage());
          images.addAll(equipment.getPicture().getSub_image());//子图
//        images.addAll(equipment.getPicture());//需要编写调用主图的函数
        context = activity;
    }

    /**
     * 从后台获取图片
     *
     * @param container
     * @param position
     * @return
     */

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_pic_item, null);
        view.setOnClickListener(new View.OnClickListener() {//给viewpager加点击的监听事件

            @Override
            public void onClick(View view) {
                smallImgClick(position);
            }
        });
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_details_image);
//        imageView.setImageResource(mImg[position]);
        Thread t=new Thread(new Runnable(){
            @Override
            public void run() {
                imageView.setImageBitmap(JsonDataUtil.getImage(images.get(position).getImageURL()));//加载 此处应加到本地 再设置
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
/*
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageView.setImageBitmap(images.get(position).getBitmapImage());//压缩一下
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                image[0] = Image.findImages(1);
                images.add(image[0]);
                if (image[0].getSub_image() != null) {
                    images.addAll(image[0].getSub_image());
                }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });*/
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 控制轮播图个数
     *
     * @return 轮播图个数
     */
    @Override
    public int getCount() {
        return lunbotuCount;
       //return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void smallImgClick(int position) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        ImageView imgView = getView(position);
        dialog.setContentView(imgView);
        dialog.show();
        // 点击图片消失
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
    }

    private ImageView getView(int position) {
        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        images.get(position).compress(Bitmap.CompressFormat.PNG, 80, baos);
        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //InputStream is = context.getResources().openRawResource(+mImg[position]);//+号处理。。。
        Drawable drawable = BitmapDrawable.createFromStream(isBm, null);
        imgView.setImageDrawable(drawable);
        return imgView;
    }
}
