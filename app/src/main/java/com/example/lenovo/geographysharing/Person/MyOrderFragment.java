package com.example.lenovo.geographysharing.Person;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Element.Order;
import com.example.lenovo.geographysharing.EntityClass.orderItem;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;
import com.example.lenovo.geographysharing.widget.orderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lisa on 2018/4/21.
 */

public class MyOrderFragment extends BaseFragment {

    private PullLoadRecyclerView mPullLoadRecyclerView;
    private orderAdapter mAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 0;
    private static final int LOAD_MORE = 0;
    public static List<orderItem> myEquipmentList = new ArrayList<>();

    private boolean isEnd = false;

    private int pageNo;

    ProgressDialog proDialog = null;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_order;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new orderAdapter(myEquipmentList);
    }

    @Override
    protected void initView() {
        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_order);
        mPullLoadRecyclerView.setGridLayout();
        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new MyOrderFragment.OnPullLoadMoreListener());
        mAdapter.setOnItemClickListener(new orderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyOrderDetailActivity.launchorderDetailActivity(getActivity(),mAdapter.myOrderList.get(position));
            }
        });

    }

    private void reFreshData() {
        //// TODO: 2017/12/31 请求接口加载数据
    }

    private void loadData() {
        proDialog  = android.app.ProgressDialog.show(getContext(), null, "加载中.....");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    proDialog.dismiss();
                    if (mAdapter.myOrderList.size() == 0) {
                        Toast.makeText(getActivity(), "没有数据的哦", Toast.LENGTH_SHORT).show();
                    } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }


        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                    pageNo++;
                    try {
                        if ( Order.findOrdersBypayee(LoginUserRegisterUtil.user.getId())!=null){
                            mAdapter.myOrderList.addAll(Order.findOrdersBypayee(LoginUserRegisterUtil.user.getId()));
                        }
                        if(Order.findOrdersBypayer(LoginUserRegisterUtil.user.getId())!=null){
                            mAdapter.myOrderList.addAll(Order.findOrdersBypayer(LoginUserRegisterUtil.user.getId()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);

            }
        });
        thread.start();








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
        loadData();
////        String shop_name, String good_status, int image_id, String good_name, String good_price,String good_num,String total_price)
//        orderItem equipment1 = new orderItem("小李", "待收货", R.drawable.b, "地质锤", "1000元/天", "2", "2000");
//        myEquipmentList.add(equipment1);
//        orderItem equipment2 = new orderItem("小田", "使用中", R.drawable.d, "地质锤", "1000元/天", "2", "2000");
//        myEquipmentList.add(equipment2);
    }
}
