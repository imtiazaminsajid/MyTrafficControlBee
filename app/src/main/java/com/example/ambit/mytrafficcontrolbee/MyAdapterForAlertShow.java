package com.example.ambit.mytrafficcontrolbee;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterForAlertShow extends RecyclerView.Adapter<MyAdapterForAlertShow.MyViewHolder> {

    private Context context;
    private List<AlertMessage> alertMessageList1;

    public MyAdapterForAlertShow(Context context, List<AlertMessage> alertMessageList1) {
        this.context = context;
        this.alertMessageList1 = alertMessageList1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.sample_layout_for_alertlist,viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        AlertMessage alertMessage = alertMessageList1.get(i);

        myViewHolder.location.setText(alertMessage.getLocation());
        myViewHolder.timeDate.setText(alertMessage.getTimeAndDate());
        myViewHolder.messages.setText(alertMessage.getMessage());
        Picasso.with(context).load(alertMessage.getImageUrl()).fit().centerCrop().into(myViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return alertMessageList1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView location, timeDate, messages;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            location =  itemView.findViewById(R.id.sampleLocation);
            timeDate =  itemView.findViewById(R.id.sampleDateTime);
            messages =  itemView.findViewById(R.id.sampleAlertText);
            imageView =  itemView.findViewById(R.id.sampleImageView);
        }
    }
}
