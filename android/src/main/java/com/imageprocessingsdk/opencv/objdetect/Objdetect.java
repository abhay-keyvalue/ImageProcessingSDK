//
// This file is auto-generated. Please don't modify it!
//
package com.imageprocessingsdk.opencv.objdetect;

import com.imageprocessingsdk.opencv.core.Mat;
import com.imageprocessingsdk.opencv.core.MatOfInt;
import com.imageprocessingsdk.opencv.core.MatOfRect;

// C++: class Objdetect
//javadoc: Objdetect

public class Objdetect {

    public static final int
            CASCADE_DO_CANNY_PRUNING = 1,
            CASCADE_SCALE_IMAGE = 2,
            CASCADE_FIND_BIGGEST_OBJECT = 4,
            CASCADE_DO_ROUGH_SEARCH = 8;


    //
    // C++:  void cv::groupRectangles(vector_Rect& rectList, vector_int& weights, int groupThreshold, double eps = 0.2)
    //

    //javadoc: groupRectangles(rectList, weights, groupThreshold, eps)
    public static void groupRectangles(MatOfRect rectList, MatOfInt weights, int groupThreshold, double eps)
    {
        Mat rectList_mat = rectList;
        Mat weights_mat = weights;
        groupRectangles_0(rectList_mat.nativeObj, weights_mat.nativeObj, groupThreshold, eps);
        
        return;
    }

    //javadoc: groupRectangles(rectList, weights, groupThreshold)
    public static void groupRectangles(MatOfRect rectList, MatOfInt weights, int groupThreshold)
    {
        Mat rectList_mat = rectList;
        Mat weights_mat = weights;
        groupRectangles_1(rectList_mat.nativeObj, weights_mat.nativeObj, groupThreshold);
        
        return;
    }




    // C++:  void cv::groupRectangles(vector_Rect& rectList, vector_int& weights, int groupThreshold, double eps = 0.2)
    private static native void groupRectangles_0(long rectList_mat_nativeObj, long weights_mat_nativeObj, int groupThreshold, double eps);
    private static native void groupRectangles_1(long rectList_mat_nativeObj, long weights_mat_nativeObj, int groupThreshold);

}