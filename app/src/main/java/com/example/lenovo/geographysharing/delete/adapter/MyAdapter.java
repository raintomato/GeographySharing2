package com.example.lenovo.geographysharing.delete.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.lenovo.geographysharing.Element.BaseElement.Collection;
import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;
import com.example.lenovo.geographysharing.delete.utils.Utils;
import com.example.lenovo.geographysharing.delete.view.SlidingButtonView;

import java.util.ArrayList;
import java.util.List;

import com.example.lenovo.geographysharing.R;
/**
 * Created by jiangzn on 17/1/1.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener,View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;
    private ButtonInterface buttonInterface;

    private Bitmap tempBitmap;
    Context context;
    Activity activity;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;


    public List<EquipmentCollection> myEquipList = new ArrayList<>();

    private List<String> mDatas = new ArrayList<String>();

    private SlidingButtonView mMenu = null;

    public MyAdapter(Context context) {
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

        final EquipmentCollection favEquipment = myEquipList.get(position);
        final Equipment equipment = (Equipment) favEquipment.getProduct();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                holder.equip_name.setText(equipment.getName());
                if(equipment.getParameter().length()>16){
                    holder.equip_property.setText(equipment.getParameter().substring(0,16)+"...");
                }else {
                    holder.equip_property.setText(equipment.getParameter()+"...");
                }
                holder.equip_address.setText(equipment.getPlace());
//                holder.buttonDeleteEquipment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (buttonInterface != null) {
//                            buttonInterface.onClick(view, position);
//                        }
//                    }
//                });
                holder.equip_image.setImageBitmap(tempBitmap);
                /**
                 * 7-6 添加holder控制图片重复加载
                 */
                String istrurl = equipment.getPicture().getImageURL();
                if (null == holder || null == istrurl || istrurl.equals("")) {
                    return;
                }
                Glide.with(context)
                        .load(istrurl)
                        .placeholder(R.drawable.loading)
                        .into(holder.equip_image);
            }
        };
        holder.equip_image.setImageResource(R.drawable.loading);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    tempBitmap = JsonDataUtil.getImage(equipment.getPicture().getImageURL());
                }catch (Exception e){
                    e.printStackTrace();
                }
                Message m = handler.obtainMessage();
                handler.sendMessage(m);
            }
        });
        thread.start();

        final MyViewHolder viewHolder = (MyViewHolder) holder;

        viewHolder.slidingButtonView.setSlidingButtonListener(MyAdapter.this);

        viewHolder.equip_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                        break;
                }
            }
        });
        viewHolder.equip_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                        break;
                }
            }
        });
        viewHolder.equip_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
                        break;
                }
            }
        });
        viewHolder.equip_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    default:
                        AlbumDetailActivity.launchAlbumDetailActivity(activity, (Equipment) myEquipList.get(position).getProduct());
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
                        EquipmentCollection.cancelCollection(myEquipList.get(position).getId());
                        myEquipList.remove(position);
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
        return myEquipList.size();
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
        }

    }

}
