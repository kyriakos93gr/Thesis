package com.example.rebelor.carre_mobile;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;


public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;
    AxisBase axis;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
//            axis.mEntries = new float[mValues.length];
//            for (int i = 0; i < mValues.length; i++) {
//                axis.mEntries[i] = (float) i;
//            }

         return mValues[(int) value];
    }

}