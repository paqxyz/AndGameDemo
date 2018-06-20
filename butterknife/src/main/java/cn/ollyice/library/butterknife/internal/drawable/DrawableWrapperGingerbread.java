package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2018/6/2.
 */

@TargetApi(9)
class DrawableWrapperGingerbread extends Drawable implements Drawable.Callback, DrawableWrapper, TintAwareDrawable {
    static final PorterDuff.Mode DEFAULT_TINT_MODE;
    private int mCurrentColor;
    private PorterDuff.Mode mCurrentMode;
    private boolean mColorFilterSet;
    DrawableWrapperGingerbread.DrawableWrapperState mState;
    private boolean mMutated;
    Drawable mDrawable;

    DrawableWrapperGingerbread( DrawableWrapperGingerbread.DrawableWrapperState state,  Resources res) {
        this.mState = state;
        this.updateLocalState(res);
    }

    DrawableWrapperGingerbread( Drawable dr) {
        this.mState = this.mutateConstantState();
        this.setWrappedDrawable(dr);
    }

    private void updateLocalState( Resources res) {
        if(this.mState != null && this.mState.mDrawableState != null) {
            Drawable dr = this.newDrawableFromState(this.mState.mDrawableState, res);
            this.setWrappedDrawable(dr);
        }

    }

    protected Drawable newDrawableFromState( ConstantState state,  Resources res) {
        return state.newDrawable(res);
    }

    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    protected void onBoundsChange(Rect bounds) {
        if(this.mDrawable != null) {
            this.mDrawable.setBounds(bounds);
        }

    }

    public void setChangingConfigurations(int configs) {
        this.mDrawable.setChangingConfigurations(configs);
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | (this.mState != null?this.mState.getChangingConfigurations():0) | this.mDrawable.getChangingConfigurations();
    }

    public void setDither(boolean dither) {
        this.mDrawable.setDither(dither);
    }

    public void setFilterBitmap(boolean filter) {
        this.mDrawable.setFilterBitmap(filter);
    }

    public void setAlpha(int alpha) {
        this.mDrawable.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mDrawable.setColorFilter(cf);
    }

    public boolean isStateful() {
        ColorStateList tintList = this.isCompatTintEnabled() && this.mState != null?this.mState.mTint:null;
        return tintList != null && tintList.isStateful() || this.mDrawable.isStateful();
    }

    public boolean setState(int[] stateSet) {
        boolean handled = this.mDrawable.setState(stateSet);
        handled = this.updateTint(stateSet) || handled;
        return handled;
    }

    public int[] getState() {
        return this.mDrawable.getState();
    }

    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || this.mDrawable.setVisible(visible, restart);
    }

    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    public boolean getPadding(Rect padding) {
        return this.mDrawable.getPadding(padding);
    }

    public ConstantState getConstantState() {
        if(this.mState != null && this.mState.canConstantState()) {
            this.mState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mState;
        } else {
            return null;
        }
    }

    public Drawable mutate() {
        if(!this.mMutated && super.mutate() == this) {
            this.mState = this.mutateConstantState();
            if(this.mDrawable != null) {
                this.mDrawable.mutate();
            }

            if(this.mState != null) {
                this.mState.mDrawableState = this.mDrawable != null?this.mDrawable.getConstantState():null;
            }

            this.mMutated = true;
        }

        return this;
    }

    DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperGingerbread.DrawableWrapperStateBase(this.mState, (Resources)null);
    }

    public void invalidateDrawable(Drawable who) {
        this.invalidateSelf();
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        this.scheduleSelf(what, when);
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        this.unscheduleSelf(what);
    }

    protected boolean onLevelChange(int level) {
        return this.mDrawable.setLevel(level);
    }

    public void setTint(int tint) {
        this.setTintList(ColorStateList.valueOf(tint));
    }

    public void setTintList(ColorStateList tint) {
        this.mState.mTint = tint;
        this.updateTint(this.getState());
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mState.mTintMode = tintMode;
        this.updateTint(this.getState());
    }

    private boolean updateTint(int[] state) {
        if(!this.isCompatTintEnabled()) {
            return false;
        } else {
            ColorStateList tintList = this.mState.mTint;
            PorterDuff.Mode tintMode = this.mState.mTintMode;
            if(tintList != null && tintMode != null) {
                int color = tintList.getColorForState(state, tintList.getDefaultColor());
                if(!this.mColorFilterSet || color != this.mCurrentColor || tintMode != this.mCurrentMode) {
                    this.setColorFilter(color, tintMode);
                    this.mCurrentColor = color;
                    this.mCurrentMode = tintMode;
                    this.mColorFilterSet = true;
                    return true;
                }
            } else {
                this.mColorFilterSet = false;
                this.clearColorFilter();
            }

            return false;
        }
    }

    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    public final void setWrappedDrawable(Drawable dr) {
        if(this.mDrawable != null) {
            this.mDrawable.setCallback((Callback)null);
        }

        this.mDrawable = dr;
        if(dr != null) {
            dr.setCallback(this);
            this.setVisible(dr.isVisible(), true);
            this.setState(dr.getState());
            this.setLevel(dr.getLevel());
            this.setBounds(dr.getBounds());
            if(this.mState != null) {
                this.mState.mDrawableState = dr.getConstantState();
            }
        }

        this.invalidateSelf();
    }

    protected boolean isCompatTintEnabled() {
        return true;
    }

    static {
        DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    }

    private static class DrawableWrapperStateBase extends DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateBase( DrawableWrapperGingerbread.DrawableWrapperState orig,  Resources res) {
            super(orig, res);
        }

        public Drawable newDrawable( Resources res) {
            return new DrawableWrapperGingerbread(this, res);
        }
    }

    protected abstract static class DrawableWrapperState extends ConstantState {
        int mChangingConfigurations;
        ConstantState mDrawableState;
        ColorStateList mTint = null;
        PorterDuff.Mode mTintMode;

        DrawableWrapperState( DrawableWrapperGingerbread.DrawableWrapperState orig,  Resources res) {
            this.mTintMode = DrawableWrapperGingerbread.DEFAULT_TINT_MODE;
            if(orig != null) {
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mDrawableState = orig.mDrawableState;
                this.mTint = orig.mTint;
                this.mTintMode = orig.mTintMode;
            }

        }

        public Drawable newDrawable() {
            return this.newDrawable((Resources)null);
        }

        public abstract Drawable newDrawable( Resources var1);

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mDrawableState != null?this.mDrawableState.getChangingConfigurations():0);
        }

        boolean canConstantState() {
            return this.mDrawableState != null;
        }
    }
}
