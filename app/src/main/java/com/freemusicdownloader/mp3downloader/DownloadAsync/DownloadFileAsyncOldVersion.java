package com.freemusicdownloader.mp3downloader.DownloadAsync;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.freemusicdownloader.mp3downloader.Activity.MainActivity;
import com.freemusicdownloader.mp3downloader.Constans.GlobalData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class DownloadFileAsyncOldVersion extends AsyncTask<String, String, String> {

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
    private ProgressDialog barProgressDialog;
    private String songNameeee;

    public DownloadFileAsyncOldVersion(String url, String filename, Activity activity) {
        this.url = url;
        this.filename = filename;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //  showDialog(DIALOG_DOWNLOAD_PROGRESS);
        barProgressDialog = new ProgressDialog(activity);
        barProgressDialog.setTitle("Downloading MP3 ...");
        barProgressDialog.setMessage("Download in progress ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(100);
        barProgressDialog.show();
        barProgressDialog.setCancelable(false);


    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;


        try {
            succesffuly_downloaded = true;
            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
            //Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());

            Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

            OutputStream output = null;
            if (isSDPresent) {

                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "Mp3Download");


                if (!folder.exists()) {
                    folder.mkdir();
                }


                currentimagepath = Environment.getExternalStorageDirectory() +
                        File.separator + "Mp3Download/" + aurl[1];


                output = new FileOutputStream(currentimagepath);


            } else {

                File folder = new File(activity.getApplication().getFilesDir() +
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


            songNameeee = aurl[1].substring(0, Math.min(aurl[1].length(), 25));

            while ((count = input.read(data)) != -1) {
                total += count;

                barProgressDialog.setProgress((int) ((total * 100) / lenghtOfFile));

                // publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent1.setData(Uri.parse("file://" + currentimagepath));
            getApplicationContext().sendBroadcast(intent1);
        } catch (Exception e) {

            succesffuly_downloaded = false;

        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        barProgressDialog.dismiss();
        // pd.dismiss();
        showNotification(currentimagepath);

        if (succesffuly_downloaded) {

            Toast.makeText(activity, "" + songNameeee + " downloaded", Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, "" + songNameeee + " added 'GALLERY'", Toast.LENGTH_SHORT).show();


            new GlobalData().setUri(Uri.parse(currentimagepath));
            new GlobalData().setMusicName(songNameeee);
            new GlobalData().setIsGalleryUpdate(true);

        } else {

            Toast.makeText(activity, " An error occurred, check your internet connection and try again! ", Toast.LENGTH_SHORT).show();

        }

    }
    public void showNotification(String filepath) {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intenttt = new Intent(activity, MainActivity.class);

        PendingIntent pendingIntenttt = TaskStackBuilder.create(activity)
                .addNextIntent(intenttt)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(activity.getApplicationContext())

                .setContentTitle(songNameeee)
                .setContentText("Download Completed ♫ ♫")
                .setSmallIcon(android.R.drawable.ic_menu_upload)
                .setContentIntent(pendingIntenttt)
                .setSound(soundUri)
                .build();


        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(1230, mNotification);
    }
}