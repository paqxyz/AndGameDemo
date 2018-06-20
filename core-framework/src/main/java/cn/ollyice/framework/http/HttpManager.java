package cn.ollyice.framework.http;

import cn.ollyice.framework.core.IHttp;

/**
 * Created by admin on 2018/6/19.
 */

public class HttpManager {
    private static IHttp instance;

    public static IHttp getInstance(){
        return instance;
    }

    public static void setInstance(IHttp instance) {
        HttpManager.instance = instance;
    }
}
