package com.example.nurshat.stitchlib.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.nurshat.stitchlib.ImagePickerActivity;
import com.example.nurshat.stitchlib.Model.ShareData;
import com.example.nurshat.stitchlib.Model.StitchConfig;

import java.io.File;

import id.zelory.compressor.Compressor;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Nurshat on 25.03.2017.
 */

public class StitchController {
    private static StitchController instance;
    private Context context;
    private StitchConfig config;

    public static synchronized StitchController getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public StitchController(Context context, StitchConfig config) {
        instance = this;
        this.context = context;
        this.config = config;
    }

    public void start() {
        context.startActivity(new Intent(context, ImagePickerActivity.class));
        // System.out.println("---->>>> " + ShareData.images.size());
    }

    public void complete(Bitmap result) {
        config.getListener().stitchSuccessListener(result);
    }

    public void stitch() {
        Stitcher stitcher = new Stitcher(context, this);
        stitcher.init();
    }

    public void compress() {
        final StitchCompressor compressor = new StitchCompressor(context);
        Observable.from(ShareData.images)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        compressor.compress(path);
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        stitch();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("Следующий, не задерживаемся");
                    }
                });

    }


}
