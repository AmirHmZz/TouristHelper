package com.entezaar.thelperdemo.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.entezaar.thelperdemo.ApplicationClass;
import com.entezaar.thelperdemo.R;

public class irsansTextView extends TextView {


    public irsansTextView(Context context) {
        super(context);
        if (!isInEditMode())
            setupTextView("");
    }

    public irsansTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attr_array = context.obtainStyledAttributes(attrs , R.styleable.TextView_irsans , 0 , 0);
        if (!isInEditMode())
            setupTextView(attr_array.getString(R.styleable.TextView_irsans_FontType));
        attr_array.recycle();
    }

    public irsansTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attr_array = context.obtainStyledAttributes(attrs , R.styleable.TextView_irsans , 0 , 0);
        if (!isInEditMode())
            setupTextView(attr_array.getString(R.styleable.TextView_irsans_FontType));
        attr_array.recycle();
    }

    public irsansTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray attr_array = context.obtainStyledAttributes(attrs , R.styleable.TextView_irsans , 0 , 0);
        if (!isInEditMode())
            setupTextView(attr_array.getString(R.styleable.TextView_irsans_FontType));
        attr_array.recycle();
    }

    private void setupTextView(String type) {
        ApplicationClass application = (ApplicationClass) getContext().getApplicationContext();

        switch (type) {
            case ("ghasem") :
                setTypeface(application.getGhasem());
                break;
            case ("none") :
                setTypeface(application.getIraniansens());
                break;
            case ("black") :
                setTypeface(application.getIraniansens_black());
                break;
            case ("bold") :
                setTypeface(application.getIraniansens_bold());
                break;
            case ("light") :
                setTypeface(application.getIraniansens_light());
                break;
            case ("medium") :
                setTypeface(application.getIraniansens_medium());
                break;
            case ("ultralight") :
                setTypeface(application.getIraniansens_ultralight());
                break;
        }

    }
}
