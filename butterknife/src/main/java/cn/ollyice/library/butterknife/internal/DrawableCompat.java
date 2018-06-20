package cn.ollyice.library.butterknife.internal;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import cn.ollyice.library.butterknife.internal.drawable.DrawableCompatApi23;
import cn.ollyice.library.butterknife.internal.drawable.DrawableCompatBase;
import cn.ollyice.library.butterknife.internal.drawable.DrawableCompatHoneycomb;
import cn.ollyice.library.butterknife.internal.drawable.DrawableCompatJellybeanMr1;
import cn.ollyice.library.butterknife.internal.drawable.DrawableCompatKitKat;
import cn.ollyice.library.butterknife.internal.drawable.DrawableCompatLollipop;
import cn.ollyice.library.butterknife.internal.drawable.DrawableWrapper;

/**
 * Created by admin on 2018/6/2.
 */

public final class DrawableCompat {
    static final DrawableCompat.DrawableImpl IMPL;

    public static void jumpToCurrentState( Drawable drawable) {
        IMPL.jumpToCurrentState(drawable);
    }

    public static void setAutoMirrored( Drawable drawable, boolean mirrored) {
        IMPL.setAutoMirrored(drawable, mirrored);
    }

    public static boolean isAutoMirrored( Drawable drawable) {
        return IMPL.isAutoMirrored(drawable);
    }

    public static void setHotspot( Drawable drawable, float x, float y) {
        IMPL.setHotspot(drawable, x, y);
    }

    public static void setHotspotBounds( Drawable drawable, int left, int top, int right, int bottom) {
        IMPL.setHotspotBounds(drawable, left, top, right, bottom);
    }

    public static void setTint( Drawable drawable,  int tint) {
        IMPL.setTint(drawable, tint);
    }

    public static void setTintList( Drawable drawable,  ColorStateList tint) {
        IMPL.setTintList(drawable, tint);
    }

    public static void setTintMode( Drawable drawable,  PorterDuff.Mode tintMode) {
        IMPL.setTintMode(drawable, tintMode);
    }

    public static int getAlpha( Drawable drawable) {
        return IMPL.getAlpha(drawable);
    }

    public static void applyTheme( Drawable drawable,  Resources.Theme t) {
        IMPL.applyTheme(drawable, t);
    }

    public static boolean canApplyTheme( Drawable drawable) {
        return IMPL.canApplyTheme(drawable);
    }

    public static ColorFilter getColorFilter( Drawable drawable) {
        return IMPL.getColorFilter(drawable);
    }

    public static void clearColorFilter( Drawable drawable) {
        IMPL.clearColorFilter(drawable);
    }

    public static void inflate( Drawable drawable,  Resources res,  XmlPullParser parser,  AttributeSet attrs,  Resources.Theme theme) throws XmlPullParserException, IOException {
        IMPL.inflate(drawable, res, parser, attrs, theme);
    }

    public static Drawable wrap( Drawable drawable) {
        return IMPL.wrap(drawable);
    }

    public static <T extends Drawable> T unwrap( Drawable drawable) {
        return (T) (drawable instanceof DrawableWrapper ?((DrawableWrapper)drawable).getWrappedDrawable():drawable);
    }

    public static boolean setLayoutDirection( Drawable drawable, int layoutDirection) {
        return IMPL.setLayoutDirection(drawable, layoutDirection);
    }

    public static int getLayoutDirection( Drawable drawable) {
        return IMPL.getLayoutDirection(drawable);
    }

    private DrawableCompat() {
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if(version >= 23) {
            IMPL = new DrawableCompat.MDrawableImpl();
        } else if(version >= 21) {
            IMPL = new DrawableCompat.LollipopDrawableImpl();
        } else if(version >= 19) {
            IMPL = new DrawableCompat.KitKatDrawableImpl();
        } else if(version >= 17) {
            IMPL = new DrawableCompat.JellybeanMr1DrawableImpl();
        } else if(version >= 11) {
            IMPL = new DrawableCompat.HoneycombDrawableImpl();
        } else {
            IMPL = new DrawableCompat.BaseDrawableImpl();
        }

    }

    static class MDrawableImpl extends DrawableCompat.LollipopDrawableImpl {
        MDrawableImpl() {
        }

        public boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return DrawableCompatApi23.setLayoutDirection(drawable, layoutDirection);
        }

        public int getLayoutDirection(Drawable drawable) {
            return DrawableCompatApi23.getLayoutDirection(drawable);
        }

        public Drawable wrap(Drawable drawable) {
            return drawable;
        }

