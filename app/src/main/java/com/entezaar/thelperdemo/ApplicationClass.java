package com.entezaar.thelperdemo;

import android.app.Application;
import android.graphics.Typeface;

public class ApplicationClass extends Application {

    public static Typeface ghasem ,
            iraniansens ,
            iraniansens_black ,
            iraniansens_bold ,
            iraniansens_medium ,
            iraniansens_light ,
            iraniansens_ultralight ;


    @Override
    public void onCreate() {
        super.onCreate();
        ghasem = Typeface.createFromAsset(getAssets(), "fonts/GHASEM.TTF");
        iraniansens = Typeface.createFromAsset(getAssets(), "fonts/IRANSANS.TTF");
        iraniansens_black = Typeface.createFromAsset(getAssets(), "fonts/IRANSANS_BLACK.TTF");
        iraniansens_bold = Typeface.createFromAsset(getAssets(), "fonts/IRANSANS_BOLD.TTF");
        iraniansens_medium = Typeface.createFromAsset(getAssets(), "fonts/IRANSANS_MEDIUM.TTF");
        iraniansens_light = Typeface.createFromAsset(getAssets(), "fonts/IRANSANS_LIGHT.TTF");
        iraniansens_ultralight = Typeface.createFromAsset(getAssets(), "fonts/IRANSANS_ULTRALIGHT.TTF");

    }

    public Typeface getGhasem () {
        return ghasem ;
    }
    public Typeface getIraniansens () {
        return iraniansens ;
    }
    public Typeface getIraniansens_black () {
        return iraniansens_black ;
    }
    public Typeface getIraniansens_bold (){
        return iraniansens_bold ;
    }
    public Typeface getIraniansens_medium () {
        return iraniansens_medium ;
    }
    public Typeface getIraniansens_light () {
        return iraniansens_light ;
    }
    public Typeface getIraniansens_ultralight () {
        return iraniansens_ultralight ;
    }

}