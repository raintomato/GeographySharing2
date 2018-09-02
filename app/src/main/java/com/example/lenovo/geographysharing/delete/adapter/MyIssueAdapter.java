package com.example.lenovo.geographysharing.delete.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.lenovo.geographysharing.Details.AlbumDetailActivity;
import com.example.lenovo.geographysharing.Details.AlbumDetailPeopleActivity;
import com.example.lenovo.geographysharing.Details.AlbumDetailSoftActivity;
import com.example.lenovo.geographysharing.Element.BaseElement.Collection;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Element.EquipmentIssue;
import com.example.lenovo.geographysharing.Element.Software;
import com.example.lenovo.geographysharing.Element.SoftwareIssue;
import com.example.lenovo.geographysharing.Element.Technology;
import com.example.lenovo.geographysharing.Element.TechnologyIssue;
import com.example.lenovo.geographysharing.Person.MyissuedActivity;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.delete.utils.Utils;
import com.example.lenovo.geographysharing.delete.view.SlidingButtonView;

import java.util.ArrayList;
import java.util.List;

import com.example.lenovo.geographysharing.R;
/**
 * Created by jiangzn on 17/1/1.
 */

public class MyIssueAdapter extends RecyclerView.Adapter<MyIssueAdapter.MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener,View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;
    private ButtonInterface buttonInterface;

    private Bitmap tempBitmap;
    Context context;
    Activity activity;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;


    public List<EquipmentIssue> myEquipList = new ArrayList<>();
    public List<SoftwareIssue> mySoftList = new ArrayList<>();
    public List<TechnologyIssue> myTechList = new ArrayList<>();


    private List<String> mDatas = new ArrayList<String>();

    private SlidingButtonView mMenu = null;

    public MyIssueAdapter(Context context) {
        this.context = context;
        activity=(Activity)context;
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
    }

    /**
     * 7-8
     * 重复图片无法加载则进行回收
     * @param holder
     */


    @Override
    public void onViewRecycled(MyViewHolder holder) {
        if (holder != null)
        {
            Glide.clear(holder.equip_image);
        }
        super.onViewRecycled(holder);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_layout, parent, false);
        //7-9

        MyViewHolder viewHolder=new MyViewHolder(view);
        final MyViewHolder eViewholer = new MyViewHolder(view);
        eViewholer.equipView=view;
        eViewholer.equipView.setOnClickListener(this);
        return viewHolder;
    }



    boolean allopen = false;

    public void setAllopen(boolean allopen) {
        this.allopen = allopen;

    }

//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//        final MyViewHolder viewHolder = (MyViewHolder) holder;
//        viewHolder.slidingButtonView.setSlidingButtonListener(MyAdapter.this);
//
//        viewHolder.equip_name.setText(mDatas.get(position));
//
//        //设置内容布局的宽为屏幕宽度
//        viewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(context) + viewHolder.rl_left.getLayoutParams().width;
////        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                //判断是否有删除菜单打开
//////                if (menuIsOpen()) {
//////                    closeMenu();//关闭菜单
//////                } else {
//////                    int n = viewHolder.getLayoutPosition();
//////                    mIDeleteBtnClickListener.onItemClick(v, n);
//////                }
////
////            }
////        });
//
//        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int n = holder.getLayoutPosition();
//                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
//            }
//        });
//        if (allopen) {
//            viewHolder.slidingButtonView.openMenu();
//            viewHolder.slidingButtonView.setCanTouch(false);
//        } else {
//            viewHolder.slidingButtonView.closeMenu();
//            viewHolder.slidingButtonView.setCanTouch(true);
//        }
//
//    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        EquipmentIssue favEquipment =null;
        Equipment equipment = null;
        SoftwareIssue softwareIssue =null;
        Software software = null;
        TechnologyIssue technologyIssue = null;
        Technology technology =null;

        switch (MyissuedActivity.typeIsssu){
            case "设备":
                favEquipment = myEquipList.get(position);
                equipment = (Equipment) favEquipment.getProduct();
                holder.equip_name.setText(equipment.getName());
                holder.equip_property.setText(equipment.getParameter());
                holder.equip_address.setText(equipment.getPlace());
                holder.equip_image.setImageResource(R.drawable.loading);
                if(equipment.getIs_checked()){
                    holder.equip_status.setText("发布成功");
                    holder.equip_status.setTextColor(Color.parseColor("#00FF77"));
                }else {
                    holder.equip_status.setText("审核中");
                    holder.equip_status.setTextColor(Color.parseColor("#AA5500"));
                }
                final String imageUrl = equipment.getPicture().getImageURL();
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        holder.equip_image.setImageBitmap(tempBitmap);
                        /**
                         * 7-6 添加holder控制图片重复加载
                         */
                        String istrurl = imageUrl;
                        if (null == holder || null == istrurl || istrurl.equals("")) {
                            return;
                        }
                        Glide.with(context)
                                .load(istrurl)
                                .placeholder(R.drawable.loading)
                                .into(holder.equip_image);
                    }
                };

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            tempBitmap = JsonDataUtil.getImage(imageUrl);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Message m = handler.obtainMessage();
                        handler.sendMessage(m);
                    }
                });
                thread.start();

                break;
            case "软件":
                softwareIssue = mySoftList.get(position);
                software = (Software) softwareIssue.getProduct();
                holder.equip_name.setText(software.getName());
                holder.equip_property.setText(software.getDescribe());
                holder.equip_address.setText(software.getOwner().getAddress());
                if(software.isChecked()){
                    holder.equip_status.setText("发布成功");
                    holder.equip_status.setTextColor(Color.parseColor("#00FF77"));
                }else {
                    holder.equip_status.setText("审核中");
                    holder.equip_status.setTextColor(Color.parseColor("#AA5500"));
                }
                holder.equip_image.setImageResource(R.drawable.sw);
                break;
            case "人员":
                technologyIssue = myTechList.get(position);
                technology = (Technology) technologyIssue.getProduct();
                holder.equip_name.setText(technology.getName());
                holder.equip_property.setText(technology.getInfo());
                holder.equip_address.setText(technology.getOwner().getAddress());
                if(technology.isChecked()){
                    holder.equip_status.setText("发布成功");
                    holder.equip_status.setTextColor(Color.parseColor("#00FF77"));
                }else {
                    holder.equip_status.setText("审核中");
                    holder.equip_status.setTextColor(Color.parseColor("#AA5500"));
                }
                holder.equip_image.setImageResource(R.drawable.techpeople);
                break;
            default:
                break;
        }
