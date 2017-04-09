package com.cyanbirds.lljy.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.entity.ClientUser;
import com.cyanbirds.lljy.manager.AppManager;
import com.cyanbirds.lljy.net.request.AddLoveRequest;
import com.cyanbirds.lljy.net.request.SendGreetRequest;
import com.cyanbirds.lljy.utils.StringUtil;
import com.cyanbirds.lljy.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wx.goodview.GoodView;

import java.text.DecimalFormat;
import java.util.List;



/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-12-26 18:34 GMT+8
 * @email 395044952@qq.com
 */
public class FindLoveAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<ClientUser> mClientUsers;
    private Context mContext;
    private boolean mShowFooter = false;

    private OnItemClickListener mOnItemClickListener;
    private DecimalFormat mFormat;

    public FindLoveAdapter(List<ClientUser> clientUsers, Context mContext) {
        this.mClientUsers = clientUsers;
        this.mContext = mContext;
        mFormat = new DecimalFormat("#.00");
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if(!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_find_love, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            ClientUser clientUser = mClientUsers.get(position);
            if(clientUser == null){
                return;
            }
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mLove.setImageResource(R.mipmap.love_default);
            itemViewHolder.userName.setText(clientUser.user_name);
            itemViewHolder.age.setText(String.valueOf(clientUser.age));
            if ("男".equals(clientUser.sex)) {
                itemViewHolder.mSexImg.setImageResource(R.mipmap.list_male);
            } else {
                itemViewHolder.mSexImg.setImageResource(R.mipmap.list_female);
            }
            itemViewHolder.marrayState.setText(clientUser.state_marry);
            itemViewHolder.constellation.setText(clientUser.constellation);
            if (null == clientUser.distance || Double.parseDouble(clientUser.distance) == 0.0) {
                itemViewHolder.distance.setText("来自" + clientUser.city);
            } else {
                itemViewHolder.distance.setText(mFormat.format(Double.parseDouble(clientUser.distance)) + " km");
            }
            itemViewHolder.signature.setText(clientUser.signature);
            if(clientUser.is_vip && AppManager.getClientUser().isShowVip){
                itemViewHolder.isVip.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.isVip.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(clientUser.face_url)) {
                itemViewHolder.portrait.setImageURI(Uri.parse(clientUser.face_url));
            } else {
                itemViewHolder.portrait.setImageURI(Uri.parse("res:///" + R.mipmap.default_head));
            }
            if (!TextUtils.isEmpty(clientUser.personality_tag)) {
                if (clientUser.personality_tag.contains(";")) {
                    List<String> tags = StringUtil.stringToIntList(clientUser.personality_tag);
                    if (tags != null && tags.size() > 0) {
                        itemViewHolder.mLableFirst.setText(tags.get(0));
                        if (tags.size() > 1) {
                            itemViewHolder.mLableSec.setVisibility(View.VISIBLE);
                            itemViewHolder.mLableSec.setText(tags.get(1));
                        }
                        if (tags.size() > 2) {
                            itemViewHolder.mLableThi.setVisibility(View.VISIBLE);
                            itemViewHolder.mLableThi.setText(tags.get(2));
                        }
                    }
                } else {
                    itemViewHolder.mLableFirst.setText(clientUser.personality_tag);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter?1:0;
        if(mClientUsers == null) {
            return begin;
        }
        return mClientUsers.size() + begin;
    }

    public ClientUser getItem(int position){
        return mClientUsers == null ? null : mClientUsers.get(position);
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView portrait;
        TextView userName;
        ImageView isVip;
        TextView age;
        TextView marrayState;
        TextView constellation;
        TextView distance;
        TextView signature;
        TextView mLableFirst;
        TextView mLableSec;
        TextView mLableThi;
        ImageView mLove;
        ImageView mSexImg;
        GoodView mGoodView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            portrait = (SimpleDraweeView) itemView.findViewById(R.id.portrait);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            isVip = (ImageView) itemView.findViewById(R.id.is_vip);
            age = (TextView) itemView.findViewById(R.id.age);
            marrayState = (TextView) itemView.findViewById(R.id.marray_state);
            constellation = (TextView) itemView.findViewById(R.id.constellation);
            distance = (TextView) itemView.findViewById(R.id.distance);
            signature = (TextView) itemView.findViewById(R.id.signature);
            mLove = (ImageView) itemView.findViewById(R.id.iv_love);
            mSexImg = (ImageView) itemView.findViewById(R.id.sex_img);
            mLableFirst = (TextView) itemView.findViewById(R.id.lable_1);
            mLableSec = (TextView) itemView.findViewById(R.id.lable_2);
            mLableThi = (TextView) itemView.findViewById(R.id.lable_3);
            itemView.setOnClickListener(this);
            mLove.setOnClickListener(this);
            mGoodView = new GoodView(mContext);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_love :
                    int position = getAdapterPosition();
                    new SendGreetTask().request(mClientUsers.get(position).userId);
                    new AddLoveTask().request(mClientUsers.get(position).userId);
                    break;
                default :
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                    break;
            }
        }

        class SendGreetTask extends SendGreetRequest {

            @Override
            public void onPostExecute(String s) {
//                ToastUtil.showMessage(s);
                mLove.setImageResource(R.mipmap.love_focused);
                mGoodView.setText(s);
                mGoodView.show(mLove);
                mGoodView.setImage(R.mipmap.love_focused);
            }

            @Override
            public void onErrorExecute(String error) {
                ToastUtil.showMessage(error);
            }
        }

        class AddLoveTask extends AddLoveRequest {
            @Override
            public void onPostExecute(String s) {
            }

            @Override
            public void onErrorExecute(String error) {
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setIsShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public void setClientUsers(List<ClientUser> users){
        this.mClientUsers = users;
        this.notifyDataSetChanged();
    }
}
