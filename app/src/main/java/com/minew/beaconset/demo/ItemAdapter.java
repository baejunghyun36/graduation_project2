package com.minew.beaconset.demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beaconset.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {

    private ArrayList<ItemData> arrayList;

    public ItemAdapter(ArrayList<ItemData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    // 리스트뷰 처음으로 생성될 때 생명주기
    public ItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_list.xml파일로부터..
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;  // holder를 return
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.CustomViewHolder holder, int position) {
        // 실제 추가될 때 생명주기
        holder.iv_profile.setImageResource(arrayList.get(position).getIv_profile());
        holder.item_name.setText(arrayList.get(position).getItem_name());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.item_name.getText().toString();
                Toast.makeText(view.getContext(),curName,Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //길게 눌렀을 때
            @Override
            public boolean onLongClick(View view) {

                remove(holder.getAdapterPosition());    //길게 누르면 해당 item삭제
                Toast.makeText(view.getContext(),"장바구니에서 삭제되었습니다", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() { // 아이템 갯수
        return (null !=arrayList?arrayList.size():0);
    }

    public void remove(int position){
        //삭제하는 함수
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_profile;
        protected TextView item_name;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = (ImageView)itemView.findViewById(R.id.profile);
            this.item_name = (TextView)itemView.findViewById(R.id.item_name);
        }
    }
}