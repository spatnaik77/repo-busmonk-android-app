package com.busmonk.activities.ride;

/**
 * Created by sr250345 on 10/18/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.busmonk.R;
import com.busmonk.activities.mapview.MapviewActivity;
import com.busmonk.service.User;
import com.busmonk.service.UserRoute;
import com.busmonk.service.UserRouteDetail;
import com.busmonk.session.SessionManager;
import com.busmonk.util.BusmonkApplication;

import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.MyViewHolder> {

    private List<UserRouteDetail> dataSet;

    private Activity context;

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView summary;
        TransitView transitView;
        TextView boardingTime;

        TextView busDetail;
        RatingBar ratingBar;
        Button btnSelect;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);

            this.transitView = (TransitView)itemView.findViewById(R.id.transitView);

            this.summary = (TextView) itemView.findViewById(R.id.summary);

            this.boardingTime = (TextView) itemView.findViewById(R.id.boardingTime);

            this.busDetail = (TextView) itemView.findViewById(R.id.busDetail);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            this.btnSelect = (Button) itemView.findViewById(R.id.btn_select);
        }
    }

    public RoutesAdapter(Activity context, List<UserRouteDetail> data) {
        this.context = context;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_route, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition)
    {
        final UserRouteDetail urd = dataSet.get(listPosition);

        //graphical view
        CardView cardView = holder.cardView;

        TransitView transitView = holder.transitView;
        transitView.initData(urd);

        TextView summary = holder.summary;
        summary.setPaintFlags(summary.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String summaryStr =  urd.getBoardingPoint().getName() + " > " + urd.getDropPoint().getName();
        summary.setText(summaryStr);

        summary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, MapviewActivity.class);
                intent.putExtra("routeId", urd.getBus().getRouteId());
                intent.putExtra("pickupStopName", urd.getBoardingPoint().getName());
                intent.putExtra("pickupLat", urd.getBoardingPoint().getLattitude());
                intent.putExtra("pickupLong", urd.getBoardingPoint().getLongitude());
                intent.putExtra("dropStopName", urd.getDropPoint().getName());
                intent.putExtra("dropLat", urd.getDropPoint().getLattitude());
                intent.putExtra("dropLong", urd.getDropPoint().getLongitude());

                context.startActivity(intent);
            }
        });

        TextView boardingTime = holder.boardingTime;
        String time = urd.getStopTime(urd.getBoardingPoint().getId());
        boardingTime.setText(time);

        TextView busDetail = holder.busDetail;
        String busDetailStr = urd.getCab().getAcType() + " " + urd.getCab().getSeatingCapacity() + " seater";
        busDetail.setText(busDetailStr);

        RatingBar ratingBar = holder.ratingBar;
        ratingBar.setNumStars(5);
        ratingBar.setRating(4);

        Button btnSelect = holder.btnSelect;
        btnSelect.setText("Select");

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(UserRouteDetail ur : dataSet)
                {
                    ur.isSelected = false;
                }
                urd.isSelected = true;
                notifyDataSetChanged();

                //add to application variables
                // context..getApplicationContext().geta
                User u = SessionManager.getInstance().getUser();
                urd.setUserId(u.getId());
                UserRoute ur = urd.getUserRoute();
                ((BusmonkApplication)(context.getApplication())).put(ur.getName(), ur);

            }
        });

        if(urd.isSelected)
        {
            cardView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            cardView.setBackgroundColor(Color.WHITE);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
