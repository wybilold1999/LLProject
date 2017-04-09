package com.cyanbirds.lljy.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.activity.MakeMoneyActivity;
import com.cyanbirds.lljy.activity.MyGoldActivity;
import com.cyanbirds.lljy.activity.VipCenterActivity;
import com.cyanbirds.lljy.adapter.TabPersonalPhotosAdapter;
import com.cyanbirds.lljy.config.ValueKey;
import com.cyanbirds.lljy.entity.ClientUser;
import com.cyanbirds.lljy.eventtype.UserEvent;
import com.cyanbirds.lljy.manager.AppManager;
import com.cyanbirds.lljy.net.request.UpdateGoldRequest;
import com.cyanbirds.lljy.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: wangyb
 * @datetime: 2016-01-02 16:32 GMT+8
 * @email: 395044952@qq.com
 * @description:
 */
public class TabPersonalFragment extends Fragment {
	@BindView(R.id.user_name)
	TextView mUserName;
	@BindView(R.id.sex_img)
	ImageView mSexImg;
	@BindView(R.id.age)
	TextView mAge;
	@BindView(R.id.occupation)
	TextView mOccupation;
	@BindView(R.id.colleage)
	TextView mColleage;
	@BindView(R.id.constellation)
	TextView mConstellation;
	@BindView(R.id.tall)
	TextView mTall;
	@BindView(R.id.weight)
	TextView mWeight;
	@BindView(R.id.married)
	TextView mMarried;
	@BindView(R.id.signature)
	TextView mSignature;
	@BindView(R.id.plable_flowlayout)
	TagFlowLayout mPlableFlowlayout;
	@BindView(R.id.part_flowlayout)
	TagFlowLayout mPartFlowlayout;
	@BindView(R.id.intrest_flowlayout)
	TagFlowLayout mIntrestFlowlayout;
	@BindView(R.id.purpose)
	TextView mPurpose;
	@BindView(R.id.loveWhere)
	TextView mLoveWhere;
	@BindView(R.id.do_what_first)
	TextView mDoWhatFirst;
	@BindView(R.id.conception)
	TextView mConception;
	@BindView(R.id.occupation_lay)
	RelativeLayout mOccupationLay;
	@BindView(R.id.colleage_text)
	TextView mColleageText;
	@BindView(R.id.colleage_lay)
	RelativeLayout mColleageLay;
	@BindView(R.id.constellation_text)
	TextView mConstellationText;
	@BindView(R.id.constellation_lay)
	RelativeLayout mConstellationLay;
	@BindView(R.id.tall_text)
	TextView mTallText;
	@BindView(R.id.tall_lay)
	RelativeLayout mTallLay;
	@BindView(R.id.weight_text)
	TextView mWeightText;
	@BindView(R.id.weight_lay)
	RelativeLayout mWeightLay;
	@BindView(R.id.married_text)
	TextView mMarriedText;
	@BindView(R.id.married_lay)
	RelativeLayout mMarriedLay;
	@BindView(R.id.signature_text)
	TextView mSignatureText;
	@BindView(R.id.signature_lay)
	RelativeLayout mSignatureLay;
	@BindView(R.id.my_info)
	CardView mMyInfo;
	@BindView(R.id.qq_id)
	TextView mQqId;
	@BindView(R.id.social_text)
	TextView mSocialText;
	@BindView(R.id.social_card)
	CardView mSocialCard;
	@BindView(R.id.plable_icon)
	SimpleDraweeView mPlableIcon;
	@BindView(R.id.plable_lay)
	RelativeLayout mPlableLay;
	@BindView(R.id.part_icon)
	ImageView mPartIcon;
	@BindView(R.id.part_lay)
	RelativeLayout mPartLay;
	@BindView(R.id.intrest_icon)
	ImageView mIntrestIcon;
	@BindView(R.id.intrest_lay)
	RelativeLayout mIntrestLay;
	@BindView(R.id.recyclerview)
	RecyclerView mRecyclerview;
	@BindView(R.id.photo_card)
	CardView mPhotoCard;
	@BindView(R.id.gift_text)
	TextView mGiftText;
	@BindView(R.id.gift_recyclerview)
	RecyclerView mGiftRecyclerview;
	@BindView(R.id.gift_card)
	CardView mGiftCard;
	@BindView(R.id.wechat_id)
	TextView mWechatId;
	@BindView(R.id.check_view_wechat)
	Button mCheckViewWechat;
	@BindView(R.id.check_view_qq)
	Button mCheckViewQq;
	private View rootView;

