package com.example.nurshat.stitchlib.Events;

/**
 * Created by Nurshat on 09.04.2017.
 */

public class StitchProgress {
    private String title;
    private int progress;

    public StitchProgress(String title, int progress) {
        this.title = title;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public String toString(){
        return progress + "% - "+title;
    }
}
