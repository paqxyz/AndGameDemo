package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by admin on 2018/6/2.
 */
@TargetApi(9)
public class DrawableCompatBase {
    DrawableCompatBase() {
    }

    public static void setTint(Drawable drawable, int tint) {
        if(drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTint(tint);
        }

    }

    public static void setTintList(Drawable drawable, ColorStateList tint) {
        if(drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTintList(tint);
        }

    }

    public static void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
        if(drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTintMode(tintMode);
        }

    }

    public static Drawable wrapForTinting(Drawable drawable) {
        return (Drawable)(!(drawable instanceof TintAwareDrawable)?new DrawableWrapperGingerbread(drawable):drawable);
    }

    public static void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws IOException, XmlPullParserException {
        drawable.inflate(res, parser, attrs);
    }
}
