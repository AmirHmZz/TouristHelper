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
import com.entezaar.thelperdemo.structures.Destination;

import java.util.List;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.MyViewHolder> {

    private List<Destination> destinationsList;
    private RecyclerViewListener recyclerViewListener ;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView name, desc ;
        public ImageView image ;
        public CardView card ;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.d_name);
            desc = view.findViewById(R.id.d_desc);
            image = view.findViewById(R.id.d_image);
            card = view.findViewById(R.id.d_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewListener.onItemClicked(this.getLayoutPosition());
        }
    }


    public DestinationsAdapter(List<Destination> destinationsList) {
        this.destinationsList = destinationsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destination, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Destination destination = destinationsList.get(position);
        holder.name.setText(destination.getName());
        holder.desc.setText(destination.getDescription());
        if (destination.isImageLoaded())
            holder.image.setImageBitmap(destination.getImage());
        else
            holder.image.setBackgroundColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return destinationsList.size();
    }

    public void setRecyclerViewListener (RecyclerViewListener recyclerViewListener)
    {
        this.recyclerViewListener = recyclerViewListener ;
    }
}