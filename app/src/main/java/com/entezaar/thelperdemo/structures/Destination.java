package com.entezaar.thelperdemo.structures;

import android.graphics.Bitmap;


public class Destination {
    private String id ;
    private String name ;
    private String description ;
    private String imageUrl ;
    private Bitmap image ;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isImageLoaded ()
    {
        return image != null ;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }
}
