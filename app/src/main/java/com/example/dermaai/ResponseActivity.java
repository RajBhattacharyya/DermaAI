package com.example.dermaai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class ResponseActivity extends AppCompatActivity {
    private TextView animatedText;
    private String originalText;
    private int currentIndex = 0;
    private static final int UPDATE_TEXT = 1;
    private static final long DELAY_MILLIS = 10;
    private final Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        animatedText = findViewById(R.id.responseText);
        originalText = getIntent().getStringExtra("apiResponse");

        if (originalText != null && !originalText.isEmpty()) {
            animateText();
        }

        Button consultBtn = findViewById(R.id.DrBtn);
        consultBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ResponseActivity.this, consultActivity.class);
            startActivity(intent);
        });
    }

    private void animateText() {
        handler.sendEmptyMessage(UPDATE_TEXT);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<ResponseActivity> activityRef;

        MyHandler(ResponseActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ResponseActivity activity = activityRef.get();
            if (activity != null && msg.what == UPDATE_TEXT) {
                TextView animatedText = activity.animatedText;
                String originalText = activity.originalText;
                int currentIndex = activity.currentIndex;

                if (currentIndex <= originalText.length()) {
                    animatedText.setText(originalText.substring(0, currentIndex));
                    currentIndex++;
                    sendEmptyMessageDelayed(UPDATE_TEXT, DELAY_MILLIS);
                    activity.currentIndex = currentIndex; // Update currentIndex in activity
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Remove all pending messages
    }
}
