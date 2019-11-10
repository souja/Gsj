package com.wangxiaobao.gsj.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangxiaobao.waiter.R;
import com.wangxiaobao.gsj.enity.dish.Dish;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

import static android.graphics.Bitmap.createBitmap;

/**
 * 推荐菜适配器
 */
public class RecommendDishListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =
            RecommendDishListAdapter.class
                    .getSimpleName();
    private Context context;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_FOOTER = 2;



    public void setNeedFooter(boolean needFooter) {
        isNeedFooter = needFooter;
        notifyDataSetChanged();
    }

    private boolean isNeedFooter;
    private RecommendDishListFragment mRecommendDishListFragment;

    private ArrayList<Dish> mDishList = new
            ArrayList<>();


    public RecommendDishListAdapter(Context context, RecommendDishListFragment recommendDishListFragment) {
        this.context = context;
        mRecommendDishListFragment = recommendDishListFragment;
    }


//
//    @Override
//    public int getItemViewType(int position) {
//        if (isNeedFooter) {
//            if (position == getItemCount() - 1) {
//                return ITEM_TYPE_FOOTER;
//            } else {
//                return ITEM_TYPE_CONTENT;
//            }
//        } else {
//            return super.getItemViewType(position);
//        }
//
//    }

    public void setDishList(List<Dish> dishList) {
        mDishList.clear();
        mDishList.addAll(dishList);
        notifyDataChanged();
    }


    public ArrayList<Dish> getData() {
        return mDishList;
    }


    private void notifyDataChanged() {
        if (mDishList.size() == 3) {
            isNeedFooter = false;
        } else {
            isNeedFooter = true;
        }
        super.notifyDataSetChanged();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend_dish, parent, false));

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;

            Dish dish = mDishList.get(position);
            loadDishPicture(holder, dish);
            holder.dishName.setText(dish.getDishName());

        }

    }




    @Override
    public int getItemCount() {
        return mDishList.size();
    }


    private void loadDishPicture(final ViewHolder holder, Dish dish) {


        if (!TextUtils.isEmpty(dish
                .getPicture())) {
            final Picasso picasso = Picasso.with(context);
            picasso.with(context).load(dish.getPicture())
                    .into(holder.dishPic, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            holder.dishPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onError() {
                            holder.dishPic.setImageResource(R.drawable.lll);
                            holder.dishPic.setScaleType(ImageView.ScaleType.CENTER);
                        }
                    });

        } else {
            holder.dishPic.setScaleType(ImageView.ScaleType.CENTER);
            holder.dishPic.setImageResource(R.drawable.lll);
        }
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dish_picture)
        public ImageView dishPic;



        @BindView(R.id.dish_name)
        public TextView dishName;






        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
