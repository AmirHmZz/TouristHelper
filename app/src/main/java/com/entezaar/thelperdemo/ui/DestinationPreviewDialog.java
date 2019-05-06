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
import com.entezaar.thelperdemo.structures.Destination;

public class DestinationPreviewDialog extends Dialog {

    public TextView name , desc ;
    public ImageView image ;
    public Button submit ;
    public Destination destination;
    public DialogListener dialogListener;

    public DestinationPreviewDialog(Activity activity , Destination destination) {
        super(activity);
        this.destination = destination;
    }

    public void setDialogListener (DialogListener dialogListener)
    {
        this.dialogListener = dialogListener ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.d_preview);
        name = findViewById(R.id.d_name);
        desc = findViewById(R.id.d_desc);
        image = findViewById(R.id.d_image);
        submit = findViewById(R.id.submit);

        name.setText(destination.getName());
        desc.setText(destination.getDescription());
        image.setImageBitmap(destination.getImage());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogListener.onDialogSubmitClicked();
            }
        });


    }

}