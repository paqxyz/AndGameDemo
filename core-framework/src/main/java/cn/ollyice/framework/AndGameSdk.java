package cn.ollyice.framework;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

import cn.ollyice.framework.core.Callback;
import cn.ollyice.framework.core.IActivity;
import cn.ollyice.framework.core.IApplication;
import cn.ollyice.framework.core.IGame;
import cn.ollyice.framework.core.IHttp;
import cn.ollyice.framework.core.ISdk;
import cn.ollyice.framework.http.HttpManager;
import cn.ollyice.framework.info.ExtraInfo;
import cn.ollyice.framework.info.OrderInfo;
import cn.ollyice.framework.info.PayInfo;
import cn.ollyice.framework.info.UserInfo;

import static cn.ollyice.framework.Systems.ApplicationName;
import static cn.ollyice.framework.Systems.HttpName;
import static cn.ollyice.framework.Systems.PropertiesName;
import static cn.ollyice.framework.Systems.SdkName;

/**
 * Created by admin on 2018/6/19.
 */

public class AndGameSdk {
    private static final Handler MainHadler = new Handler(Looper.getMainLooper());
    public static IApplication Application;
    public static IGame Game;
    public static IActivity Activity;

    private static IApplication ApplicationProxy;
    private static ISdk SdkProxy;

    static{
        try {
            InputStream is = AndGameSdk.class.getClassLoader().getResourceAsStream(PropertiesName);
            Properties properties = new Properties();
            properties.load(is);
            ApplicationProxy = newInstance(properties.getProperty(ApplicationName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Application = new AndGameApplication();
    }


    static void runOnUiThread(Runnable runnable) {
        MainHadler.post(runnable);
    }

    private static <T> T newInstance(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final class AndGameApplication implements IApplication {
        private static final String TAG = "ApplicationProxy";

        private void initSdk(Context context){
            try {
                InputStream is = AndGameSdk.class.getClassLoader().getResourceAsStream(PropertiesName);
                Properties properties = new Properties();
                properties.load(is);
                SdkProxy = newInstance(properties.getProperty(SdkName));
                IHttp http = newInstance(properties.getProperty(HttpName));
                HttpManager.setInstance(http);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Object object = new AndGameActivity();

            Game = (IGame) object;
            Activity = (IActivity) object;
        }

        @Override
        public void attachBaseContext(android.app.Application app) {
            if (ApplicationProxy != null) {
                ApplicationProxy.attachBaseContext(app);
                initSdk(app);
            } else {
                Log.e(TAG, "the proxy application object is null");
            }
        }

        @Override
        public void onCreate(android.app.Application app) {
            if (ApplicationProxy != null) {
                ApplicationProxy.onCreate(app);
            } else {
                Log.e(TAG, "the proxy application object is null");
            }
        }

        @Override
        public void onTerminate(Application app) {
            if (ApplicationProxy != null) {
                ApplicationProxy.onTerminate(app);
            } else {
                Log.e(TAG, "the proxy application object is null");
            }
        }

        @Override
        public void onConfigurationChanged(Application app, Configuration newConfig) {
            if (ApplicationProxy != null) {
                ApplicationProxy.onConfigurationChanged(app, newConfig);
            } else {
                Log.e(TAG, "the proxy application object is null");
            }
        }

        @Override
        public void onLowMemory(Application app) {
            if (ApplicationProxy != null) {
                ApplicationProxy.onLowMemory(app);
            } else {
                Log.e(TAG, "the proxy application object is null");
            }
        }

        @Override
        public void onTrimMemory(Application app, int level) {
            if (ApplicationProxy != null) {
                ApplicationProxy.onTrimMemory(app, level);
            } else {
                Log.e(TAG, "the proxy application object is null");
            }
        }
    }

    private static final class AndGameActivity implements ISdk {
        private static final String TAG = "ActivityProxy";

        @Override
        public void doInit(final android.app.Activity activity, final Callback<Integer> onSuccess, final Callback<Throwable> onFailed, final Callback<Integer> onLogout) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SdkProxy != null) {
                        SdkProxy.doInit(activity, onSuccess, onFailed,onLogout);
                    } else {
                        Log.e(TAG, "the proxy sdk object is null");
                    }
                }
            });
        }

        @Override
        public void doLogin(final Activity activity, final Callback<Integer> onSuccess, final Callback<Throwable> onFailed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SdkProxy != null) {
                        SdkProxy.doLogin(activity, onSuccess, onFailed);
                    } else {
                        Log.e(TAG, "the proxy sdk object is null");
                    }
                }
            });
        }

        @Override
        public void doLogout(final Activity activity) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SdkProxy != null) {
                        SdkProxy.doLogout(activity);
                    } else {
                        Log.e(TAG, "the proxy sdk object is null");
                    }
                }
            });
        }

        @Override
        public void doPay(final Activity activity, final PayInfo pay, final Callback<Integer> onSuccess, final Callback<Throwable> onFailed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SdkProxy != null) {
                        SdkProxy.doPay(activity, pay, onSuccess, onFailed);
                    } else {
                        Log.e(TAG, "the proxy sdk object is null");
                    }
                }
            });
        }

        @Override
        public void doSubmit(final Activity activity, final ExtraInfo extra) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SdkProxy != null) {
                        SdkProxy.doSubmit(activity, extra);
                    } else {
                        Log.e(TAG, "the proxy sdk object is null");
                    }
                }
            });
        }

        @Override
        public void doExit(Activity activity, Callback<Integer> onSuccess, Callback<Throwable> onError) {
            if (SdkProxy != null) {
                SdkProxy.doExit(activity, onSuccess, onError);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public UserInfo getUserInfo() {
            if (SdkProxy != null) {
                return SdkProxy.getUserInfo();
            }
            return null;
        }

        @Override
        public OrderInfo getOrderInfo() {
            if (SdkProxy != null) {
                return SdkProxy.getOrderInfo();
            }
            return null;
        }

        @Override
        public void openUserCenter(final Activity activity) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (SdkProxy != null) {
                        SdkProxy.openUserCenter(activity);
                    } else {
                        Log.e(TAG, "the proxy sdk object is null");
                    }
                }
            });
        }

        @Override
        public void openUserClient(Activity activity) {

        }

        @Override
        public void onCreate(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onCreate(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onResume(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onResume(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onRestart(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onRestart(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onStart(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onStart(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onPause(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onPause(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onStop(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onStop(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onDestroy(final Activity activity) {
            if (SdkProxy != null) {
                SdkProxy.onDestroy(activity);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data) {
            if (SdkProxy != null) {
                SdkProxy.onActivityResult(activity, requestCode, resultCode, data);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onRequestPermissionsResult(final Activity activity,int requestCode, String[] permissions, int[] grantResults) {
            if (SdkProxy != null) {
                SdkProxy.onRequestPermissionsResult(activity,requestCode,permissions,grantResults);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onConfigurationChanged(final Activity activity, final Configuration newConfig) {
            if (SdkProxy != null) {
                SdkProxy.onConfigurationChanged(activity, newConfig);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

        @Override
        public void onNewIntent(final Activity activity, final Intent intent) {
            if (SdkProxy != null) {
                SdkProxy.onNewIntent(activity, intent);
            } else {
                Log.e(TAG, "the proxy sdk object is null");
            }
        }

    }
}
