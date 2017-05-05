package com.example.nurshat.stitchlib;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.nurshat.stitchlib.Model.ShareData;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class PanoramViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_panoram_view);


        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageURI(Uri.fromFile(new File(ShareData.resultPath)));
        photoView.setMaximumScale(5f);
        photoView.setScale(3f, true);
    }

    public void onClick(View v) {
        this.finish();
    }
}
