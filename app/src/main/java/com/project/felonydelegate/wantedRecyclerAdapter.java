package com.project.felonydelegate;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class wantedRecyclerAdapter extends RecyclerView.Adapter<wantedRecyclerAdapter.wantedViewHolder> {

    private ArrayList data,imgdata;

    public wantedRecyclerAdapter(ArrayList data,ArrayList imgdata) {
        this.data = data;
        this.imgdata = imgdata;
    }

    @NonNull
    @Override
    public wantedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wanted_list_layout,parent,false);
        return new wantedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wantedViewHolder holder, int position) {
        String title = data.get(position).toString();
        holder.txt.setText(title);
        Picasso.get().load(imgdata.get(position).toString()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class wantedViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt;
        public wantedViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.wantedImage);
            txt = itemView.findViewById(R.id.wantedDetails);
        }
    }
}
