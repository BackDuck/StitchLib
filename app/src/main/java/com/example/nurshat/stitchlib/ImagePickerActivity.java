package com.example.nurshat.stitchlib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nurshat.stitchlib.Controller.StitchController;
import com.example.nurshat.stitchlib.Model.ShareData;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ImagePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(ShareData.config.getLimitPickPhoto())
                .singlePhoto(false)
                .hintOfPick("this is pick hint")
                .filterMimeTypes(new String[]{"image/*"})
                .build();
        GalleryActivity.openActivity(ImagePickerActivity.this, 1, config);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            ShareData.images = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
            StitchController.getInstance().compress();
            this.finish();
        }
    }

}
