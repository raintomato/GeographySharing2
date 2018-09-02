package com.example.lenovo.geographysharing.Person;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Details.AlbumDetailRequestActivity;
import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.EntityClass.Equipment;
import com.example.lenovo.geographysharing.Issue.IssueDetailRequestActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.delete.adapter.MyDemandAdapter;
import com.example.lenovo.geographysharing.widget.MyFavRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.MyIssueRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.MyRequestRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018/4/21.
 */


public class MyRequestFragment extends BaseFragment  implements MyDemandAdapter.IonSlidingViewClickListener{

    private PullLoadRecyclerView mPullLoadRecyclerView = null;
    private MyRequestRecyclerAdapter mAdapter = null;
    private MyDemandAdapter myAdapters;
    private Handler handler = new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 0;
    private static final int LOAD_MORE = 0;
    int pageNo;
    private boolean isEnd = false;

    ProgressDialog proDialog = null;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_request;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNo = 0;
        mAdapter = new MyRequestRecyclerAdapter();
        myAdapters=new MyDemandAdapter(getActivity());

    }

    @Override
    protected void initView() {

        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_request);
        mPullLoadRecyclerView.setGridLayout();
        mPullLoadRecyclerView.setAdapter(myAdapters);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener());
//        mAdapter.buttonSetOnclick(new MyRequestRecyclerAdapter.ButtonInterface() {
//            @Override
//            public void onClick(View view, final int position) {
//                final Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        mAdapter.myEquipList.remove(position);
//                        mAdapter.notifyItemRemoved(position);
//                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
//                    }
//                };
//
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Demand.deleteDemand(mAdapter.myEquipList.get(position).getId());
//                        Demand.deleteDemand(28);
//                        Message m = handler.obtainMessage();
//                        handler.sendMessage(m);
//                    }
//                });
//
//                thread.start();
//                try {
//                    thread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////                            }
//            }
//        });

        myAdapters.setOnItemClickListener(new MyDemandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IssueDetailRequestActivity.launchIIssueDetailRequestActivity(getActivity(), myAdapters.myEquipList.get(position));
            }
        });

    }

    private void reFreshData() {
        //// TODO: 2017/12/31 请求接口加载数据
    }

    private void loadData() {
        proDialog = ProgressDialog.show(getContext(),null, "加载中.....");
        proDialog.setCancelable(true);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                    super.handleMessage(msg);
                    proDialog.dismiss();
                    if (myAdapters.myEquipList.size()==0) {
                        Toast.makeText(getActivity(), "没有数据的哦", Toast.LENGTH_SHORT).show();
                    } else {
                        proDialog.dismiss();
                        if ((myAdapters.myEquipList.size() % 10) == 0) {
                            myAdapters.notifyDataSetChanged();
                        } else {
                            myAdapters.notifyDataSetChanged();
                    }
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    pageNo++;
                try{
                    if (Demand.findDemands(LoginUserRegisterUtil.user.getPhone(), pageNo) != null) {
                        myAdapters.myEquipList.addAll(Demand.findDemands(LoginUserRegisterUtil.user.getPhone(), pageNo));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });
        thread.start();
        //// TODO: 2017/12/31 加载更多数据，刚进来的时候调用，上拉加载再次调用
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {

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
    public void onPause() {
        super.onPause();
        if(proDialog!=null){
            proDialog.dismiss();
        }
    }

    @Override
    protected void initData() {
        loadData();
    }
}
