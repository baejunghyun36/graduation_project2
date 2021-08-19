package com.minew.beaconset.demo;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconSetting;
import com.minew.beaconset.R;

import java.util.List;


public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.MyViewHolder> {

    private List<MinewBeacon> mMinewBeacons;

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
        View view = View.inflate(parent.getContext(), R.layout.main_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setDataAndUi(mMinewBeacons.get(position));

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
        if (mMinewBeacons != null) {
            return mMinewBeacons.size();
        }
        return 0;
    }

    public void setData(List<MinewBeacon> minewBeacons) {
        this.mMinewBeacons = minewBeacons;
        notifyDataSetChanged();

    }

    public MinewBeacon getData(int position) {
        return mMinewBeacons.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private MinewBeacon mMinewBeacon;
        private final TextView mDevice_name;
        private final TextView mDevice_uuid;
        private final TextView mDevice_other;
        private final TextView mConnectable;

        private final TextView mDevice_location;





        public MyViewHolder(View itemView) {
            super(itemView);
            mDevice_name = (TextView) itemView.findViewById(R.id.device_name);
            mDevice_uuid = (TextView) itemView.findViewById(R.id.device_uuid);
            mDevice_other = (TextView) itemView.findViewById(R.id.device_other);
            mConnectable = (TextView) itemView.findViewById(R.id.device_connectable);

            mDevice_location = (TextView) itemView.findViewById(R.id.device_location);

        }

        public void setDataAndUi(MinewBeacon minewBeacon) {
            mMinewBeacon = minewBeacon;
            mDevice_name.setText(mMinewBeacon.getName());
            mDevice_uuid.setText("UUID:" + mMinewBeacon.getUuid());
            if (mMinewBeacon.isConnectable()) {
                mConnectable.setText("CONN: YES");
            } else {
                mConnectable.setText("CONN: NO");
            }
            String format = String.format("Major:%s Minor:%s Rssi:%s Battery:%s Distance:%s",
                    mMinewBeacon.getMajor(),
                    mMinewBeacon.getMinor(),
                    mMinewBeacon.getRssi(),
                    mMinewBeacon.getBattery(),
                    mMinewBeacon.getDistance());
            mDevice_other.setText(format);


//            if(mMinewBeacon.getSystemId()=="E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"){
//                mDevice_location.setText("지하주차장A");
//
//            }
//            else if(mMinewBeacon.getUuid()=="E2c56DB5-DFFB-48D2-B060-D0F5A71096E0"){
//                mDevice_location.setText("지하주차JJB");
//
//            }



        }
    }
}
