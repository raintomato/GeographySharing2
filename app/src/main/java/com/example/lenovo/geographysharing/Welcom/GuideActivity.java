package com.example.lenovo.geographysharing.Welcom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * app引导页
 * 安装后的第一次打开出现三个滑动引导页
 */

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener{

    private List<View> mViewList;
    private ViewPager mViewPager;
    //private ImageView[] mDotList;
    //private int mLastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Log.i("ssssss",LoginUserRegisterUtil.TOKEN);
        initView();
        initViewPager();
        initDots();
    }

    private void initView () {
        Log.i("GuideActivity", LoginUserRegisterUtil.TOKEN);
        LayoutInflater inflater = LayoutInflater.from(this);
        mViewList = new ArrayList<>();
        mViewList.add(inflater.inflate(R.layout.guide_one_layout, null));
        mViewList.add(inflater.inflate(R.layout.guide_two_layout, null));
        mViewList.add(inflater.inflate(R.layout.guide_three_layout, null));
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MyPagerAdapter adapter = new MyPagerAdapter(mViewList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initDots() {
//        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.ll_dots_layout);
//        mDotList = new ImageView[mViewList.size()];
//        for (int i = 0; i < mViewList.size(); i++) {
//            mDotList[i] = (ImageView) dotsLayout.getChildAt(i);
//            mDotList[i].setEnabled(false);
//        }
//        mLastPosition = 0;
//        mDotList[0].setEnabled(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDotPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setCurrentDotPosition(int position) {
//        mDotList[position].setEnabled(true);
//        mDotList[mLastPosition].setEnabled(false);
//        mLastPosition = position;
    }

    class MyPagerAdapter extends PagerAdapter {

        private List<View> mImageViewList;
        private Context mContext;

        MyPagerAdapter(List<View> list, Context context) {
            super();
            mImageViewList = list;
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mImageViewList != null) {
                if (mImageViewList.size() > 0) {
                    container.addView(mImageViewList.get(position));
                    if (position == mImageViewList.size() - 1) {
                        ImageView imageView = (ImageView) mImageViewList.get(position).findViewById(R.id.iv_start);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startHomeActivity();
                                setGuided();
                            }
                        });
                    }
                    return mImageViewList.get(position);
                }
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (mImageViewList != null) {
                if (mImageViewList.size() > 0) {
                    container.removeView(mImageViewList.get(position));
                }
            }

        }

        @Override
        public int getCount() {
            if (mImageViewList != null) {
                return  mImageViewList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void setGuided() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("mIsFirstIn", false);
        editor.commit();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
