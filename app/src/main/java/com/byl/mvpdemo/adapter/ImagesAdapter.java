package com.byl.mvpdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.byl.mvpdemo.R;
import com.byl.mvpdemo.model.modelbean.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends BaseAdapter<ImagesAdapter.MyViewHolder> {

    public ImagesAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_girls, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        News news = (News) listDatas.get(position);//转换
        if (!TextUtils.isEmpty(news.getPicUrl())) {
            Picasso.with(context).load(news.getPicUrl()).error(R.mipmap.banner).into(holder.img);
        } else {
            holder.img.setImageResource(R.mipmap.banner);
        }
        holder.tv_title.setText(news.getTitle());
        holder.iv_zan.setOnClickListener(new ViewClikListener(onViewClickListener, position, 1));//赞 viewtype=1代表赞点击事件
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;//内容
        ImageView img, iv_zan;//赞

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_zan = (ImageView) view.findViewById(R.id.iv_zan);

        }
    }

}
