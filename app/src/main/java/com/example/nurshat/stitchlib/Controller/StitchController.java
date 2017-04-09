package com.example.nurshat.stitchlib.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.nurshat.stitchlib.Events.StitchProgress;
import com.example.nurshat.stitchlib.ImagePickerActivity;
import com.example.nurshat.stitchlib.Model.Errors;
import com.example.nurshat.stitchlib.Model.ShareData;
import com.example.nurshat.stitchlib.Model.StitchConfig;
import com.example.nurshat.stitchlib.StitchInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Nurshat on 25.03.2017.
 */

public class StitchController {
    private static StitchController instance;
    private Context context;
    private StitchInterface listener;

    public static synchronized StitchController getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public StitchController(Context context, StitchConfig config) {
        instance = this;
        this.context = context;
        ShareData.config = config;
        this.listener = ShareData.config.getListener();
        EventBus.getDefault().register(this);
        ShareData.images = null;
    }

    public void start() {
        if (ShareData.config.isPickImages()) {
            context.startActivity(new Intent(context, ImagePickerActivity.class));
        }else{
            if(ShareData.config.getImages().size()>1) {
                ShareData.images = ShareData.config.getImages();
                compress();
            }else {
                onErrorEvent(Errors.IMAGES_NOT_FOUND);
            }
        }
    }

    public void stitch() {
        Stitcher stitcher = new Stitcher(context);
        stitcher.init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(String error) {
        EventBus.getDefault().unregister(this);
        listener.stitchErrorListener(error);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressEvent(StitchProgress progress) {
        listener.stitchProgressListener(progress);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccsesEvent(Bitmap result) {
        EventBus.getDefault().unregister(this);
        listener.stitchSuccessListener(result);
    }

    public void compress() {
        final StitchCompressor compressor = new StitchCompressor(context);
        ShareData.CompressImages.clear();
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
                    }
                });

    }


}
