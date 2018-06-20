package cn.ollyice.framework.core;

import java.util.Map;

/**
 * Created by admin on 2018/6/19.
 */

public interface IHttp {
    void update(Map<String,String> body,CallUpdate call,CallError error);

    void login(Map<String,String> body,CallLogin call,CallError error);

    void submit(Map<String,String> body,CallString call,CallError error);

    void pay(Map<String,String> body,CallPay call,CallError error);

    void notify(Map<String,String> body,CallString call,CallError error);

    void post(String url,Map<String,String> body,CallString call,CallError error);

    interface CallUpdate{
        void onNext(String notifyUrl,String downloadUrl,int appVersion,int resVersion,Map<String,String> extraInfo);
    }

    interface CallLogin{
        void onNext(String userId,String userName,String userToken);
    }

    interface CallPay{
        void onNext(String orderId,String notifyUrl,Map<String,String> extraInfo);
    }

    interface CallString{
        void onNext(String response);
    }

    interface CallError{
        void onNext(String error);
    }
}
