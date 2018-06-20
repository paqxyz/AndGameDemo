package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;

/**
 * Created by admin on 2018/6/2.
 */

@TargetApi(21)
class DrawableWrapperLollipop extends DrawableWrapperKitKat {
    DrawableWrapperLollipop(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperLollipop(DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    public void setHotspot(float x, float y) {
        this.mDrawable.setHotspot(x, y);
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        this.mDrawable.setHotspotBounds(left, top, right, bottom);
    }

    public void getOutline(Outline outline) {
        this.mDrawable.getOutline(outline);
    }

    public Rect getDirtyBounds() {
        return this.mDrawable.getDirtyBounds();
    }

    public void setTintList(ColorStateList tint) {
        if(this.isCompatTintEnabled()) {
            super.setTintList(tint);
        } else {
            this.mDrawable.setTintList(tint);
        }

    }

    public void setTint(int tintColor) {
        if(this.isCompatTintEnabled()) {
            super.setTint(tintColor);
        } else {
            this.mDrawable.setTint(tintColor);
        }

    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        if(this.isCompatTintEnabled()) {
            super.setTintMode(tintMode);
        } else {
            this.mDrawable.setTintMode(tintMode);
        }

    }

    public boolean setState(int[] stateSet) {
        if(super.setState(stateSet)) {
            this.invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

    protected boolean isCompatTintEnabled() {
        if(Build.VERSION.SDK_INT != 21) {
            return false;
        } else {
            Drawable drawable = this.mDrawable;
            return drawable instanceof GradientDrawable || drawable instanceof DrawableContainer || drawable instanceof InsetDrawable;
        }
    }

    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperLollipop.DrawableWrapperStateLollipop(this.mState, (Resources)null);
    }

    private static class DrawableWrapperStateLollipop extends DrawableWrapperState {
        DrawableWrapperStateLollipop( DrawableWrapperState orig,  Resources res) {
            super(orig, res);
        }

        public Drawable newDrawable( Resources res) {
            return new DrawableWrapperLollipop(this, res);
        }
    }
}
