package com.minew.beaconset.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconConnectionListener;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;
import com.minew.beaconset.R;

import pl.polidea.view.ZoomView;

import java.util.Collections;
import java.util.List;


public class CurrentLoc extends AppCompatActivity implements View.OnClickListener {

    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

    private String TAG = "AnimationActivity";
    private LinearLayout ll1;
    private Button btn1;
    private ImageView btn2;
    private Button btn3;
    private float screenWidth;
    private float screenHeight;
    private int section =0;
    private float toX = 0, fromY = 0;
    private float toY = 0;
    public static int nowX =0, nowY = 0;
    private int add = 80;
    private int to_x = 0;
    private int to_y = 0;

    private int lw = 600;
    private int rw = 950;
    private int uw = 650;
    private int dw = 1080;
    private int mx = 750;
    private int my = 900;

    private float x = nowX;
    private float y = nowY;

    public static int now_x = 300;
    public static int now_y = 300;

    FrameLayout frameView;

    int[] pos=new int[4];

    public static TextView tv_sub;

    private RecyclerView       mRecycle;
    private MinewBeaconManager mMinewBeaconManager;

    private subBeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private       ProgressDialog mpDialog;
    public static MinewBeacon    clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;

    final float a[] = subBeaconListAdapter.i;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    private TextView test_view;
    private int check[] = subBeaconListAdapter.check;

    private float[] filtered = new float[4];
    private KalmanFilter Kdis1;
    private KalmanFilter Kdis2;
    private KalmanFilter Kdis3;
    private KalmanFilter Kdis4;
    private float[] sum_dis = new float[4];
    private int count = 0;



    private float Ax = 0;//충헌
    private float Ay = 0;
    private float Bx = 10; //정현
    private float By = 0;
    private float Cx = 5; //교수
    private float Cy = 12;

    private float lamda;
    private float delta;

    private float Da,Db,Dc, Mx,My,Mz;


    private float zoom_prev_section =0;
    private float zoom_section =0;
    private float zoom_check =0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_item2, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("Mini Map Test"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정
        zoomView.zoomTo(1,0,0);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container1);
        container.addView(zoomView);


        initView();
        initManager();
        checkBluetooth();
        checkLocation();

        dialogshow();

        init(zoomView);
        //createTextView();
        mMinewBeaconManager.startService();

        Kdis1 = new KalmanFilter(0.0f);
        Kdis2= new KalmanFilter(0.0f);
        Kdis3 = new KalmanFilter(0.0f);
        Kdis4 = new KalmanFilter(0.0f);

        tv1= findViewById(R.id.test1);
        tv2= findViewById(R.id.test2);
        tv3= findViewById(R.id.test3);
        tv4= findViewById(R.id.test4);



