package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2018/6/2.
 */
@TargetApi(23)
public class DrawableCompatApi23 {
    DrawableCompatApi23() {
    }

    public static boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
        return drawable.setLayoutDirection(layoutDirection);
    }

    public static int getLayoutDirection(Drawable drawable) {
        return drawable.getLayoutDirection();
    }
}
