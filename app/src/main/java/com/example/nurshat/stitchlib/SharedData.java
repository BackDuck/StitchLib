package com.example.nurshat.stitchlib;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class SharedData {
	/**
	 * The list of images to be stitched. 
	 * This class allows it to be shared between activities.
	 */
	public static List<Mat> panoImgs = new ArrayList<Mat>();
}