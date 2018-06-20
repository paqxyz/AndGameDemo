package cn.ollyice.framework.core;

import java.util.Map;

/**
 * Created by admin on 2018/6/19.
 */

public interface ICallback {
    /**
     * 获取渠道方用户信息
     * @return
     */
    Map<String,String> getChannelLoginInfo();
    /**
     * 登录成功回调
     */
    void onLoginSuccess();
    /**
     *登录失败
     */
    void onLoginFailed();

    /**
     * 获取渠道方支付信息
     * @return
     */
    Map<String,String> getChannelPayInfo();
    /**
     * 支付成功回调
     */
    void onPaySuccess();
    /**
     *支付失败
     */
    void onPayFailed();
}
