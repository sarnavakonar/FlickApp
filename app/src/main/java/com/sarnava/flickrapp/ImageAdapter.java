package com.sarnava.flickrapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImageModel> itemList;

    private class CellViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;

        public CellViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv);
            img = (ImageView) itemView.findViewById(R.id.iv);

        }

    }

    public ImageAdapter(List<ImageModel> itemList) {
        this.itemList = itemList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new CellViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            default: {
                ImageModel item = itemList.get(position);
                CellViewHolder holder = (CellViewHolder) viewHolder;

                Context context = holder.itemView.getContext();

                int w= item.getWidth();
                int h = item.getHeight();

                Picasso.with(context).load(item.getImg_url()).resize(w,h).placeholder(R.drawable.no_image).into(holder.img);

//                Glide.with(ctx)
//                        .load(item.getImg_url()).asBitmap().override(w, h)
//                        .placeholder(R.drawable.no_image)
//                        .skipMemoryCache(true)
//                        .into(holder.img);

                holder.title.setText(item.getImg_title());

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}

