package com.example.dermaai;

import static com.example.dermaai.BuildConfig.GEMINI_KEY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView drconsult = findViewById(R.id.consultPage);
        drconsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SignupActivity when TextView is clicked
                Intent intent = new Intent(DashboardActivity.this, consultActivity.class);
                startActivity(intent);
            }
        });

        Button resbutton = findViewById(R.id.submitbtn);
        EditText age = findViewById(R.id.sexInp);
        EditText sex = findViewById(R.id.ageInp);
        EditText duration = findViewById(R.id.durInp);
        EditText med = findViewById(R.id.medInp);
        EditText sym = findViewById(R.id.symInp);

        Button btnOpenCamera = findViewById(R.id.uploadbtn);
        imagePreview = findViewById(R.id.imageView);

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent();
                } else {
                    requestCameraPermission();
                }
            }
        });

        resbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAge = age.getText().toString().trim();
                String userSex = sex.getText().toString().trim();
                String userDur = duration.getText().toString().trim();
                String userMed = med.getText().toString().trim();
                String userSym = sym.getText().toString().trim();
                Bitmap imageBitmap = ((BitmapDrawable) imagePreview.getDrawable()).getBitmap();

                if (userAge.isEmpty() || userSex.isEmpty() || userDur.isEmpty() || userMed.isEmpty() || userSym.isEmpty()) {
                    Toast.makeText(DashboardActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Please Wait!", Toast.LENGTH_SHORT).show();
                    displayRes(userAge,userSex,userDur,userMed, userSym,imageBitmap);
                    age.setText("");
                    sex.setText("");
                    duration.setText("");
                    med.setText("");
                    sym.setText("");
                    // Close the keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private void displayRes(String userAge, String userSex, String userDur, String userMed, String userSym, Bitmap imageBitmap) {
        GenerativeModel gm = new GenerativeModel("gemini-pro-vision", GEMINI_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("the following parameters:" +
                        "Patient's age: " + userAge + " and gender: " + userSex + " .Duration of the skin condition: " + userDur + " .Associated symptoms: " + userSym + " .Relevant medical history: " + userMed + ". AI Response: The image analysis indicates a possible skin condition of [specific skin condition], considering the patient's profile and symptoms. This condition is typically characterized by [detail description of symptoms and appearance].Recommended Treatment:Topical treatment: [Specify AI-generated topical creams, ointments, or solutions].Oral medication: [Specify AI-generated oral medications, if applicable].Lifestyle changes: [AI-generated recommendations for lifestyle adjustments, such as diet or hygiene practices].Follow-up care: [AI-generated advice on follow-up appointments or monitoring].Please note that a precise diagnosis and treatment plan should be provided by a healthcare professional after a thorough examination.")
                .addImage(imageBitmap)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Executor executor = Executors.newSingleThreadExecutor();
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                Log.d("YourTag", resultText);
                Intent intent = new Intent(DashboardActivity.this, ResponseActivity.class);
                intent.putExtra("apiResponse", resultText);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, executor);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imagePreview.setBackground(null);
                imagePreview.setImageBitmap(imageBitmap);
            }
        }
    }
}