package com.example.nurshat.stitchlib.Model;

import com.example.nurshat.stitchlib.StitchInterface;

import java.util.List;

/**
 * Created by Nurshat on 25.03.2017.
 */

public class StitchConfig {
    private boolean waveCorrect;
    private boolean multibandBlender;
    private boolean pickImages;
    private StitchInterface listener;
    private List<String> images;
    private int maxImgWidth;
    private int quality;
    private int limitPickPhoto;
    private int maxImgHeight;

    public StitchConfig() {
        waveCorrect = true;
        multibandBlender = true;
        pickImages = false;
        maxImgHeight = 480;
        maxImgWidth = 640;
        limitPickPhoto = 10;
        quality = 75;
    }

    public int getQuality() {
        return quality;
    }

    public StitchConfig setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public int getLimitPickPhoto() {
        return limitPickPhoto;
    }

    public StitchConfig setLimitPickPhoto(int limitPickPhoto) {
        this.limitPickPhoto = limitPickPhoto;
        return this;
    }

    public boolean isWaveCorrect() {
        return waveCorrect;
    }

    public StitchConfig useWaveCorrect(boolean waveCorrect) {
        this.waveCorrect = waveCorrect;
        return this;
    }

    public boolean isMultibandBlender() {
        return multibandBlender;
    }

    public StitchConfig useMultibandBlender(boolean multibandBlender) {
        this.multibandBlender = multibandBlender;
        return this;
    }

    public boolean isPickImages() {
        return pickImages;
    }

    public StitchConfig useDefaultImagePicker(boolean pickImages) {
        this.pickImages = pickImages;
        return this;
    }

    public StitchInterface getListener() {
        return listener;
    }

    public StitchConfig setListener(StitchInterface listener) {
        this.listener = listener;
        return this;
    }

    public List<String> getImages() {
        return images;
    }

    public StitchConfig setImages(List<String> images) {
        this.images.addAll(images);
        return this;
    }

    public StitchConfig setImage(String image) {
        this.images.add(image);
        return this;
    }

    public StitchConfig setMaxImgHeight(int maxImgHeight) {
        this.maxImgHeight = maxImgHeight;
        return this;
    }

    public StitchConfig setMaxImgWidth(int maxImgWidth) {
        this.maxImgWidth = maxImgWidth;
        return this;
    }

    public int getMaxImgWidth() {
        return maxImgWidth;
    }

    public int getMaxImgHeight() {
        return maxImgHeight;
    }

}