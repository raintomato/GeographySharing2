package com.example.lenovo.geographysharing.Issue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Element.BaseElement.Issue;
import com.example.lenovo.geographysharing.Element.BaseElement.Type;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentType;
import com.example.lenovo.geographysharing.Element.Image;
import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.Person.MyCertificateActivity;
import com.example.lenovo.geographysharing.Person.PersonDataActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.Bimp;
import com.example.lenovo.geographysharing.Utils.BitmapUtils;
import com.example.lenovo.geographysharing.Utils.CompressImageUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;
import com.example.lenovo.geographysharing.Utils.ToastUitls;
import com.example.lenovo.geographysharing.adapters.PhotoAdapter;
import com.example.lenovo.geographysharing.beans.ImageItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.lenovo.geographysharing.Utils.CompressImageUtil.compressImageUtil;


/**
 * Created by lenovo on 2018/1/2.
 * 产品发布的详情界面。
 */

public class IssueDetailActivity extends BaseActivity implements View.OnClickListener {
    private Spinner issueSpinner = null;
    private EditText issueName = null;
    private TextView issueBuyDate = null;
    private TextView issueFreeBegin = null;
    private TextView issueFreeEnd = null;
  //  private EditText issueBugPrice = null;
    //private TextView issueOverDate = null;
    private EditText issueRentPrice = null;
    private EditText issueProperty = null;
    private EditText issueRemark = null;
    private EditText issueAddress = null;
    Context context = this;
    private TextView issueClassifyText = null;

    private Button btn_Publish; //发布设备按钮
    private ImageView img = null;//加载设备的图片

    ArrayAdapter arrayAdapter;   //下拉框适配器

    private List<String> list = new ArrayList<>();

    private static final String EQUIPMENT = "equipment";
    private Equipment equipment;

    int mYear, mMonth, mDay;
    String days;
    String test;
    String[] spinnerItems;
    String name;
    String smallType;
    boolean isSuccess;
    String imagePaths = null;

    private static final int PICK_PHOTO = 1;
    private List<Bitmap> mResults = new ArrayList<>();

    public static Bitmap bimap;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private int isOK = 20;

//
//    // 返回码：系统图库
//    private static final int RESULT_IMAGE = 100;
//    // 返回码：相机
//    private static final int RESULT_CAMERA = 200;
//
//    private Uri imageUri = Uri.parse("content://media/external/images/media/1");
//
//    private File outputImage = null;//相机拍摄图片的缓存位置
//
//    private Uri uri=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        spinnerItems = getResources().getStringArray(R.array.geology_spinner);

        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused);

        mResults.add(bimap);
        //issueSpinner = bindViewId(R.id.issue_classify_spinner);
//        数据源
//        ArrayList<String> spinners = new ArrayList<>();
//        spinners.add("类别1");
//        spinners.add("类别2");
//        spinners.add("类别3");
//        spinners.add("类别4");
//        spinners.add("类别5");
//        spinners.add("类别6");
//        //设置ArrayAdapter内置的item样式-这里是单行显示样式
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinners);
//        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        //设置Adapter了
//        issueSpinner.setAdapter(adapter);
//        //监听Spinner的操作
//        issueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            //选取时候的操作
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                issueClassifyText.setText(adapter.getItem(position));
//            }
//
//            //没被选取时的操作
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                issueClassifyText.setText("未选择");
//            }
//        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_details;
    }

    @Override
    protected void initView() throws InterruptedException {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("填写发布设备信息");

        issueSpinner = bindViewId(R.id.issue_classify_spinner);

        //时间相关控件的绑定
        issueBuyDate = bindViewId(R.id.issue_buy_date);
      //  issueOverDate = bindViewId(R.id.issue_over_date);
        issueFreeBegin = bindViewId(R.id.issue_free_date_begin);
        issueFreeEnd = bindViewId(R.id.issue_free_date_end);
        //其他发布信息输入框绑定
        issueName = bindViewId(R.id.issue_name);
       // issueBugPrice = bindViewId(R.id.issue_buy_price);
        issueRentPrice = bindViewId(R.id.issue_rent_price);
        issueProperty = bindViewId(R.id.issue_property);
        issueRemark = bindViewId(R.id.issue_remark);
        issueAddress = bindViewId(R.id.issue_address);
        issueClassifyText = bindViewId(R.id.tv_classify);
        issueBuyDate.setVisibility(View.GONE);
        //接收fragment传进来的参数

        Intent intent = getIntent();
        if (name == null) {
            name = intent.getStringExtra("type_name");
        }

        Log.i("type_name", "initView: " + name);
//        list=EquipmentType.findSecondEquipmentTypes(name);  //去获取小类别
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.spinner_item_select,list);
//        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
//        issueSpinner.setAdapter(arrayAdapter);
        //加个switch
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = EquipmentType.findSecondEquipmentTypes(name).getSub_type();  //去获取小类别
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        t.join();

        arrayAdapter = new ArrayAdapter(context, R.layout.spinner_item_select, list);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
        issueSpinner.setAdapter(arrayAdapter);
        issueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //选取时候的操作
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smallType = arrayAdapter.getItem(position).toString();
                issueClassifyText.setText(smallType);
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                    }
                }).start();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_Publish = bindViewId(R.id.issue);//发布设备按钮
        issueBuyDate.setOnClickListener(this);
        //issueOverDate.setOnClickListener(this);
        issueFreeEnd.setOnClickListener(this);
        issueFreeBegin.setOnClickListener(this);
        btn_Publish.setOnClickListener(this);
        Intent intent1 = this.getIntent();
        if (intent1 != null) {
            equipment = (Equipment) intent1.getSerializableExtra(EQUIPMENT);
            name = intent1.getStringExtra("type_name");
        }

    /*
    * 以下被注释的方法适合一个button时用
    * */
