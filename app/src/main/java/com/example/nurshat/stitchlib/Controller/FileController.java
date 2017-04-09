package com.example.nurshat.stitchlib.Controller;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.nurshat.stitchlib.Model.ShareData;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Nurshat on 09.04.2017.
 */

public class FileController {
    public final String PATH_TO_SAVE_RESULT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Result";

    public void removeTempImg() {
        for (String path : ShareData.CompressImages) {
            File file = new File(path);
            file.delete();
        }
    }


    public void saveImage(Bitmap img) {
        File file = new File(ShareData.config.getResultSavePath() == null ? PATH_TO_SAVE_RESULT : ShareData.config.getResultSavePath(), ShareData.config.getResultImageName() + ".jpg");
        file.getParentFile().mkdirs();

        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
