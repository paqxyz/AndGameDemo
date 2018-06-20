package cn.ollyice.framework.core;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cn.ollyice.framework.http.HttpManager;
import cn.ollyice.framework.info.ExtraInfo;
import cn.ollyice.framework.info.OrderInfo;
import cn.ollyice.framework.info.PayInfo;
import cn.ollyice.framework.info.UserInfo;

/**
 * Created by admin on 2018/6/19.
 */

public abstract class AbsSdk implements ISdk,IInterpolator,ICallback,IPay{
    final String TAG = "AbsSdk";
    private OrderInfo mOrderInfo = new OrderInfo();
    private UserInfo mUserInfo = new UserInfo();
    private ExtraInfo mExtraInfo;

    private LoginParams mLoginParams = new LoginParams();
    private PayParams mPayParams = new PayParams();

    private Callback<Integer> mLogout;

    @Override
    public void doInit(Activity activity, Callback<Integer> onSuccess, Callback<Throwable> onError, Callback<Integer> onLogout) {
        mLogout = onLogout;
    }

    @Override
    public void doLogin(Activity activity, Callback<Integer> onSuccess, Callback<Throwable> onError) {
        //通过自定义类来保存游戏方传入的参数
        mLoginParams.activity = activity;
        mLoginParams.onSuccess = onSuccess;
        mLoginParams.onError = onError;
    }

    @Override
    public void doSubmit(Activity activity, ExtraInfo extra) {
        mExtraInfo = extra;
        if (HttpManager.getInstance() != null){
            HttpManager.getInstance().submit(mExtraInfo.hashMap(), new IHttp.CallString() {
                @Override
                public void onNext(String response) {

                }
            }, new IHttp.CallError() {
                @Override
                public void onNext(String error) {

                }
            });
        }
    }

    @Override
    public final void doPay(final Activity activity, final PayInfo payInfo, final Callback<Integer> onSuccess, final Callback<Throwable> onError) {
        //通过自定义类来保存游戏方传入的参数
        mPayParams.activity = activity;
        mPayParams.payInfo = payInfo;
        mPayParams.onSuccess = onSuccess;
        mPayParams.onError = onError;

        //生成默认订单状态
        getOrderInfo().setOrderState(OrderInfo.ORDER_STATE_WAIT_SERVER_NOTIFY)
                .setErrorMessage("等待服务器通知")
                .setOrderPrice(payInfo.getPrice())
                .setOrderId(payInfo.getOrderId());
        if (!payInfo.success()){//payInfo里面有空值
            String error = "订单数据不全:" + payInfo.hashMap().toString();
            getOrderInfo().setOrderState(OrderInfo.ORDER_STATE_PAY_FAILED)
                    .setErrorMessage(error)
                    .setOrderPrice(payInfo.getPrice())
                    .setOrderId(payInfo.getOrderId());
            onPayFailed();
            return;
        }
        if (HttpManager.getInstance() != null) {//http接口实现了
            HttpManager.getInstance().pay(mergePayInfo(payInfo), new IHttp.CallPay() {//请求服务器记录订单信息
                @Override
                public void onNext(String orderId, String notifyUrl, Map<String, String> extraInfo) {
                    if (!TextUtils.isEmpty(orderId)) {//替换订单id
                        payInfo.setOrderId(orderId);
                    }
                    if (!TextUtils.isEmpty(notifyUrl)){//替换支付回调地址
                        payInfo.setPayNotifyUrl(notifyUrl);
                    }
                    //开始支付
                    startPay1(activity,payInfo,onSuccess,onError,extraInfo);
                }
            }, new IHttp.CallError() {
                @Override
                public void onNext(String error) {
                    getOrderInfo().setOrderState(OrderInfo.ORDER_STATE_PAY_FAILED)
                            .setErrorMessage(error)
                            .setOrderPrice(payInfo.getPrice())
                            .setOrderId(payInfo.getOrderId());
                    onPayFailed();
                }
            });
        }else{//没有实现http接口  无法连接服务器 直接返回失败
            String error = "无法连接服务器:请实现相关服务";
            getOrderInfo().setOrderState(OrderInfo.ORDER_STATE_PAY_FAILED)
                    .setErrorMessage(error)
                    .setOrderPrice(payInfo.getPrice())
                    .setOrderId(payInfo.getOrderId());
            onPayFailed();
        }
    }

    @Override
    public void onLoginSuccess() {
        if (interpolateLoginSuccess()){//需要拦截渠道方sdk登录成功消息
            if (HttpManager.getInstance() != null){
                HttpManager.getInstance().login(getChannelLoginInfo(), new IHttp.CallLogin() {
                    @Override
                    public void onNext(String userId, String userName, String userToken) {
                        getUserInfo().setUserId(userId)
                                .setUserName(userName)
                                .setUserToken(userToken);
                        mLoginParams.onSuccess.onNext(0);
                    }
                }, new IHttp.CallError() {
                    @Override
                    public void onNext(String error) {
                        onLoginFailed();
                    }
                });
            }else{
                Log.e(TAG,"请实现http接口");
                onLoginFailed();
            }
        }else{
            mLoginParams.onSuccess.onNext(0);
        }
    }

    @Override
    public void onLoginFailed() {
        mLoginParams.onError.onNext(new Throwable("登录失败"));
    }

    @Override
    public void onPaySuccess() {
        if (interpolatePaySuccess()){
            //部分渠道sdk不会通知服务器发货 所以需要我们发行方SDK通知服务器去查询是否到账然后发货
            if (HttpManager.getInstance() != null){
                HttpManager.getInstance().notify(getChannelOrderInfo(), new IHttp.CallString() {
                    @Override
                    public void onNext(String response) {
                        mPayParams.onSuccess.onNext(0);
                    }
                }, new IHttp.CallError() {
                    @Override
                    public void onNext(String error) {
                        getOrderInfo().setErrorMessage(error);
                        onPayFailed();
                    }
                });
            }else{
                Log.e(TAG,"请实现http接口");
                getOrderInfo().setErrorMessage("支付失败");
                onPayFailed();
            }
        }else{
            mPayParams.onSuccess.onNext(0);
        }
    }

    @Override
    public void onPayFailed() {
        mPayParams.onError.onNext(new Throwable(getOrderInfo().getErrorMessage()));
    }

    @Override
    public OrderInfo getOrderInfo() {
        return mOrderInfo;
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    private final Map<String,String> mergePayInfo(PayInfo payInfo){
        if(getChannelPayInfo() != null && getChannelPayInfo().size() > 0){
            Map<String,String> map = new HashMap();
            map.putAll(payInfo.hashMap());
            map.putAll(getChannelPayInfo());
            return map;
        }else{
            return payInfo.hashMap();
        }
    }

    public static class LoginParams{
        Activity activity;
        Callback<Integer> onSuccess;
        Callback<Throwable> onError;
    }

    public static class PayParams{
        Activity activity;
        PayInfo payInfo;
        Callback<Integer> onSuccess;
        Callback<Throwable> onError;

    }
}