//        btn_Upload_Pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Chose_Pic_or_Cam();
//            }
//        });

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(IssueDetailActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Intent intent = new Intent(IssueDetailActivity.this,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue:
                CompressImageUtil.hideInput(IssueDetailActivity.this,view);
                //7-2
                if(!checkForm()){
                    ToastUitls.showShortToast(context,"信息不全，请完善信息");

                    break;
                }

                if (PhotoPickerActivity.mSelectList.isEmpty()) {
                    ToastUitls.showShortToast(IssueDetailActivity.this, "请选择图片");

                    break;
                }
                /**
                 * 7-21
                 */
                final ProgressDialog proDialog = android.app.ProgressDialog.show(IssueDetailActivity.this,null, "发布中。。");
                proDialog.setCancelable(true);
                btn_Publish.setEnabled(false);
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        if (PhotoPickerActivity.mSelectList.isEmpty()) {
                            ToastUitls.showShortToast(IssueDetailActivity.this, "请选择图片");
                        } else if (isSuccess && !(PhotoPickerActivity.mSelectList.isEmpty())) {
                            ToastUitls.showLongToast(IssueDetailActivity.this, "发布成功");
                            proDialog.dismiss();
//                            PhotoPickerActivity.mSelectList.clear();
//                            mResults.clear();
//                            mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
                            delPic();
//                            new android.support.v7.app.AlertDialog.Builder(IssueDetailActivity.this).setTitle("提示").setMessage("发布成功，请等待审核，是否继续发布？").setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
//                                    adapter.notifyDataSetChanged();
//                                    issueRentPrice.getText().clear();
//                                    issueProperty.getText().clear();
//                                    issueAddress.getText().clear();
//                                    issueRemark.getText().clear();
//
//                                }
//                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    proDialog.dismiss();
//                                    finish();
//                                }
//                            }).show();
//                            //ToastUitls.showShortToast(IssueDetailActivity.this, "发布成功");
//                            mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
//                            adapter.notifyDataSetChanged();
                            finish();
                        } else {

                            ToastUitls.showLongToast(IssueDetailActivity.this, "发布成功");
                            proDialog.dismiss();
//                            PhotoPickerActivity.mSelectList.clear();
//                            mResults.clear();
//                            mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
                            delPic();

//                            new android.support.v7.app.AlertDialog.Builder(IssueDetailActivity.this).setTitle("提示").setMessage("发布成功，请等待审核，是否继续发布？").setPositiveButton("是", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
//
//                                            issueRentPrice.getText().clear();
//                                            issueProperty.getText().clear();
//                                            issueAddress.getText().clear();
//                                            issueRemark.getText().clear();
//                                }
//                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    proDialog.dismiss();
//                                    finish();
//                                }
//                            }).show();

                        }
                    }


                };

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if (!(PhotoPickerActivity.mSelectList.isEmpty())) {
                                isOK = Image.uploadImage(1, null, PhotoPickerActivity.mSelectList.get(0));
                                for (int i = 0; i < PhotoPickerActivity.mSelectList.size(); i++) {
                                    Image.uploadImage(2, isOK, PhotoPickerActivity.mSelectList.get(i));
                                }
                                isSuccess = Equipment.issueEquipment("编号", issueName.getText().toString(),
                                        "2011-1-1T1:1:1",
                                        "2011-1-1T1:1:1",
                                        0.0,
                                        TimeUtil.checkTime(issueFreeBegin.getText().toString()),
                                        TimeUtil.checkTime(issueFreeEnd.getText().toString()),
                                        Double.valueOf(issueRentPrice.getText().toString()),
                                        issueProperty.getText().toString(),
                                        issueAddress.getText().toString(),
                                        issueRemark.getText().toString(),
                                        EquipmentType.findSecondEquipmentTypes(name).getId(),
                                        LoginUserRegisterUtil.user.getId(),
                                        isOK);
                                Log.i("Success", isSuccess+"");

                            }

                            Message message = handler.obtainMessage();
                            handler.sendMessage(message);

                        }catch (Exception e){
                            e.printStackTrace();
                            proDialog.dismiss();
                            ToastUitls.showLongToast(IssueDetailActivity.this,"发布失败");
                        }
                    }
                });
                thread.start();
                break;
            case R.id.issue_buy_date:
                new DatePickerDialog(IssueDetailActivity.this, onDateSetListener1, mYear, mMonth, mDay).show();
                break;
