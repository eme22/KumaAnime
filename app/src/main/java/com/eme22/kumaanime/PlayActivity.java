package com.eme22.kumaanime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.ServerUtil;
import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.NoSourceFoundException;
import com.eme22.kumaanime.AppUtils.Servers.Common.CommonServer;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.Datum;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.MultiServer;
import com.eme22.kumaanime.AppUtils.Theming;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PlayActivityFragments.Util.SourceFetcher_v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class PlayActivity extends AppCompatActivity {


    public static final String DATA_1 = "DATA_1";
    public static final String ACTION_1 = "INIT";
    private static final TaskRunner taskRunner = new TaskRunner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theming theme = new Theming(this);
        theme.themecheck();
        setContentView(R.layout.activity_play);

        String ACTION = getIntent().getAction();

        if (ACTION.equals(ACTION_1)){
            setAction1();
        }
        else throw new NullPointerException();



    }

    private void setAction1(){
        Bundle extras = getIntent().getExtras();
        String[] sources;

        if (extras != null) {
            sources = extras.getString(DATA_1).split(",");
            String source1 = null;
            String source2 = null;
            String source3 = null;
            try {
                source1 = sources[0];
                source2 = sources[1];
                source3 = sources[2];
            } catch (IndexOutOfBoundsException ignored){ }

            taskRunner.executeAsync(new SourceFetcher_v2(source1, source2, source3, new Callback() {
                @Override
                public void onSuccess(Object o) {
                    ArrayList<Server> servers = (ArrayList<Server>) o;
                    for (Server s: servers) {
                        Log.d("SERVER",s.getName()+" "+s.getSource());
                    }
                    serverChoose(servers);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        else try {
            throw new NoSourceFoundException();
        } catch (NoSourceFoundException e) {
            e.printStackTrace();
        }
    }

    private void setAction2(Server server){

        taskRunner.executeAsync(new ServerUtil.decode(this,server, new Callback() {
                @Override
                public void onSuccess(Object o) {
                    manageServer(o);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
        }));


    }


    public void manageServer(Object o){
        if (o instanceof MultiServer) {
            MultiServer data = (MultiServer) o;
            HashMap<String, String> links = data.getData();
            if (links.size()>1){
                String[] Links = {};
                String[] Labels = {};
                for (Map.Entry<String, String> d: links.entrySet()) {
                    Links = append(Links,d.getValue());
                    Labels = append(Labels,d.getKey());
                }
                if (!(Links.length == Labels.length)) throw new NullPointerException();
                subvideo(Labels,Links);
            }
        }
        if (o instanceof CommonServer){
            CommonServer data = (CommonServer) o;
            startVideo(data.getFile());
        }

    }

    private void startVideo(String video){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(video), "video/mp4");
        startActivity(intent);
    }

    public void subvideo(String[] labels, String[] links) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Video: ");

        builder.setItems(labels, (dialog, which) -> startVideo(links[which]));

        // create and show the alert dialog
        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    public void serverChoose(ArrayList<Server> servers) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Servidor: ");

        String[] Nombres = {};
        for (Server d: servers) {
            Nombres = append(Nombres,d.getName());
        }

        Log.d("Nombres:" , String.valueOf(Nombres.length));
        Log.d("Servers", String.valueOf(servers.size()));

        if (Nombres.length == servers.size()){
            builder.setItems(Nombres, (dialog, which) -> setAction2(servers.get(which)));

            new Handler(Looper.getMainLooper()).post(() -> {
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }



    }


    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

}