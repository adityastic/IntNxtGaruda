package com.nxtvision.tradenivesh.adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.nxtvision.tradenivesh.R;
import com.nxtvision.tradenivesh.utils.Common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import static com.nxtvision.tradenivesh.utils.Common.CHANNEL_ID;
import static com.nxtvision.tradenivesh.utils.Common.notificationManager;

public class ReportsListAdapter extends RecyclerView.Adapter<ReportsListAdapter.ServicesHolder> {

    Context context;
    ArrayList<String> list;

    private ProgressDialog dialog;

    DownloadManager downloadManager;
    private Uri Download_Uri;
    public static ArrayList<Long> reflist = new ArrayList<>();

    public ReportsListAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicesHolder(LayoutInflater.from(context).inflate(R.layout.recycler_tracklist_item, parent, false));
    }

    boolean downloadComplete;

    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, final int position) {
        final String service = list.get(position);

        holder.name.setText(service);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(context);
                dialog.setMessage("Doing something, please wait.");
                dialog.show();

                try {
                    Log.e("DON", Common.REPORTS_LINK + URLEncoder.encode(service, "UTF-8").replace("+", "%20") + "");
                    Download_Uri = Uri.parse(Common.REPORTS_LINK + URLEncoder.encode(service, "UTF-8").replace("+", "%20"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle("Downloading " + service);
                request.setDescription("Downloading " + service);
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + context.getResources().getString(R.string.app_name) + "/" + "/Reports/" + service);

                final long downloadId = downloadManager.enqueue(request);

                final int UPDATE_PROGRESS = 5020;

                downloadComplete = true;
                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == UPDATE_PROGRESS) {
                            String downloaded = String.format("%.2f MB", (double) ((msg.arg1) / 1024) / 1024);
                            String total = String.format("%.2f MB", (double) ((msg.arg2) / 1024) / 1024);
                            String status = downloaded + " / " + total;
                            dialog.setMessage(status);
                            if (total.equals(downloaded) && downloadComplete) {
                                downloadComplete = false;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, service + " Downloaded Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        sendNotification(
                                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + context.getResources().getString(R.string.app_name) + "/Reports/" + service
                                        );
                                    }
                                }, 500);
                            }
                        }
                        super.handleMessage(msg);
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean downloading = true;
                        while (downloading) {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(downloadId);
                            Cursor cursor = downloadManager.query(q);
                            cursor.moveToFirst();
                            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                downloading = false;
                            }
                            //Post message to UI Thread
                            Message msg = handler.obtainMessage();
                            msg.what = UPDATE_PROGRESS;
                            //msg.obj = statusMessage(cursor);
                            msg.arg1 = bytes_downloaded;
                            msg.arg2 = bytes_total;
                            handler.sendMessage(msg);
                            cursor.close();
                        }
                    }
                }).start();
            }
        });
    }

    public static Intent getOpenFileIntent(Context context, String path) {
        File file = new File(path);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = getFileExtension(new File(path));
        String type = mime.getMimeTypeFromExtension(extension);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkURI = FileProvider.getUriForFile(context
                ,
                context.getApplicationContext()
                        .getPackageName() + ".provider", file);
        intent.setDataAndType(apkURI, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    private static String getFileExtension(File file) {

        String name = file.getName().substring(Math.max(file.getName().lastIndexOf('/'),
                file.getName().lastIndexOf('\\')) < 0 ? 0 : Math.max(file.getName().lastIndexOf('/'),
                file.getName().lastIndexOf('\\')));
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1); // doesn't return "." with extension
    }

    private void sendNotification(String Path) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int notificationNumber = prefs.getInt("notificationNumber", 0);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0/*Request code*/, getOpenFileIntent(context,Path), PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Downloaded")
                .setContentText("Complete " + new File(Path).getName())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationNumber, notification);
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt("notificationNumber", notificationNumber);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ServicesHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ServicesHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
        }
    }
}