	private ClientUser clientUser;
	private List<String> mVals = null;
	private List<String> mPhotoList;

	private TabPersonalPhotosAdapter mAdapter;
	private LinearLayoutManager layoutManager;
	private LinearLayoutManager mGiftLayoutManager;
	private Gson gson = new Gson();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.tab_item_personal, null);
			ButterKnife.bind(this, rootView);
			EventBus.getDefault().register(this);
			setupViews();
			setupEvent();
			setupData();
			setHasOptionsMenu(true);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		ButterKnife.bind(this, rootView);
		return rootView;
	}

	private void setupEvent() {
	}

	private void setupViews() {
		layoutManager = new LinearLayoutManager(
				getActivity(), LinearLayoutManager.HORIZONTAL, false);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerview.setLayoutManager(layoutManager);
		mRecyclerview.setItemAnimator(new DefaultItemAnimator());

		mGiftLayoutManager = new LinearLayoutManager(
				getActivity(), LinearLayoutManager.HORIZONTAL, false);
		mGiftLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mGiftRecyclerview.setLayoutManager(mGiftLayoutManager);
		mGiftRecyclerview.setItemAnimator(new DefaultItemAnimator());
	}

	private void setupData() {
		mVals = new ArrayList<>();
		if (getArguments() != null) {
			clientUser = (ClientUser) getArguments().getSerializable(ValueKey.ACCOUNT);
			if (clientUser != null) {
				setUserInfo(clientUser);
				/**
				 * 用户的图片
				 */
				if (!TextUtils.isEmpty(clientUser.imgUrls)) {
					Type listType = new TypeToken<ArrayList<String>>() {
					}.getType();
					List<String> urls = gson.fromJson(clientUser.imgUrls, listType);
					if (urls != null && urls.size() > 0) {
						mPhotoCard.setVisibility(View.VISIBLE);
						mPhotoList = new ArrayList<>();
						mPhotoList.addAll(urls);
						mAdapter = new TabPersonalPhotosAdapter(getActivity(), mPhotoList);
						mRecyclerview.setAdapter(mAdapter);
					} else {
						mPhotoCard.setVisibility(View.GONE);
					}
				} else {
					mPhotoCard.setVisibility(View.GONE);
				}
				/**
				 * 用户收到的礼物
				 */
				if (!TextUtils.isEmpty(clientUser.gifts)) {
					mGiftText.setVisibility(View.VISIBLE);
					mGiftCard.setVisibility(View.VISIBLE);
					mAdapter = new TabPersonalPhotosAdapter(getActivity(),
							StringUtil.stringToIntList(clientUser.gifts));
					mGiftRecyclerview.setAdapter(mAdapter);
				} else {
					mGiftText.setVisibility(View.GONE);
					mGiftCard.setVisibility(View.GONE);
				}
			}
		}
	}

	private void setUserInfo(ClientUser clientUser) {
		if (AppManager.getClientUser().isShowVip) {
			mSocialCard.setVisibility(View.VISIBLE);
			mSocialText.setVisibility(View.VISIBLE);
		} else {
			mSocialCard.setVisibility(View.GONE);
			mSocialText.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(clientUser.user_name)) {
			mUserName.setText(clientUser.user_name);
		}
		if ("男".equals(clientUser.sex)) {
			mSexImg.setImageResource(R.mipmap.list_male);
		} else {
			mSexImg.setImageResource(R.mipmap.list_female);
		}
		mAge.setText(String.valueOf(clientUser.age));
		if (!TextUtils.isEmpty(clientUser.purpose)) {
			mPurpose.setText(clientUser.purpose);
		}
		if (!TextUtils.isEmpty(clientUser.love_where)) {
			mLoveWhere.setText(clientUser.love_where);
		}
		if (!TextUtils.isEmpty(clientUser.do_what_first)) {
			mDoWhatFirst.setText(clientUser.do_what_first);
		}
		if (!TextUtils.isEmpty(clientUser.conception)) {
			mConception.setText(clientUser.conception);
		}
		if (!TextUtils.isEmpty(clientUser.tall)) {
			mTall.setText(clientUser.tall);
		}
		if (!TextUtils.isEmpty(clientUser.state_marry)) {
			mMarried.setText(clientUser.state_marry);
		}
		if (!TextUtils.isEmpty(clientUser.weight)) {
			int index = clientUser.weight.indexOf("k");
			String weight = clientUser.weight.substring(0, index);
			int w = 0;
			if (Integer.parseInt(weight) > 70) {
				w = Integer.parseInt(weight) / 2;
			} else {
				w = Integer.parseInt(weight);
			}
			mWeight.setText(w + "kg");
		}
		if (!TextUtils.isEmpty(clientUser.signature)) {
			mSignature.setText(clientUser.signature);
		}
		if (!TextUtils.isEmpty(clientUser.constellation)) {
			mConstellation.setText(clientUser.constellation);
		}
		if (!TextUtils.isEmpty(clientUser.occupation)) {
			mOccupation.setText(clientUser.occupation);
		}
		if (!TextUtils.isEmpty(clientUser.education)) {
			mColleage.setText(clientUser.education);
		}
		if (clientUser.userId.equals(AppManager.getClientUser().userId)) {
			if (!TextUtils.isEmpty(clientUser.weixin_no)) {
				mWechatId.setText(clientUser.weixin_no);
			}
			if (!TextUtils.isEmpty(clientUser.qq_no)) {
				mQqId.setText(clientUser.qq_no);
			}
			mCheckViewWechat.setVisibility(View.GONE);
			mCheckViewQq.setVisibility(View.GONE);
		} else {
			if (!TextUtils.isEmpty(clientUser.weixin_no)) {
				String weChat = clientUser.weixin_no;
				String subUrl = clientUser.weixin_no.substring(2, clientUser.weixin_no.length() - 3);
				weChat = weChat.replaceAll(subUrl, "****");
				mWechatId.setText(weChat);
			}
			if (!TextUtils.isEmpty(clientUser.qq_no)) {
				String qq = clientUser.qq_no;
				String subUrl = clientUser.qq_no.substring(2, clientUser.qq_no.length() - 3);
				qq = qq.replaceAll(subUrl, "****");
				mQqId.setText(qq);
			}
		}

		if (!TextUtils.isEmpty(clientUser.part_tag)) {
			mPartFlowlayout.setVisibility(View.VISIBLE);
			mVals.clear();
			mVals = StringUtil.stringToIntList(clientUser.part_tag);
			mPartFlowlayout.setAdapter(
					new PersonalTagAdapter(mVals, mPartFlowlayout));
		}
		if (!TextUtils.isEmpty(clientUser.personality_tag)) {
			mPlableFlowlayout.setVisibility(View.VISIBLE);
			mVals.clear();
			mVals = StringUtil.stringToIntList(clientUser.personality_tag);
			mPlableFlowlayout.setAdapter(
					new PersonalTagAdapter(mVals, mPlableFlowlayout));
		}
		if (!TextUtils.isEmpty(clientUser.intrest_tag)) {
			mIntrestFlowlayout.setVisibility(View.VISIBLE);
			mVals.clear();
			mVals = StringUtil.stringToIntList(clientUser.intrest_tag);
			mIntrestFlowlayout.setAdapter(
					new PersonalTagAdapter(mVals, mIntrestFlowlayout));
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void updateUserInfo(UserEvent event) {
		setUserInfo(AppManager.getClientUser());
	}

	@OnClick({R.id.check_view_wechat, R.id.check_view_qq})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.check_view_wechat:
				if (AppManager.getClientUser().is_vip) {
					if (AppManager.getClientUser().gold_num < 1) {
						String tips = String.format(getResources().getString(R.string.social_id_need_gold), "微信");
						showBuyGoldDialog(tips);
					} else if (AppManager.getClientUser().gold_num < 101){
						String tips = String.format(getResources().getString(R.string.social_id_need_more_gold), "微信");
						showBuyGoldDialog(tips);
					} else {
						mWechatId.setText(clientUser.weixin_no);
						if (!AppManager.getClientUser().is_download_vip) {
							//更新服务器上的金币数量
							AppManager.getClientUser().gold_num -= 101;
							new UpdateGoldTask().request(AppManager.getClientUser().gold_num, "");
						}
					}
				} else {
					showTurnOnVipDialog("微信");
				}
				break;
			case R.id.check_view_qq:
				if (AppManager.getClientUser().is_vip) {
					if (AppManager.getClientUser().gold_num < 1) {
						String tips = String.format(getResources().getString(R.string.social_id_need_gold), "QQ");
						showBuyGoldDialog(tips);
					} else if (AppManager.getClientUser().gold_num < 101){
						String tips = String.format(getResources().getString(R.string.social_id_need_more_gold), "QQ");
						showBuyGoldDialog(tips);
					} else {
						mQqId.setText(clientUser.qq_no);
						if (!AppManager.getClientUser().is_download_vip) {
							//更新服务器上的金币数量
							AppManager.getClientUser().gold_num -= 101;
							new UpdateGoldTask().request(AppManager.getClientUser().gold_num, "");
						}
					}
				} else {
					showTurnOnVipDialog("QQ");
				}
				break;
		}
	}

	/**
	 * 不是下载赚钱会员，查看微信、QQ号时，减少金币数量
	 */
	class UpdateGoldTask extends UpdateGoldRequest {
		@Override
		public void onPostExecute(final Integer integer) {
			if (AppManager.getClientUser().isShowDownloadVip) {
				Snackbar.make(getActivity().findViewById(R.id.content),
						"您还不是赚钱会员，查看该号码已消耗101枚金币", Snackbar.LENGTH_LONG)
						.setActionTextColor(Color.RED)
						.setAction("开通赚钱会员", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(), MakeMoneyActivity.class);
								intent.putExtra(ValueKey.FROM_ACTIVITY, getActivity().getClass().getSimpleName());
								startActivity(intent);
							}
						}).show();
			} else {
				Snackbar.make(getActivity().findViewById(R.id.content), "查看该号码已消耗101枚金币",
						Snackbar.LENGTH_LONG).show();
			}
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	private void showTurnOnVipDialog(String socialTpe) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(String.format(getResources().getString(R.string.social_id_need_vip), socialTpe));
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(getActivity(), VipCenterActivity.class);
				startActivity(intent);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void showBuyGoldDialog(String tips) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(tips);
		builder.setPositiveButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						Intent intent = new Intent(getActivity(), MyGoldActivity.class);
						startActivity(intent);
					}
				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * tag的适配器
	 */
	class PersonalTagAdapter extends TagAdapter<String> {
		private TagFlowLayout mLayout;

		public PersonalTagAdapter(List<String> datas, TagFlowLayout layout) {
			super(datas);
			this.mLayout = layout;
		}

		@Override
		public View getView(FlowLayout parent, int position, String t) {
			TextView tv = (TextView) LayoutInflater.from(
					getActivity()).inflate(R.layout.item_tv,
					mLayout, false);
			setIntrestItemColor(mLayout, tv);
			tv.setText(t);
			return tv;
		}
	}

	/**
	 * 根据不同类型设置不同的颜色
	 *
	 * @param mLayout
	 * @param tv
	 */
	private void setIntrestItemColor(FlowLayout mLayout, TextView tv) {
		if (mLayout == mPlableFlowlayout) {
			if (Build.VERSION.SDK_INT >= 16) {
				tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.hot_lable_checked_bg));
			} else {
				tv.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.hot_lable_checked_bg));
			}
			tv.setTextColor(getResources().getColor(R.color.book_text_color));
		} else if (mLayout == mPartFlowlayout) {
			if (Build.VERSION.SDK_INT >= 16) {
				tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.hot_lable_music_bg));
			} else {
				tv.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.hot_lable_music_bg));
			}
			tv.setTextColor(getResources().getColor(R.color.music_text_color));
		} else if (mLayout == mIntrestFlowlayout) {
			if (Build.VERSION.SDK_INT >= 16) {
				tv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.hot_lable_intrest_bg));
			} else {
				tv.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.hot_lable_intrest_bg));
			}
			tv.setTextColor(getResources().getColor(R.color.travel_text_color));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}
}
