package com.busmonk.activities.ride;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.busmonk.R;
import com.busmonk.service.UserRouteDetail;
import com.busmonk.util.Util;

/**
 * Created by sr250345 on 11/2/16.
 */

public class TransitView extends View {

    private UserRouteDetail urd;

    public TransitView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //Paint paint = new Paint();
        //paint.setColor(Color.rgb(0, 0, 0));
        //paint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
        //paint.setStrokeWidth(5);

        // Line1
        Paint p1 = new Paint();
        p1.setAlpha(255);
        p1.setStrokeWidth(2);
        p1.setColor(Color.BLACK);
        //p1.setColor(Color.rgb(0, 0, 0));
        p1.setStyle(Paint.Style.FILL_AND_STROKE);
        p1.setPathEffect(new DashPathEffect(new float[] {2,4}, 50));
        int startx = 20;
        int starty = 100;
        int endx   = 320;
        int endy   = 100;
        canvas.drawLine(startx, starty, endx, endy, p1);

        // Line2
        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setStrokeWidth(5);
        startx = 320;
        starty = 100;
        endx = 620;
        endy = 100;
        canvas.drawLine(startx, starty, endx, endy, p2);

        // Line3
        Paint p3 = new Paint();
        p3.setStrokeWidth(3);
        startx = 620;
        starty = 100;
        endx = 920;
        endy = 100;
        canvas.drawLine(startx, starty, endx, endy, p3);


        //circle
        Paint p4 = new Paint();

        p4.setColor(Color.BLACK);
        p4.setStyle(Paint.Style.STROKE);
        p4.setStrokeWidth(5);
        canvas.drawCircle(20, 100, 15,  p4);

        p4.setStyle(Paint.Style.FILL);
        p4.setColor(Color.RED);
        canvas.drawCircle(320, 100, 15, p4);

        p4.setStyle(Paint.Style.FILL);
        canvas.drawCircle(620, 100, 15, p4);

        p4.setColor(Color.BLACK);
        canvas.drawCircle(920, 100, 15, p4);


        Paint p5 = new Paint();
        p5.setColor(Color.rgb(0, 0, 0));
        p5.setTextSize(40);
        //sourceToPickup
        canvas.drawText(urd.getWalkingTimeFromSourceToPickupStop() + " min", 130, 150, p5);

        //ride time
        long rideDuration = Util.getRideDuration(urd.getStopTime(urd.getBoardingPoint().getId()), urd.getStopTime(urd.getDropPoint().getId()));
        canvas.drawText(rideDuration + " min", 470, 150, p5);

        //dropPointToDestination
        canvas.drawText(urd.getWalkingTimeFromDropStopToDestination() + " min", 760, 150, p5);


        Paint p6 = new Paint();
        p6.setColor(Color.rgb(0, 0, 0));
        p6.setTextSize(40);

        //pickup point
        canvas.drawText(urd.getBoardingPoint().getName(), 200, 50, p6);

        //Drop point
        canvas.drawText(urd.getDropPoint().getName(), 540, 50, p6);

        //walking icon

        Paint p7 = new Paint();
        p7.setColor(Color.rgb(0, 0, 0));


        Bitmap walking = BitmapFactory.decodeResource(getResources(), R.drawable.walking);
        walking = Bitmap.createScaledBitmap(walking, 60, 60, true);
        canvas.drawBitmap(walking, 60, 100, p7);
        canvas.drawBitmap(walking, 700, 100, p7);

        //bus icon
        Bitmap bus = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
        bus = Bitmap.createScaledBitmap(bus, 60, 60, true);
        canvas.drawBitmap(bus, 400, 100, p7);


    }
    public void initData(UserRouteDetail userRouteDetail)
    {
        this.urd = userRouteDetail;
    }
}
