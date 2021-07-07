package com.piyushjaiswal12.primeflix.adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.model.EpisodesClass;

import java.util.HashMap;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.MyViewHolder> {

    private Context context;
    private List<EpisodesClass> list;
    private clickListener listener;
    private View v;

    public EpisodesAdapter(Context context, List<EpisodesClass> list,clickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

   v = LayoutInflater.from(context).inflate(R.layout.episodes_recycler,parent,false);
    return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Glide.with(context).load("https://geekstocode.com/"+list.get(position).getThumb()).into(holder.imageView);
      Log.d("url","https://geekstocode.com/"+list.get(position).getThumb());
        holder.textView.setText(list.get(position).getEpisodes());
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

        TextView textView,time;
        ImageView imageView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.epi_image);
            textView = itemView.findViewById(R.id.episode_name);
            time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
    public interface   clickListener
    {
        void onClicker(int position);
    }

}
