package com.entezaar.thelperdemo.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.entezaar.thelperdemo.R;
import com.entezaar.thelperdemo.interfaces.DialogListener;
import com.entezaar.thelperdemo.structures.Hotel;

public class HotelPreviewDialog extends Dialog {

    public TextView name , desc ;
    public ImageView image ;
    public Button submit ;
    public Hotel hotel;
    public DialogListener dialogListener;

    public HotelPreviewDialog(Activity activity , Hotel hotel) {
        super(activity);
        this.hotel = hotel;
    }

    public void setDialogListener (DialogListener dialogListener)
    {
        this.dialogListener = dialogListener ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.h_preview);
        name = findViewById(R.id.h_name);
        desc = findViewById(R.id.h_desc);
        image = findViewById(R.id.h_image);
        submit = findViewById(R.id.submit);

        name.setText(hotel.getName());
        desc.setText(hotel.getDescription());
        image.setImageBitmap(hotel.getImage());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.onDialogSubmitClicked();
            }
        });


    }

}