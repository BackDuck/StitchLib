package com.example.nurshat.stitchlib;

import android.content.Context;
import android.hardware.Camera.Size;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

import java.util.List;

/**
 * Lifted these methods from OpenCV for Android Tutorial 3: Camera Control
 * @author OpenCV
 */
@SuppressWarnings("deprecation")
public class CameraView extends JavaCameraView {
	
	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }

}