package com.entezaar.thelperdemo.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.entezaar.thelperdemo.R;
import com.entezaar.thelperdemo.interfaces.RecyclerViewListener;
import com.entezaar.thelperdemo.structures.Hotel;

import java.util.List;

public class HotelsAdapter extends RecyclerView.Adapter <HotelsAdapter.MyViewHolder> {

    private List<Hotel> hotelsList;
    private RecyclerViewListener recyclerViewListener ;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView name, desc ;
        public ImageView image ;
        public CardView card ;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.h_name);
            desc = view.findViewById(R.id.h_desc);
            image = view.findViewById(R.id.h_image);
            card = view.findViewById(R.id.h_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewListener.onItemClicked(this.getLayoutPosition());
        }
    }


    public HotelsAdapter(List<Hotel> hotelsList) {
        this.hotelsList = hotelsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Hotel hotel = hotelsList.get(position);
        holder.name.setText(hotel.getName());
        holder.desc.setText(hotel.getDescription());
        if (hotel.isImageLoaded())
            holder.image.setImageBitmap(hotel.getImage());
        else
            holder.image.setBackgroundColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return hotelsList.size();
    }

    public void setRecyclerViewListener (RecyclerViewListener recyclerViewListener)
    {
        this.recyclerViewListener = recyclerViewListener ;
    }
}