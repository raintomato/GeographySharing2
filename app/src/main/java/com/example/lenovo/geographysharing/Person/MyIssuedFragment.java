package com.example.lenovo.geographysharing.Person;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentIssue;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.Element.SoftwareIssue;
import com.example.lenovo.geographysharing.Element.TechnologyIssue;
import com.example.lenovo.geographysharing.Issue.IssueDetailActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;
import com.example.lenovo.geographysharing.delete.adapter.MyIssueAdapter;
import com.example.lenovo.geographysharing.widget.MyIssueRecyclerAdapter;
import com.example.lenovo.geographysharing.widget.PullLoadRecyclerView;

import java.util.ArrayList;

/**
 * Created by abc on 2018/4/21.
 */

public class MyIssuedFragment extends BaseFragment implements MyIssueAdapter.IonSlidingViewClickListener {

    private ImageButton imgIssueDelete = null;

    private PullLoadRecyclerView mPullLoadRecyclerView = null;
    private MyIssueRecyclerAdapter mAdapter = null;
    private MyIssueAdapter myAdapters;
    private Handler handler = new Handler(Looper.getMainLooper());//表示在主线程的意思
    private static final int REFRESH_DURATION = 0;
    private static final int LOAD_MORE = 0;
    private int pageNo;
    private boolean isEnd = false;
    String bigType;
    ProgressDialog proDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_issued;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNo = 0;
        mAdapter = new MyIssueRecyclerAdapter(getActivity().getBaseContext());
        myAdapters=new MyIssueAdapter(getActivity());

    }

    @Override
    protected void initView() {
//        imgIssueDelete = bindViewId(R.id.img_issue_delete);

        mPullLoadRecyclerView = bindViewId(R.id.recycler_my_issue);
        mPullLoadRecyclerView.setGridLayout();
//        mPullLoadRecyclerView.setAdapter(mAdapter);
        mPullLoadRecyclerView.setAdapter(myAdapters);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new OnPullLoadMoreListener());

//        mAdapter.buttonSetOnclick(new MyIssueRecyclerAdapter.ButtonInterface() {
//            @Override
//            public void onClick(View view, final int position) {
//                final ProgressDialog proDialog = android.app.ProgressDialog.show(getContext(), null, "删除中.....");
//                proDialog.setCancelable(true);
//                final Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        proDialog.dismiss();
//                        mAdapter.myEquipList.remove(position);
//                        mAdapter.notifyItemRemoved(position);
//                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
//                    }
//                };
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Equipment.cancelEquipment(((Equipment) (mAdapter.myEquipList.get(position).getProduct())).getId());
//                        Message m = handler.obtainMessage();
//                        handler.sendMessage(m);
//                    }
//                });
//                thread.start();
//            }
//        });



//        mAdapter.setOnItemClickListener(new MyIssueRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, final int position) {
//                bigType = ((Equipment) mAdapter.myEquipList.get(position).getProduct()).getType().getType_name();
//                IssueDetailActivity.launchIssueDetailActivity(getActivity(), ((Equipment) mAdapter.myEquipList.get(position).getProduct()), bigType);
//            }
//        });
        myAdapters.setOnItemClickListener(new MyIssueAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                bigType = ((Equipment) myAdapters.myEquipList.get(position).getProduct()).getType().getType_name();
                IssueDetailActivity.launchIssueDetailActivity(getActivity(), ((Equipment) myAdapters.myEquipList.get(position).getProduct()), bigType);
            }
        });
    }

    private void reFreshData() {
        //// TODO: 2017/12/31 请求接口加载数据
    }

    private void loadData() {
        proDialog = ProgressDialog.show(getContext(), null, "加载中.....");


        switch (MyissuedActivity.typeIsssu){
            case "设备":
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
               proDialog.dismiss();
                        if (myAdapters.myEquipList.size() == 0) {
                            ToastUitls.showShortToast(getActivity(), "没有数据的哦");
                        } else {
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
                        try {
                            if (EquipmentIssue.findEquipmentIssues(LoginUserRegisterUtil.user.getPhone(), pageNo) != null) {
                                myAdapters.myEquipList.addAll(EquipmentIssue.findEquipmentIssues(LoginUserRegisterUtil.user.getPhone(), pageNo));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message m = handler.obtainMessage();
                        handler.sendMessage(m);
                    }
                });
                thread.start();
                break;
            case "软件":

                final Handler handler1 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        super.handleMessage(msg);
                proDialog.dismiss();
                        if (myAdapters.mySoftList.size() == 0) {
                            ToastUitls.showShortToast(getActivity(), "没有数据的哦");
                        } else {
                            if ((myAdapters.mySoftList.size() % 10) == 0) {
                                myAdapters.notifyDataSetChanged();
                            } else {
                                myAdapters.notifyDataSetChanged();
                            }
                        }
                    }
                };

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (SoftwareIssue.findSoftwareIssues(LoginUserRegisterUtil.user.getPhone())!=null){
                                myAdapters.mySoftList.addAll(SoftwareIssue.findSoftwareIssues(LoginUserRegisterUtil.user.getPhone()));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message m = handler1.obtainMessage();
                        handler1.sendMessage(m);
                    }
                });
                thread1.start();


                break;
            case "人员":

                final Handler handler2 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        super.handleMessage(msg);
                        proDialog.dismiss();
                        if (myAdapters.myTechList.size() == 0) {
                            ToastUitls.showShortToast(getActivity(), "没有数据的哦");
                        } else {
                            if ((myAdapters.myTechList.size() % 10) == 0) {
                                myAdapters.notifyDataSetChanged();
                            } else {
                                myAdapters.notifyDataSetChanged();
                            }
                        }
                    }
                };

                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if(TechnologyIssue.findTechnologyIssues(LoginUserRegisterUtil.user.getPhone())!=null){
                                myAdapters.myTechList.addAll(TechnologyIssue.findTechnologyIssues(LoginUserRegisterUtil.user.getPhone()));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message m = handler2.obtainMessage();
                        handler2.sendMessage(m);
                    }
                });
                thread2.start();

                break;
            default:
                ToastUitls.showShortToast(getContext(),"没有网络");
                break;
        }

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
        if (proDialog != null) {
            proDialog.dismiss();
        }
    }

    @Override
    protected void initData() {
        loadData();
//        Equipment equipment1 = new Equipment("取样钻", "性能参数", "北京市海淀区学院路", "100元/天", R.drawable.a);
//        myEquipmentList.add(equipment1);

    }
}
