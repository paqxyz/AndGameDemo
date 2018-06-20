package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2018/6/2.
 */

@TargetApi(19)
public class DrawableCompatKitKat {
    DrawableCompatKitKat() {
    }

    public static void setAutoMirrored(Drawable drawable, boolean mirrored) {
        drawable.setAutoMirrored(mirrored);
    }

    public static boolean isAutoMirrored(Drawable drawable) {
        return drawable.isAutoMirrored();
    }

    public static Drawable wrapForTinting(Drawable drawable) {
        return (Drawable)(!(drawable instanceof TintAwareDrawable)?new DrawableWrapperKitKat(drawable):drawable);
    }

    public static int getAlpha(Drawable drawable) {
        return drawable.getAlpha();
    }
}
