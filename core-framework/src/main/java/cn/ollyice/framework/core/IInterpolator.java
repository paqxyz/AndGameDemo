package cn.ollyice.framework.core;

/**
 * Created by admin on 2018/6/19.
 */

public interface IInterpolator {
    /**
     * 是否在拦截登录成功消息自己处理
     * @return
     */
    boolean interpolateLoginSuccess();
    /**
     * 是否拦截充值成功消息自己处理
     * @return
     */
    boolean interpolatePaySuccess();
}
