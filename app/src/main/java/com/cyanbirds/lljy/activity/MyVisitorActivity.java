package com.cyanbirds.lljy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.activity.base.BaseActivity;
import com.cyanbirds.lljy.adapter.MyVisitorAdapter;
import com.cyanbirds.lljy.config.ValueKey;
import com.cyanbirds.lljy.entity.VisitorModel;
import com.cyanbirds.lljy.net.request.VisitorListRequest;
import com.cyanbirds.lljy.ui.widget.CircularProgress;
import com.cyanbirds.lljy.ui.widget.DividerItemDecoration;
import com.cyanbirds.lljy.ui.widget.WrapperLinearLayoutManager;
import com.cyanbirds.lljy.utils.DensityUtil;
import com.cyanbirds.lljy.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-01-13 22:17 GMT+8
 * @email 395044952@qq.com
 */
public class MyVisitorActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private CircularProgress mCircularProgress;
    private TextView mNoUserInfo;
    private MyVisitorAdapter mAdapter;

    private int pageNo = 1;
    private int pageSize = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_visitor);
        setupView();
        setupEvent();
        setupData();
    }

    private void setupView(){
        mCircularProgress = (CircularProgress) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mNoUserInfo = (TextView) findViewById(R.id.info);
        LinearLayoutManager manager = new WrapperLinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL, DensityUtil
                .dip2px(this, 12), DensityUtil.dip2px(
                this, 12)));
    }

    private void setupEvent(){

    }

    private void setupData(){
        mAdapter = new MyVisitorAdapter(MyVisitorActivity.this);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mCircularProgress.setVisibility(View.VISIBLE);
        new VisitorListTask().request(pageNo, pageSize);
    }

    private MyVisitorAdapter.OnItemClickListener mOnItemClickListener = new MyVisitorAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            VisitorModel visitorModel = mAdapter.getItem(position);
            Intent intent = new Intent(MyVisitorActivity.this, PersonalInfoActivity.class);
            intent.putExtra(ValueKey.USER_ID, String.valueOf(visitorModel.userId));
            startActivity(intent);
        }
    };

    class VisitorListTask extends VisitorListRequest {
        @Override
        public void onPostExecute(List<VisitorModel> visitorModels) {
            mCircularProgress.setVisibility(View.GONE);
            if(null != visitorModels && visitorModels.size() > 0){
                mNoUserInfo.setVisibility(View.GONE);
                mAdapter.setmVisitorModels(visitorModels);
            } else {
                mNoUserInfo.setText("最近暂时没有人访问过您哦");
                mNoUserInfo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onErrorExecute(String error) {
            ToastUtil.showMessage(error);
            mCircularProgress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }
}