        btn2.setX(nowX);  //이미지 초기 값 -> 정 가운데
        btn2.setY(nowY);

        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1500);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {
                                (new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        while (!Thread.interrupted())
                                            try {
                                                Thread.sleep(1000);
                                                runOnUiThread(new Runnable() // start actions in UI thread
                                                {
                                                    @Override
                                                    public void run() {

                                                        if(zoom_check==1){
                                                            if(zoom_section==1){
                                                                zoomView.zoomTo(1.8f,800,1100);
                                                            }
                                                            else if(zoom_section==2){
                                                                zoomView.zoomTo(1.8f,800,1100);
                                                            }
                                                            else if(zoom_section==3){
                                                                zoomView.zoomTo(1.8f,800,1100);
                                                            }
                                                            else if (zoom_section==4){
                                                                zoomView.zoomTo(1.8f,800 ,1100);
                                                            }

                                                            btn2.setScaleX((float)0.5);
                                                            btn2.setScaleY((float)0.5);
                                                            zoom_check=0;
                                                        }

                                                        tv1.setText("정현이꺼 " + check[0] + " "  +a[0]);
                                                        tv2.setText("은윤이꺼 " + check[1]+ " "  +a[1]);
                                                        tv3.setText("충헌이꺼 " + check[2] +" " +a[2]+ "   현재 위치 X : "+Mx + " Y : "+ My + " "  );
                                                        tv4.setText("교수님꺼 " + check[3]+ " "  +a[3]+ "                     X : "+ nowX + " Y : "+ nowY );


//                                                        filtered[0] = (float) Kdis1.update(a[0]);
//                                                        filtered[1] = (float) Kdis2.update(a[1]);
//                                                        filtered[2] = (float) Kdis3.update(a[2]);
//                                                        filtered[3] = (float) Kdis4.update(a[3]);
//
//
//                                                        if(count == 8 ){
//                                                            for(int j=0; j<4; j++){
//                                                                a[j] = sum_dis[j] / count;
//                                                                //subBeaconListAdapter.i[j] = a[j];
//                                                            }
//                                                            count = 0;
//                                                            for(int i=0; i<4; i++){
//                                                                sum_dis[i] = 0;
//                                                            }
//                                                            tv1.setText("정현이꺼 " + check[0] + " "  +a[0]);
//                                                            tv2.setText("은윤이꺼 " + check[1]+ " "  +a[1]);
//                                                            tv3.setText("충헌이꺼 " + check[2] +" " +a[2]+ "   현재 위치 X : "+Mx + " Y : "+ My + " "  );
//                                                            tv4.setText("교수님꺼 " + check[3]+ " "  +a[3]+ "                     X : "+ nowX + " Y : "+ nowY );
//
//                                                        }
//                                                        else {
//                                                            for(int i=0; i<4; i++){
//                                                                sum_dis[i] += filtered[i];
//                                                            }
//                                                            count++;
//                                                        }
//
//                                                        Db = a[0]; // 정현
//                                                        Da = a[2]; // 충헌
//                                                        Dc = a[3]; // 교수
//                                                        tv1.setText("정현이꺼 " + a[0]);
//                                                        tv2.setText("은윤이꺼 " + a[1]);
//                                                        tv3.setText("충헌이꺼 " + a[2] +"   현재 위치 X : "+Mx + " Y : "+ My);
//                                                        tv4.setText("교수님꺼 " + a[3]+"                     X : "+ nowX + " Y : "+ nowY);


//                                                        Mx = (Da*Da - Db*Db + Bx*Bx)/(2*Bx);
//                                                        My = ((Cx*Cx) + (Cy*Cy)+(Da*Da) -(Dc*Dc)-(2*Mx*Cx))/(2*Cy);


//                                                        lamda = Ax*Ax+Ay*Ay-Da*Da-Bx*Bx-By*By+Db*Db+((By-Ay)*(Da*Da-Dc*Dc-Ay*Ay+Cy*Cy+Cx*Cx-Ax*Ax))/(Cy-Ay);
//                                                        delta = 2*((By-Ay)*(Cx-Ax)-(Bx-Ax)*(Cy-Ay));
//
//                                                        Mx=lamda*(Cy-Ay)/delta;
//                                                        My=(Da*Da-Dc*Dc-Ay*Ay+Cy*Cy+Cx*Cx-Ax*Ax-2*(Cx-Ax)*Mx)/(2*(Cy-Ay));

                                                        // for(int i=0; i<cnt; i++
                                                        //+"x:"+linearView.getWidth()+" y:"+linearView.getHeight()
                                                        // );
                                                        //  tv5.setText(Optimal_Distance.result_array[0]+ " " + Optimal_Distance.result_array[1]+ " " + Optimal_Distance.result_array[2]+ " " + Optimal_Distance.result_array[3]+ " " );
                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                // ooops
                                            }
                                    }
                                })).start();
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }

            }
        })).start();
    }


    private void init(View v){
        ll1 = findViewById(R.id.ll1);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);


        test_view = v.findViewById(R.id.print_test);

        btn1.setOnClickListener((View.OnClickListener) this);
        btn2.setOnClickListener((View.OnClickListener) this);
        btn3.setOnClickListener((View.OnClickListener) this);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Log.e(TAG, "Width : " + point.x + " , Height : " + point.y);


        test_view.setX(0);
        test_view.setY(0);
        test_view.setText("(0,0)");


        screenWidth = point.x;
        screenHeight = point.y;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn1 :
                fromY = screenHeight - ll1.getHeight() - btn2.getHeight();
                Log.e(TAG, "fromX : " + toX + ",  fromY : " +fromY);

                (new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        while (!Thread.interrupted())
                            try
                            {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() // start actions in UI thread
                                {

                                    @Override
                                    public void run()
                                    {

                                        zoom_prev_section=zoom_section;


                                        now_x = nowX;
                                        now_y = nowY;
                                     /*  a[0] = 정현
                                        a[1]=은윤
                                        a[2]= 충헌
                                        a[3]= 교수*/


                                        //5구역
                                        if((a[0]+a[3]-a[2]-a[1]>-0.4)&&a[0]+a[3]-a[2]-a[1 ]<0.4){

                                            if(nowX<mx&&nowY<my){

                                                nowX+=add;
                                                nowY+=add;
                                                if(nowX>mx)nowX=mx;
                                                if(nowY>my)nowY=my;
                                            }


                                            else  if(nowX>=mx&&nowY<my){
                                                nowX-=add;
                                                nowY+=add;
                                                if(nowX<mx)nowX=mx;
                                                if(nowY>my)nowY=my;
                                            }
                                            else if(nowX<mx&&nowY>=my){
                                                nowX+=add;
                                                nowY-=add;
                                                if(nowX>mx)nowX=mx;
                                                if(nowY<my)nowY=my;
                                            }

                                            else if(nowX>=500&&nowY>=650){
                                                nowX-=add;
                                                nowY-=add;
                                                if(nowX<500)nowX=500;
                                                if(nowY<600)nowY=600;
                                            }

                                        }


                                        //2구역
                                        if(a[0]>0.5&&a[2]>0.5&&a[0]<=1&&a[2]<=1&&a[1]>3&&a[3]>3){
                                            if(nowX<mx&&nowY<uw){
                                                nowY=uw; nowX+=add;
                                            }
                                            if(nowX>mx&&nowY<uw){
                                                nowX-=add; nowY=uw;
                                            }
                                            if(nowX<mx&&nowY>=uw){
                                                nowX+=add; nowY-=add;
                                            }

                                            if(nowX>mx&&nowY>=uw){
                                                nowX-=add; nowY-=add;
                                            }
                                            section=2;
                                        }

                                        //1구역
                                        else if(a[0]<a[1]&&a[0]<a[2]&&a[0]<a[3]){
                                            if(nowX<lw&&nowY<uw){
                                                nowX=lw;
                                                nowY=uw;
                                            }
                                            if(nowX<lw&&nowY>uw){
                                                nowX=lw;
                                                nowY-=add;
                                            }
                                            if(nowX>=lw&&nowY<=uw){
                                                nowX-=add;
                                                nowY=uw;
                                            }

                                            nowX-=add;
                                            nowY-=add;

                                            if(a[0]<0.3){
                                                nowX=500;
                                                nowY=650;
                                            }
                                            section=1;

                                        }
                                        //3구역
                                        else if(a[2]<a[0]&&a[2]<a[1]&&a[2]<a[3]){

                                            if(nowX>rw&&nowY<uw){
                                                nowX=rw;
                                                nowY=uw;
                                            }
//
                                            if( nowX<=rw&&nowY<uw){
                                                nowX+=add;
                                                nowY=uw;
                                            }
                                            if(nowX>rw&&nowY>=uw){
                                                nowY-=add;
                                                nowX=rw;
                                            }
                                            nowX+=add;
                                            nowY-=add;

                                            if(a[2]<0.3){
                                                nowX=950;
                                                nowY=650;
                                            }
                                            section=3;
                                        }

                                        //7구역
                                        else if(a[3]<a[0]&&a[3]<a[1]&&a[3]<a[2]){

                                            if(nowX<lw&&nowY>dw){
                                                nowX=lw;
                                                nowY=dw;
                                            }

                                            if(nowX<lw&&nowY<=dw){
                                                nowX=lw;
                                                nowY+=add;
                                            }

                                            if( nowX>=lw&&nowY>dw){
                                                nowX-=add;
                                                nowY=dw;
                                            }
                                            nowX-=add;
                                            nowY+=add;

                                            if(a[3]<0.3){
                                                nowX=500;
                                                nowY=1190;


                                            }

                                            section =7;

                                        }

                                        // 은윤 9구역
                                        else if(a[1]<a[0]&&a[1]<a[2]&&a[1]<a[3]){

                                            if(nowX>rw&&nowY>dw){
                                                nowX=rw;
                                                nowY=dw;
                                            }
                                            if( nowX>rw&&nowY<=dw){
                                                nowX=rw;
                                                nowY+=add;
                                            }
                                            if(nowX<=rw&&nowY>dw){
                                                nowY=dw;
                                                nowX+=add;
                                            }

                                            nowX+=add;
                                            nowY+=add;

                                            if(a[1]<0.3){
                                                nowX=950;
                                                nowY=1150;
                                            }

                                            section=9;
                                        }


                                        if(nowX<500)nowX=500;
                                        if(nowY<650)nowY=650;
                                        if(nowX>rw)nowX=rw;
                                        if(nowY>dw)nowY=dw;

                                        to_x = nowX;
                                        to_y = nowY;
                                        if(nowX<=500&&nowY<=600){
                                            zoom_section=1;
                                            if(zoom_section==zoom_prev_section){
                                                zoom_check=0;
                                            }
                                            else{
                                                zoom_check=1;
                                            }
                                        }
                                        else if(nowX>500&&nowY<=600){
                                            zoom_section=2;
                                            if(zoom_section==zoom_prev_section){
                                                zoom_check=0;
                                            }
                                            else{
                                                zoom_check=1;
                                            }
                                        }
                                        else if(nowX<=500&&nowY>600){
                                            zoom_section=3;
                                            if(zoom_section==zoom_prev_section){
                                                zoom_check=0;
                                            }
                                            else{
                                                zoom_check=1;
                                            }
                                        }
                                        else if(nowX>500&&nowY>600){
                                            zoom_section=4;
                                            if(zoom_section==zoom_prev_section){
                                                zoom_check=0;
                                            }
                                            else{
                                                zoom_check=1;
                                            }
                                        }


                                        TranslateAnimation animation = new TranslateAnimation(now_x,to_x, now_y, to_y);

                                        animation.setDuration(1000);
                                        animation.setFillAfter(false);
                                        animation.setFillEnabled(true);
                                        animation.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                            }
                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                pos[0]= btn2.getLeft();
                                                pos[1]= btn2.getTop() ;
                                                pos[2]= btn2.getRight()+ (int)fromY;;
                                                pos[3]= btn2.getTop();
                                                btn2.layout(pos[0], pos[1], pos[2], pos[3]);
                                                Log.e(TAG, " 1: " + btn2.getLeft() + " , 2 : " + btn2.getTop() + "  , 3 : "  + btn2.getRight() + "  , 4 : " + btn2.getBottom());
                                            }
                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        btn2.startAnimation(animation);




                                    }
                                });
                            }
                            catch (InterruptedException e)
                            {
                                // ooops
                            }
                    }
                })).start();


                break;

            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
        }
    }

    private void initView() {
        mRecycle = (RecyclerView) findViewById(R.id.sub_recyeler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new subBeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL));
    }
    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }
    private void checkLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }
    }
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }
    private void initListener() {
        mMinewBeaconManager.setMinewbeaconManagerListener(new MinewBeaconManagerListener() {
            @Override
            public void onUpdateBluetoothState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "bluetooth off", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onRangeBeacons(List<MinewBeacon> beacons) {
                Collections.sort(beacons, comp);
                mAdapter.setData(beacons);
            }
            @Override
            public void onAppearBeacons(List<MinewBeacon> beacons) {
            }
            @Override
            public void onDisappearBeacons(List<MinewBeacon> beacons) {
            }
        });
        mAdapter.setOnItemClickLitener(new subBeaconListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mpDialog.setMessage(getString(R.string.connecting)
                        + mAdapter.getData(position).getName());
                mpDialog.show();
                mMinewBeaconManager.stopScan();
                //connect to beacon
                MinewBeacon minewBeacon = mAdapter.getData(position);
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(CurrentLoc.this, minewBeacon);
                minewBeaconConnection.setMinewBeaconConnectionListener(minewBeaconConnectionListener);
                minewBeaconConnection.connect();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    //connect listener;
    MinewBeaconConnectionListener minewBeaconConnectionListener = new MinewBeaconConnectionListener() {
        @Override
        public void onChangeState(MinewBeaconConnection connection, ConnectionState state) {
            switch (state) {
                case BeaconStatus_Connected:
                    mpDialog.dismiss();
                    Intent intent = new Intent(CurrentLoc.this, DetilActivity.class);
                    intent.putExtra("mac", connection.setting.getMacAddress());
                    startActivity(intent);
                    break;
                case BeaconStatus_ConnectFailed:
                case BeaconStatus_Disconnect:
                    if (mpDialog != null) {
                        mpDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
        }
        @Override
        public void onWriteSettings(MinewBeaconConnection connection, boolean success) {
        }
    };

    @Override
    protected void onResume() {
        mMinewBeaconManager.startScan();
        initListener();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMinewBeaconManager.stopScan();
        super.onPause();
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(CurrentLoc.this);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpDialog.setTitle(null);//
        mpDialog.setIcon(null);//
        mpDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
            }
        });
        mpDialog.setCancelable(true);//
        mpDialog.setCanceledOnTouchOutside(false);
    }
    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                mMinewBeaconManager.startScan();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        mMinewBeaconManager.stopService();
        super.onDestroy();
    }
    class KalmanFilter {
        public double Q = 0.00000001;
        public double R = 0.00001;
        public double X = 0, P = 1, K;

        KalmanFilter(double initValue) {
            X = initValue;
        }

        private void measurementUpdate() {
            K = (P + Q) / (P + Q + R);
            P = R * (P + Q) / (R + P + Q);
        }

        public double update(double measurement) {
            measurementUpdate();
            X = X + (measurement - X) * K;

            return X;
        }
    }
}