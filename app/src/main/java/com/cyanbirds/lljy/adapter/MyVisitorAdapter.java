package com.cyanbirds.lljy.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.entity.VisitorModel;
import com.cyanbirds.lljy.manager.AppManager;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-01-14 18:47 GMT+8
 * @email 395044952@qq.com
 */
public class MyVisitorAdapter extends RecyclerView.Adapter<MyVisitorAdapter.ViewHolder> {
    private Context mContext;
    private List<VisitorModel> mVisitorModels;

    private OnItemClickListener mOnItemClickListener;
    public MyVisitorAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_my_attention, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VisitorModel followModel = mVisitorModels.get(position);
        if(followModel == null){
            return;
        }
        holder.portrait.setImageURI(Uri.parse(followModel.faceUrl));
        holder.age.setText(String.valueOf(followModel.age));
        if ("男".equals(followModel.sex)) {
            holder.mSexImg.setImageResource(R.mipmap.list_male);
        } else {
            holder.mSexImg.setImageResource(R.mipmap.list_female);
        }
        holder.user_name.setText(followModel.nickname);
        holder.marray_state.setText(followModel.emotionStatus);
        holder.constellation.setText(followModel.constellation);
        holder.signature.setText(followModel.signature);
        holder.distance.setText(followModel.distance + "公里");
        if(followModel.isVip && AppManager.getClientUser().isShowVip){
            holder.mIsVip.setVisibility(View.VISIBLE);
        } else {
            holder.mIsVip.setVisibility(View.GONE);
        }
    }

    public VisitorModel getItem(int position){
        return mVisitorModels == null ? null : mVisitorModels.get(position);
    }

    @Override
    public int getItemCount() {
        return mVisitorModels == null ? 0 : mVisitorModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        SimpleDraweeView portrait;
        TextView user_name;
        TextView age;
        TextView marray_state;
        TextView constellation;
        TextView signature;
        TextView distance;
        ImageView mIsVip;
        ImageView mSexImg;
        public ViewHolder(View itemView) {
            super(itemView);
            portrait = (SimpleDraweeView) itemView.findViewById(R.id.portrait);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            age = (TextView) itemView.findViewById(R.id.age);
            marray_state = (TextView) itemView.findViewById(R.id.marray_state);
            constellation = (TextView) itemView.findViewById(R.id.constellation);
            signature = (TextView) itemView.findViewById(R.id.signature);
            distance = (TextView) itemView.findViewById(R.id.distance);
            mIsVip = (ImageView) itemView.findViewById(R.id.is_vip);
            mSexImg = (ImageView) itemView.findViewById(R.id.sex_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setmVisitorModels(List<VisitorModel> visitorModels){
        this.mVisitorModels = visitorModels;
        this.notifyDataSetChanged();
    }
}
