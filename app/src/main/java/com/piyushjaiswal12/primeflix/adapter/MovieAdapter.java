package com.piyushjaiswal12.primeflix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.piyushjaiswal12.primeflix.R;
import com.piyushjaiswal12.primeflix.model.MoviesClass;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<MoviesClass> list;
    private List<MoviesClass> list2;
    private onClickListener listener;


    public MovieAdapter(Context context, List<MoviesClass> list, onClickListener listener) {
        this.context = context;
        this.list = list;
        list2 = new ArrayList<>(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.children, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(context).load("https://geekstocode.com/movies_images/"+list.get(position).getThumb()).into(holder.imageView);
        Log.d("thumbcheck","https://geekstocode.com/movies_images/"+list.get(position).getThumb());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MoviesClass> normalClasses = new ArrayList<>();
            if(constraint != null || constraint.length() != 0)
            {
                String fill = constraint.toString().toLowerCase().trim();
                Log.d("SERP",fill + list2.size());
                for(MoviesClass normalClass : list2)
                {
                    if(normalClass.getName().toLowerCase().contains(fill))
                    {

                        normalClasses.add(normalClass);
                    }
                }
            }
            else
            {normalClasses.addAll(list2);}
            FilterResults results = new FilterResults();
            results.values = normalClasses;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }


    public interface onClickListener
    {
        void onClick(int position);
    }


}
