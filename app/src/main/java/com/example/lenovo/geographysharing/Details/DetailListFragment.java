package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.widget.PullLoadRVAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/12/30.
 */

public class DetailListFragment extends BaseFragment {
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";
    private Context mContext;
    private PullLoadRecyclerView mPullLoadRecyclerView;
    private android.widget.SearchView mSearchView;
        private PullLoadRVAdapter mAdapter;
        private Handler handler = new Handler(Looper.getMainLooper());//表示在主线程的意思
        private static final int REFRESH_DURATION = 1500;
        private static final int LOAD_MORE = 3000;
        private TextView mEmptyView;
        public static List<Equipment> myEquipmentList = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        public DetailListFragment() {
        }

    public static Fragment newInstance(int channId) {
        DetailListFragment detailListFragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CHANNEL_ID, channId);
        // bundle.putInt(SITE_ID, SiteId);
        detailListFragment.setArguments(bundle);
        return detailListFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detailist;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        mAdapter = new PullLoadRVAdapter(myEquipmentList);//
    }


    @Override
    protected void initView() {
        mSearchView = bindViewId(R.id.searchView);
        mSearchView.clearFocus();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mPullLoadRecyclerView = bindViewId(R.id.pullloadRecyclerView);
        mPullLoadRecyclerView.setGridLayout();
        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener());
        mAdapter.setOnItemClickListener(new PullLoadRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        AlbumDetailActivity.launchAlbumDetailActivity(getActivity(),myEquipmentList.get(0),0);
                        break;
                    default:
                        if (getArguments().getInt(CHANNEL_ID) == 9) {
                            AlbumDetailRequestActivity.launchAlbumDetailRequestActivity(getActivity());
                        } else {

                            //直接传对象

                            AlbumDetailActivity.launchAlbumDetailActivity(getActivity(),myEquipmentList.get(position),0);
                        }
                        break;
                }
            }
        });
    }

    private void reFreshData() {
        //// TODO: 2017/12/31 请求接口加载数据
    }

    private void loadData() {
        //// TODO: 2017/12/31 加载更多数据，刚进来的时候调用，上拉加载再次调用
    }

    //实现pullload中的监听接口
    class OnPullLoadMoreListener implements PullLoadRecyclerView.OnPullLoadMoreListener {

        @Override
        public void refresh() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reFreshData();
                    mPullLoadRecyclerView.setRefreshCompletely();

                }
            }, REFRESH_DURATION);
        }

        @Override
        public void loadMore() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                    mPullLoadRecyclerView.setLoadMoreCompleted();

                }
            }, LOAD_MORE);
        }
    }


    @Override
    protected void initData() {

        /**
         * 拉取一个设备类型的所有设备对象
         * 存入myEquipmentList
         */

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                myEquipmentList.addAll(equipments);
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                equipments.addAll(Equipment.findEquipments(1));//拉取第一页
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
/*
        Equipment equipment1 = new Equipment("取样钻", "性能参数", "北京市海淀区学院路", "100元/天", R.drawable.a);
        myEquipmentList.add(equipment1);
        Equipment equipment2 = new Equipment("刻槽取样机", "性能参数", "北京市海淀区学院路", "200元/天", R.drawable.b);
        myEquipmentList.add(equipment2);
        myEquipmentList.add(equipment311);*/
    }

}
