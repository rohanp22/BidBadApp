package com.wielabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Download extends AsyncTask<String, Object, String> {

    private static final String CHANNEL_ID = "Download";
    private static final int NOTIFICATION_ID = 42;

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private AlertDialog dialog;
    private PowerManager.WakeLock wakeLock;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationChannel notificationChannel;

    public Download(Context context, AlertDialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        String res = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            File dir = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath());
            if (!(dir.exists() && dir.isDirectory())) {
                if (!dir.mkdirs())
                    return "Failed to create Download folder";
            }
            File file = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath(), params[1]);
            long fileSizeLocal = 0;
            if (file.exists()) {
                fileSizeLocal = (int) file.length();
                Log.d("Length", params[2] + " " + fileSizeLocal);
                if (fileSizeLocal == Long.parseLong(params[2]))
                    return params[1];
                connection.setRequestProperty("Range", "bytes=" + fileSizeLocal + "-");
            } else {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK && connection.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
            long startTime = System.nanoTime();
            long totalFileSize = connection.getContentLength() + fileSizeLocal;
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(file, fileSizeLocal > 0);
            byte[] data = new byte[4096];
            long totalDownloaded = fileSizeLocal;
            int count;
            long prevTotal = totalDownloaded;
            while ((count = inputStream.read(data)) != -1) {
                totalDownloaded += count;
                if (totalFileSize > 0 && (totalDownloaded - prevTotal >= 100000)) {
                    // Percentage
                    prevTotal = totalDownloaded;
                    publishProgress(startTime, totalFileSize, totalDownloaded);
                }
                outputStream.write(data, 0, count);
            }
            outputStream.flush();
            res = params[1];
        } catch (Exception e) {
            e.printStackTrace();
            res = null;
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        return res;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        long totalSize = (long) values[1];
        long downloadedSize = (long) values[2];
        int percentage = (int) ((downloadedSize / totalSize) * 100);
        Log.d("onProgressUpdate", percentage + "");
        dialog.setMessage("Downloading Bidbad update (" + percentage + "%)");
        notificationBuilder.setProgress(100, percentage, false)
                .setContentText(percentage + "% Downloaded");
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @SuppressLint("WakelockTimeout")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Acquire a wakelock to allow downloads in background
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire();
        //Create Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "Download Notification", NotificationManager.IMPORTANCE_DEFAULT);
        }
        //Initialise Notification
        notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        //Start Download Notification
        notificationBuilder.setSmallIcon(R.drawable.ic_brand_logo)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Downloading BidBad update")
                .setProgress(0, 0, true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Release Wakelock Now
        wakeLock.release();
        if (result != null) {
            //Download Success
            notificationBuilder.setProgress(0, 0, false)
                    .setContentText("Download Success"); //TODO: Move to resources for translation?
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(context.getExternalFilesDir(null), result));
            //Log.d("Download", uri + "");
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            //Download Failed
            notificationBuilder.setProgress(0, 0, false)
                    .setContentText("Download Failed"); //TODO: Move to resources for translation?
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }
}
