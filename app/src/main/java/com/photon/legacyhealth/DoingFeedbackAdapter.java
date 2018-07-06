package com.photon.legacyhealth;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DoingFeedbackAdapter extends RecyclerView.Adapter<DoingFeedbackAdapter.MyViewHolder> {
    private List<FeelingFeedback> fList;
    private static DoingFeedbackAdapter.MyClickListener myClickListener;
    private int[] badgeInitial={1,1,1,1,1,1,1,1,1};

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView fName;
        public ImageView fImg;
        public TextView badge;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            fName = (TextView) view.findViewById(R.id.doing_name);
            fImg= (ImageView) view.findViewById(R.id.doing_img);
            badge = (TextView) view.findViewById(R.id.doing_badge_text);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
    public void setOnItemClickListener(DoingFeedbackAdapter.MyClickListener myClickListener) {
        DoingFeedbackAdapter.myClickListener = myClickListener;
    }
    public DoingFeedbackAdapter(List<FeelingFeedback> feelingsList) {
        this.fList = feelingsList;
    }

    @Override
    public DoingFeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doing_feedback, parent, false);
        return new DoingFeedbackAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(DoingFeedbackAdapter.MyViewHolder holder, int position) {
        FeelingFeedback feeling = fList.get(position);
        if(!fList.get(position).isBadgeVisible())
            holder.badge.setVisibility(View.GONE);
        else {
            holder.badge.setText(String.valueOf(badgeInitial[position]));
            holder.badge.setVisibility(View.VISIBLE);
        }
        holder.fName.setText(feeling.getfText());
        String url = feeling.getfImgUrl();
        GlideApp.with(holder.itemView).load(url).fitCenter().placeholder(R.drawable.allergies).into(holder.fImg);
        holder.fImg.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    public void increaseBadgeCount(int index, int curBadgeCount){
        if(!fList.get(index).isBadgeVisible)
            fList.get(index).setBadgeVisible(true);
        else
            badgeInitial[index]=curBadgeCount+1;
        notifyItemChanged(index);
    }


    @Override
    public int getItemCount() {
        return fList.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
