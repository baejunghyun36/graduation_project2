package com.minew.beaconset.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class SearchActivity extends AppCompatActivity {
    private TextView SearchingItem;
    private static String TAG = "phpquerytest";
    private static final String TAG_NAME = "name";
    public static String basket[] = MainActivity.basket;
    public static String name[] = MainActivity.name;
    public static int id[] = MainActivity.id;
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_id = "id";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    String mJsonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String search_item = intent.getStringExtra("Searching");
        setContentView(R.layout.activity_search);


        SearchingItem =  findViewById(R.id.tv_searching);
        SearchingItem.setText(search_item);

        Button btn_inCart = findViewById(R.id.btn_inCart);
        Button direct_optimal = findViewById(R.id.btn_direct_optimal);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();    // 이전 페이지로 돌아가기
            }
        });





        btn_inCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //String tmp = B.getText().toString();
                //basket[Basket_index++] = tmp;
                MainActivity.basket[MainActivity.Basket_index++] = search_item;
             /*   ImageView iv_item = findViewById(R.id.imageView4);
                final int iv = 0;
                iv_item.setImageResource(iv);
                ItemData itemData = new ItemData(iv,"");
                MainActivity.arrayList.add(itemData);    // 해당 아이템 추가
                MainActivity.itemAdapter.notifyDataSetChanged(); //새로고침
*/

                Toast.makeText(getApplicationContext(),"장바구니에 담았습니다!",
                        Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(SearchActivity.this, home.class);
                //startActivity(intent);
            }
        });


        direct_optimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.basket[MainActivity.Basket_index++] = search_item;
                //MainActivity.Basket_index++;
                    GetData task = new GetData();
                    task.execute(basket[0]);
            }
        });

    }
    public class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchActivity.this,
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
                    String item_name = item.getString(TAG_NAME);
                    String x = item.getString(TAG_X);
                    String y = item.getString(TAG_Y);
                    int Item_id = Integer.parseInt(item.getString(TAG_id));
                    MainActivity.item_location_x[MainActivity.search_complete] = Integer.parseInt(x);
                    MainActivity.item_location_y[MainActivity.search_complete] = Integer.parseInt(y);
                    MainActivity.item_location_x2[Item_id] = Integer.parseInt(x);
                    MainActivity.item_location_y2[Item_id] = Integer.parseInt(y);
                    MainActivity.id[MainActivity.search_complete] = Item_id;
                    MainActivity.name[Item_id] = item_name;
                    MainActivity.search_complete++;

                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
            if(MainActivity.search_complete == MainActivity.Basket_index) {
                Intent intent = new Intent(SearchActivity.this, SubActivity.class);
                startActivity(intent);
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[0];


            String serverURL = home.commonURL;
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