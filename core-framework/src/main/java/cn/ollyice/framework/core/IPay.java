package cn.ollyice.framework.core;

import android.app.Activity;

import java.util.Map;

import cn.ollyice.framework.info.PayInfo;

/**
 * Created by admin on 2018/6/19.
 */

public interface IPay {
    void startPay1(Activity activity, PayInfo payInfo, Callback<Integer> onSuccess, Callback<Throwable> onError,Map<String, String> extarData);
    void startPay3(Activity activity, PayInfo payInfo, Callback<Integer> onSuccess, Callback<Throwable> onError,Map<String, String> extarData);
    Map<String,String> getChannelOrderInfo();
}
