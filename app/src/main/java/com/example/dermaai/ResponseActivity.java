package com.example.dermaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.animation.ValueAnimator;
import android.text.SpannableStringBuilder;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_response);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView = findViewById(R.id.responseText);
        String apiResponse = getIntent().getStringExtra("apiResponse");
        textView.setText(apiResponse);

        Button consultBtn = findViewById(R.id.DrBtn);
        consultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignupActivity when TextView is clicked
                Intent intent = new Intent(ResponseActivity.this, consultActivity.class);
                startActivity(intent);
            }
        });
    }
}