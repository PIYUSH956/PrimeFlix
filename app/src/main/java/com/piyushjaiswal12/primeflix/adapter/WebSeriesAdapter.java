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
import com.piyushjaiswal12.primeflix.model.WebSeriesClass;

import java.util.ArrayList;
import java.util.List;

public class WebSeriesAdapter extends RecyclerView.Adapter<WebSeriesAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<WebSeriesClass> list;
    private List<WebSeriesClass> list2;
    private onClickListener listener;


    public WebSeriesAdapter(Context context, List<WebSeriesClass> list, onClickListener listener) {
        this.context = context;
        this.list = list;
        Log.d("Inside Constructor"," "+list.size());
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
        Glide.with(context).load("https://geekstocode.com/web_series_images/"+list.get(position).getThumb()).into(holder.imageView);
        Log.d("thumbcheck","https://geekstocode.com/web_series_images/"+list.get(position).getThumb());
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
            List<WebSeriesClass> normalClasses = new ArrayList<>();
            if(constraint != null || constraint.length() != 0)
            {
                String fill = constraint.toString().toLowerCase().trim();
                Log.d("SERP",fill + list2.size());
                for(WebSeriesClass normalClass : list2)
                {
                    if(normalClass.getName().toLowerCase().contains(fill))
                    {
                        Log.d("SERP",normalClass.getName().toLowerCase()+" "+ fill);
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









