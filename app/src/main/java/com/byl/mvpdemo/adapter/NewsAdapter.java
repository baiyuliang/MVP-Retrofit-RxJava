package com.byl.mvpdemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.model.modelbean.ImageModel;
import com.byl.mvpdemo.model.modelbean.News;
import com.byl.mvpdemo.model.modelbean.NewsModel;
import com.byl.mvpdemo.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter<NewsAdapter.MyViewHolder> {

    boolean isViewPagerLoadScucess = false;//viewpager是否加载成功

    /**
     * @param context
     * @param listDatas1          banner图片数据
     * @param listDatas2          新闻列表数据
     * @param onViewClickListener 我们要设置item（header）中某控件的点击事件
     */
    public NewsAdapter(Context context, List<Object> listDatas1, List<Object> listDatas2, OnViewClickListener onViewClickListener) {
        super(context, listDatas1, listDatas2, onViewClickListener);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);//当前环境不继承父类的点击事件
        if (position == 0) {//header
            if (!isViewPagerLoadScucess && listDatas1.size() > 0) {
                initViewPager(holder);
            }
            holder.ll_1.setOnClickListener(new ViewClikListener(onViewClickListener, position, 1));
            holder.ll_2.setOnClickListener(new ViewClikListener(onViewClickListener, position, 2));
            holder.ll_3.setOnClickListener(new ViewClikListener(onViewClickListener, position, 3));
            holder.ll_4.setOnClickListener(new ViewClikListener(onViewClickListener, position, 4));
        } else {//列表
            News news = (News) listDatas2.get(position - 1);//转换（注意：是position-1）
            if (!TextUtils.isEmpty(news.getPicUrl())) {
                Picasso.with(context).load(news.getPicUrl()).error(R.mipmap.banner).into(holder.iv_icon);
            } else {
                holder.iv_icon.setImageResource(R.mipmap.banner);
            }
            holder.tv_title.setText(news.getTitle());//
            holder.tv_des.setText(news.getDescription());//
            holder.tv_time.setText(news.getCtime());//
            holder.itemView.setOnClickListener((v) -> {//item点击事件
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position - 1);
                }
            });
        }
    }

    /**
     * 注意事项：
     * 因为banner为一个header，属于recycleview的一个item，
     * 当前情况下，listDatas1为banner数据，listDatas2为新闻列表数据
     * 即便此时listDatas1有多条数据，但是是属于头部item 的数据源，所以整体的ItemCount=1+listDatas2.size()，1即代表header；
     * <p>
     * 如果是非header的情况，即两个数据源的列表，则ItemCount=listDatas1.size()+listDatas2.size()；
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return 1 + listDatas2.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.item_home_head;
        } else {
            return R.layout.item_news;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ViewPager vp;
        LinearLayout ll_1, ll_2, ll_3, ll_4;

        ImageView iv_icon;//
        TextView tv_title, tv_des, tv_time;//

        public MyViewHolder(View view) {
            super(view);
            vp = (ViewPager) view.findViewById(R.id.vp);//banner
            ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);//新闻1
            ll_2 = (LinearLayout) view.findViewById(R.id.ll_2);//新闻2
            ll_3 = (LinearLayout) view.findViewById(R.id.ll_3);//新闻3
            ll_4 = (LinearLayout) view.findViewById(R.id.ll_4);//新闻4

            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);//
            tv_title = (TextView) view.findViewById(R.id.tv_title);//标题
            tv_des = (TextView) view.findViewById(R.id.tv_des);//内容
            tv_time = (TextView) view.findViewById(R.id.tv_time);//时间
        }
    }


    private void initViewPager(MyViewHolder holder) {
        isViewPagerLoadScucess = true;
        List<View> imageViews = new ArrayList<>();
        for (int i = 0; i < listDatas1.size(); i++) {
            final ImageModel imageModel = (ImageModel) listDatas1.get(i);
            View view = mInflater.inflate(R.layout.item_img, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img);
            Picasso.with(context).load(imageModel.getUrl()).error(R.mipmap.banner).into(imageView);
            //设置广告点击事件
            view.setOnClickListener(v -> {
                Toast.makeText(context, "图片>>" + imageModel.getUrl(), Toast.LENGTH_SHORT).show();
            });
            imageViews.add(view);
        }
        holder.vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViews.size();
            }

            @Override
            public Object instantiateItem(ViewGroup arg0, int arg1) {
                arg0.addView(imageViews.get(arg1));
                return imageViews.get(arg1);
            }

            @Override
            public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
                arg0.removeView((View) arg2);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    public void setViewPagerLoadScucess(boolean viewPagerLoadScucess) {
        isViewPagerLoadScucess = viewPagerLoadScucess;
    }
}
