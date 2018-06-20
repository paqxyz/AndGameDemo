package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by admin on 2018/6/2.
 */

@TargetApi(17)
public class DrawableCompatJellybeanMr1 {
    private static final String TAG = "DrawableCompatJellybeanMr1";
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;

    DrawableCompatJellybeanMr1() {
    }

    public static boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
        if(!sSetLayoutDirectionMethodFetched) {
            try {
                sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", new Class[]{Integer.TYPE});
                sSetLayoutDirectionMethod.setAccessible(true);
            } catch (NoSuchMethodException var3) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve setLayoutDirection(int) method", var3);
            }

            sSetLayoutDirectionMethodFetched = true;
        }

        if(sSetLayoutDirectionMethod != null) {
            try {
                sSetLayoutDirectionMethod.invoke(drawable, new Object[]{Integer.valueOf(layoutDirection)});
                return true;
            } catch (Exception var4) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to invoke setLayoutDirection(int) via reflection", var4);
                sSetLayoutDirectionMethod = null;
            }
        }

        return false;
    }

    public static int getLayoutDirection(Drawable drawable) {
        if(!sGetLayoutDirectionMethodFetched) {
            try {
                sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
                sGetLayoutDirectionMethod.setAccessible(true);
            } catch (NoSuchMethodException var2) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve getLayoutDirection() method", var2);
            }

            sGetLayoutDirectionMethodFetched = true;
        }

        if(sGetLayoutDirectionMethod != null) {
            try {
                return ((Integer)sGetLayoutDirectionMethod.invoke(drawable, new Object[0])).intValue();
            } catch (Exception var3) {
                Log.i("DrawableCompatJellybeanMr1", "Failed to invoke getLayoutDirection() via reflection", var3);
                sGetLayoutDirectionMethod = null;
            }
        }

        return -1;
    }
}
