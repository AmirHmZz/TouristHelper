package com.entezaar.thelperdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.entezaar.thelperdemo.adapters.HotelsAdapter;
import com.entezaar.thelperdemo.interfaces.DialogListener;
import com.entezaar.thelperdemo.interfaces.RecyclerViewListener;
import com.entezaar.thelperdemo.structures.Hotel;
import com.entezaar.thelperdemo.ui.HotelPreviewDialog;
import com.entezaar.thelperdemo.util.PrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.entezaar.thelperdemo.MainActivity.mCurrentLocation;
import static com.entezaar.thelperdemo.util.ServerUtil.FacebookShopUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.HotelRequestPageUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.HotelsPageUrl;
import static com.entezaar.thelperdemo.util.ServerUtil.retrieveImage;
import static com.entezaar.thelperdemo.util.ServerUtil.retrieveString;

public class HotelRequestActivity extends AppCompatActivity implements DialogListener , RecyclerViewListener {

    private List<Hotel> hotelsList = new ArrayList<>();
    private HotelsAdapter hAdapter;
    private int current_page = 0 ;
    private int selected_hotel ;
    private boolean is_endOfScrolling = false ;
    private boolean is_loaded = true ;
    private PrefManager pref ;
    private List<LoadImageTask> Threads = new ArrayList<>();
    private HotelPreviewDialog preview ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_request);

        pref = new PrefManager(this);
        ImageButton nightModeButton = findViewById(R.id.night_mode);
        nightModeButton.setVisibility(View.INVISIBLE);
        ImageButton backButton = findViewById(R.id.menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  HotelRequestActivity.this.finish();  }
        });
        backButton.setImageResource(R.drawable.ic_back);

        RecyclerView recyclerView = findViewById(R.id.d_recyclerview);
        hAdapter = new HotelsAdapter(hotelsList);
        hAdapter.setRecyclerViewListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (is_loaded && !is_endOfScrolling)
                    {
                        current_page += 1 ;
                        new GetHotelsTask().execute(current_page);
                    }
                }
            }
        });
        new GetHotelsTask().execute(current_page);

        Button Shop = findViewById(R.id.shop);
        Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FacebookShopUrl));
                startActivity(browserIntent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            for(int i=0 ; i<Threads.size() ; i++)
                Threads.get(i).cancel(true);
        } catch (Exception ignored) {} ;

    }


    @Override
    public void onDialogSubmitClicked() {
        String Latitude , Longitude ;
        if (mCurrentLocation == null)
        {
            Latitude = "0";
            Longitude = "0";
        } else {
            Latitude = mCurrentLocation.getLatitude() + "" ;
            Longitude = mCurrentLocation.getLongitude() + "" ;
        }
        new SubmitHotelRequestTask().execute( pref.getPhoneNumber() ,
                Latitude , Longitude , hotelsList.get(selected_hotel).getId());
    }

    @Override
    public void onItemClicked(Integer position) {
        preview =
                new HotelPreviewDialog(HotelRequestActivity.this ,
                        hotelsList.get(position));
        try {
            WindowManager.LayoutParams lp = preview.getWindow().getAttributes();
            lp.dimAmount = 0.0f ;
            preview.getWindow().setAttributes(lp);
        } catch (Exception ignored) { }

        preview.setDialogListener(this);
        preview.show();
        selected_hotel = position ;
    }

    public class GetHotelsTask extends AsyncTask<Integer , Void , Hotel[]> {

        @Override
        protected Hotel[] doInBackground(Integer... integers) {
            Gson gson = new Gson();
            String json = retrieveString(HotelsPageUrl + "?Page=" + integers[0] );
            Hotel[] hotels = gson.fromJson(json , Hotel[].class);
            is_endOfScrolling = hotels.length < 5 ;
            return hotels ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            is_loaded = false;
        }

        @Override
        protected void onPostExecute(Hotel[] hotels) {
            super.onPostExecute(hotels);
            for (int i=0 ; i<hotels.length ; i++)
            {
                hotelsList.add(hotels[i]);
                Threads.add(new LoadImageTask());
                Threads.get(Threads.size()-1).execute(hotelsList.size()-1);
            }

            hAdapter.notifyDataSetChanged();
            is_loaded = true ;

        }
    }

    private class LoadImageTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            String Url = hotelsList.get(integers[0]).getImageUrl().toLowerCase();
            hotelsList.get(integers[0]).setImage(retrieveImage(Url));
            return integers[0];
        }
        @Override
        protected void onPostExecute(Integer index) {
            hAdapter.notifyItemChanged(index);
        }
    }

    private class SubmitHotelRequestTask extends AsyncTask <String , Void , Boolean>
    {
        //phone_number , latitude , longitude , hotel_id
        @Override
        protected Boolean doInBackground(String... strings) {
            String Params = "phone_number=" + strings[0] + "&" +
                    "latitude=" + strings[1] + "&" +
                    "longitude=" + strings[2] + "&" +
                    "hotel_id=" + strings[3] ;
            String respone = retrieveString(HotelRequestPageUrl + "?" + Params);
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
            super.onPostExecute(result);
            if (result)
                Toast.makeText(getApplicationContext(), getString(R.string.done), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            preview.dismiss();
        }
    }


}
