package com.cyanbirds.lljy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.activity.base.BaseActivity;
import com.cyanbirds.lljy.config.ValueKey;
import com.cyanbirds.lljy.entity.Info;
import com.cyanbirds.lljy.entity.YuanFenModel;
import com.cyanbirds.lljy.net.request.GetYuanFenUserRequest;
import com.cyanbirds.lljy.ui.widget.RadarViewGroup;
import com.cyanbirds.lljy.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：wangyb
 * 时间：2016/9/18 19:54
 * 描述：请求缘分
 */
public class RadarActivity extends BaseActivity implements RadarViewGroup.IRadarClickListener{

	private int pageNo = 0;
	private int pageSize = 200;

	private RadarViewGroup radarViewGroup;

	private SparseArray<Info> mDatas = new SparseArray<>();
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radar_view);
		setupView();
		initData();
	}

	private void setupView() {
		radarViewGroup = (RadarViewGroup) findViewById(R.id.radar);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				radarViewGroup.setDatas(mDatas);
				new GetYuanFenUserTask().request(pageNo, pageSize);
			}
		}, 2000);
		radarViewGroup.setiRadarClickListener(this);
	}

	private void initData() {
		for (int i = 0; i < 8; i++) {
			Info info = new Info();
			info.setSex(i % 3 == 0 ? false : true);
			info.setDistance(Math.round((Math.random() * 10) * 100) / 100);
			mDatas.put(i, info);
		}
	}

	@Override
	public void onRadarItemClick(int position) {

	}

	class GetYuanFenUserTask extends GetYuanFenUserRequest {
		@Override
		public void onPostExecute(List<YuanFenModel> yuanFenModels) {
			Intent intent = new Intent(RadarActivity.this, CardActivity.class);
			intent.putExtra(ValueKey.USER, (Serializable) yuanFenModels);
			startActivity(intent);
			finish();
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
			finish();
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
