package com.minew.beaconset.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 9월 3일 은윤

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_pass;
    //private RecyclerView       mRecycle;
    private MinewBeaconManager mMinewBeaconManager;
    private BeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private       ProgressDialog mpDialog;
    public static MinewBeacon    clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    private RecyclerView recyclerview;
    public static String rest2 = "";

    private DrawerLayout drawerLayout;
    private View drawerView;

    private Button  btn_move;

    private static String TAG = "phpquerytest";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ADDRESS = "rest";
    String mJsonString;
    EditText item_find;
    private Button btn_current, btn_my_page;
    public Button[] items = new Button[12]; // 아이템 버튼 배열

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");
        final String userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_pass.setText(userPass);

        btn_current = findViewById(R.id.btn_current_location);
        btn_my_page = findViewById(R.id.btn_my_page);
        items[0] = findViewById(R.id.BtnNum1);
        items[1] = findViewById(R.id.BtnNum1);
        items[2] = findViewById(R.id.BtnNum1);
        items[3] = findViewById(R.id.BtnNum1);

        item_find = (EditText)findViewById(R.id.item);
        btn_move = findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                GetData task = new GetData();
                task.execute(item_find.getText().toString());
          //      tv_pass.setText(rest2);
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });
        btn_current.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });
        btn_my_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, mypages.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        // (9/4은윤)
        items[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickShowAlert(v, items[0]);
            }
        });
        items[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickShowAlert(v, items[1]);
            }
        });
        items[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickShowAlert(v, items[2]);
            }
        });
        items[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickShowAlert(v, items[3]);
            }
        });


        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        ExpandableListAdapter.Item group1 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "정육/계란");
        group1.invisibleChildren = new ArrayList<>();
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "한우"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "수입육"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "돼지고기"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "닭/오리고기"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "계란"));


        ExpandableListAdapter.Item group2 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "과일");
        group2.invisibleChildren = new ArrayList<>();
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "사과"));
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "바나나"));
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "포도"));
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "키위"));

        ExpandableListAdapter.Item group3 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "유제품/베이커리");
        group3.invisibleChildren = new ArrayList<>();
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "우유"));
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "치즈"));
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "저지방우유"));
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "요플레"));

        ExpandableListAdapter.Item group4 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "세제/욕실/청소");
        group4.invisibleChildren = new ArrayList<>();
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "persil"));
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "다우니"));
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "습기제거"));
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "락스"));

        data.add(group1);
        data.add(group2);
        data.add(group3);
        data.add(group4);


        recyclerview.setAdapter(new ExpandableListAdapter(data));



        initView();
        initManager();
        checkBluetooth();
        checkLocation();

        dialogshow();
        mMinewBeaconManager.startService();


        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer );


        ImageButton btn_open1 = (ImageButton)findViewById(R.id.btn_open1);
        btn_open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });


        Button btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener((new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        }));
    }

    // 장바구니 담기 Alert 창 (9/4 은윤)
    public void onClickShowAlert(View view, Button B) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        // alert의 title과 Messege 세팅

        myAlertBuilder.setTitle("장바구니 버튼");
        myAlertBuilder.setMessage(B.getText().toString()+"을(를) 장바구니에 담으시겠어요?");

        // Yes Button or No Button
        myAlertBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                Toast.makeText(getApplicationContext(),"장바구니에 담았습니다!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        myAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"취소되었습니다!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show();
    }

    DrawerLayout.DrawerListener listener=new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }
        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);
        //mRecycle = (RecyclerView) findViewById(R.id.main_recyeler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //mRecycle.setLayoutManager(layoutManager);
        mAdapter = new BeaconListAdapter();
        //mRecycle.setAdapter(mAdapter);
        //mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }


    /*
    * check location
    * */
    private void checkLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }

    }
    /**
     * check Bluetooth state
     */
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
        //scan listener;
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

        mAdapter.setOnItemClickLitener(new BeaconListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mpDialog.setMessage(getString(R.string.connecting)
                        + mAdapter.getData(position).getName());
                mpDialog.show();
                mMinewBeaconManager.stopScan();
                //connect to beacon
                MinewBeacon minewBeacon = mAdapter.getData(position);
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(MainActivity.this, minewBeacon);
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
                    Intent intent = new Intent(MainActivity.this, DetilActivity.class);
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
        mpDialog = new ProgressDialog(MainActivity.this);
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
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
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
                    rest2 = address;
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[0];

            String serverURL = "http://192.168.0.146/query2.php";
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
