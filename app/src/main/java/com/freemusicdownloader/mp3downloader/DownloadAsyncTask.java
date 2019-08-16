package com.freemusicdownloader.mp3downloader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DownloadAsyncTask extends AsyncTask<String, String, String> {

    private String url;
    private String filename;
    private Activity activity;
    private int NOTIFY_ID;

    public String currentimagepath;
    private NotificationManager notifManager;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;
    private String songName;
    private File folder;
    private static final int MY_PERMISSION_REQUEST_CODE = 1001;

    private Boolean succesffuly_downloaded;
    private String error_message;

    public DownloadAsyncTask(String url, String filename, Activity activity, int NOTIFY_ID) {
        this.url = url;
        this.filename = filename;
        this.activity = activity;
        this.NOTIFY_ID = NOTIFY_ID;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        notificationManager = NotificationManagerCompat.from(activity);

    }

    @Override
    protected String doInBackground(final String... aurl) {
        int count;

        try {
            succesffuly_downloaded = true;
            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            final int lenghtOfFile = conexion.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());

            Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);


            OutputStream output = null;
            if (isSDPresent) {


                folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "Mp3Download");


                if (!folder.exists()) {
                    folder.mkdir();
                }

                currentimagepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) +
                        File.separator + aurl[1];
//                currentimagepath = Environment.getExternalStorageDirectory() +
//                        File.separator + "Mp3Download/" + aurl[1];


                output = new FileOutputStream(currentimagepath);


            } else {

                folder = new File(activity.getApplication().getFilesDir() +
                        File.separator + "Mp3Download/" + aurl[1]);


                if (!folder.exists()) {
                    folder.mkdir();
                }

                currentimagepath = activity.getApplication().getFilesDir() +
                        File.separator + "Mp3Download/" + aurl[1];


                output = new FileOutputStream(currentimagepath);

            }
            byte data[] = new byte[1024];

            long total = 0;

            String id = "default_channel_id";
            String title = "Default Channel";
            Intent intent;
            PendingIntent pendingIntent;


            Intent intentnatif = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(currentimagepath));

            intentnatif.setDataAndType(Uri.parse("file://" + currentimagepath), "audio/mp3");


            if (notifManager == null) {
                notifManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(activity, id);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentnatif, 0);
                builder.setSmallIcon(R.drawable.ic_file_download_black_24dp)   // required
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent)
                        .setProgress(100, 0, false)
                        .setPriority(Notification.PRIORITY_HIGH);
            } else {
                builder = new NotificationCompat.Builder(getApplicationContext(), id);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentnatif, 0);
                builder.setSmallIcon(R.drawable.ic_file_download_black_24dp)   // required
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent)
                        .setProgress(100, 0, false)
                        .setPriority(Notification.PRIORITY_HIGH);
            }

            songName = aurl[1].substring(0, Math.min(aurl[1].length(), 25));

            while ((count = input.read(data)) != -1) {
                total += count;
                try {
                    final long finalTotal = total;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            builder.setContentTitle(songName)
                                    .setSmallIcon(R.drawable.ic_file_download_black_24dp)// required
                                    .setContentText("%" + (int) ((finalTotal * 100) / lenghtOfFile))
                                    .setOngoing(true)
                                    .setProgress(100, (int) ((finalTotal * 100) / lenghtOfFile), false)
                                    .setOnlyAlertOnce(true);
                            notificationManager.notify(NOTIFY_ID, builder.build());
                            SystemClock.sleep(500);

                        }
                    }).start();
                } catch (Exception e) {
                    Log.i("errorror", "download failed");
                    succesffuly_downloaded = false;
                    error_message = e.getMessage();
                    //Toast.makeText(getApplicationContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                output.write(data, 0, count);

            }
            output.flush();
            output.close();
            input.close();

            Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent1.setData(Uri.parse("file://"+currentimagepath));
            getApplicationContext().sendBroadcast(intent1);

        } catch (Exception e) {
            Log.i("erorrorr", "" + e);
            error_message = e.getMessage();
            succesffuly_downloaded = false;
        }
        return null;

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(succesffuly_downloaded){

            Toast.makeText(activity, "" + songName + " downloaded", Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, "" + songName + " added 'GALLERY'", Toast.LENGTH_SHORT).show();

            notificationManager.cancel(NOTIFY_ID);
            createNotification(currentimagepath, songName);
            new GlobalData().setUri(Uri.parse(currentimagepath));
            new GlobalData().setMusicName(songName);
            new GlobalData().setIsGalleryUpdate(true);

        }else{

            Toast.makeText(activity, " An error occurred, check your internet connection and try again! ", Toast.LENGTH_SHORT).show();

        }



    }


    public void createNotification(String filepath, String songNamee) {
        String id = "default_channel_id";
        String title = "Default Channel";
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intenttt = new Intent(activity, MainActivity.class);

        PendingIntent pendingIntenttt = TaskStackBuilder.create(activity)
                .addNextIntent(intenttt)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        if (notifManager == null) {
            notifManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.enableVibration(false);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            builder.setContentTitle(songNamee)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentText("Download Completed ♫ ♫ \uD83D\uDE0A")
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntenttt)
                    .setTicker("Download Completed ♫ ♫ \uD83D\uDE0A");


        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            builder.setContentTitle(songNamee)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentText("Download Completed ♫ ♫ \uD83D\uDE0A")
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntenttt)
                    .setTicker("Download Completed ♫ ♫ \uD83D\uDE0A")
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

}
