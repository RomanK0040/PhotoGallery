package com.example.photogallery;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<GalleryItem> mGalleryItems;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public static class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);

            mItemImageView = itemView.findViewById(R.id.item_image_view);
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
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
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        GalleryItem galleryItem = mGalleryItems.get(position);
        mThumbnailDownloader.queueThumbnail(holder, galleryItem.getUrl());
    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }




}
