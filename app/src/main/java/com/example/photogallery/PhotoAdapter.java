package com.example.photogallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<GalleryItem> mGalleryItems;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    private Context mContext;

    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);

            mItemImageView = itemView.findViewById(R.id.item_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }
        public void bindGalleryItem(GalleryItem galleryItem) {
            mGalleryItem = galleryItem;
        }

        @Override
        public void onClick(View v) {
            Intent i = PhotoPageActivity.newIntent(mContext, mGalleryItem.getPhotoPageUri());
            mContext.startActivity(i);
        }
    }

    public PhotoAdapter(List<GalleryItem> galleryItems, ThumbnailDownloader<PhotoHolder> thumbnailDownloader) {
        mGalleryItems = galleryItems;
        mThumbnailDownloader = thumbnailDownloader;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gallery_item, parent, false);
        mContext = parent.getContext();
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        GalleryItem galleryItem = mGalleryItems.get(position);
        holder.bindGalleryItem(galleryItem);
        mThumbnailDownloader.queueThumbnail(holder, galleryItem.getUrl());
    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }


}
