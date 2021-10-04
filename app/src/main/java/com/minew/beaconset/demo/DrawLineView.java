package com.minew.beaconset.demo;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.util.AttributeSet;

import android.view.View;

public class DrawLineView extends View {

    public DrawLineView(Context context) {
        super(context);
    }
    public DrawLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override

    protected void onDraw(Canvas canvas) {

        Paint pnt = new Paint();

        //배경색
        pnt.setColor(Color.WHITE);
        pnt.setAlpha(100);

        canvas.drawPaint(pnt);

        pnt.setColor(Color.BLACK);
        pnt.setStrokeWidth(15);
        for(int i=0; i < home.Basket_index-1; i++){
            int x = home.item_location_x[Optimal_Distance.arr[i]-1]*10;
            int y = home.item_location_y[Optimal_Distance.arr[i]-1]*10;
            float[] pts_test={y,x,home.item_location_y[Optimal_Distance.arr[i+1]-1]*10,home.item_location_x[Optimal_Distance.arr[i+1]-1]*10};
            canvas.drawLines(pts_test, pnt);
        }

    }

}
