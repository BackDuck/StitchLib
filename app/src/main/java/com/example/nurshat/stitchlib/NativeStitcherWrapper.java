package com.example.nurshat.stitchlib;

import android.util.Log;

import com.example.nurshat.stitchlib.Controller.Stitcher;

import org.opencv.core.Mat;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class NativeStitcherWrapper {
    private static final String TAG = "Stitcher::NativeStitch";

    /**
     * @param inputs      : the images to stitch together
     * @param waveCorrect : whether to use the built-in wave correction
     * @param multiBand   : whether to use multi-band or feather blending
     * @param st
     * @return the stitched image
     */
    public static void stitch(List<Mat> inputs, boolean waveCorrect, boolean multiBand, Stitcher st) {
        System.loadLibrary("native-lib");

        Mat imgArr[] = new Mat[inputs.size()];
        imgArr = inputs.toArray(imgArr);

        Log.i(TAG, "about to begin stitching");

        Mat result = new Mat();
        NativeStitch(imgArr, result.getNativeObjAddr(), waveCorrect, multiBand);
        Log.i(TAG, "done with stitching");
        st.complete(result);
    }

    private static native void NativeStitch(Mat imgs[], long resultMatAddr, boolean waveCorrect, boolean multiBand);
}