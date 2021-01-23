package com.eme22.kumaanime.AnimeActivity_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.eme22.kumaanime.AnimeActivity_fragments.Utils.ServerUtil;
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
import com.zbiyikli.sgetter.SourceGetter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Play mess fragment {@link DialogFragment} subclass.
 * Don't use {@link PlayFragment#newInstance}.
 * Use {@link PlayFragment_v2} instead.
 */

@Deprecated
public class PlayFragment extends DialogFragment {


    private static final TaskRunner taskRunner = new TaskRunner();
    private static final String SOURCES = "ANIME_SOURCES";
    private String SOURCES_REAL;
    private LowCostVideo xGetter;
    private SourceGetter sGetter;
    private Server ACTUAL_SOURCE;
    private boolean playingProxy= false;
    private boolean firstalert = true;
    private  boolean secondalert = true;

    public static PlayFragment newInstance(String sources) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString(SOURCES, sources);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SOURCES_REAL = getArguments().getString(SOURCES);
            xGetter = new LowCostVideo(requireContext());
            sGetter = new SourceGetter(requireContext());
        }
        else getParentFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_play, container, false);
        final ProgressDialog pd = ProgressDialog.show(requireContext(), "", getString(R.string.loading),true);
        String[] sources = SOURCES_REAL.split(",");
        if (sources.length>3) throw new NullPointerException();

        xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                secondalert = false;
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
                secondalert = false;
                taskRunner.executeAsync(new ServerUtil.decode(requireContext(),ACTUAL_SOURCE, new Callback() {
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
                secondalert = false;
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
                secondalert = false;
                taskRunner.executeAsync(new ServerUtil.decode(requireContext(),ACTUAL_SOURCE, new Callback() {
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

        taskRunner.executeAsync(new SourceFetcher_v3(sources,new Callback() {
            @Override
            public void onSuccess(Object o) {
                pd.dismiss();
                ArrayList<Server> servers = (ArrayList<Server>) o;
                for (Server server: servers) {
                    Log.d("SERVER",server.getName()+" "+server.getSource());
                }
                serverChoose(servers);
            }

            @Override
            public void onError(Exception e) {
                pd.dismiss();
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(requireContext(),"ERROR CARGANDO SERVIDORES",Toast.LENGTH_SHORT).show());
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        }));


        return v;
    }

    private void decode(Server server, boolean test){
        if (test) {
            try {
                sGetter.find(server.getSource());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else xGetter.find(server.getSource());
    }

    public void manageServer(Object o){
        if (o instanceof Fembed) {
            Fembed data = (Fembed) o;
            List<Datum> links = data.getData();
            if (links.size()>1){
                String[] Links = {};
                String[] Labels = {};
                for (Datum d: links) {
                    Links = append(Links,d.getFile());
                    Labels = append(Labels,d.getLabel());
                }
                if (!(Links.length == Labels.length)) throw new NullPointerException();
                subvideo(Labels,Links);
            }
            else startVideo(links.get(0).getFile());
        }
        else if (o instanceof Yu){
            Log.d("YU HERE 2", "");
            String url = ((Yu) o).getFile();
            String referer = ((Yu) o).getReferer();

            taskRunner.executeAsync(new DownloadTask(url, new String[]{"Referer"}, new String[]{referer}, requireContext(), () -> {

                BufferProxy bufferProxy = new BufferProxy(requireContext());
                bufferProxy.setBufferFile(new BufferFile() {
                    @Override
                    public File getFile() {
                        return new File(requireContext().getExternalCacheDir(), "CACHE.mp4");
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
                        playingProxy = false;
                    }

                    @Override
                    public void onResume() {

                    }
                });
                bufferProxy.start();
                String proxy = bufferProxy.getPrivateAddress("CACHE.mp4");
                Log.d("PROXY LINK", proxy);
                playingProxy = true;
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

    private void startVideo(String video){
        firstalert = false;
        secondalert = false;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(video), "video/mp4");
        try {
            startActivity(intent);
        }
        catch (Exception ignore){}

        getParentFragmentManager().beginTransaction().remove(PlayFragment.this).commit();
    }

    public void subvideo(String[] labels, String[] links) {

        // setup the alert builder
        AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext());
        secondalert = true;
        builder2.setTitle("Seleccione Video: ");

        builder2.setItems(labels, (dialog, which) -> startVideo(links[which]));

        // create and show the alert dialog
        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog dialog = builder2.create();
            dialog.show();
        });

        builder2.setOnDismissListener(dialog -> {
            secondalert = false;
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        builder2.setOnCancelListener(dialog -> {
            secondalert = false;
            requireActivity().getSupportFragmentManager().popBackStack();
        });

    }

    public void serverChoose(ArrayList<Server> servers) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
        firstalert = true;
        builder1.setTitle("Seleccione Servidor: ");

        String[] Nombres = {};
        for (Server d: servers) {
            Nombres = append(Nombres,d.getName());
        }

        Log.d("Nombres:" , String.valueOf(Nombres.length));
        Log.d("Servers", String.valueOf(servers.size()));


        if (Nombres.length == servers.size()){
            builder1.setItems(Nombres, (dialog, which) -> {
                ACTUAL_SOURCE = servers.get(which);
                decode(servers.get(which),false);
            });

            new Handler(Looper.getMainLooper()).post(() -> {
                AlertDialog dialog = builder1.create();
                dialog.show();
            });
        }
        //builder.setOnDismissListener(dialog -> requireActivity().getSupportFragmentManager().popBackStack());
        builder1.setOnCancelListener(dialog -> {
            secondalert = false;
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PLAY", "RESUME");
        if (!playingProxy && !firstalert && !secondalert ) requireActivity().getSupportFragmentManager().popBackStack();
    }

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }


}