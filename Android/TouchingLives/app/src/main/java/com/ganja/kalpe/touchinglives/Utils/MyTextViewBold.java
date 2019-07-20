package com.ganja.kalpe.touchinglives.Utils;

/**
 * Created by Owner on 04-Jan-18.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;

import android.util.AttributeSet;
import android.view.View;


public class MyTextViewBold extends AppCompatTextView {

    public MyTextViewBold(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);
        init();
    }

    public MyTextViewBold(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    public MyTextViewBold(Context context){
        super(context);
        init();
    }

    public void init()
    {
        Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"fonts/Raleway-Bold.ttf");
        setTypeface(tf);
    }
}