package com.entezaar.thelperdemo.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public final int DestinationType = 1 , HotelsType = 2;
    public PrefManager(Context context) {
        pref = context.getSharedPreferences("THelper", 0);
        editor = pref.edit();
    }

    public void submitLogin (String phoneNumber)
    {
        editor.putString("phonenumber" , phoneNumber);
        editor.commit();
    }

    public String getPhoneNumber ()
    {
        return pref.getString("phonenumber" , null) ;
    }

    public boolean checkLogin ()
    {
        return getPhoneNumber() != null ;
    }

}