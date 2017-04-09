package com.cyanbirds.lljy.net.request;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.lljy.CSApplication;
import com.cyanbirds.lljy.R;
import com.cyanbirds.lljy.entity.ClientUser;
import com.cyanbirds.lljy.manager.AppManager;
import com.cyanbirds.lljy.net.base.ResultPostExecute;
import com.cyanbirds.lljy.utils.AESOperator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 
 * @ClassName:CheckSmsCodeRequest
 * @Description:微信登陆
 * @Author:wangyb
 * @Date:2015年5月11日下午4:35:22
 *
 */
public class UpdateWechatRequest extends ResultPostExecute<Integer> {

	public void request(String sex, String personalityTag,
						String partTag,  String tall, String weight) {
		ArrayMap<String, String> params = new ArrayMap<>();
		params.put("sex", sex);
		params.put("personalityTag", personalityTag);
		params.put("partTag", partTag);
		params.put("tall", tall);
		params.put("weight", weight);
		Call<ResponseBody> call = AppManager.getUserService().updateWechat(AppManager.getClientUser().sessionId, params);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
				if(response.isSuccessful()){
					try {
						parseJson(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						response.body().close();
					}
				} else {
					onErrorExecute(CSApplication.getInstance()
							.getResources()
							.getString(R.string.network_requests_error));
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				onErrorExecute(CSApplication.getInstance()
						.getResources()
						.getString(R.string.network_requests_error));
			}
		});
	}

	private void parseJson(String json){
		try {
			String decryptData = AESOperator.getInstance().decrypt(json);
			JsonObject obj = new JsonParser().parse(decryptData).getAsJsonObject();
			int code = obj.get("code").getAsInt();
			if (code != 0) {
				onErrorExecute(CSApplication.getInstance().getResources()
						.getString(R.string.data_load_error));
				return;
			}
			onPostExecute(code);
		} catch (Exception e) {
			onErrorExecute(CSApplication.getInstance().getResources()
					.getString(R.string.data_parser_error));
		}
	}
}
