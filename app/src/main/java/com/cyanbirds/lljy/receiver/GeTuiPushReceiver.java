package com.cyanbirds.lljy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cyanbirds.lljy.manager.AppManager;
import com.cyanbirds.lljy.utils.PushMsgUtil;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

public class GeTuiPushReceiver extends BroadcastReceiver {

    private Handler mHandler = new Handler();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    if (AppManager.getClientUser().isShowVip) {
                        final String data = new String(payload);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                PushMsgUtil.getInstance().handlePushMsg(true, data);
                            }
                        });
                    }
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                if (online) {
                    PushManager.getInstance().unBindAlias(context, AppManager.getClientUser().userId, true);
                    PushManager.getInstance().setTag(context, null, System.currentTimeMillis() + "");

                    PushManager.getInstance().bindAlias(context, AppManager.getClientUser().userId);
                    Tag[] tags;
                    if ("男".equals(AppManager.getClientUser().sex)) {
                        Tag ftag = new Tag();
                        ftag.setName("female");
                        tags = new Tag[]{ftag};
                    } else {
                        Tag mtag = new Tag();
                        mtag.setName("male");
                        tags = new Tag[]{mtag};
                    }
                    PushManager.getInstance().setTag(context, tags, System.currentTimeMillis() + "");
                }
                break;

            case PushConsts.SET_TAG_RESULT:
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;

            default:
                break;
        }
    }
}
