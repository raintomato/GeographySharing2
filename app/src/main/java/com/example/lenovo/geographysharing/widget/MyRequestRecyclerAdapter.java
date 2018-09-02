package com.example.lenovo.geographysharing.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.Element.Demand;
import com.example.lenovo.geographysharing.EntityClass.Equipment;
import com.example.lenovo.geographysharing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/12/31.
 */

public class MyRequestRecyclerAdapter extends RecyclerView.Adapter<MyRequestRecyclerAdapter.EViewholer> implements View.OnClickListener{
    //声明接口变量
    private OnItemClickListener mOnItemClickListener = null;
    private ButtonInterface buttonInterface;

    //接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //创建存储设备的数组。
    public List<Demand> myEquipList = new ArrayList<>();
    //控件的绑定
    static class EViewholer extends RecyclerView.ViewHolder {
        //声明控件
        TextView equip_name;
        TextView equip_property;
        TextView equip_address;
        ImageView equip_image;
        ImageButton delete_request;
        //声明item
        View equipView;

        public EViewholer(View itemView) {
            super(itemView);
            equipView = itemView;
            //实例化控件
            equip_name = (TextView) itemView.findViewById(R.id.equip_name);
            equip_property = (TextView) itemView.findViewById(R.id.equip_property);
            equip_address = (TextView) itemView.findViewById(R.id.equip_address);
            equip_image = (ImageView) itemView.findViewById(R.id.equip_image);
//            delete_request = (ImageButton)itemView.findViewById(R.id.img_btn_delete_request);
        }
    }

    //adapter构造函数
    public MyRequestRecyclerAdapter() {
    }

    @Override
    public EViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_request_item, parent, false);
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

    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }
    public interface ButtonInterface
    {
        void onClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(EViewholer holder, final int position) {
        Demand equipment = myEquipList.get(position);
        holder.equip_name.setText(equipment.getTitle());
        holder.equip_property.setText(equipment.getContent());
        holder.equip_address.setText(equipment.getDemander().getAddress());
        holder.equip_image.setImageResource(R.drawable.loading);
//        holder.delete_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (buttonInterface!=null)
//                {
//                    buttonInterface.onClick(view,position);
//                }
//            }
//        });

        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return myEquipList.size();
    }
    //暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
