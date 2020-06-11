package com.piyushjaiswal12.primeflix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.model.UpComingClass;

import java.util.List;

public class UpComingAdapter extends RecyclerView.Adapter<UpComingAdapter.MyViewHolder> {


    private Context context;
    private List<UpComingClass> list;
    private UpComingAdapter.clickListener listener;
    private View v;

    public UpComingAdapter(Context context, List<UpComingClass> list, UpComingAdapter.clickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UpComingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        v = LayoutInflater.from(context).inflate(R.layout.children,parent,false);
        return new UpComingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingAdapter.MyViewHolder holder, final int position) {

        Glide.with(context).load("https://geekstocode.com/upcoming_images/"+list.get(position).getThumb()).into(holder.imageView);
        Log.d("checking","https://geekstocode.com.episodes_thumb/"+list.get(position).getThumb());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClicker(position);
            }
        });

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

    public interface   clickListener
    {
        void onClicker(int position);
    }



}
