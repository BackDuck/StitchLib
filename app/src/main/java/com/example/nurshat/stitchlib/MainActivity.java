package com.example.nurshat.stitchlib;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nurshat.stitchlib.Controller.StitchController;
import com.example.nurshat.stitchlib.Events.StitchProgress;
import com.example.nurshat.stitchlib.Model.ShareData;
import com.example.nurshat.stitchlib.Model.StitchConfig;

import org.opencv.android.BaseLoaderCallback;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements StitchInterface {

    TextView tv;
    ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        tv = (TextView) findViewById(R.id.progress);
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        List<String> images = new ArrayList<>();

        StitchConfig config = new StitchConfig();
        config.useDefaultImagePicker(true)
                .useMultibandBlender(true)
                .useWaveCorrect(false)
                .setImages(images)
                .setListener(this)
                .setLimitPickPhoto(25)
                .setQuality(100)
                .setMaxImgHeight(720)
                .setMaxImgWidth(1280);

        StitchController sController = new StitchController(MainActivity.this, config);
        sController.start();
    }

    @Override
    public void stitchSuccessListener(Bitmap panoramImage) {
        ImageView imageView = (ImageView) findViewById(R.id.result);
        imageView.setImageBitmap(panoramImage);
        if (panoramImage == null) {
            System.out.println("все не ок");
        }
        System.out.println("Все окей!");

    }

    @Override
    public void stitchErrorListener(String error) {
        tv.setText(error);

    }

    @Override
    public void stitchProgressListener(StitchProgress progress) {
        tv.setText(progress.toString());
        pb.setProgress(progress.getProgress());
    }


}