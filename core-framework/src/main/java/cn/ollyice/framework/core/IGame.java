package cn.ollyice.framework.core;

import android.app.Activity;

import cn.ollyice.framework.info.*;

/**
 * Created by ollyice on 2018/6/19.
 */

public interface IGame {
    //初始化接口
    void doInit(Activity activity, Callback<Integer> onSuccess, Callback<Throwable> onError, Callback<Integer> onLogout);
    //登录接口
    void doLogin(Activity activity, Callback<Integer> onSuccess, Callback<Throwable> onError);
    //登出接口
    void doLogout(Activity activity);
    //支付接口
    void doPay(Activity activity, PayInfo payInfo, Callback<Integer> onSuccess, Callback<Throwable> onError);
    //提交游戏角色信息接口
    void doSubmit(Activity activity, ExtraInfo extra);
    //登录成功后获取用户信息
    //退出游戏
    void doExit(Activity activity, Callback<Integer> onSuccess, Callback<Throwable> onError);
    UserInfo getUserInfo();
    //获取支付订单信息
    OrderInfo getOrderInfo();
    //打开用户中心
    void openUserCenter(Activity activity);
    //打开联系客服
    void openUserClient(Activity activity);
}
