package com.example.lenovo.geographysharing.Details;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.Element.Technology;
import com.example.lenovo.geographysharing.Issue.IssueDetailActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.CompressImageUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;
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
    public static String type;
    private Context mContext;
    private PullLoadRecyclerView mPullLoadRecyclerView;
    private android.widget.SearchView mSearchView;

    private List<Equipment> searchList = new ArrayList<>();
    private PullLoadRVAdapter mAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 0;
    private static final int LOAD_MORE = 0;
    private TextView mEmptyView;
    public static List<Equipment> tempEquipmentList = new ArrayList<>();
    private boolean isSearchSuccess = true;
    List<Equipment> equipments = new ArrayList<>();
    private boolean isEnd = false;
    private String theType = "地质类";//分类
    private int pageNo;
    private int pageNoForGeo;
    private int pageNoForWuTan;
    private int pageNoForCeLiang;
    private int pageNoForJianCe;
    private int pageNoForPeoPle;
    private int pageNoForSoftWare;
    ProgressDialog proDialog = null;


    public DetailListFragment() {

    }

    public static Fragment newInstance(String channId) {
        DetailListFragment detailListFragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHANNEL_ID, channId);
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
        pageNo = 0;
        pageNoForGeo = 0;
        pageNoForJianCe = 0;
        pageNoForCeLiang = 0;
        pageNoForWuTan = 0;
        pageNoForPeoPle = 0;
        pageNoForSoftWare = 0;
        mAdapter = new PullLoadRVAdapter(getActivity().getBaseContext());

    }


    @Override
    protected void initView() {
        type = getArguments().getString(CHANNEL_ID);
        mSearchView = bindViewId(R.id.searchView);
        switch (type) {
            case "地质类":
            case "物探类":
            case "测量类":
            case "检测类":

                break;
            case "软件":
                mSearchView.setQueryHint("                   输入软件名称");
                break;
            case "人员":
                mSearchView.setQueryHint("                   输入人员名称");
                break;
            default:

                break;
        }
        mSearchView.clearFocus();//SearchView焦点问题
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconified(false);//SearchView展开状态
        mSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            @Override
            public boolean onQueryTextSubmit(final String s) {
                mSearchView.clearFocus();

                switch (type) {
                    case "地质类":
                    case "物探类":
                    case "测量类":
                    case "检测类":
                        mAdapter.myEquipList.clear();
                        break;
                    case "软件":
                        mAdapter.mysoftList.clear();
                        break;
                    case "人员":
                        mAdapter.myTechList.clear();
                        break;
                    default:
                        break;
                }

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mSearchView.clearFocus();
                        switch (type) {
                            case "地质类":
                            case "物探类":
                            case "测量类":
                            case "检测类":
                                if (mAdapter.myEquipList.size() == 0) {
                                    ToastUitls.showShortToast(getActivity(), "对不起没有此商品");
                                }
                                Log.i("ssss123", "onQueryTextSubmit: " + mAdapter.myEquipList.size());
                                break;
                            case "软件":
                                if (mAdapter.mysoftList.size() == 0) {
                                    ToastUitls.showShortToast(getActivity(), "对不起没有此商品");
                                }
                                Log.i("ssss456", "onQueryTextSubmit: " + mAdapter.mysoftList.size());
                                break;
                            case "人员":
                                if (mAdapter.myTechList.size() == 0) {
                                    ToastUitls.showShortToast(getActivity(), "对不起没有此商品");
                                }
                                Log.i("ssss789", "onQueryTextSubmit: " + mAdapter.myTechList.size());
                                break;
                            default:
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                };

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /**
                             * 写的他们就是si
                             */
                            switch (type) {
                                case "地质类":
                                case "物探类":
                                case "测量类":
                                case "检测类":
                                    if (Equipment.findEquipments(s, 1) != null) {
                                        mAdapter.myEquipList = Equipment.findEquipments(s, 1);
                                    }
                                    break;
                                case "软件":
                                    if (Software.findSoftWares(s, 1) != null) {
                                        mAdapter.mysoftList = Software.findSoftWares(s, 1);
                                    }
                                    break;
                                case "人员":
                                    if (Technology.findTechnologies(s, 1) != null) {
                                        mAdapter.myTechList = Technology.findTechnologies(s, 1);
                                    }
                                    break;
                                default:
                                    break;
                            }

//                            if (s != null) {
//                                mAdapter.myEquipList = Equipment.findEquipments(s, 1);
//                            }
//                            if (s.isEmpty()) {
//                                mAdapter.myEquipList = Equipment.findEquipments(1);
//                            }
//                            if (mAdapter.myEquipList != null) {
//                                Message message = handler.obtainMessage();
//                                handler.sendMessage(message);
//                            }else {
//                                isSearchSuccess = false;
//                            }
                            Message message = handler.obtainMessage();
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            mSearchView.clearFocus();
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
//                if (isSearchSuccess == false) {
//                    mSearchView.clearFocus();
//                    ToastUitls.showShortToast(getActivity(), "对不起没有此商品");
//                    isSearchSuccess = true;
//                }

                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            //暂时不使用此
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mPullLoadRecyclerView = bindViewId(R.id.pullloadRecyclerView);
        mPullLoadRecyclerView.setGridLayout();
        mAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener());
        mAdapter.setOnItemClickListener(new PullLoadRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    default:
                        switch (type) {
                            case "地质类":
                            case "物探类":
                            case "测量类":
                            case "检测类":
                                AlbumDetailActivity.launchAlbumDetailActivity(getActivity(), mAdapter.myEquipList.get(position));
                                break;
                            case "软件":
                                AlbumDetailSoftActivity.launchAlbumDetailSoftActivity(getActivity(), mAdapter.mysoftList.get(position));
                                break;
                            case "人员":
                                AlbumDetailPeopleActivity.launchAlbumDetailPeoPleActivity(getActivity(), mAdapter.myTechList.get(position));
                                break;
                            default:
                                break;
                        }
                        //直接传对象

                        break;
                }
            }
        });
    }

    private void reFreshData() {
        //// TODO: 2017/12/31 请求接口加载数据
    }

    private void loadData() {

        proDialog = ProgressDialog.show(getContext(), null, "加载中.....");

        proDialog.setCancelable(true);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (isEnd) {
                    proDialog.dismiss();
                    mSearchView.clearFocus();
                } else {
                    super.handleMessage(msg);
                    if (mAdapter.myEquipList.size() == 0 && mAdapter.mysoftList.size() == 0 && mAdapter.myTechList.size() == 0) {
                        proDialog.dismiss();
                        ToastUitls.showShortToast(getActivity(), "没有数据哦");
                    } else {
                        proDialog.dismiss();
                        if ((mAdapter.myEquipList.size() % 10) == 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyDataSetChanged();
                            isEnd = true;
                        }
                    }
                    mSearchView.clearFocus();
                }
            }
        };
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                switch (getArguments().getString(CHANNEL_ID)) {//前面传过来的类别
                    case "地质类":
                        theType = "地质类";
                        if (pageNoForGeo == 0) {
                            pageNo = 0;

                            pageNoForJianCe = 0;
                            pageNoForCeLiang = 0;
                            pageNoForWuTan = 0;
                            pageNoForPeoPle = 0;
                            pageNoForSoftWare = 0;
                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNoForGeo++;
                            try {
                                if (Equipment.findEquipments(pageNoForGeo, theType) != null) {
                                    mAdapter.myEquipList.addAll(Equipment.findEquipments(pageNoForGeo, theType));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case "物探类":
                        theType = "物探类";
                        if (pageNoForWuTan == 0) {
                            pageNo = 0;
                            pageNoForGeo = 0;
                            pageNoForJianCe = 0;
                            pageNoForCeLiang = 0;
                            pageNoForPeoPle = 0;
                            pageNoForSoftWare = 0;
                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNoForWuTan++;
                            if (Equipment.findEquipments(pageNoForWuTan, theType) != null) {
                                mAdapter.myEquipList.addAll(Equipment.findEquipments(pageNoForWuTan, theType));
                            }

                        }
                        break;
                    case "测量类":
                        theType = "测量类";
                        if (pageNoForCeLiang == 0) {
                            pageNo = 0;
                            pageNoForGeo = 0;
                            pageNoForJianCe = 0;

                            pageNoForWuTan = 0;
                            pageNoForPeoPle = 0;
                            pageNoForSoftWare = 0;
                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNoForCeLiang++;
                            if (Equipment.findEquipments(pageNoForCeLiang, theType) != null) {
                                mAdapter.myEquipList.addAll(Equipment.findEquipments(pageNoForCeLiang, theType));
                            }

                        }
                        break;
                    case "检测类":
                        theType = "检测类";
                        if (pageNoForJianCe == 0) {
                            pageNo = 0;
                            pageNoForGeo = 0;

                            pageNoForCeLiang = 0;
                            pageNoForWuTan = 0;
                            pageNoForPeoPle = 0;
                            pageNoForSoftWare = 0;
                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNoForJianCe++;
                            try {
                                if (Equipment.findEquipments(pageNoForJianCe, theType) != null) {
                                    mAdapter.myEquipList.addAll(Equipment.findEquipments(pageNoForJianCe, theType));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case "软件":
                        theType = "软件";
                        if (pageNoForSoftWare == 0) {
                            pageNo = 0;
                            pageNoForGeo = 0;
                            pageNoForJianCe = 0;
                            pageNoForCeLiang = 0;
                            pageNoForWuTan = 0;
                            pageNoForPeoPle = 0;

                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNoForSoftWare++;
                            try {
                                if (Software.findSoftWares(pageNoForSoftWare) != null) {
                                    mAdapter.mysoftList.addAll(Software.findSoftWares(pageNoForSoftWare));
                                    //mAdapter.myEquipList.addAll(Equipment.findEquipments(pageNoForSoftWare, theType));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case "人员":
                        theType = "人员";
                        if (pageNoForPeoPle == 0) {
                            pageNo = 0;
                            pageNoForGeo = 0;
                            pageNoForJianCe = 0;
                            pageNoForCeLiang = 0;
                            pageNoForWuTan = 0;

                            pageNoForSoftWare = 0;
                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNoForPeoPle++;
                            try {
                                if (Technology.findTechnologies(pageNoForPeoPle) != null) {
                                    mAdapter.myTechList.addAll(Technology.findTechnologies(pageNoForPeoPle));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        theType = "地质类";
                        if (pageNo == 0) {

                            pageNoForGeo = 0;
                            pageNoForJianCe = 0;
                            pageNoForCeLiang = 0;
                            pageNoForWuTan = 0;
                            pageNoForSoftWare = 0;
                            pageNoForPeoPle = 0;
                            mAdapter.myEquipList.clear();
                        }
                        if (!isEnd) {
                            pageNo++;
                            if (Equipment.findEquipments(pageNo, theType) != null) {
                                mAdapter.myEquipList.addAll(Equipment.findEquipments(pageNo, theType));
                            }
                        }
                        break;
                }
                Log.i("sssssssssssssssssssb::", "run:ojbk ");
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });

        thread.start();
        // TODO: 2017/12/31 加载更多数据，刚进来的时候调用，上拉加载再次调用
    }


    //实现pullload中的监听接口
    class OnPullLoadMoreListener implements PullLoadRecyclerView.OnPullLoadMoreListener {
        @Override
        public void refresh() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
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
        /**
         * 拉取一个设备类型的所有设备对象
         * 存入myEquipmentList
         */
        /*
        Equipment equipment1 = new Equipment("取样钻", "性能参数", "北京市海淀区学院路", "100元/天", R.drawable.a);
        myEquipmentList.add(equipment1);
        Equipment equipment2 = new Equipment("刻槽取样机", "性能参数", "北京市海淀区学院路", "200元/天", R.drawable.b);
        myEquipmentList.add(equipment2);
        myEquipmentList.add(equipment311);*/
    }

}