        public void clearColorFilter(Drawable drawable) {
            drawable.clearColorFilter();
        }
    }

    static class LollipopDrawableImpl extends DrawableCompat.KitKatDrawableImpl {
        LollipopDrawableImpl() {
        }

        public void setHotspot(Drawable drawable, float x, float y) {
            DrawableCompatLollipop.setHotspot(drawable, x, y);
        }

        public void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
            DrawableCompatLollipop.setHotspotBounds(drawable, left, top, right, bottom);
        }

        public void setTint(Drawable drawable, int tint) {
            DrawableCompatLollipop.setTint(drawable, tint);
        }

        public void setTintList(Drawable drawable, ColorStateList tint) {
            DrawableCompatLollipop.setTintList(drawable, tint);
        }

        public void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
            DrawableCompatLollipop.setTintMode(drawable, tintMode);
        }

        public Drawable wrap(Drawable drawable) {
            return DrawableCompatLollipop.wrapForTinting(drawable);
        }

        public void applyTheme(Drawable drawable, Resources.Theme t) {
            DrawableCompatLollipop.applyTheme(drawable, t);
        }

        public boolean canApplyTheme(Drawable drawable) {
            return DrawableCompatLollipop.canApplyTheme(drawable);
        }

        public ColorFilter getColorFilter(Drawable drawable) {
            return DrawableCompatLollipop.getColorFilter(drawable);
        }

        public void clearColorFilter(Drawable drawable) {
            DrawableCompatLollipop.clearColorFilter(drawable);
        }

        public void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws IOException, XmlPullParserException {
            DrawableCompatLollipop.inflate(drawable, res, parser, attrs, t);
        }
    }

    static class KitKatDrawableImpl extends DrawableCompat.JellybeanMr1DrawableImpl {
        KitKatDrawableImpl() {
        }

        public void setAutoMirrored(Drawable drawable, boolean mirrored) {
            DrawableCompatKitKat.setAutoMirrored(drawable, mirrored);
        }

        public boolean isAutoMirrored(Drawable drawable) {
            return DrawableCompatKitKat.isAutoMirrored(drawable);
        }

        public Drawable wrap(Drawable drawable) {
            return DrawableCompatKitKat.wrapForTinting(drawable);
        }

        public int getAlpha(Drawable drawable) {
            return DrawableCompatKitKat.getAlpha(drawable);
        }
    }

    static class JellybeanMr1DrawableImpl extends DrawableCompat.HoneycombDrawableImpl {
        JellybeanMr1DrawableImpl() {
        }

        public boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return DrawableCompatJellybeanMr1.setLayoutDirection(drawable, layoutDirection);
        }

        public int getLayoutDirection(Drawable drawable) {
            int dir = DrawableCompatJellybeanMr1.getLayoutDirection(drawable);
            return dir >= 0?dir:0;
        }
    }

    static class HoneycombDrawableImpl extends DrawableCompat.BaseDrawableImpl {
        HoneycombDrawableImpl() {
        }

        public void jumpToCurrentState(Drawable drawable) {
            DrawableCompatHoneycomb.jumpToCurrentState(drawable);
        }

        public Drawable wrap(Drawable drawable) {
            return DrawableCompatHoneycomb.wrapForTinting(drawable);
        }
    }

    static class BaseDrawableImpl implements DrawableCompat.DrawableImpl {
        BaseDrawableImpl() {
        }

        public void jumpToCurrentState(Drawable drawable) {
        }

        public void setAutoMirrored(Drawable drawable, boolean mirrored) {
        }

        public boolean isAutoMirrored(Drawable drawable) {
            return false;
        }

        public void setHotspot(Drawable drawable, float x, float y) {
        }

        public void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
        }

        public void setTint(Drawable drawable, int tint) {
            DrawableCompatBase.setTint(drawable, tint);
        }

        public void setTintList(Drawable drawable, ColorStateList tint) {
            DrawableCompatBase.setTintList(drawable, tint);
        }

        public void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
            DrawableCompatBase.setTintMode(drawable, tintMode);
        }

        public Drawable wrap(Drawable drawable) {
            return DrawableCompatBase.wrapForTinting(drawable);
        }

        public boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return false;
        }

        public int getLayoutDirection(Drawable drawable) {
            return 0;
        }

        public int getAlpha(Drawable drawable) {
            return 0;
        }

        public void applyTheme(Drawable drawable, Resources.Theme t) {
        }

        public boolean canApplyTheme(Drawable drawable) {
            return false;
        }

        public ColorFilter getColorFilter(Drawable drawable) {
            return null;
        }

        public void clearColorFilter(Drawable drawable) {
            drawable.clearColorFilter();
        }

        public void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws IOException, XmlPullParserException {
            DrawableCompatBase.inflate(drawable, res, parser, attrs, t);
        }
    }

    interface DrawableImpl {
        void jumpToCurrentState(Drawable var1);

        void setAutoMirrored(Drawable var1, boolean var2);

        boolean isAutoMirrored(Drawable var1);

        void setHotspot(Drawable var1, float var2, float var3);

        void setHotspotBounds(Drawable var1, int var2, int var3, int var4, int var5);

        void setTint(Drawable var1, int var2);

        void setTintList(Drawable var1, ColorStateList var2);

        void setTintMode(Drawable var1, PorterDuff.Mode var2);

        Drawable wrap(Drawable var1);

        boolean setLayoutDirection(Drawable var1, int var2);

        int getLayoutDirection(Drawable var1);

        int getAlpha(Drawable var1);

        void applyTheme(Drawable var1, Resources.Theme var2);

        boolean canApplyTheme(Drawable var1);

        ColorFilter getColorFilter(Drawable var1);

        void clearColorFilter(Drawable var1);

        void inflate(Drawable var1, Resources var2, XmlPullParser var3, AttributeSet var4, Resources.Theme var5) throws IOException, XmlPullParserException;
    }
}
