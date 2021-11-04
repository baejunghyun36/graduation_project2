package com.minew.beaconset.demo;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beaconset.R;


public class SearchActivity extends AppCompatActivity {
    public static int id[] = home.id;
    private TextView SearchingItem;
    public static int Last_num = home.Basket_index;

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
                MainActivity.basket[MainActivity.Basket_index] = search_item;
                MainActivity.Basket_index++;
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

                MainActivity.basket[MainActivity.Basket_index] = search_item;
                //MainActivity.Basket_index++;
                Intent intent = new Intent(SearchActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });

    }
}