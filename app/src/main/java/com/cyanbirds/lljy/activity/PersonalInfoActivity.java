package com.cyanbirds.lljy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.activity.base.BaseActivity;
import com.cyanbirds.lljy.adapter.TabFragmentAdapter;
import com.cyanbirds.lljy.config.ValueKey;
import com.cyanbirds.lljy.entity.ClientUser;
import com.cyanbirds.lljy.eventtype.UserEvent;
import com.cyanbirds.lljy.fragment.TabDynamicFragment;
import com.cyanbirds.lljy.fragment.TabPersonalFragment;
import com.cyanbirds.lljy.manager.AppManager;
import com.cyanbirds.lljy.net.request.AddFollowRequest;
import com.cyanbirds.lljy.net.request.AddLoveRequest;
import com.cyanbirds.lljy.net.request.GetUserInfoRequest;
import com.cyanbirds.lljy.net.request.SendGreetRequest;
import com.cyanbirds.lljy.utils.ProgressDialogUtils;
import com.cyanbirds.lljy.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: wangyb
 * @datetime: 2016-01-02 16:26 GMT+8
 * @email: 395044952@qq.com
 * @description: 个人信息界面
 */
public class PersonalInfoActivity extends BaseActivity {

	@BindView(R.id.portrait)
	SimpleDraweeView mPortrait;
	@BindView(R.id.iv_isVip)
	ImageView mIvIsVip;
	@BindView(R.id.toolbar)
	Toolbar mToolbar;
	@BindView(R.id.collapsingToolbarLayout)
	CollapsingToolbarLayout mCollapsingToolbarLayout;
	@BindView(R.id.tabs)
	TabLayout mTabLayout;
	@BindView(R.id.viewpager)
	ViewPager mViewpager;
	@BindView(R.id.fab)
	FloatingActionButton mFab;
	@BindView(R.id.attention)
	TextView mAttention;
	@BindView(R.id.love)
	TextView mLove;
	@BindView(R.id.message)
	TextView mMessage;
	@BindView(R.id.bottom_layout)
	LinearLayout mBottomLayout;
	@BindView(R.id.gift)
	TextView mGift;
	@BindView(R.id.age)
	TextView mAge;
	@BindView(R.id.city_distance)
	TextView mCityDistance;
	@BindView(R.id.identify_state)
	TextView mIdentifyState;

	private List<String> tabList;
	private List<Fragment> fragmentList;
	private Fragment personalFragment;//个人信息tab
	private Fragment dynamicFragment; //动态tab

