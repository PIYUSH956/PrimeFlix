package com.piyushjaiswal12.primeflix.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.model.WebSeriesClass;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder>
        {
    private Context context;
    private List<WebSeriesClass> list;
            private int lastPosition = -1;
            private onClickListener listener;





    public TrendingAdapter(Context context, List<WebSeriesClass> list,onClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trending,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(context).load("https://geekstocode.com/web_series_images/"+list.get(position).getThumb()).into(holder.imageView);
        setAnimation(holder.imageView, position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick1(position);
            }
        });

    }
            private void setAnimation(View viewToAnimate, int position) {

                if (position > lastPosition) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.right_to_left);
                    viewToAnimate.startAnimation(animation);
                    lastPosition = position;
                }
            }

    @Override
    public int getItemCount() {
        return list.size();
    }




            static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }


      public interface onClickListener
            {
                void onClick1(int position);
            }





}
