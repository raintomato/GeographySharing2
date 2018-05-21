package com.example.lenovo.geographysharing.Issue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shdeng on 2018/4/20.
 * 需求发布的详情界面
 */

public class IssueDetailRequestActivity extends BaseActivity {
    private Spinner requestSpinner = null;
    private TextView tv_request_classify = null;

    private Button btn_issue_request = null;
    private EditText edt_request_name = null;
    private EditText edt_request_content = null;
    private EditText edt_request_expense = null;
    private EditText edt_request_address = null;

    String[] spinnerItems;

    private static final String DEMAND = "demand";
    private Demand demand;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_requirements;
    }

    //处理左上角返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        spinnerItems = getResources().getStringArray(R.array.request_classify);

        //设置ArrayAdapter内置的item样式-这里是单行显示样式
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);
        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //设置Adapter了
        requestSpinner.setAdapter(adapter);
        //监听Spinner的操作
        requestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //选取时候的操作
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_request_classify.setText(adapter.getItem(position));
            }

            //没被选取时的操作
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_request_classify.setText("未选择");
            }
        });
    }

    @Override
    protected void initView() {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("发布需求");

        requestSpinner = bindViewId(R.id.request_classify_spinner);
        tv_request_classify = bindViewId(R.id.tv_request_classify);

        edt_request_name = bindViewId(R.id.issue_request_name);
        edt_request_content = bindViewId(R.id.issue_request_content);
        edt_request_expense = bindViewId(R.id.issue_request_expense);
        edt_request_address = bindViewId(R.id.issue_request_address);

        btn_issue_request = bindViewId(R.id.issue_request_button);

        btn_issue_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForm();
                Calendar now = Calendar.getInstance();
                final String nowDate = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        demand_id,type,title,content,time,money,demander
                        boolean isSuccess = Demand.addDemand(13,
                                tv_request_classify.getText().toString(),
                                edt_request_name.getText().toString()
                                , edt_request_content.getText().toString()
                                , TimeUtil.checkTime(nowDate)
//                                ,"Time"
                                , edt_request_expense.getText().toString()
                                , LoginUserRegisterUtil.user.getId());
                    }
                });
                Log.i(nowDate, "时间格式");
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    /*
    * 以下被注释的方法适合一个button时用
    * */
//        btn_Upload_Pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Chose_Pic_or_Cam();
//            }
//        });

        Intent intent = this.getIntent();
        if (intent != null) {
            demand = (Demand) intent.getSerializableExtra(DEMAND);
        }
    }


    @Override
    protected void initData() {

        edt_request_name.setText(demand.getTitle());
        edt_request_content.setText(demand.getContent());
        edt_request_expense.setText(demand.getMoney());
        edt_request_address.setText(demand.getDemander().getAddress());


    }

    public static void launchIIssueDetailRequestActivity(Activity activity) {
        Intent intent = new Intent(activity, IssueDetailRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

    }

    public static void launchIIssueDetailRequestActivity(Activity activity, Demand demand) {
        Intent intent = new Intent(activity, IssueDetailRequestActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DEMAND, demand);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }

    private boolean checkForm() {
        final String name = edt_request_name.getText().toString();
        final String content = edt_request_content.getText().toString();
        final String expense = edt_request_expense.getText().toString();
        final String address = edt_request_address.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            edt_request_name.setError("请输入需求名称!");
            isPass = false;
        } else {
            edt_request_name.setError(null);
        }

        if (content.isEmpty()) {
            edt_request_content.setError("请描述一下需求的故事或者细节!");
            isPass = false;
        } else {
            edt_request_content.setError(null);
        }

        if (expense.isEmpty()) {
            edt_request_expense.setError("请填写求租价格（¥/天）!");
            isPass = false;
        } else {
            edt_request_expense.setError(null);
        }

        if (address.isEmpty()) {
            edt_request_address.setError("请填写需求所在地!");
            isPass = false;
        } else {
            edt_request_address.setError(null);
        }

        return isPass;

    }


}
