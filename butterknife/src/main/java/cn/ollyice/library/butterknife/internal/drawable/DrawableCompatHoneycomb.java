package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2018/6/2.
 */

@TargetApi(11)
public class DrawableCompatHoneycomb {
    DrawableCompatHoneycomb() {
    }

    public static void jumpToCurrentState(Drawable drawable) {
        drawable.jumpToCurrentState();
    }

    public static Drawable wrapForTinting(Drawable drawable) {
        return (Drawable)(!(drawable instanceof TintAwareDrawable)?new DrawableWrapperHoneycomb(drawable):drawable);
    }
}
