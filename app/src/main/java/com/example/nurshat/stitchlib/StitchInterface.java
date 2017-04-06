package com.example.nurshat.stitchlib;

import android.graphics.Bitmap;

/**
 * Created by Nurshat on 24.03.2017.
 */

public interface StitchInterface {
    void stitchSuccessListener(Bitmap panoramImage);
    void stitchErrorListener(String error);
}
