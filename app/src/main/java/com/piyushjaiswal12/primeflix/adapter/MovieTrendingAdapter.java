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
import com.piyushjaiswal12.primeflix.model.MoviesClass;

import java.util.List;

public class MovieTrendingAdapter extends RecyclerView.Adapter<MovieTrendingAdapter.MyViewHolder> {

        private Context context;
        private List<MoviesClass> list;
        private int lastPosition = -1;
        private MovieTrendingAdapter.onClickListener listener;





    public MovieTrendingAdapter(Context context, List<MoviesClass> list, MovieTrendingAdapter.onClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;


    }

        @NonNull
        @Override
        public MovieTrendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trending,parent,false);
        return new MovieTrendingAdapter.MyViewHolder(v);
    }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(context).load("https://geekstocode.com/movies_images/"+list.get(position).getThumb()).into(holder.imageView);
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
