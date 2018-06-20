package cn.ollyice.framework.core;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by ollyice on 2018/6/19.
 */

public interface IApplication {
    void attachBaseContext(Application app);
    void onCreate(Application app);
    void onTerminate(Application app);
    void onConfigurationChanged(Application app, Configuration newConfig);
    void onLowMemory(Application app);
    void onTrimMemory(Application app, int level);
}
