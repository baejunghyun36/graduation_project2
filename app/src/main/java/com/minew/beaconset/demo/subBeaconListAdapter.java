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

import org.w3c.dom.Text;

import java.util.List;


public class subBeaconListAdapter extends RecyclerView.Adapter<subBeaconListAdapter.MyViewHolder> {

    public static float[] i = new float[4];


    public static String distance;
    public static String location;

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
        View view = View.inflate(parent.getContext(), R.layout.sub_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (Float.parseFloat(mMinewBeacons.get(position).getMinor()) == 56388) {
            i[0] = mMinewBeacons.get(position).getDistance();
        } else if (Float.parseFloat(mMinewBeacons.get(position).getMinor()) == 56877) {
            i[1] = mMinewBeacons.get(position).getDistance();
        } else if (Float.parseFloat(mMinewBeacons.get(position).getMinor()) == 56878) {
            i[2] = mMinewBeacons.get(position).getDistance();
        } else {
            i[3] = mMinewBeacons.get(position).getDistance();
        }
        holder.itemView.setVisibility(View.GONE);
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
        private final TextView mDevice_other;
        private final TextView mDevice_location;
        private final TextView mDevice_who;
        private final TextView distance1;


        public MyViewHolder(View itemView) {
            super(itemView);
            mDevice_other = (TextView) itemView.findViewById(R.id.device_other);
            mDevice_location = (TextView) itemView.findViewById(R.id.device_location);
            mDevice_who = (TextView) itemView.findViewById(R.id.who_device);
            distance1 = (TextView) itemView.findViewById(R.id.distance1);

        }


    }
}
