package com.cyanbirds.lljy.net.request;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.lljy.CSApplication;
import com.cyanbirds.lljy.R;
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
 * Created by Administrator on 2016/5/1.
 */
public class UpdateGoldRequest extends ResultPostExecute<Integer> {
    public void request(int goldNum, String curVideoUrl) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("goldNum", String.valueOf(goldNum));
        params.put("curVideoUrl", curVideoUrl);
        Call<ResponseBody> call = AppManager.getUserService().updateGold(AppManager.getClientUser().sessionId, params);
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

    private void parseJson(String json) {
        try {
            String decryptData = AESOperator.getInstance().decrypt(json);
            JsonObject obj = new JsonParser().parse(decryptData).getAsJsonObject();
            int code = obj.get("code").getAsInt();
            if (code == 1) {
                onErrorExecute(obj.get("msg").getAsString());
                return;
            }
            int goldNum = obj.get("data").getAsInt();
            onPostExecute(goldNum);
        } catch (Exception e) {
            onErrorExecute(CSApplication.getInstance().getResources()
                    .getString(R.string.data_parser_error));
        }
    }
}
