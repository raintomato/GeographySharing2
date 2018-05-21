package com.example.lenovo.geographysharing.Person;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Details.AlbumDetailRequestActivity;
import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.EntityClass.Equipment;
import com.example.lenovo.geographysharing.Issue.IssueDetailRequestActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.widget.MyFavRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.MyIssueRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.MyRequestRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018/4/21.
 */


public class MyRequestFragment extends BaseFragment {

    private PullLoadRecyclerView mPullLoadRecyclerView = null;
    private MyRequestRecyclerAdapter mAdapter = null;
    private Handler handler= new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 1500;
    private static final int LOAD_MORE = 3000;

    private List<Demand> myEquipmentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_request;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyRequestRecyclerAdapter(myEquipmentList);
    }

    @Override
    protected void initView() {

        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_request);
        mPullLoadRecyclerView.setGridLayout();
        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener());
        mAdapter.setOnItemClickListener(new MyRequestRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IssueDetailRequestActivity.launchIIssueDetailRequestActivity(getActivity(),myEquipmentList.get(position));
            }
        });

    }

    private void reFreshData(){
        //// TODO: 2017/12/31 请求接口加载数据
    }
    private void loadData(){
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
            },LOAD_MORE);
        }
    }


    @Override
    protected void initData() {
        Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            myEquipmentList.addAll(Demand.findDemands(LoginUserRegisterUtil.user.getPhone()));
        }
    });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
