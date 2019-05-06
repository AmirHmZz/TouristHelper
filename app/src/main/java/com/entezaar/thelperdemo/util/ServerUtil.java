package com.entezaar.thelperdemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerUtil {
    public static final String MainHostURL = "http://entezardev.ir/";
    public static final String DestinationsPageUrl = MainHostURL + "getDestinations.php";
    public static final String HotelsPageUrl = MainHostURL + "getHotels.php";
    public static final String TaxiRequestPageUrl = MainHostURL + "SubmitTaxiRequest.php";
    public static final String HotelRequestPageUrl = MainHostURL + "SubmitHotelRequest.php";
    public static final String BillRequestPageUrl = MainHostURL + "SubmitBillRequest.php";
    public static final String LoginPageUrl = MainHostURL + "LoginPage.php";
    public static final String FacebookShopUrl = "https://www.facebook.com/pg/iranmarhaba/shop/?ref=page_internal";
    public static final int    ConnectionTimeout = 4000;

    public static String retrieveString(String Url) {

        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Url).openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(ConnectionTimeout);
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
        } catch (Exception e) {
            return null; }
        return stringBuilder.toString();
    }

    public static Bitmap retrieveImage (String Url)
    {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(Url)).openConnection();
            connection.setConnectTimeout(ConnectionTimeout);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            connection.disconnect();
            return myBitmap;
        }catch (Exception e){ return null; }
    }



}
