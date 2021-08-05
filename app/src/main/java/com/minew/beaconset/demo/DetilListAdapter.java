package com.minew.beaconset.demo;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minew.beaconset.R;

import java.util.List;


public class DetilListAdapter extends RecyclerView.Adapter<DetilListAdapter.MyViewHolder> {

    private List<String> mData;
    private List<String> mListname;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.detil_item, null);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setDataAndUi(mData.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setListname(List<String> listname) {
        mListname = listname;
    }

    public void setData(List<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public String getData(int position) {
        return mData.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mNmae;
        private final TextView mData;

        public MyViewHolder(View itemView) {
            super(itemView);
            mNmae = (TextView) itemView.findViewById(R.id.name);
            mData = (TextView) itemView.findViewById(R.id.data);
        }

        public void setDataAndUi(String data) {
            mNmae.setText(mListname.get(getPosition()));
            mData.setText(data);
        }
    }
}
