package cn.ollyice.library.butterknife.internal.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

/**
 * Created by admin on 2018/6/2.
 */
public interface TintAwareDrawable {
    void setTint( int var1);

    void setTintList(ColorStateList var1);

    void setTintMode(PorterDuff.Mode var1);
}
