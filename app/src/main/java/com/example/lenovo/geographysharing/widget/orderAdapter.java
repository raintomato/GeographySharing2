package com.example.lenovo.geographysharing.widget;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.EntityClass.orderItem;
import com.example.lenovo.geographysharing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lisa on 2018/4/21.
 */

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.EViewholer> implements View.OnClickListener {

    //声明接口变量
    private OnItemClickListener mOnItemClickListener = null;
    //接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //创建存储订单的数组
    private List<orderItem> myOrderList = new ArrayList<>();

    static class EViewholer extends RecyclerView.ViewHolder{
        //声明控件
        TextView shopName;
        TextView goodStatus;   //商品状态
        ImageView goodImage;
        TextView goodName;
        TextView goodPrice;
        TextView goodNum;
        TextView totalPrice;  //总价

        //声明item
        View equipView;

        public EViewholer(View itemView){
            super(itemView);
            equipView = itemView;
            //实例化控件
            shopName=(TextView) itemView.findViewById(R.id.shop_name);
            goodStatus=(TextView) itemView.findViewById(R.id.good_status);
            goodImage=(ImageView) itemView.findViewById(R.id.good_pic);
            goodName=(TextView) itemView.findViewById(R.id.good_name);
            goodPrice=(TextView) itemView.findViewById(R.id.good_price);
            goodNum=(TextView) itemView.findViewById(R.id.good_num);
            totalPrice=(TextView) itemView.findViewById(R.id.total_price);
        }
    }

    //adapter构造函数
    public orderAdapter(List<orderItem> order) {
        myOrderList = order;
    }

    @Override
    public void onClick(View view) {

        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    @Override
    public EViewholer onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        final EViewholer eViewholer = new EViewholer(view);
        eViewholer.equipView.setOnClickListener(this);
        return eViewholer;
    }

    @Override
    public void onBindViewHolder(EViewholer holder, int position) {
        orderItem order=myOrderList.get(position);
        holder.shopName.setText(order.getshopName());
        holder.goodStatus.setText(order.getgoodStatus());
        holder.goodName.setText(order.getgoodName());
        holder.goodPrice.setText(order.getgoodPrice());
        holder.goodNum.setText(order.getgoodNum());
        holder.totalPrice.setText(order.gettotalPrice());
        holder.goodImage.setImageResource(order.getImageId());

        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    //暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