	private ClientUser mClientUser; //当前用户
	private String curUserId; //当前用户id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		ButterKnife.bind(this);
		EventBus.getDefault().register(this);
		setupView();
		setupEvent();
		setupData();
	}

	private void setupView() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mCollapsingToolbarLayout.setTitle(" ");

		tabList = new ArrayList<>();
		tabList.add("简介");
		tabList.add("动态");
		mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
		mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(0)));//添加tab选项卡
		mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(1)));

		fragmentList = new ArrayList<>();

		personalFragment = new TabPersonalFragment();
		dynamicFragment = new TabDynamicFragment();
		fragmentList.add(personalFragment);
		fragmentList.add(dynamicFragment);

	}


	private void setupEvent() {
	}

	private void setupData() {
		curUserId = getIntent().getStringExtra(ValueKey.USER_ID);
		if (!TextUtils.isEmpty(curUserId)) {
			if (AppManager.getClientUser().userId.equals(curUserId)) {
				mFab.setVisibility(View.VISIBLE);
				mBottomLayout.setVisibility(View.GONE);
				mClientUser = AppManager.getClientUser();
				setUserInfoAndValue(mClientUser);
			} else {
				mFab.setVisibility(View.GONE);
				mBottomLayout.setVisibility(View.VISIBLE);
				ProgressDialogUtils.getInstance(PersonalInfoActivity.this).show(R.string.dialog_request_data);
				new GetUserInfoTask().request(curUserId);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (AppManager.getClientUser().userId.equals(curUserId)) {
			getMenuInflater().inflate(R.menu.personal_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.modify_info) {
			Intent intent = new Intent(this, ModifyUserInfoActivity.class);
			startActivity(intent);
		} else {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@OnClick({R.id.gift, R.id.portrait, R.id.fab, R.id.attention, R.id.love, R.id.message})
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
			case R.id.portrait:
				intent.setClass(this, PhotoViewActivity.class);
				if (null != mClientUser) {
					if (!TextUtils.isEmpty(mClientUser.face_local) && new File(mClientUser.face_local).exists()) {
						intent.putExtra(ValueKey.IMAGE_URL, "file://" + mClientUser.face_local);
					} else {
						intent.putExtra(ValueKey.IMAGE_URL, mClientUser.face_url);
					}
				}
				intent.putExtra(ValueKey.FROM_ACTIVITY, "PersonalInfoActivity");
				startActivity(intent);
				break;
			case R.id.fab:
				intent.setClass(this, PublishDynamicActivity.class);
				startActivity(intent);
				break;
			case R.id.attention:
				if (null != mClientUser) {
					new AddFollowTask().request(mClientUser.userId);
				}
				break;
			case R.id.gift:
				intent.setClass(this, GiftMarketActivity.class);
				intent.putExtra(ValueKey.USER, mClientUser);
				startActivity(intent);
				break;
			case R.id.love:
				if (null != mClientUser) {
					new SendGreetRequest().request(mClientUser.userId);
					new AddLoveTask().request(mClientUser.userId);
				}
				break;
			case R.id.message:
				if (null != mClientUser) {
					intent.setClass(this, ChatActivity.class);
					intent.putExtra(ValueKey.USER, mClientUser);
					startActivity(intent);
				}
				break;
		}
	}

	/**
	 * 关注
	 */
	class AddFollowTask extends AddFollowRequest {
		@Override
		public void onPostExecute(String s) {
			if (s.equals("已关注")) {
				mAttention.setTextColor(getResources().getColor(R.color.colorPrimary));
				mAttention.setText(R.string.attentioned);
				ToastUtil.showMessage(R.string.attention_success);
			} else {
				mAttention.setTextColor(getResources().getColor(R.color.gray_text));
				mAttention.setText(R.string.attention);
				ToastUtil.showMessage(R.string.cancle_attention);
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(R.string.attention_faiure);
		}
	}

	/**
	 * 喜欢
	 */
	class AddLoveTask extends AddLoveRequest {
		@Override
		public void onPostExecute(String s) {
			if (s.equals("已喜欢")) {
				mLove.setText(s);
				ToastUtil.showMessage(R.string.like_success);
				mLove.setTextColor(getResources().getColor(R.color.colorPrimary));
			} else {
				mLove.setText(R.string.like);
				mLove.setTextColor(getResources().getColor(R.color.gray_text));
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}


	/**
	 * 获取用户信息
	 */
	class GetUserInfoTask extends GetUserInfoRequest {
		@Override
		public void onPostExecute(ClientUser clientUser) {
			ProgressDialogUtils.getInstance(PersonalInfoActivity.this).dismiss();
			mClientUser = clientUser;
			if (null != mClientUser) {
				setUserInfoAndValue(clientUser);
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
			ProgressDialogUtils.getInstance(PersonalInfoActivity.this).dismiss();
		}
	}

	/**
	 * 将TabLayout和ViewPager关联起来。
	 *
	 * @param clientUser
	 */
	private void setUserInfoAndValue(ClientUser clientUser) {
		Bundle personBundle = new Bundle();
		personBundle.putSerializable(ValueKey.ACCOUNT, clientUser);
		personalFragment.setArguments(personBundle);

		Bundle dynamicBundle = new Bundle();
		dynamicBundle.putString(ValueKey.USER_ID, clientUser.userId);
		dynamicFragment.setArguments(dynamicBundle);

		//如果是本人信息，先查找本地有没有头像，有就加载，没有就用face_url;如果是其他用户信息，直接使用face_url
		String imagePath = "";
		if (clientUser.userId.equals(AppManager.getClientUser().userId)) {
			if (!TextUtils.isEmpty(clientUser.face_url)) {
				imagePath = clientUser.face_url;
			} else if (!TextUtils.isEmpty(clientUser.face_local) && new File(clientUser.face_local).exists()) {
				imagePath = "file://" + clientUser.face_local;
			} else {
				imagePath = "res:///" + R.mipmap.default_head;
			}
		} else {
			imagePath = clientUser.face_url;
		}
		if (!TextUtils.isEmpty(imagePath)) {
			mPortrait.setImageURI(Uri.parse(imagePath));
		}
		mCollapsingToolbarLayout.setTitle(clientUser.user_name);
		if (AppManager.getClientUser().isShowVip && clientUser.is_vip) {
			mIvIsVip.setVisibility(View.VISIBLE);
			mIdentifyState.setVisibility(View.VISIBLE);
		} else {
			mIdentifyState.setVisibility(View.GONE);
			mIvIsVip.setVisibility(View.GONE);
		}
		mAge.setText(String.valueOf(clientUser.age) + "岁");
		if (!TextUtils.isEmpty(clientUser.distance) && Double.parseDouble(clientUser.distance) != 0) {
			mCityDistance.setText("," + clientUser.distance + "km");
		} else if (!TextUtils.isEmpty(clientUser.city)) {
			mCityDistance.setText(", " + clientUser.city);
		}

		if (AppManager.getClientUser().userId.equals(clientUser.userId)) {
			mAttention.setVisibility(View.GONE);
			mCityDistance.setVisibility(View.GONE);
		}

		if (mClientUser.isFollow) {
			mAttention.setText("已关注");
			mAttention.setTextColor(getResources().getColor(R.color.colorPrimary));
		}

		TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(
				getSupportFragmentManager(), fragmentList, tabList);
		mViewpager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
		mTabLayout.setupWithViewPager(mViewpager);//将TabLayout和ViewPager关联起来。
		mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void updateUserInfo(UserEvent event) {
		//如果是本人信息，先查找本地有没有头像，有就加载，没有就用face_url;如果是其他用户信息，直接使用face_url
		ClientUser clientUser = AppManager.getClientUser();
		String imagePath = "";
		if (!TextUtils.isEmpty(clientUser.face_url)) {
			imagePath = clientUser.face_url;
		} else if (!TextUtils.isEmpty(clientUser.face_local) && new File(clientUser.face_local).exists()) {
			imagePath = "file://" + clientUser.face_local;
		} else {
			imagePath = "res:///" + R.mipmap.default_head;
		}
		mPortrait.setImageURI(Uri.parse(imagePath));
		mCollapsingToolbarLayout.setTitle(AppManager.getClientUser().user_name);
		if (clientUser.is_vip) {
			mIvIsVip.setVisibility(View.VISIBLE);
		} else {
			mIvIsVip.setVisibility(View.GONE);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
