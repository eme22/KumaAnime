package com.eme22.kumaanime.AnimeActivity_fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.eme22.kumaanime.AnimeActivity;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.ServerUtil;
import com.eme22.kumaanime.AnimeActivity_fragments.Utils.downloader.DownloadManager_v2;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_update.AnimeUpdate;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIAdapter;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io.MyAnimeListAPIService;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Servers.Common.CommonServer;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.Datum;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.Fembed;
import com.eme22.kumaanime.AppUtils.Servers.Yu.Yu;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.PlayActivityFragments.Util.SourceFetcher_v3;
import com.eme22.kumaanime.R;
import com.eme22.serverproxy.BufferFile;
import com.eme22.serverproxy.BufferProxy;
import com.eme22.serverproxy.DownloadTask;
import com.htetznaing.lowcostvideo.LowCostVideo;
import com.htetznaing.lowcostvideo.Model.XModel;
import com.tingyik90.prefmanager.PrefManager;
import com.zbiyikli.sgetter.SourceGetter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Play implements Runnable{

    private static final TaskRunner taskRunner = new TaskRunner();
    private static Server ACTUAL_SOURCE;
    private final PrefManager prefs;
    Context context;
    String[] sources;
    MiniEpisode episode_anim;
    private int episode;
    private final SourceGetter sGetter;
    private final LowCostVideo xGetter;
    DialogFragment newFragment;
    final FragmentManager fragmentManager;
    private MyAnimeListAPIService api;
    private ProgressDialog pd;

    public Play(Context context, FragmentManager manager, MiniEpisode episode) {
        this.context = context;
        this.episode_anim = episode;
        this.episode = Integer.parseInt(episode.getEpisode());
        this.sources = episode.getLink().split(",");
        this.fragmentManager = manager;
        this.sGetter = new SourceGetter(context);
        this.xGetter = new LowCostVideo(context);
        prefs = new PrefManager(context);
        if (prefs.getBoolean("isLogged",false)){
            this.api = MyAnimeListAPIAdapter.getApiServiceWithAuth(context);
        }
        else this.api = null;
    }

    @Override
    public void run() {

        if ( new DownloadManager_v2(context).isAnimeDownloaded(episode_anim)){
            startVideoOffline();
        }
        else
        searchlinks();
    }



    @SuppressWarnings("unchecked")
    private void searchlinks() {
        xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                if (multiple_quality){
                    List<Datum> data = new ArrayList<>();
                    for (XModel model : vidURL) {
                        String name = model.getQuality();
                        String url = model.getUrl();
                        data.add(new Datum().withFile(url).withLabel(name));
                    }
                    manageServer(new Fembed().withData(data));

                } else {
                    String url  = vidURL.get(0).getUrl();
                    manageServer(new CommonServer().withFile(url));
                }
            }

            @Override
            public void onError() {
                taskRunner.executeAsync(new ServerUtil.decode(context,ACTUAL_SOURCE, new Callback() {
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
        });

        sGetter.onFinish(new SourceGetter.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<com.zbiyikli.sgetter.Model.XModel> vidURL, boolean multiple_quality) {
                if (multiple_quality){
                    List<Datum> data = new ArrayList<>();
                    for (com.zbiyikli.sgetter.Model.XModel model : vidURL) {
                        String name = model.getQuality();
                        String url = model.getUrl();
                        data.add(new Datum().withFile(url).withLabel(name));
                    }
                    manageServer(new Fembed().withData(data));

                } else {
                    String url  = vidURL.get(0).getUrl();
                    manageServer(new CommonServer().withFile(url));
                }
            }

            @Override
            public void goBitLY(String url) {
                Log.d("BITLY?", url);
            }

            @Override
            public void onError() {
                taskRunner.executeAsync(new ServerUtil.decode(context,ACTUAL_SOURCE, new Callback() {
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
        });

        showPD();
        taskRunner.executeAsync(new SourceFetcher_v3(sources, new Callback() {
            @Override
            public void onSuccess(Object o) {
                    ArrayList<Server> servers = (ArrayList<Server>) o;
                    ArrayList<String> data = new ArrayList<>();
                    ArrayList<String> label = new ArrayList<>();
                    for (Server server: servers) {
                        Log.d("SERVER",server.getName()+" "+server.getSource());
                        data.add(server.getSource());
                        label.add(server.getName());
                    }
                    hidePD();
                    newFragment = PlayFragment_v2.newInstance("Seleccione Servidor: ",label.toArray(new String[0]),data.toArray(new String[0]), new PlayObserver() {
                        @Override
                        public void onServerPress(String link) {
                            ACTUAL_SOURCE = findUsingEnhancedForLoop(link,servers);
                            decode(ACTUAL_SOURCE);
                        }

                         @Override
                         public void onCancelOrDismiss() {   hidePD(); }
                     });
                    newFragment.show(fragmentManager,"DATA");

            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    hidePD();
                    Toast.makeText(context, "ERROR CARGANDO SERVIDORES", Toast.LENGTH_SHORT).show();
                });
            }
        }));
    }

    private void decode(Server server){
        /*
        try {
                sGetter.find(server.getSource());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
        xGetter.find(server.getSource());
    }

    public void manageServer(Object o){
        showPD();
        if (o instanceof Fembed) {
            Fembed data = (Fembed) o;
            List<Datum> links = data.getData();
            if (links.size()>1){
                ArrayList<String> data2 = new ArrayList<>();
                ArrayList<String> label = new ArrayList<>();
                for (Datum d: links) {
                    data2.add(d.getFile());
                    label.add(d.getLabel());
                }
                if (!(data2.size() == label.size())) throw new NullPointerException();
                subvideo(data2.toArray(new String[0]),label.toArray(new String[0]));
            }
            else startVideo(links.get(0).getFile());
        }
        else if (o instanceof Yu){
            Log.d("YU HERE 2", "");
            String url = ((Yu) o).getFile();
            String referer = ((Yu) o).getReferer();

            taskRunner.executeAsync(new DownloadTask(url, new String[]{"Referer"}, new String[]{referer}, context, () -> {

                BufferProxy bufferProxy = new BufferProxy(context);
                bufferProxy.setBufferFile(new BufferFile() {
                    @Override
                    public File getFile() {
                        return new File(context.getExternalCacheDir(), "CACHE.mp4");
                    }

                    @Override
                    public Long getContentLength() {
                        return null;
                    }

                    @Override
                    public long getEstimatedSize() {
                        return 0;
                    }

                    @Override
                    public boolean isWorkDone() {
                        return false;
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onResume() {

                    }
                });
                bufferProxy.start();
                String proxy = bufferProxy.getPrivateAddress("CACHE.mp4");
                Log.d("PROXY LINK", proxy);
                startVideo(proxy);


            }));

        }
        else if (o instanceof CommonServer){
            CommonServer data = (CommonServer) o;
            Log.e("SOURCE", data.getFile());
            startVideo(data.getFile());
        }
        else {
            Log.e("SOURCE", "error");
        }

    }

    private void subvideo(String[] links, String[] labels) {
        hidePD();
        newFragment = PlayFragment_v2.newInstance("Seleccione Video:", labels, links, new PlayObserver() {
            @Override
            public void onServerPress(String link) {
                startVideo(link);
            }

            @Override
            public void onCancelOrDismiss() {
                hidePD();
            }
        });

        newFragment.show(fragmentManager,"DATA_2");

    }

    void startVideo(String file) {
        hidePD();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(file), "video/mp4");
        updateanime();
        Log.d("EPISODE SEEING", String.valueOf(episode));

        try {
            context.startActivity(intent);
        }
        catch (Exception ignore){}
    }

    private void startVideoOffline() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(new DownloadManager_v2(context).getFileUri(episode_anim), "video/mp4");
        updateanime();
        Log.d("EPISODE SEEING", String.valueOf(episode));
        try {
            context.startActivity(intent);
        }
        catch (Exception ignored){}
    }

    void updateanime(){

        if (context instanceof AnimeActivity) {
            if (((AnimeActivity) context).getEpisode() >= episode) return;
            int malid = ((AnimeActivity) context).getMalid();
            ((AnimeActivity) context).setEpisodeAndUpdate(episode);
            if (prefs.getBoolean("isLogged",false)){
                try {
                    api.updatemyanime(malid,null,null,null, episode,null,null,null,null,null).enqueue(new retrofit2.Callback<AnimeUpdate>() {
                        @Override
                        public void onResponse(@NotNull Call<AnimeUpdate> call, @NotNull Response<AnimeUpdate> response) {
                            Log.d("Update online", "DA");
                        }

                        @Override
                        public void onFailure(@NotNull Call<AnimeUpdate> call, @NotNull Throwable t) {
                            Log.d("Update online", "ERROR");
                        }
                    });
                }
                catch (Exception e){e.printStackTrace();}

            }
        }
    }


    public Server findUsingEnhancedForLoop(String name, List<Server> customers) {

        for (Server customer : customers) {
            if (customer.getSource().equals(name)) {
                return customer;
            }
        }
        return null;
    }

    private void showPD(){
        new Handler(Looper.getMainLooper()).post(() -> {
            pd = null;
            pd = ProgressDialog.show(context, "", context.getString(R.string.loading),true);
            pd.show();
        });
    }

    void hidePD(){
        new Handler(Looper.getMainLooper()).post(() -> pd.dismiss());
    }
}
