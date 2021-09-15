package com.minew.beaconset.demo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class home extends AppCompatActivity {

    public static Context context_main;
    public String str="주차한 정보가 없습니다";

    private int cnt_check=0;
    public static int Basket_index;
    private static String TAG = "phpquerytest";
    public static String rest[] = new String[10];
    public static String name[] = new String[10];
    public static int[] item_location_x = new int[20];
    public static int[] item_location_y = new int[20];
    public static String id[] = new String[10];
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ADDRESS = "rest";
    private static final String TAG_id = "id";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    String mJsonString;
    private ImageView btn_move;

    private Button btn_search;
    private ImageView btnnum2;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private Button btn_current, btn_my_page;
    EditText item_find;
    private Context context;
    //parking
    private RecyclerView mRecycle;
    private MinewBeaconManager mMinewBeaconManager;
    private subBeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private ProgressDialog mpDialog;
    public static MinewBeacon clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    final float a[] = subBeaconListAdapter.i;
    public static int check;

    private TextView parking_info2;
    public Dialog custom_parking; // 커스텀 다이얼로그
    public Dialog custom_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context_main =this;
        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");
        final String userName = intent.getStringExtra("userName");

        btn_move = findViewById(R.id.btn_move);

        btn_search = findViewById(R.id.btn_search);
        item_find = (EditText)findViewById(R.id.item) ;
        btnnum2 = findViewById(R.id.BtnNum2);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer );
        btn_current = findViewById(R.id.btn_current_location);
        btn_my_page = findViewById(R.id.btn_my_page);


        custom_parking = new Dialog(home.this);       // Dialog 초기화
        custom_parking.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        custom_parking.setContentView(R.layout.custom_parking);   //커스텀 다이얼로그 연결

        //parking

        parking_info2 = findViewById(R.id.parking_info2);
        initView();
        initManager();
        checkBluetooth();
        checkLocation();
        dialogshow();
        mMinewBeaconManager.startService();

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

                                                        parking_info2.setText(a[0] + " " + a[1] + " " + a[2] + " " + a[3]);

                                                        if((a[0]>0||a[1]>0||a[2]>0||a[3]>0)&&(cnt_check==0)){
                                                            cnt_check=1;
                                                            String parking_info3 = "000005";
                                                            countDown(parking_info3);
                                                        }


                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                // ooops
                                            }
                                    }
                                })).start();
                            }
                        });
                    } catch (InterruptedException e) {
                        // ooops
                    }
            }
        })).start();




        btn_move.setColorFilter(Color.parseColor("#6492C3"));
        btn_move.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(home.this, SubActivity.class);
                startActivity(intent);
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if ( item_find.getText().toString().length() != 0 ) { //검색창 Null 아닐때
                    GetData task = new GetData();
                    task.execute(item_find.getText().toString());
                }
                else {    // 검색창 Null일때
                    Toast.makeText(getApplicationContext(),"검색어를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_current.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(home.this, SubActivity.class);
                startActivity(intent);
            }
        });
        btn_my_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(home.this, mypages.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });



        ImageView btn_open1 = (ImageView)findViewById(R.id.btn_open1);

        btn_open1.setColorFilter(Color.parseColor("#6492C3"));
        btn_open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        btnnum2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }






    public void countDown(String time) {

        cnt_check=1;
        String item_name = str;
//        TextView tv_item = custom_parking.findViewById(R.id.item);
//        tv_item.setText(item_name);
        custom_parking.show(); // 다이얼로그 띄우기

        long conversionTime = 0;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간
        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {
            TextView tv_item = custom_parking.findViewById(R.id.item);

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 시간단위
//                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));

//                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                // 밀리세컨드 단위
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "" + second;
                }

                if(a[0]<a[1]&&a[0]<a[2])check =1;
                else if (a[1]<a[0]&&a[1]<a[2])check =2;
                else if(a[2]<a[0]&&a[2]<a[1])check =3;
                if(check==1){  //정현
                    if(a[0]>a[1]||a[0]>a[2]){
                        if(a[1]<a[2])check =2;
                        else check =3;
                        second="10";
                    }
                }
                else if(check==2){ //은윤
                    if(a[1]>a[0]||a[1]>a[2]){
                        if(a[0]<a[2])check =1;
                        else check =3;
                        second="10";
                    }

                }
                else if(check==3){ //충헌
                    if(a[2]>a[0]||a[2]>a[1]){
                        if(a[0]<a[1])check =1;
                        else check =2;
                        second="10";
                    }
                }
                Button noBtn = custom_parking.findViewById(R.id.noBtn);
                noBtn.setText("닫기");
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),"즐거운 쇼핑되세요!",

                                Toast.LENGTH_SHORT).show();
                        custom_parking.dismiss(); // 다이얼로그 닫기

                    }
                });


                tv_item.setText(second);
            }

            // 제한시간 종료시
            public void onFinish() {
                if(check==1){
                    str= "지하 2층 A-1 구역";
                }
                else if(check ==2){

                    str= "지하 2층 B-3 구역";

                }
                else if(check ==3){

                    str= "지하 2층 C-2 구역";
                }
                else{

                    str= "뚜벅이";
                }
                TextView parking_id = custom_parking.findViewById(R.id.parking_id);
                TextView myinfo = custom_parking.findViewById(R.id.myinfo);

                parking_id.setText("");
                tv_item.setText(str);
                myinfo.setText("메뉴 -> 마이페이지 -> 주차위치찾기 ");


                // NO 버튼
                Button noBtn = custom_parking.findViewById(R.id.noBtn);
                noBtn.setText("확인");
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"즐거운 쇼핑 되세요!",
                                Toast.LENGTH_SHORT).show();
                        custom_parking.dismiss(); // 다이얼로그 닫기
                    }
                });
            }
        }.start();
    }











    private void initView() {
        mRecycle = (RecyclerView) findViewById(R.id.sub_recyeler2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new subBeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));
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
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(home.this, minewBeacon);
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
                    Intent intent = new Intent(home.this, DetilActivity.class);
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
        mpDialog = new ProgressDialog(home.this);
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
    public class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(home.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mJsonString = result;
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    String address = item.getString(TAG_ADDRESS);
                    String x = item.getString(TAG_X);
                    String y = item.getString(TAG_Y);
                    String Item_id = item.getString(TAG_id);
                    rest[Basket_index] = address;
                    item_location_x[Basket_index] = Integer.parseInt(x);
                    item_location_y[Basket_index] = Integer.parseInt(y);
                    id[Basket_index] = Item_id;
                    Basket_index++;
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
            Intent intent = new Intent(home.this, SearchActivity.class);
            startActivity(intent);
        }
        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[0];

            String serverURL = "http://192.168.0.3/query2.php";
            String postParameters = "country=" + searchKeyword1 + "&name=" + searchKeyword2;
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
}