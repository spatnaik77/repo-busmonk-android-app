package com.busmonk.activities.mybus;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.busmonk.R;
import com.busmonk.service.Cab;
import com.busmonk.service.UserRoute;
import com.busmonk.util.Util;

import java.util.List;

/**
 * Created by sr250345 on 11/11/16.
 */

public class MyBusAdapter extends RecyclerView.Adapter<MyBusAdapter.MyBusViewHolder> {

    private List<UserRoute> dataSet;

    private Context context;

    public  class MyBusViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView routeName;
        TextView summary;
        TextView boardingTime;
        TextView travelCompany;
        RatingBar ratingBar;
        TextView driverName;
        TextView vehicleDetail;
        TextView busNumber;


        public MyBusViewHolder(View itemView)
        {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.routeName = (TextView) itemView.findViewById(R.id.routeName);
            this.summary = (TextView) itemView.findViewById(R.id.summary);
            this.boardingTime = (TextView) itemView.findViewById(R.id.boardingTime);
            this.travelCompany = (TextView) itemView.findViewById(R.id.travelCompany);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            this.driverName = (TextView) itemView.findViewById(R.id.driverName);
            this.vehicleDetail = (TextView) itemView.findViewById(R.id.vehicleDetail);
            this.busNumber = (TextView) itemView.findViewById(R.id.busNumber);
        }
    }
    public MyBusAdapter(Context context, List<UserRoute> data) {
        this.context = context;
        this.dataSet = data;
    }

    @Override
    public MyBusAdapter.MyBusViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_mybus, parent, false);

        MyBusViewHolder myBusViewHolder = new MyBusViewHolder(view);
        return myBusViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyBusAdapter.MyBusViewHolder holder, final int listPosition)
    {
        final UserRoute ur = dataSet.get(listPosition);

        TextView routeNameT = holder.routeName;
        String routeName = ur.getName(); //"Home to Office";
        routeNameT.setText(routeName);

        TextView summaryT = holder.summary;
        summaryT.setPaintFlags(summaryT.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String summary = ur.getBoardingPoint() + " > " + ur.getDropPoint();      //"Marathalli bridge > Ecospace";
        summaryT.setText(summary);

        TextView boardingTimeT = holder.boardingTime;
        String boardingTime =  ur.getBoardingTime();  //"08:00 (60 min)";
        String dropTime     =  ur.getDropTime();
        long rideDuration = Util.getRideDuration(boardingTime, dropTime);
        boardingTimeT.setText(boardingTime + "(" + rideDuration + " min)");

        TextView travelCompanyT = holder.travelCompany;
        String travelCompany = "ABC Travels";
        travelCompanyT.setText(travelCompany);

        RatingBar ratingBar = holder.ratingBar;
        ratingBar.setNumStars(5);
        ratingBar.setRating(4);

        TextView driverNameT = holder.driverName;
        String driverName =   ur.getDriverName();  //"Ramesh Reddy";
        driverNameT.setText(driverName);

        TextView vehicleDetailT = holder.vehicleDetail;
        Cab c = ur.getCab();
        String vehicleDetail = c.getAcType() + " " + c.getSeatingCapacity()+ " Seater:" + c.getRegistrationNumber();//"A/C 12 Seater: KA MC 7392";
        vehicleDetailT.setText(vehicleDetail);

        TextView busNumberT = holder.busNumber;
        String busNumber = "Route: " + ur.getBusId();
        busNumberT.setText(busNumber);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public List<UserRoute> getData()
    {
        return dataSet;
    }

}
