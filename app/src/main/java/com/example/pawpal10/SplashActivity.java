package com.example.pawpal10;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar for full screen experience
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);

        // Set the video URI and start playing
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo_videp);
        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(mp -> {
            // Start your main activity once the video finishes
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();  // Finish the splash activity
        });
        videoView.start();
    }
}
