package com.example.nurshat.stitchlib.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

import com.example.nurshat.stitchlib.Model.ShareData;
import com.example.nurshat.stitchlib.NativeStitcherWrapper;
import com.example.nurshat.stitchlib.SharedData;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Nurshat on 26.03.2017.
 */

public class Stitcher {
    Bitmap bmpp1, bmpp2;
    //        ImagesForStitch imagesForStitch;
    private static final String TAG = "Stitcher::MainActivity";
    //        private MenuItem mItemWriteFile;
//        private MenuItem mItemSetWaveCorrect;
    private boolean useWaveCorrect;
    //        private MenuItem mItemSetBlender;
    private boolean useMultibandBlender;
    private Context context;
    private Bitmap[][] images = {{null, null}, {null, null}};
    private StitchController sCtrl;
    static Mat result;
    Bitmap bmpImage;

    public Stitcher(Context context, StitchController sCtrl) {
        this.context = context;
        this.sCtrl = sCtrl;
    }

    boolean onCVStart = false;

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
        if (!onCVStart) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, context, mLoaderCallback);
        }
    }


    public void start() {

//
//                bmpp1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagesForStitch.getImage1());
//                bmpp2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagesForStitch.getImage2());

        System.out.println("Compressed images count" + ShareData.CompressImages.size());


        for (String path : ShareData.CompressImages) {
            Bitmap btm = BitmapFactory.decodeFile(path);
            Mat mat = new Mat(btm.getWidth(), btm.getHeight(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
            Mat RgbImg = new Mat();
            btm = btm.copy(Bitmap.Config.ARGB_8888, true);
            Utils.bitmapToMat(btm, mat);
            Imgproc.cvtColor(mat, RgbImg, Imgproc.COLOR_BGRA2BGR);
            SharedData.panoImgs.add(RgbImg.clone());
        }

        stitch();

    }


    private void setCurrentImage(Bitmap bmp) {
        int row = useWaveCorrect ? 1 : 0;
        int col = useMultibandBlender ? 1 : 0;
        images[row][col] = bmp;
    }

    private Bitmap getCurrentImage() {
        int row = useWaveCorrect ? 1 : 0;
        int col = useMultibandBlender ? 1 : 0;
        return images[row][col];
    }

//        private void toast(String msg) {
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//        }

    /**
     * Grab the current images from {@link SharedData#panoImgs SharedData.panoImgs},
     * stitch them together, and redisplay
     */
    private void stitchAndDisplay() {

        //  ImageView imview = (ImageView) findViewById(R.id.imageView1);

		/* if result exists in cache, don't bother restitching, just redisplay */
        bmpImage = getCurrentImage();

        if (bmpImage != null) {
            //  imview.setImageBitmap(bmpImage);
            return;
        }

        System.loadLibrary("native-lib");

       final Stitcher st = this;

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
//                        System.out.println("ОШИБКА");
                    }

                    @Override
                    public void onNext(Object o) {
//                        complete();
                    }
                });


//            ShareData.result = bmpImage;

        //BitmapData.resultBmp = bmpIm
        // age;
        //  EventBus.getDefault().post(new ProgressStatus(100));
        // imview.setImageBitmap(bmpImage);
    }


    public void complete(Mat res) {
        if (res.empty()) {
            //toast("Failed to stitch!");
            // EventBus.getDefault().post(new ProgressStatus(0));
            // startActivity(new Intent(this, MainActivity.class));
            System.out.println("Ошибка тут");
            return;
        }

        //result = SharedData.panoImgs.get(0);
        Mat display = new Mat();
        Imgproc.cvtColor(res, display, Imgproc.COLOR_BGR2BGRA);

        bmpImage = Bitmap.createBitmap(display.cols(), display.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(display, bmpImage);

        if (bmpImage == null) {
            System.out.println("нет результата");
        } else {
            System.out.println("результат есть");
        }
        sCtrl.complete(bmpImage);


        File file = new File(StitchCompressor.PATH_TO_COMPRESS_IMAGES, "imageStitched.jpg");
        file.getParentFile().mkdirs();

        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCurrentImage(bmpImage);
    }

    /*
    EventBus.getDefault().register(this);
    @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEvent(ProgressStatus status){
            System.out.println("trololololololololololololo......." + status.getProgressStatus());
            progressBar.setProgress(status.getProgressStatus());
        }
     */
    public void stitch() {
        // setContentView(R.layout.activity_processing);


		/* set defaults */
        useWaveCorrect = true;
        useMultibandBlender = true;
        stitchAndDisplay();
    }


}