//                holder.buttonDeleteEquipment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (buttonInterface != null) {
//                            buttonInterface.onClick(view, position);
//                        }
//                    }
//                });

        final MyViewHolder viewHolder = (MyViewHolder) holder;

        viewHolder.slidingButtonView.setSlidingButtonListener(MyIssueAdapter.this);

        viewHolder.equip_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        switch (MyissuedActivity.typeIsssu){
                            case "设备":
                                AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                                break;
                            case "软件":
                                AlbumDetailSoftActivity.launchAlbumDetailSoftActivity(activity,(Software)mySoftList.get(position).getProduct());
                                break;
                            case "人员":
                                AlbumDetailPeopleActivity.launchAlbumDetailPeoPleActivity(activity,(Technology)myTechList.get(position).getProduct());
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        });
        viewHolder.equip_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        switch (MyissuedActivity.typeIsssu){
                            case "设备":
                                AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                                break;
                            case "软件":
                                AlbumDetailSoftActivity.launchAlbumDetailSoftActivity(activity,(Software)mySoftList.get(position).getProduct());
                                break;
                            case "人员":
                                AlbumDetailPeopleActivity.launchAlbumDetailPeoPleActivity(activity,(Technology)myTechList.get(position).getProduct());
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        });
        viewHolder.equip_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        switch (MyissuedActivity.typeIsssu){
                            case "设备":
                                AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                                break;
                            case "软件":
                                AlbumDetailSoftActivity.launchAlbumDetailSoftActivity(activity,(Software)mySoftList.get(position).getProduct());
                                break;
                            case "人员":
                                AlbumDetailPeopleActivity.launchAlbumDetailPeoPleActivity(activity,(Technology)myTechList.get(position).getProduct());
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        });
        viewHolder.equip_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        switch (MyissuedActivity.typeIsssu){
                            case "设备":
                                AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                                break;
                            case "软件":
                                AlbumDetailSoftActivity.launchAlbumDetailSoftActivity(activity,(Software)mySoftList.get(position).getProduct());
                                break;
                            case "人员":
                                AlbumDetailPeopleActivity.launchAlbumDetailPeoPleActivity(activity,(Technology)myTechList.get(position).getProduct());
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }
        });
        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        switch (MyissuedActivity.typeIsssu){
                            case "设备":
                                Equipment.cancelEquipment(((Equipment)(myEquipList.get(position).getProduct())).getId());
                                myEquipList.remove(position);
                                break;
                            case "软件":
                                Software.cancelSoftwares(((Software)mySoftList.get(position).getProduct()).getId());
                                mySoftList.remove(position);
                                break;
                            case "人员":
                                Technology.cancelTechnology(((Technology)myTechList.get(position).getProduct()).getId());
                                myTechList.remove(position);
                                break;
                            default:
                                break;
                        }
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    }
                }).start();

                /**
                 * 7-8
                 * 在此处删除
                 */
                Log.i("98k", "onClick: "+position);
            }
        });
        if (allopen) {
            viewHolder.slidingButtonView.openMenu();
            viewHolder.slidingButtonView.setCanTouch(false);
        } else {
            viewHolder.slidingButtonView.closeMenu();
            viewHolder.slidingButtonView.setCanTouch(true);
        }


        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        int tempSize = 0;
        switch (MyissuedActivity.typeIsssu){
            case "设备":
                tempSize = myEquipList.size();
                break;
            case "软件":
                tempSize = mySoftList.size();
                break;
            case "人员":
                tempSize = myTechList.size();
                break;
            default:
                break;
        }
        return  tempSize;
    }


    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }

    public void addData(int position) {
        mDatas.add(position, "添加项");
        notifyItemInserted(position);
    }


    public interface ButtonInterface {
        void onClick(View view, int position);
    }
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }
    //接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    /**
     * 7-8
     * 删除
     * @param position
     */
    public void removeData(int position) {
        myEquipList.remove(position);
        notifyItemRemoved(position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView equip_name;
        public TextView equip_property;
        public TextView equip_address;
        public ImageView equip_image;
        public RelativeLayout rl_left;
        public ViewGroup layout_content;
        public SlidingButtonView slidingButtonView;
        public View equipView;
        public TextView btn_Delete;
        public TextView equip_status;



        public MyViewHolder(View itemView) {
            super(itemView);
            equip_name = (TextView) itemView.findViewById(R.id.equip_name);
            equip_property = (TextView) itemView.findViewById(R.id.equip_property);
            equip_address = (TextView) itemView.findViewById(R.id.equip_address);
            equip_image = (ImageView) itemView.findViewById(R.id.equip_image);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            slidingButtonView = (SlidingButtonView) itemView;
            rl_left = (RelativeLayout) itemView.findViewById(R.id.rl_left);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            equip_status = (TextView)itemView.findViewById(R.id.equip_status);
        }

    }

}
