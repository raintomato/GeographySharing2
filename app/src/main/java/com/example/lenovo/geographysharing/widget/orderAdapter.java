package com.example.lenovo.geographysharing.widget;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.geographysharing.BaseClass.BaseFragment;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.User;
import com.example.lenovo.geographysharing.EntityClass.orderItem;
import com.example.lenovo.geographysharing.R;

import java.util.ArrayList;
import java.util.List;
import com.example.lenovo.geographysharing.Element.Order;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.Utils.LoginUserRegisterUtil;

import static com.example.lenovo.geographysharing.Issue.Application.getContext;

/**
 * Created by Lisa on 2018/4/21.
 */

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.EViewholer> implements View.OnClickListener {

    //声明接口变量
    private OnItemClickListener mOnItemClickListener = null;
    private User user;
    private Equipment equipment;
    private Bitmap tempBitmap;
    ProgressDialog proDialog =null;
    //接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //创建存储订单的数组
    public List<Order> myOrderList = new ArrayList<>();

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
            shopName=(TextView) itemView.findViewById(R.id.shop_name);//商店名
            goodStatus=(TextView) itemView.findViewById(R.id.good_status);//状态
            goodImage=(ImageView) itemView.findViewById(R.id.good_pic);//图片
            goodName=(TextView) itemView.findViewById(R.id.good_name);//设备的具体名称
            goodPrice=(TextView) itemView.findViewById(R.id.good_price);//单价
            goodNum=(TextView) itemView.findViewById(R.id.good_num);//个数
            totalPrice=(TextView) itemView.findViewById(R.id.total_price);//应付金额
        }
    }

    //adapter构造函数
    public orderAdapter(List<orderItem> order) {

//        myOrderList = order;

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
    public void onBindViewHolder(final EViewholer holder, int position) {
        final Order order=myOrderList.get(position);

//        shopName=(TextView) itemView.findViewById(R.id.shop_name);//卖家
//        goodStatus=(TextView) itemView.findViewById(R.id.good_status);//状态
//        goodImage=(ImageView) itemView.findViewById(R.id.good_pic);//图片
//        goodName=(TextView) itemView.findViewById(R.id.good_name);//设备的具体名称
//        goodPrice=(TextView) itemView.findViewById(R.id.good_price);//单价
//        goodNum=(TextView) itemView.findViewById(R.id.good_num);//个数
//        totalPrice=(TextView) itemView.findViewById(R.id.total_price);//应付金额

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(order.getPayee().getId()== LoginUserRegisterUtil.user.getId()){
                    holder.shopName.setText(user.getNick_name()+"(卖家)");
                }else {
                    holder.shopName.setText(user.getNick_name()+"(买家)");
                }
                if(order.getStatus()){
                    holder.goodStatus.setText("已支付");
                }else {
                    holder.goodStatus.setText("未支付");
                }
                try{
                    holder.goodName.setText(equipment.getName());
                    holder.goodPrice.setText(equipment.getPrice().toString());
                    holder.goodNum.setText((int)(order.getTotal_amount()/equipment.getPrice())+"");
                    holder.totalPrice.setText(order.getTotal_amount().toString());
                    holder.goodImage.setImageBitmap(tempBitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    user = User.findUser(order.getPayee().getId());
                    equipment = Equipment.findEquipment(((Equipment)(order.getProduct())).getId());
                    tempBitmap = JsonDataUtil.getImage(equipment.getPicture().getImageURL());
                }catch (Exception e){
                    e.printStackTrace();
                }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });
        thread.start();


        holder.goodImage.setImageResource(R.drawable.loading);

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
