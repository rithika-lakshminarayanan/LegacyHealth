package com.photon.legacyhealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FeelingAdapter extends RecyclerView.Adapter<FeelingAdapter.MyViewHolder> {
    private Context mContext;
    private boolean firstLoad;
    private static MyClickListener myClickListener;
    private int prevfeeling=2;
    private List<Feeling> feelings;

    public class MyViewHolder extends ViewHolder implements View.OnClickListener {
        public TextView feeling;
        public ImageView sym;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            feeling = (TextView) view.findViewById(R.id.symptom_name);
            sym= (ImageView) view.findViewById(R.id.symptom);
        }


        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        FeelingAdapter.myClickListener = myClickListener;
    }
    public FeelingAdapter(List<Feeling> feelingsList) {
        this.feelings = feelingsList;
        this.firstLoad = true;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeling_button, parent, false);

        return new MyViewHolder(itemView);
    }

    public void changeImage(int index) {
        feelings.get(index).setImageChanged(true);
        if(prevfeeling!=-1) {
            feelings.get(prevfeeling).setImageChanged(false);
            notifyItemChanged(prevfeeling);
        }
        notifyItemChanged(index);
        prevfeeling=index;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Feeling feeling = feelings.get(position);
        holder.feeling.setText(feeling.getSymptom_name());
        String url = feeling.getImgUrl();
        if(feeling.isImageChanged()){
            GlideApp.with(holder.itemView).load(feeling.getImgSelUrl()).fitCenter().placeholder(R.drawable.allergies).into(holder.sym);
        }
        else
           if(position==2) {
            if(firstLoad) {
                GlideApp.with(holder.itemView).load(feeling.getImgSelUrl()).fitCenter().placeholder(R.drawable.allergies).into(holder.sym);
                firstLoad=false;
            }
            else
                GlideApp.with(holder.itemView).load(feeling.getImgUrl()).fitCenter().placeholder(R.drawable.allergies).into(holder.sym);
           }
        else
            GlideApp.with(holder.itemView).load(url).fitCenter().placeholder(R.drawable.allergies).into(holder.sym);
        }

    @Override
    public int getItemCount() {
        return feelings.size();
    }
    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}

