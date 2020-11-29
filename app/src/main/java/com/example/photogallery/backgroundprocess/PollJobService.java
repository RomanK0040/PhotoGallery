package com.example.photogallery.backgroundprocess;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.photogallery.FlickrFetchr;
import com.example.photogallery.GalleryItem;
import com.example.photogallery.PhotoGalleryActivity;
import com.example.photogallery.QueryPreferences;
import com.example.photogallery.R;

import java.util.List;

public class PollJobService extends JobService {
    private static final String TAG = "PollJobService";
    private static final String CHANNEL_ID = "photogallery.services.001";

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.example.photogallery.backgroundprocess.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.example.photogallery.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    @Override
    public boolean onStartJob(JobParameters params) {

        createNotificationChannel();

        doSearchInBackground(params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return true;
    }


    private void  createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "PhotosNotification";
            String description = "new photo notification chanel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void doSearchInBackground(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String query = QueryPreferences.getStoredQuery(getApplicationContext());
                String lastResultId = QueryPreferences.getLastResultId(getApplicationContext());
                List<GalleryItem> items;

                if (query == null) {
                    items = new FlickrFetchr().fetchRecentPhotos();
                } else {
                    items = new FlickrFetchr().searchPhotos(query);
                }

                if (items.size() > 0) {
                    String resultId = items.get(0).getId();
                    if (resultId.equals(lastResultId)) {
                        Log.i(TAG, "Got an old result: " + resultId);
                    } else {
                        Log.i(TAG, "Got a new result: " + resultId);
                        Notification notification = createNotification();
                        showBackgroundNotification(0, notification);

                    }
                    QueryPreferences.setLastResultId(getApplicationContext(), resultId);
                }

                jobFinished(params, true);
            }
        }).start();
    }

    private Notification createNotification() {
        Resources resources = getResources();
        Intent i = PhotoGalleryActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        return notification;
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);

    }
}
