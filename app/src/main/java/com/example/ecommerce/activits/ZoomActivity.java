package com.example.ecommerce.activits;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;

public class ZoomActivity extends AppCompatActivity {

    private ImageView imageView;
    private ZoomControls zoomControls;
    private float currentScale = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        imageView = findViewById(R.id.Imgview);

        String imageUrl = getIntent().getStringExtra("imageUrl");
// Use the imageUrl as needed in your new activity
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        zoomControls = findViewById(R.id.zoom);

        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
            }
        });
    }

    private void zoomIn() {
        if (currentScale < 2.0f) { // Maximum zoom level
            currentScale += 0.1f;
            applyScale();
            showToast("Zoom In");
        } else {
            showToast("Maximum 100% zoom in");
        }
    }

    private void zoomOut() {
        if (currentScale > 0.1f) { // Minimum zoom level
            currentScale -= 0.1f;
            applyScale();
            showToast("Zoom Out");
        } else {
            showToast("Minimum 10% zoom out");
        }
    }

    private void applyScale() {
        imageView.setScaleX(currentScale);
        imageView.setScaleY(currentScale);
    }

    private void showToast(String message) {
        Toast.makeText(ZoomActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}