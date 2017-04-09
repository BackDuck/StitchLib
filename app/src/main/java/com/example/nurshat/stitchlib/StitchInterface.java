package com.example.nurshat.stitchlib;

import android.graphics.Bitmap;

import com.example.nurshat.stitchlib.Events.StitchProgress;

/**
 * Created by Nurshat on 24.03.2017.
 */

public interface StitchInterface {
    void stitchSuccessListener(Bitmap panoramImage);
    void stitchErrorListener(String error);
    void stitchProgressListener(StitchProgress error);
}
