package com.photon.legacyhealth;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DoingAdapter extends RecyclerView.Adapter<DoingAdapter.MyViewHolder> {
    private static DoingAdapter.MyClickListener myClickListener;
    private int prevfeeling=0;
    private List<Feeling> feelings;
    private String prevSymptom;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
    public void setOnItemClickListener(DoingAdapter.MyClickListener myClickListener) {
        DoingAdapter.myClickListener = myClickListener;
    }
    public DoingAdapter(List<Feeling> feelingsList) {
        this.feelings = feelingsList;
    }

    @Override
    public DoingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeling_button, parent, false);

        return new DoingAdapter.MyViewHolder(itemView);
    }

    public void changeImage(int index) {
        if(!feelings.get(index).isImageChanged()) {
                feelings.get(index).setImageChanged(true);
            }
            else
                if(feelings.get(index).isImageChanged())
                {
                    feelings.get(index).setImageChanged(false);
                }
            notifyItemChanged(index);
        if(prevfeeling!=index) {
            if(prevfeeling>getItemCount()){
                if(prevSymptom.equalsIgnoreCase("indoor"))
                    prevfeeling=0;
                else
                if(prevSymptom.equalsIgnoreCase("outdoor"))
                    prevfeeling=1;
            }
            if(prevfeeling!=-1){
                if(isPrimaryButton(prevfeeling) && isPrimaryButton(index)){
                    feelings.get(prevfeeling).setImageChanged(false);
                    notifyItemChanged(prevfeeling);
                }
            }
            if(feelings.get(index).getSymptom_name().equalsIgnoreCase("market"))
                prevfeeling=2;
            else
            prevfeeling = index;
            prevSymptom = getActivityName(prevfeeling);
        }
    }

    public boolean isFilterSelected(int position){
        if(feelings.get(position).isImageChanged())
            return true;
        else
            return false;
    }

    public void unselectImage(int index){
        feelings.get(index).setImageChanged(false);
        notifyItemChanged(index);
    }

    @Override
    public void onBindViewHolder(DoingAdapter.MyViewHolder holder, int position) {
        Feeling feeling = feelings.get(position);
        holder.feeling.setText(feeling.getSymptom_name());
        String url = feeling.getImgUrl();
        if(feeling.isImageChanged()){
            GlideApp.with(holder.itemView).load(feeling.getImgSelUrl()).fitCenter().placeholder(R.drawable.allergies).into(holder.sym);
        }
        else
            GlideApp.with(holder.itemView).load(url).fitCenter().placeholder(R.drawable.allergies).into(holder.sym);
    }

    @Override
    public int getItemCount() {
        return feelings.size();
    }

    public int getPosition(String key){
        int pos=-1;
        for(int i=0; i<feelings.size();i++){
            if(feelings.get(i).getSymptom_name().equalsIgnoreCase(key)){
                pos=i;
                break;
            }
        }
        return pos;
    }


    public void addItemsOnFilter(ArrayList<Feeling> expandedList, int position){
        for(int i=0; i<expandedList.size();i++){
            feelings.add(position,expandedList.get(i));
        }
        notifyDataSetChanged();
    }

    public void removeItemsOnUnselect(ArrayList<Feeling> expandedList, int position){
        for(int i=0; i<expandedList.size();i++){
            feelings.remove(position-i);
        }
        notifyDataSetChanged();
    }

    public String getActivityName(int position){
        if(feelings.get(position).getSymptom_name().equalsIgnoreCase("races"))
            return "running";
        else
        if(feelings.get(position).getSymptom_name().equalsIgnoreCase("trails"))
            return "walking";
        else
        return feelings.get(position).getSymptom_name();
    }

    public boolean isPrimaryButton(int position){
        if(getActivityName(position).equalsIgnoreCase("indoor")||getActivityName(position).equalsIgnoreCase("outdoor"))
            return true;
        else
            return false;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
