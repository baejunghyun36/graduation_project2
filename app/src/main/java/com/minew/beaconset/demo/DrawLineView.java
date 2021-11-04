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

        float[] pts_test2={SubActivity.pr_x,SubActivity.pr_y,MainActivity.item_location_x2[Optimal_Distance.arr[0]]*10,MainActivity.item_location_y2[Optimal_Distance.arr[0]]*10};
        canvas.drawLines(pts_test2, pnt);
        for(int i=0; i < MainActivity.Basket_index-1; i++){
            int x = MainActivity.item_location_x2[Optimal_Distance.arr[i]]*10;
            int y = MainActivity.item_location_y2[Optimal_Distance.arr[i]]*10;
            float[] pts_test={x,y,MainActivity.item_location_x2[Optimal_Distance.arr[i+1]]*10,MainActivity.item_location_y2[Optimal_Distance.arr[i+1]]*10};
            canvas.drawLines(pts_test, pnt);
            if(i==MainActivity.Basket_index-2){
                float[] pts_test3={MainActivity.item_location_x2[Optimal_Distance.arr[i+1]]*10,MainActivity.item_location_y2[Optimal_Distance.arr[i+1]]*10,120,1400};
                canvas.drawLines(pts_test3,pnt);
            }
        }
    }

}
