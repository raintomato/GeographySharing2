package com.example.lenovo.geographysharing.Person;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.delete.adapter.MyAdapter;
import com.example.lenovo.geographysharing.widget.MyFavRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRVAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by abc on 2018/4/21.
 */

public class MyFavFragment extends BaseFragment implements MyAdapter.IonSlidingViewClickListener {
    private PullLoadRecyclerView mPullLoadRecyclerView = null;
    private MyFavRecyclerAdapter mAdapter = null;
    private MyAdapter myAdapters;
    private Handler handler = new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 0;
    private static final int LOAD_MORE = 0;
    private ImageButton deleteFav = null;

    private boolean isEnd = false;

    private int pageNo;


    ProgressDialog proDialog = null;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_fav;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNo = 0;
        mAdapter = new MyFavRecyclerAdapter(getActivity().getBaseContext());

    }

    @Override
    protected void initView() {
        ArrayList<String> date;
        date = new ArrayList<>();
//        for (int i = 1; i < 50; i++) {
//            date.add(i + "");
//        }
        myAdapters=new MyAdapter(getActivity());
        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_fav);
        mPullLoadRecyclerView.setGridLayout();
//        mAdapter.notifyDataSetChanged();
        myAdapters.notifyDataSetChanged();
        mPullLoadRecyclerView.setAdapter(myAdapters);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener());

//        mAdapter.buttonSetOnclick(new MyFavRecyclerAdapter.ButtonInterface() {
//            boolean isOk = true;
//
//            @Override
//            public void onClick(View view, final int position) {
//
//                final ProgressDialog proDialog = android.app.ProgressDialog.show(getContext(), null, "删除中.....");
//                proDialog.setCancelable(true);
//                final Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        proDialog.dismiss();
//                        if (isOk) {
//                            try {
//                                mAdapter.myEquipList.remove(position);
//                                mAdapter.notifyItemRemoved(position);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                };
//
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            EquipmentCollection.cancelCollection(mAdapter.myEquipList.get(position).getId());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            isOk = false;
//                        }
//                        Message m = handler.obtainMessage();
//                        handler.sendMessage(m);
//                    }
//                });
//                thread.start();
//            }
//        });
//        myAdapters.buttonSetOnclick(new MyAdapter.ButtonInterface() {
//            boolean isOk = true;
//
//            @Override
//            public void onClick(View view, final int position) {
//
//                final ProgressDialog proDialog = android.app.ProgressDialog.show(getContext(), null, "删除中.....");
//                proDialog.setCancelable(true);
//                final Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        proDialog.dismiss();
//                        if (isOk) {
//                            try {
//                                myAdapters.myEquipList.remove(position);
//                                myAdapters.notifyItemRemoved(position);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                };
//
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            EquipmentCollection.cancelCollection(myAdapters.myEquipList.get(position).getId());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            isOk = false;
//                        }
//                        Message m = handler.obtainMessage();
//                        handler.sendMessage(m);
//                    }
//                });
//                thread.start();
//            }
//        });
//        mAdapter.setOnItemClickListener(new MyFavRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
//                switch (position) {
//                    default:
//                        AlbumDetailActivity.launchAlbumDetailActivity(getActivity(), (Equipment) mAdapter.myEquipList.get(position).getProduct(), true);
//                        break;
//                }
//
//            }
//        });
        myAdapters.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                switch (position) {
                    default:
                        AlbumDetailActivity.launchAlbumDetailActivity(getActivity(), (Equipment) myAdapters.myEquipList.get(position).getProduct());
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

        proDialog = ProgressDialog.show(getContext(), null, "加载中.....");

        proDialog.setCancelable(true);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                proDialog.dismiss();
//                if (mAdapter.myEquipList.size() == 0) {
//                    Toast.makeText(getActivity(), "没有数据的哦", Toast.LENGTH_SHORT).show();
//                } else {
//                  mAdapter.notifyDataSetChanged();
//                }
                if (myAdapters.myEquipList.size() == 0) {
                    Toast.makeText(getActivity(), "没有数据的哦", Toast.LENGTH_SHORT).show();
                } else {
                    myAdapters.notifyDataSetChanged();
                }
            }

        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pageNo++;
                try {
                    if (EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone(), pageNo) != null) {
//                        mAdapter.myEquipList.addAll(EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone(), pageNo));
                        myAdapters.myEquipList.addAll(EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone(), pageNo));
                    }
                } catch (Exception e) {
                    try {
                        if (EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone(), pageNo) != null) {
//                        mAdapter.myEquipList.addAll(EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone(), pageNo));
                            myAdapters.myEquipList.addAll(EquipmentCollection.findEquipmentCollections(LoginUserRegisterUtil.user.getPhone(), pageNo));
                        }
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });
        thread.start();
    }

    @Override
    public void onItemClick(View view, int position) {
       // AlbumDetailActivity.launchAlbumDetailActivity(getActivity(), (Equipment) myAdapters.myEquipList.get(position).getProduct(), true);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {

        Log.i("98K", "onDeleteBtnCilck: ");
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
        if (proDialog != null) {
            proDialog.dismiss();
        }
    }

    @Override
    protected void initData() {

        loadData();

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


    }

}
