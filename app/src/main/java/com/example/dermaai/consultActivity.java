package com.example.dermaai;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

public class consultActivity extends AppCompatActivity {

    private static final long AUTO_SCROLL_DELAY = 3000; // Delay in milliseconds between each scroll
    private boolean isAutoScrollEnabled = true; // Flag to control auto-scrolling
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consult);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Initialize auto-scrolling handler and runnable
        autoScrollHandler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAutoScrollEnabled) {
                    int currentItem = viewPager.getCurrentItem();
                    int itemCount = viewPager.getAdapter() != null ? viewPager.getAdapter().getCount() : 0;
                    if (currentItem < itemCount - 1) {
                        viewPager.setCurrentItem(currentItem + 1, true);
                    } else {
                        viewPager.setCurrentItem(0, true);
                    }
                    autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAutoScrollEnabled = true;
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAutoScrollEnabled = false;
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

}