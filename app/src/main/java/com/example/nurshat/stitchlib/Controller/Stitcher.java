package com.example.nurshat.stitchlib.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.nurshat.stitchlib.Events.StitchProgress;
import com.example.nurshat.stitchlib.Model.Errors;
import com.example.nurshat.stitchlib.Model.ProgressTitles;
import com.example.nurshat.stitchlib.Model.ShareData;
import com.example.nurshat.stitchlib.NativeStitcherWrapper;
import com.example.nurshat.stitchlib.SharedData;

import org.greenrobot.eventbus.EventBus;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nurshat on 26.03.2017.
 */

public class Stitcher {
    private static final String TAG = "Stitcher::";
    private boolean useWaveCorrect;
    private boolean useMultibandBlender;
    private Context context;
    private FileController fController;
    private Bitmap bmpImage;

    private boolean onCVStart = false;

    Stitcher(Context context) {
        this.context = context;
        fController = new FileController();
        useMultibandBlender = ShareData.config.isMultibandBlender();
        useWaveCorrect = ShareData.config.isWaveCorrect();
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    System.loadLibrary("native-lib");
                    start();
                    onCVStart = true;
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public void init() {
        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 5));
        if (!onCVStart) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, context, mLoaderCallback);
        }
    }


    public void start() {

        System.out.println("Compressed images count" + ShareData.CompressImages.size());

        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 10));
        for (String path : ShareData.CompressImages) {
            Bitmap btm = BitmapFactory.decodeFile(path);
            Mat mat = new Mat(btm.getWidth(), btm.getHeight(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
            Mat RgbImg = new Mat();
            btm = btm.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(btm, mat);
            Imgproc.cvtColor(mat, RgbImg, Imgproc.COLOR_BGRA2BGR);
            SharedData.panoImgs.add(RgbImg.clone());
        }

        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 25));

        stitch();

    }

    public void complete(Mat result) {
        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 80));
        fController.removeTempImg();
        if (result.empty()) {
            EventBus.getDefault().post(Errors.STITCH_ERROR);
            System.out.println("Ошибка тут");
            return;
        }

        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 95));
        Mat display = new Mat();
        Imgproc.cvtColor(result, display, Imgproc.COLOR_BGR2BGRA);

        bmpImage = Bitmap.createBitmap(display.cols(), display.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(display, bmpImage);

        if (bmpImage == null) {
            System.out.println("нет результата");
        } else {
            System.out.println("результат есть");
        }
        fController.saveImage(bmpImage);
        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 100));
        EventBus.getDefault().post(bmpImage);
    }


    public void stitch() {
        System.loadLibrary("native-lib");

        final Stitcher st = this;
        EventBus.getDefault().post(new StitchProgress(ProgressTitles.INITIALIZATION, 50));
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                NativeStitcherWrapper.stitch(
                        SharedData.panoImgs,
                        useWaveCorrect,
                        useMultibandBlender, st
                );
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
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
