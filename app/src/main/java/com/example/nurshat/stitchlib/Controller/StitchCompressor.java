package com.example.nurshat.stitchlib.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.nurshat.stitchlib.Model.ShareData;

import java.io.File;

import id.zelory.compressor.Compressor;

/**
 * Created by Nurshat on 25.03.2017.
 */

public class StitchCompressor {
    private Context context;

    public static final String PATH_TO_COMPRESS_IMAGES = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempx";

    public StitchCompressor(Context context) {
        this.context = context;
    }

    public void compress(String path){
        File compressedImage = new Compressor.Builder(context)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(PATH_TO_COMPRESS_IMAGES)
                .build()
                .compressToFile(new File(path));

        ShareData.CompressImages.add(PATH_TO_COMPRESS_IMAGES+"/"+compressedImage.getName());

            System.out.println("file name " + compressedImage.getName());

    }
}