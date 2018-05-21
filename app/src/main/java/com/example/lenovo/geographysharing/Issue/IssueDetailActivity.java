package com.example.lenovo.geographysharing.Issue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentType;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;
import com.example.lenovo.geographysharing.Utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

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
    private EditText issueBugPrice = null;
    private TextView issueOverDate = null;
    private EditText issueRentPrice = null;
    private EditText issueProperty = null;
    private EditText issueRemark = null;
    private EditText issueAddress = null;
    Context context=this;
    private TextView issueClassifyText = null;

    private Button btn_Upload_Pic; //上传图片按钮
    private Button btn_Publish; //发布设备按钮
    private ImageView img = null;//加载设备的图片

    private static final String EQUIPMENT = "equipment";
    private Equipment equipment;

    int mYear, mMonth, mDay;
    String days;
    String[] spinnerItems;


    // 返回码：系统图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;

    private Uri imageUri = Uri.parse("content://media/external/images/media/1");

    private File outputImage = null;//相机拍摄图片的缓存位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        spinnerItems = getResources().getStringArray(R.array.geology_spinner);
        issueSpinner = bindViewId(R.id.issue_classify_spinner);
//        数据源
        ArrayList<String> spinners = new ArrayList<>();
        spinners.add("类别1");
        spinners.add("类别2");
        spinners.add("类别3");
        spinners.add("类别4");
        spinners.add("类别5");
        spinners.add("类别6");
        //设置ArrayAdapter内置的item样式-这里是单行显示样式
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinners);
        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //设置Adapter了
        issueSpinner.setAdapter(adapter);
        //监听Spinner的操作
        issueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //选取时候的操作
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issueClassifyText.setText(adapter.getItem(position));
            }

            //没被选取时的操作
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                issueClassifyText.setText("未选择");
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_details;
    }

    @Override
    protected void initView() {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("填写发布设备信息");

        //时间相关控件的绑定
        issueBuyDate = bindViewId(R.id.issue_buy_date);
        issueOverDate = bindViewId(R.id.issue_over_date);
        issueFreeBegin = bindViewId(R.id.issue_free_date_begin);
        issueFreeEnd = bindViewId(R.id.issue_free_date_end);
        //其他发布信息输入框绑定
        issueName = bindViewId(R.id.issue_name);
        issueBugPrice = bindViewId(R.id.issue_buy_price);
        issueRentPrice = bindViewId(R.id.issue_rent_price);
        issueProperty = bindViewId(R.id.issue_property);
        issueRemark = bindViewId(R.id.issue_remark);
        issueAddress = bindViewId(R.id.issue_address);
        issueClassifyText = bindViewId(R.id.tv_classify);

        img = bindViewId(R.id.issue_detail_pic);

        btn_Publish = bindViewId(R.id.issue);//发布设备按钮
        btn_Upload_Pic = bindViewId(R.id.issue_pic);//上传图片按钮

        issueBuyDate.setOnClickListener(this);
        issueOverDate.setOnClickListener(this);
        issueFreeEnd.setOnClickListener(this);
        issueFreeBegin.setOnClickListener(this);
        btn_Publish.setOnClickListener(this);
        btn_Upload_Pic.setOnClickListener(this);

        Intent intent = this.getIntent();
        if (intent != null) {
            equipment = (Equipment)intent.getSerializableExtra(EQUIPMENT);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue:
//                checkForm();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSuccess = Equipment.addEquipment(issueName.getText().toString(),
                                TimeUtil.checkTime(issueBuyDate.getText().toString()),
                                TimeUtil.checkTime(issueOverDate.getText().toString()),
                                Double.valueOf(issueBugPrice.getText().toString()) ,
                                TimeUtil.checkTime(issueFreeBegin.getText().toString()),
                                TimeUtil.checkTime(issueFreeEnd.getText().toString()),
                                Double.valueOf(issueRentPrice.getText().toString()),
                                issueProperty.getText().toString(),
                                issueAddress.getText().toString(),
                                issueRemark.getText().toString(),
                                EquipmentType.findSecondEquipmentTypes("地质类").get(0).getId(),
                                LoginUserRegisterUtil.user.getId(),
                                1);
                        String test;
                        if (isSuccess == true) {
                            test = "成功啦";
                        } else {
                            test = "失败啦";
                        }
//                        Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.issue_pic:
                Choose_Pic_or_Cam();//上传图片时，拍照还是图库。
                break;
            case R.id.issue_buy_date:
                new DatePickerDialog(IssueDetailActivity.this, onDateSetListener1, mYear, mMonth, mDay).show();
                break;
            case R.id.issue_over_date:
                new DatePickerDialog(IssueDetailActivity.this, onDateSetListener2, mYear, mMonth, mDay).show();
                break;
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
            issueOverDate.setText(days);
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

    private void Choose_Pic_or_Cam() {
        //将文件读写权限写在此处。
        if (ContextCompat.checkSelfPermission(IssueDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IssueDetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//参数为0,表示不跳向图库activity。
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                  /*.setTitle("选择图片：")
                  * */
                .setItems(new String[]{"相机", "图库"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                openAlbum();
                                break;
                            default:
                                break;
                        }

                    }
                });
        builder.create().show();
    }

    private boolean checkForm() {
        final String name = issueName.getText().toString();
        final String buyDate = issueBuyDate.getText().toString();
        final String freeBegin = issueFreeBegin.getText().toString();
        final String freeEnd = issueFreeEnd.getText().toString();
        final String buyPrice = issueBugPrice.getText().toString();
        final String overDate = issueOverDate.getText().toString();
        final String rentPrice = issueRentPrice.getText().toString();
        final String property = issueProperty.getText().toString();
        final String remark = issueRemark.getText().toString();
        final String address = issueAddress.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()) {
            issueName.setError("请输入设备名称!");
            isPass = false;
        } else {
            issueName.setError(null);
        }

        if (buyDate.isEmpty()) {
            issueBuyDate.setError("请选择购买日期!");
            isPass = false;
        } else {
            issueBuyDate.setError(null);
        }

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

        if (buyPrice.isEmpty()) {
            issueBugPrice.setError("请输入设备原购买价格!");
            isPass = false;
        } else {
            issueBugPrice.setError(null);
        }

        if (overDate.isEmpty()) {
            issueOverDate.setError("请选择设备到年限!");
            isPass = false;
        } else {
            issueOverDate.setError(null);
        }

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

        if (remark.isEmpty()) {
            issueRemark.setError("请输入备注信息!");
            isPass = false;
        } else {
            issueRemark.setError(null);
        }

        if (address.isEmpty()) {
            issueAddress.setError("请输入设备所在地址!");
            isPass = false;
        } else {
            issueAddress.setError(null);
        }


        return isPass;

    }

    /**
     * 打开相册
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_IMAGE);
    }

    /**
     * 拍摄
     */
    private void takePhoto() {
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(IssueDetailActivity.this, "com.example.lenovo.geographysharing.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //测试点
        Toast.makeText(IssueDetailActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAMERA);
    }

    /**
     * 图库访问许可后调用图片    此APP未用
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            }
        }
    }


    /**
     * 回调
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try {

                compressImageUtil.CreateFiles();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                if (Build.VERSION.SDK_INT >= 19) {//版本兼容
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
            } else if (requestCode == RESULT_CAMERA) {
                // 相机
                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                Bitmap showBitmap = compressImageUtil.getSmallBitmap_File(outputImage, 245, 180);//压缩图片
                img.setImageBitmap(showBitmap);
            }
        }


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

    /**
     * 相册选择图片
     * 大于4.4版本SDK
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    /**
     * 相册选择图片
     * 小于于4.4版本SDK
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 获取相册图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 显示图片
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//显示缩略图
            //Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 245, 180);//显示缩略图
            Bitmap showBitmap = compressImageUtil.getSmallBitmap_String(imagePath, 245, 180);
            img.setImageBitmap(showBitmap);

        } else {
            Toast.makeText(this, "请选择正确的图片路径！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void initData() {
//        issueSpinner.sette
        if (equipment!=null)
        {
//            issueSpinner
            issueName.setText(equipment.getName());
            issueBuyDate.setText(equipment.getStart().substring(0,10));
            issueFreeBegin.setText(equipment.getEnd().substring(0,10));
            issueFreeEnd.setText(equipment.getEnd().substring(0,10));
            issueBugPrice.setText(equipment.getExpense().toString());
            issueOverDate.setText(equipment.getOver().substring(0,10));
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
    public static void launchIssueDetailActivity(Activity activity,Equipment equipment)
    {
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        //防止多次实例调用
        /**
         * 传一个对象
         */
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EQUIPMENT,equipment);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

}

