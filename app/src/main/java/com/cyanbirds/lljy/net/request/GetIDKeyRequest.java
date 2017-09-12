package com.cyanbirds.lljy.net.request;

import android.text.TextUtils;

import com.cyanbirds.lljy.entity.IDKey;
import com.cyanbirds.lljy.manager.AppManager;
import com.cyanbirds.lljy.net.base.ResultPostExecute;
import com.cyanbirds.lljy.utils.AESOperator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by wangyb on 2017/5/17.
 * 描述：获取微信id
 */

public class GetIDKeyRequest extends ResultPostExecute<List<IDKey>> {

    public void request(String pay) {
        Call<ResponseBody> call = AppManager.getUserService().getIdKey(pay);
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
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void parseJson(String json){
        try {
            String decryptData = AESOperator.getInstance().decrypt(json);
            if (!TextUtils.isEmpty(decryptData)) {
                Type listType = new TypeToken<ArrayList<IDKey>>() {
                }.getType();
                Gson gson = new Gson();
                ArrayList<IDKey> idKeys = gson.fromJson(decryptData, listType);
                onPostExecute(idKeys);
            }
        } catch (Exception e) {
        }
    }

}