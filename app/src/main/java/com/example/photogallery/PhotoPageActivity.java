package com.example.photogallery;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;


public class PhotoPageActivity extends SingleFragmentActivity {
    private PhotoPageFragment mChildFragment;

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(photoPageUri);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        mChildFragment = PhotoPageFragment.newInstance(getIntent().getData());
        return mChildFragment;
    }


    @Override
    public void onBackPressed() {
        if (!mChildFragment.canWebViewGoBack()) {
            super.onBackPressed();
        }
    }
}