//            case R.id.issue_over_date:
//                new DatePickerDialog(IssueDetailActivity.this, onDateSetListener2, mYear, mMonth, mDay).show();
//                break;
            case R.id.issue_free_date_begin:
                new DatePickerDialog(IssueDetailActivity.this, onDateSetListener3, mYear, mMonth, mDay).show();
                break;
            case R.id.issue_free_date_end:
                new DatePickerDialog(IssueDetailActivity.this, onDateSetListener4, mYear, mMonth, mDay).show();
                break;
            default:
                break;

        }

    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            issueBuyDate.setText(days);
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            //issueOverDate.setText(days);
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListener3 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            issueFreeBegin.setText(days);
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListener4 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            issueFreeEnd.setText(days);
        }
    };

//    private void Choose_Pic_or_Cam() {
//        //将文件读写权限写在此处。
//        if (ContextCompat.checkSelfPermission(IssueDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(IssueDetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//参数为0,表示不跳向图库activity。
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                  /*.setTitle("选择图片：")
//                  * */
//                .setItems(new String[]{"相机", "图库"}, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                takePhoto();
//                                break;
//                            case 1:
//                                openAlbum();
//                                break;
//                            default:
//                                break;
//                        }
//
//                    }
//                });
//        builder.create().show();
//    }

    private boolean checkForm() {
        final String name = issueName.getText().toString();
       // final String buyDate = issueBuyDate.getText().toString();
        final String freeBegin = issueFreeBegin.getText().toString();
        final String freeEnd = issueFreeEnd.getText().toString();
        //final String buyPrice = issueBugPrice.getText().toString();
        //final String overDate = issueOverDate.getText().toString();
        final String rentPrice = issueRentPrice.getText().toString();
        final String property = issueProperty.getText().toString();
        final String remark = issueRemark.getText().toString();
        final String address = issueAddress.getText().toString();
       // Log.i("name", "checkForm: " + name + buyDate.isEmpty());
        boolean isPass = true;

        if (name.isEmpty()) {
            issueName.setError("请输入设备名称!");
            isPass = false;
        } else {
            issueName.setError(null);
        }

//        if (buyDate.isEmpty()) {
//            issueBuyDate.setError("请选择购买日期!");
//            isPass = false;
//        } else {
//            issueBuyDate.setError(null);
//        }

        if (freeBegin.isEmpty()) {
            issueFreeBegin.setError("请选择闲置开始时间!");
            isPass = false;
        } else {
            issueFreeBegin.setError(null);
        }

        if (freeEnd.isEmpty()) {
            issueFreeEnd.setError("请选择闲置结束时间!");
            isPass = false;
        } else {
            issueFreeEnd.setError(null);
        }

//        if (buyPrice.isEmpty()) {
//            issueBugPrice.setError("请输入设备原购买价格!");
//            isPass = false;
//        } else {
//            issueBugPrice.setError(null);
//        }
//
//        if (overDate.isEmpty()) {
//            issueOverDate.setError("请选择设备到年限!");
//            isPass = false;
//        } else {
//            issueOverDate.setError(null);
//        }

        if (rentPrice.isEmpty()) {
            issueRentPrice.setError("请输入设备出租价格（¥/天）!");
            isPass = false;
        } else {
            issueRentPrice.setError(null);
        }

        if (property.isEmpty()) {
            issueProperty.setError("请输入设备性能参数!");
            isPass = false;
        } else {
            issueProperty.setError(null);
        }
        if (address.isEmpty()) {
            issueAddress.setError("请输入设备所在地址!");
            isPass = false;
        } else {
            issueAddress.setError(null);
        }
        return isPass;
    }

