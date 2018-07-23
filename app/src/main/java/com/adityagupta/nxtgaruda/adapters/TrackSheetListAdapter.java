package com.adityagupta.nxtgaruda.adapters;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adityagupta.nxtgaruda.R;
import com.adityagupta.nxtgaruda.activities.ServiceDescActivity;
import com.adityagupta.nxtgaruda.data.ServicesInfo;
import com.adityagupta.nxtgaruda.utils.Common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TrackSheetListAdapter extends RecyclerView.Adapter<TrackSheetListAdapter.ServicesHolder> {

    Context context;
    ArrayList<String> list;

    private ProgressDialog dialog;

    DownloadManager downloadManager;
    private Uri Download_Uri;
    public static ArrayList<Long> reflist = new ArrayList<>();

    public TrackSheetListAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicesHolder(LayoutInflater.from(context).inflate(R.layout.recycler_services_item, parent, false));
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
                    Log.e("DON", Common.TRACKSHEET_LINK + URLEncoder.encode(service, "UTF-8").replace("+","%20") + "");
                    Download_Uri = Uri.parse(Common.TRACKSHEET_LINK + URLEncoder.encode(service, "UTF-8").replace("+","%20"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle("Garuda Downloading " + service);
                request.setDescription("Downloading " + service);
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Garuda/" + "/" + service);

                final long downloadId = downloadManager.enqueue(request);

                final int UPDATE_PROGRESS = 5020;

                downloadComplete = true;
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what==UPDATE_PROGRESS){
                            String downloaded = String.format("%.2f MB", (double)((msg.arg1)/1024)/1024);
                            String total = String.format("%.2f MB", (double) ((msg.arg2)/1024)/1024);
                            String status = downloaded + " / " + total;
                            dialog.setMessage(status);
                            if(total.equals(downloaded) && downloadComplete)
                            {
                                downloadComplete = false;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, service+" Downloaded Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                },500);
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
