package com.entezaar.thelperdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.entezaar.thelperdemo.adapters.DestinationsAdapter;
import com.entezaar.thelperdemo.interfaces.DialogListener;
import com.entezaar.thelperdemo.interfaces.RecyclerViewListener;
import com.entezaar.thelperdemo.structures.Destination;
import com.entezaar.thelperdemo.ui.DestinationPreviewDialog;
import com.entezaar.thelperdemo.util.PrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.entezaar.thelperdemo.MainActivity.mCurrentLocation;
import static com.entezaar.thelperdemo.util.ServerUtil.DestinationsPageUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.TaxiRequestPageUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.retrieveImage;
import static com.entezaar.thelperdemo.util.ServerUtil.retrieveString;

//TODO THEN START FUNCTIONAL PART OF NIGHT MODE (ADD IMAGE URL OF NIGHT IMAGES TO DB AND STRUCT...)

public class TaxiRequestActivity extends AppCompatActivity implements DialogListener , RecyclerViewListener {

    private List<Destination> destinationsList = new ArrayList<>();
    private DestinationsAdapter dAdapter;
    private int current_page = 0 ;
    private int selected_destination ;
    private boolean is_endOfScrolling = false ;
    private boolean is_loaded = true ;
    private PrefManager pref ;
    private boolean night_mode = false ;
    private ImageButton nightModeButton;
    private List<LoadImageTask> Threads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_request);

        pref = new PrefManager(this);

        nightModeButton = findViewById(R.id.night_mode);
        /*nightModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMode();
            }
        });*/
        nightModeButton.setVisibility(View.INVISIBLE);
        ImageButton backButton = findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  TaxiRequestActivity.this.finish();  }
        });
        backButton.setImageResource(R.drawable.ic_back);

        RecyclerView recyclerView = findViewById(R.id.d_recyclerview);
        dAdapter = new DestinationsAdapter(destinationsList);
        dAdapter.setRecyclerViewListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (is_loaded && !is_endOfScrolling)
                    {
                        current_page += 1 ;
                        new GetDestinationsTask().execute(current_page);
                    }
                }
            }
        });

        new GetDestinationsTask().execute(current_page);

    }


    private void updateNightModeButton ()
    {
        if (night_mode) nightModeButton.setImageResource(R.drawable.ic_night);
        else nightModeButton.setImageResource(R.drawable.ic_day);
    }

    private void changeMode ()
    {
        night_mode = !night_mode ;
        updateNightModeButton();

        for(int i=0 ; i<destinationsList.size() ; i++)
            new LoadImageTask().execute(i);

    }
    @Override
    public void onDialogSubmitClicked() {
        SubmitTaxiRequestTask TaxiReq = new SubmitTaxiRequestTask();
        String Latitude , Longitude ;
        if (mCurrentLocation == null)
        {
            Latitude = "0";
            Longitude = "0";
        } else {
            Latitude = mCurrentLocation.getLatitude() + "" ;
            Longitude = mCurrentLocation.getLongitude() + "" ;
        }
        TaxiReq.execute( pref.getPhoneNumber() ,
                Latitude , Longitude ,
                destinationsList.get(selected_destination).getId());
    }

    @Override
    public void onItemClicked(Integer position) {
        DestinationPreviewDialog preview =
                new DestinationPreviewDialog(TaxiRequestActivity.this ,
                        destinationsList.get(position));
        try {
            WindowManager.LayoutParams lp = preview.getWindow().getAttributes();
            lp.dimAmount = 0.0f ;
            preview.getWindow().setAttributes(lp);
        } catch (Exception ignored) { }

        preview.setDialogListener(this);
        preview.show();
        selected_destination = position ;
    }

    public class GetDestinationsTask extends AsyncTask<Integer , Void , Destination[]> {

        @Override
        protected Destination[] doInBackground(Integer... integers) {
            Gson gson = new Gson();
            String json = retrieveString(DestinationsPageUrl + "?Page=" + integers[0] );
            Destination[] destinations = gson.fromJson(json , Destination[].class);
            is_endOfScrolling = destinations.length < 5 ;
            return destinations ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            is_loaded = false;
        }

        @Override
        protected void onPostExecute(Destination[] destinations) {
            super.onPostExecute(destinations);
            for (int i=0 ; i<destinations.length ; i++)
            {
                destinationsList.add(destinations[i]);
                Threads.add(new LoadImageTask());
                Threads.get(Threads.size()-1).execute(destinationsList.size()-1);
            }

            dAdapter.notifyDataSetChanged();
            is_loaded = true ;

        }
    }

    private class LoadImageTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            String Url = destinationsList.get(integers[0]).getImageUrl().toLowerCase();
            destinationsList.get(integers[0]).setImage(retrieveImage(Url));
            return integers[0];
        }
        @Override
        protected void onPostExecute(Integer index) {
            dAdapter.notifyItemChanged(index);
        }
    }

    private class SubmitTaxiRequestTask extends AsyncTask <String , Void , Boolean>
    {
        //phone_number , latitude , longitude , des_id
        @Override
        protected Boolean doInBackground(String... strings) {
            String Params = "phone_number=" + strings[0] + "&" +
                    "latitude=" + strings[1] + "&" +
                    "longitude=" + strings[2] + "&" +
                    "des_id=" + strings[3] ;

            String respone = retrieveString(TaxiRequestPageUrl + "?" + Params);
            if (respone == null) return false ;
            return  respone.contains("1");

        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(), getString(R.string.done), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            for(int i=0 ; i<Threads.size() ; i++)
                Threads.get(i).cancel(true);
        } catch (Exception ignored) {} ;

    }

}
