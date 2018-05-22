package com.example.lenovo.geographysharing.Person;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.widget.MyFavRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRVAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2018/4/21.
 */

public class MyFavFragment extends BaseFragment {
    private PullLoadRecyclerView mPullLoadRecyclerView = null;
    private MyFavRecyclerAdapter mAdapter = null;
    private Handler handler= new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 1500;
    private static final int LOAD_MORE = 3000;
    private List<EquipmentCollection> myEquipmentList = new ArrayList<>();
    private ImageButton deleteFav = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_fav;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyFavRecyclerAdapter(myEquipmentList);
    }

    @Override
    protected void initView() {
        deleteFav = bindViewId(R.id.img_fav_delete);

        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_fav);
        mPullLoadRecyclerView.setGridLayout();
        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new MyFavFragment.OnPullLoadMoreListener());
        mAdapter.setOnItemClickListener(new MyFavRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                switch (position){
                    case 0:
                        AlbumDetailActivity.launchAlbumDetailActivity(getActivity(),(Equipment)myEquipmentList.get(0).getProduct(),1);
                        break;
                    default:
                        deleteFav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                EquipmentCollection.cancelCollection()
                                myEquipmentList.remove(position);
                                final Handler handler =new Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        mAdapter = new MyFavRecyclerAdapter(myEquipmentList);
                                        mPullLoadRecyclerView.setAdapter(mAdapter);
                                    }
                                };

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        EquipmentCollection.cancelCollection(myEquipmentList.get(position).getId());
                                        Message m = handler.obtainMessage();
                                        handler.sendMessage(m);
                                    }
                                });
                                thread.start();
                            }
                        });
                        AlbumDetailActivity.launchAlbumDetailActivity(getActivity(),(Equipment)myEquipmentList.get(position).getProduct(),1);
                        break;
                }

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

//        Equipment equipment1 = new Equipment("取样钻", "性能参数", "北京市海淀区学院路", "100元/天", R.drawable.c);
//        myEquipmentList.add(equipment1);
//        Equipment equipment2 = new Equipment("刻槽取样机", "性能参数", "北京市海淀区学院路", "200元/天", R.drawable.d);
//        myEquipmentList.add(equipment2);

//        final Handler handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                myEquipmentList.addAll(myEquipmentList);
//            }
//        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                myEquipmentList.addAll(EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone()));
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