//    /**
//     * 打开相册
//     */
//    public void openAlbum() {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent, RESULT_IMAGE);
//    }
//
//    /**
//     * 拍摄
//     */
//    private void takePhoto() {
//        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (Build.VERSION.SDK_INT >= 24) {
//            imageUri = FileProvider.getUriForFile(IssueDetailActivity.this, "com.example.lenovo.geographysharing.fileprovider", outputImage);
//        } else {
//            imageUri = Uri.fromFile(outputImage);
//        }
//        //测试点
//        Toast.makeText(IssueDetailActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, RESULT_CAMERA);
//    }
//
//    /**
//     * 图库访问许可后调用图片    此APP未用
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openAlbum();
//            }
//        }
//    }
//
//
//    /**
//     * 回调
//     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//
//            try {
//
//                imagePaths= compressImageUtil.CreateFiles();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (requestCode == RESULT_IMAGE && data != null) {
//                // 相册
//                if (Build.VERSION.SDK_INT >= 19) {//版本兼容
//                    handleImageOnKitKat(data);
//                } else {
//                    handleImageBeforeKitKat(data);
//                }
//            } else if (requestCode == RESULT_CAMERA) {
//                // 相机
//                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                Bitmap showBitmap = compressImageUtil.getSmallBitmap_File(outputImage, 245, 180);//压缩图片
//                img.setImageBitmap(showBitmap);
//            }
//        }
//
//
//    }

    //处理左上角返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            delPic();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //
//    /**
//     * 相册选择图片
//     * 大于4.4版本SDK
//     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void handleImageOnKitKat(Intent data) {
//        String imagePath = null;
//        uri = data.getData();
//        if (DocumentsContract.isDocumentUri(this, uri)) {
//            String documentId = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                String id = documentId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
//                imagePath = getImagePath(contentUri, null);
//            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//                imagePath = uri.getPath();
//            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//                imagePath = uri.getPath();
//            }
//            displayImage(imagePath);
//        }
//    }
//
//    /**
//     * 相册选择图片
//     * 小于于4.4版本SDK
//     */
//    private void handleImageBeforeKitKat(Intent data) {
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri, null);
//        displayImage(imagePath);
//    }
//
//    /**
//     * 获取相册图片路径
//     */
//    private String getImagePath(Uri uri, String selection) {
//        String path = null;
//        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//
//                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
//            }
//            cursor.close();
//        }
//        return path;
//    }
//
//    /**
//     * 显示图片
//     */
//    private void displayImage(String imagePath) {
//        if (imagePath != null) {
//            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//显示缩略图
//            //Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 245, 180);//显示缩略图
//            Bitmap showBitmap = compressImageUtil.getSmallBitmap_String(imagePath, 245, 180);
//            img.setImageBitmap(showBitmap);
//
//        } else {
//            Toast.makeText(this, "请选择正确的图片路径！", Toast.LENGTH_SHORT).show();
//        }
//    }
//

    @Override
    protected void initData() {
//        issueSpinner.sette
        if (equipment != null) {
//            issueSpinner
            issueName.setText(equipment.getName());
            issueBuyDate.setText(equipment.getStart().substring(0, 10));
            issueFreeBegin.setText(equipment.getEnd().substring(0, 10));
            issueFreeEnd.setText(equipment.getEnd().substring(0, 10));
            //issueBugPrice.setText(equipment.getExpense().toString());
            //issueOverDate.setText(equipment.getOver().substring(0, 10));
            issueRentPrice.setText(equipment.getPrice().toString());
            issueProperty.setText(equipment.getParameter());
            issueRemark.setText(equipment.getComment());
            issueAddress.setText(equipment.getPlace());
        }


    }

    public static void launchIssueDetailActivity(Activity activity) {
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    public static void launchIssueDetailActivity(Activity activity, Equipment equipment, String typeName) {
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EQUIPMENT, equipment);
        bundle.putString("type_name", typeName);
        intent.putExtras(bundle);
        intent.putExtra("type_name", typeName);
        Log.i("type_name", "launchIssueDetailActivity: " + typeName);
        activity.startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
            }
        }
    }

    private void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (paths.size() != 0) {
            // mResults.remove(mResults.size() - 1);
            mResults.clear();
        }
        mResults.clear();
        Bimp.tempSelectBitmap.clear();
        Log.i("path", paths.size() + "");
        Log.i("tempSelectBitmap1", Bimp.tempSelectBitmap.size() + "");
        for (int i = 0; i < paths.size(); i++) {
            // 压缩图片
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(paths.get(i), 400, 500);

            bitmap = CompressImageUtil.compressForInternetImage(bitmap);

            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            Bimp.tempSelectBitmap.add(takePhoto);
        }
        Log.i("result", mResults.size() + "");
        Log.i("tempSelectBitmap2", Bimp.tempSelectBitmap.size() + "");
        mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
        adapter.notifyDataSetChanged();
    }

    /**
     * 适配器
     */
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return mResults.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gridview, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_focused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }


    /**
     * 7-20
     * 解决finish（）后图片不清零的问题
     * 模拟删除
     */

    public void delPic(){
                PhotoPickerActivity.mSelectList.clear();
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                Intent intent = new Intent("data.broadcast.action");
                sendBroadcast(intent);
                finish();
        }

}

