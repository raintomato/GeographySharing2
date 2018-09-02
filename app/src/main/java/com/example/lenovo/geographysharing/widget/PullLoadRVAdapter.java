package com.example.lenovo.geographysharing.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Details.DetailListFragment;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.Element.Technology;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.others.FragmentManagerWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 2017/12/31.
 */

public class PullLoadRVAdapter extends RecyclerView.Adapter<PullLoadRVAdapter.EViewholer> implements View.OnClickListener{
    //声明接口变量
    private OnItemClickListener mOnItemClickListener = null;
    private Bitmap tempBitmap = null;
    Context context;
    //接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;
    //创建存储设备的数组。
    public List<Equipment> myEquipList = new ArrayList<>();
    public List<Technology> myTechList = new ArrayList<>();
    public List<Software> mysoftList = new ArrayList<>();

    //控件的绑定
    static class EViewholer extends RecyclerView.ViewHolder {
        //声明控件
        TextView equip_name;
        TextView equip_property;
        TextView equip_address;
        TextView equip_price;
        ImageView equip_image;

        //声明item
        View equipView;

        public EViewholer(View itemView) {
            super(itemView);
            equipView = itemView;
            //实例化控件
            equip_name = (TextView) itemView.findViewById(R.id.equip_name);
            equip_property = (TextView) itemView.findViewById(R.id.equip_property);
            equip_address = (TextView) itemView.findViewById(R.id.equip_address);
            equip_price = (TextView) itemView.findViewById(R.id.equip_price);
            equip_image = (ImageView) itemView.findViewById(R.id.equip_image);
        }
    }

    //adapter构造函数

    /**
     * 7-6添加context传值
     * @param context
     */
    public PullLoadRVAdapter(Context context) {
//        myEquipList = equipments;
        this.context=context;
    }

    /**
     * 7-6
     * 重复图片无法加载则进行回收
     * @param holder
     */
    @Override
    public void onViewRecycled(EViewholer holder) {
        if (holder != null)
        {
            Glide.clear(holder.equip_image);
        }
        super.onViewRecycled(holder);
        super.onViewRecycled(holder);
    }

    @Override
    public EViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_item, parent, false);
        //实例化Viewholder
        //点击事件

        final EViewholer eViewholer = new EViewholer(view);
//        eViewholer.equipView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position=eViewholer.getAdapterPosition();
//                Equipment equipment=myEquipList.get(position);
//                Toast.makeText(v.getContext(), "You click the View "+equipment.getEquipName(), Toast.LENGTH_SHORT).show();
////                initFragemnt();
////                switchFragment(DetailsFragment.class);
//            }
//        });
        eViewholer.equipView.setOnClickListener(this);
        return eViewholer;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }


    @Override
    public void onBindViewHolder(final EViewholer holder, final int position) {
        Equipment equipment = null;
        Software software = null;
        Technology technology= null;
        switch (DetailListFragment.type) {
            case "地质类":
            case "物探类":
            case "测量类":
            case "检测类":
               equipment = myEquipList.get(position);
                break;
            case "软件":
                software = mysoftList.get(position);
                break;
            case "人员":
                technology  = myTechList.get(position);
                break;
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                /**
                 * 7-9
                 */

                holder.equip_image.setImageBitmap(tempBitmap);
                /**
                 * 7-6 添加holder控制图片重复加载
                 */
                String istrurl = myEquipList.get(position).getPicture().getImageURL();
                if (null == holder || null == istrurl || istrurl.equals("")) {
                    return;
                }
                Glide.with(context)
                        .load(istrurl)
                        .placeholder(R.drawable.loading)
                        .into(holder.equip_image);
            }
        };



//        if(equipment!=null){
//            holder.equip_name.setText(equipment.getName());
//            holder.equip_property.setText(equipment.getParameter());
//            holder.equip_address.setText(equipment.getPlace());
//            holder.equip_price.setText(equipment.getPrice().toString()+"元/天");
//            holder.equip_image.setImageResource(R.drawable.loading);
//        }
//        if(software!=null){
//
//        }
//        if(technology!=null){
//
//        }
        switch (DetailListFragment.type){
            case "地质类":
            case "物探类":
            case "测量类":
            case "检测类":
                holder.equip_name.setText(equipment.getName());
                holder.equip_property.setText(equipment.getParameter());
                holder.equip_address.setText(equipment.getPlace());
                holder.equip_price.setText(equipment.getPrice().toString()+"元/天");
                holder.equip_image.setImageResource(R.drawable.loading);
                break;
            case "软件":
                holder.equip_name.setText(software.getName());
                holder.equip_property.setText(software.getDescribe());
                holder.equip_address.setText(software.getOs());
                holder.equip_price.setText(software.getPrice().toString()+"元/天");
                holder.equip_image.setImageResource(R.drawable.sw);
                break;
            case "人员":
                holder.equip_name.setText(technology.getName());
                holder.equip_property.setText(technology.getInfo());
                holder.equip_address.setText(technology.getComment());
                holder.equip_price.setText(technology.getPrice().toString()+"元/天");
                holder.equip_image.setImageResource(R.drawable.techpeople);
                break;
            default:
                break;
        }
        if(equipment!=null){
        String ImageUrl= null;
        ImageUrl = equipment.getPicture().getImageURL();
        final String finalImageUrl = ImageUrl;

        Thread t=new Thread(new Runnable(){

            @Override
            public void run() {
                try{
                    tempBitmap=JsonDataUtil.getImage(finalImageUrl);//应该写到本地
                }
                catch (Exception e){

                }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });
        t.start();
        //将position保存在itemView的Tag中，以便点击时进行获取
        }

        holder.itemView.setTag(position);
    }
    @Override
    public int getItemCount() {
        int tempSize =0;
        switch (DetailListFragment.type) {
            case "地质类":
            case "物探类":
            case "测量类":
            case "检测类":
                tempSize =myEquipList.size();
            break;
            case "软件":
                tempSize = mysoftList.size();
                break;
            case "人员":
                tempSize = myTechList.size();
                break;
            default:
                tempSize =0;
                break;
        }

        Log.i("size", "getItemCount: "+tempSize);

        return tempSize;
    }
    //暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}