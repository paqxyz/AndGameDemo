package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by admin on 2018/6/2.
 */


@TargetApi(21)
public class DrawableCompatLollipop {
    DrawableCompatLollipop() {
    }

    public static void setHotspot(Drawable drawable, float x, float y) {
        drawable.setHotspot(x, y);
    }

    public static void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
        drawable.setHotspotBounds(left, top, right, bottom);
    }

    public static void setTint(Drawable drawable, int tint) {
        drawable.setTint(tint);
    }

    public static void setTintList(Drawable drawable, ColorStateList tint) {
        drawable.setTintList(tint);
    }

    public static void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
        drawable.setTintMode(tintMode);
    }

    public static Drawable wrapForTinting(Drawable drawable) {
        return (Drawable)(!(drawable instanceof TintAwareDrawable)?new DrawableWrapperLollipop(drawable):drawable);
    }

    public static void applyTheme(Drawable drawable, Resources.Theme t) {
        drawable.applyTheme(t);
    }

    public static boolean canApplyTheme(Drawable drawable) {
        return drawable.canApplyTheme();
    }

    public static ColorFilter getColorFilter(Drawable drawable) {
        return drawable.getColorFilter();
    }

    public static void clearColorFilter(Drawable drawable) {
        drawable.clearColorFilter();
        if(drawable instanceof InsetDrawable) {
            clearColorFilter(((InsetDrawable)drawable).getDrawable());
        } else if(drawable instanceof DrawableWrapper) {
            clearColorFilter(((DrawableWrapper)drawable).getWrappedDrawable());
        } else if(drawable instanceof DrawableContainer) {
            DrawableContainer container = (DrawableContainer)drawable;
            DrawableContainer.DrawableContainerState state = (DrawableContainer.DrawableContainerState)container.getConstantState();
            if(state != null) {
                int i = 0;

                for(int count = state.getChildCount(); i < count; ++i) {
                    Drawable child = state.getChild(i);
                    if(child != null) {
                        clearColorFilter(child);
                    }
                }
            }
        }

    }

    public static void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws IOException, XmlPullParserException {
        drawable.inflate(res, parser, attrs, t);
    }
}
