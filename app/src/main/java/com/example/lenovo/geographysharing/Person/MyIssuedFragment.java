package com.example.lenovo.geographysharing.Person;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentIssue;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Issue.IssueDetailActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.widget.MyFavRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.MyIssueRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018/4/21.
 */

public class MyIssuedFragment extends BaseFragment {

    private ImageButton imgIssueDelete = null;


    private PullLoadRecyclerView mPullLoadRecyclerView = null;
    private MyIssueRecyclerAdapter mAdapter = null;
    private Handler handler= new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 1500;
    private static final int LOAD_MORE = 3000;

    private List<EquipmentIssue> myEquipmentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_issued;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyIssueRecyclerAdapter(myEquipmentList);
    }

    @Override
    protected void initView() {
        imgIssueDelete = bindViewId(R.id.img_issue_delete);


        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_issue);
        mPullLoadRecyclerView.setGridLayout();
        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new MyIssuedFragment.OnPullLoadMoreListener());
        mAdapter.setOnItemClickListener(new MyIssueRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                imgIssueDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        myEquipmentList.remove(position);
                        //设置字段为不显示，api还没做。
                    }
                });
                IssueDetailActivity.launchIssueDetailActivity(getActivity(),((Equipment) myEquipmentList.get(position).getProduct()));
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
//        Equipment equipment1 = new Equipment("取样钻", "性能参数", "北京市海淀区学院路", "100元/天", R.drawable.a);
//        myEquipmentList.add(equipment1);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                myEquipmentList.addAll(EquipmentIssue.findEquipmentIssues(LoginUserRegisterUtil.user.getPhone()));
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
