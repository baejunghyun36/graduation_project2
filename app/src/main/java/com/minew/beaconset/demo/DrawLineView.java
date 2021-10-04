package com.minew.beaconset.demo;

import android.content.Context;
import com.minew.beaconset.demo.SubActivity;


import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.RectF;

import android.util.AttributeSet;

import android.view.View;



public class DrawLineView extends View {

    public float start_x = 0;
    public float start_y = 0;
    public float end_x = 100;
    public float end_y = 100;


    public DrawLineView(Context context) {

        super(context);

        // TODO Auto-generated constructor stub

        System.out.println("MyView2(Context context) 호출");

    }



    public DrawLineView(Context context, AttributeSet attrs) {

        super(context, attrs);

        // TODO Auto-generated constructor stub

        System.out.println("MyView2(Context context, AttributeSet attrs) 호출");

    }


    @Override

    protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub
        //super.onDraw(canvas);


        Paint pnt = new Paint();

        //배경색
        pnt.setColor(Color.WHITE);
        pnt.setAlpha(100);

        canvas.drawPaint(pnt);

        /*
        // 빨간색 둥근 사각형
        RectF r = new RectF(10,10,200,200);
        //사각형 배경색
        pnt.setColor(Color.YELLOW);
        //꼭지점 라운드 정도
        canvas.drawRoundRect(r, 10, 10, pnt);
         */

        pnt.setColor(Color.RED);
        pnt.setStrokeWidth(10);
        float[] pts_test={400,500,200,500};
        canvas.drawLines(pts_test, pnt);

        pnt.setColor(Color.BLUE);
        pnt.setStrokeWidth(10);
        float[] pts_t={200,500,200,200};
        canvas.drawLines(pts_t, pnt);


        pnt.setColor(Color.GREEN);
        pnt.setStrokeWidth(10);
        float[] pts_draw={200,200,40,110};
        //float[] pts_draw={start_x,start_y,end_x,end_y};
        canvas.drawLines(pts_draw, pnt);

        //검정색 점 3개

        pnt.setColor(Color.BLACK);

        pnt.setStrokeWidth(15);
        float[] pts2={500,300,500,400,500,700};

        canvas.drawPoints(pts2, pnt);
    }

}