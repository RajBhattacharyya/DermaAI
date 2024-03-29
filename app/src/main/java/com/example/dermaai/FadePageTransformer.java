package com.example.dermaai;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class FadePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            page.setAlpha(MIN_ALPHA);
        } else if (position <= 1) {
            float alpha = Math.max(MIN_ALPHA, 1 - Math.abs(position));
            page.setAlpha(alpha);
        } else {
            page.setAlpha(MIN_ALPHA);
        }
    }
}

