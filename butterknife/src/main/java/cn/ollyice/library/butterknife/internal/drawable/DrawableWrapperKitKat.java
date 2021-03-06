package cn.ollyice.library.butterknife.internal.drawable;


import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

@TargetApi(19)
class DrawableWrapperKitKat extends DrawableWrapperHoneycomb {
    DrawableWrapperKitKat(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperKitKat(DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    public void setAutoMirrored(boolean mirrored) {
        this.mDrawable.setAutoMirrored(mirrored);
    }

    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperKitKat.DrawableWrapperStateKitKat(this.mState, (Resources)null);
    }

    private static class DrawableWrapperStateKitKat extends DrawableWrapperState {
        DrawableWrapperStateKitKat( DrawableWrapperState orig,  Resources res) {
            super(orig, res);
        }

        public Drawable newDrawable( Resources res) {
            return new DrawableWrapperKitKat(this, res);
        }
    }
}
