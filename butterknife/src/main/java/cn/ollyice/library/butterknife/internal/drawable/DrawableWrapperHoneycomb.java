package cn.ollyice.library.butterknife.internal.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2018/6/2.
 */

@TargetApi(11)
class DrawableWrapperHoneycomb extends DrawableWrapperGingerbread {
    DrawableWrapperHoneycomb(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperHoneycomb(DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperHoneycomb.DrawableWrapperStateHoneycomb(this.mState, (Resources)null);
    }

    private static class DrawableWrapperStateHoneycomb extends DrawableWrapperState {
        DrawableWrapperStateHoneycomb( DrawableWrapperState orig,  Resources res) {
            super(orig, res);
        }

        public Drawable newDrawable( Resources res) {
            return new DrawableWrapperHoneycomb(this, res);
        }
    }
}
