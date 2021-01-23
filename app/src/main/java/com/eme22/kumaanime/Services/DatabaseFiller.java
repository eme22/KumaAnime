package com.eme22.kumaanime.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.OtherUtils;
import com.eme22.kumaanime.AppUtils.StringUtils;
import com.eme22.kumaanime.Databases.MainTable.MiniAnimeTable_Repo;
import com.eme22.kumaanime.MainActivity_fragments.util.TaskRunner;
import com.eme22.kumaanime.R;
import com.tingyik90.prefmanager.PrefManager;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
public class DatabaseFiller extends Service {

    NotificationCompat.Builder builder;
    NotificationManagerCompat mNotifyManager;

    public DatabaseFiller() {
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "service starting", android.widget.Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        TaskRunner taskRunner = new TaskRunner();
        mNotifyManager = NotificationManagerCompat.from(this);
        String NOTIFICATION_CHANNEL = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NOTIFICATION_CHANNEL = OtherUtils.createNotificationChannel(this, "database_load", "Buscador de Animes", "Notificaciones del canal del buscador de animes");
            builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_cloud_download)
//                .setTicker("TICKER") // use something from something from R.string
                    .setContentTitle("Creando Base de Datos Local") // use something from something from
                    .setContentText("Espere")
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        else  builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_cloud_download)
//              .setTicker("TICKER") // use something from something from R.string
                .setContentTitle("Creando Base de Datos Local") // use something from something from
                .setContentText("Espere")
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        taskRunner.executeAsync(new FLVLinks(), result1 -> taskRunner.executeAsync(new JKLinks(), result2 -> taskRunner.executeAsync(new IDLinks(), result3 -> taskRunner.executeAsync(new offlineFetch(result1, result2, result3), result4 -> taskRunner.executeAsync(new SaveOnDB(result4), result6 -> {

            stopForeground(true);
            stopSelf();

        })))));



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class FLVLinks implements Callable<ArrayList<MiniAnime>> {

        @Override
        public ArrayList<MiniAnime> call() throws IOException {
            ArrayList<MiniAnime> flv_list = new ArrayList<>();
            String baseurl = "https://www3.animeflv.net/browse?page=";
            int i = 1;
            while (true){
                try {



                    builder.setContentText("Buscando links de FLV").setProgress(100, i * 100 / 140, false);
                    mNotifyManager.notify(0, builder.build());
                    Log.d("MASTER LINK", baseurl+i);
                    Document doc = Connection.getDocOk(baseurl + i);
                    int size = flv_list.size();
                    //flv_list = Connection.getanimes(doc, 0, flv_list);
                    Connection.getanimes(doc, 0, flv_list,0);
                    //Log.d("LINK_SIZE", String.valueOf(flv_list.size()));
                    if (size == flv_list.size()) break;
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
            }
                i= i+1;
            }
            return flv_list;

        }

    }

    private class JKLinks implements Callable<ArrayList<MiniAnime>> {

        @Override
        public ArrayList<MiniAnime> call() throws IOException {

            ArrayList<MiniAnime> flv_list = new ArrayList<>();
            String url = "https://jkanime.net/letra/";
            int j = 1;
            for(char alphabet = 'a'; alphabet <='z'; alphabet ++ )
            {
                int i = 1;
                while (true){
                    try {
                        String nuevaurl = url + Character.toString(alphabet).toUpperCase() +"/" + i + "/";
                        Log.d("MASTER LINK", nuevaurl);
                        Document doc = Connection.getDocOk(nuevaurl);
                        //if (i % 10 == 0) System.out.println("TRABAJANDO: ANIME4"+ i);
                        int size = flv_list.size();
                        //flv_list = Connection.getanimes(doc, 1, flv_list);
                        Connection.getanimes(doc, 1, flv_list,0);
                        if (size == flv_list.size()) break;
                        Log.d("LINK_SIZE", String.valueOf(size));
                    } catch (NullPointerException  e) {
                        e.printStackTrace();
                    }
                    i++;
                    j++;
                }
                builder.setContentText("Buscando links de JK").setProgress(100, j*10/27, false);
                mNotifyManager.notify(0, builder.build());
            }

            int i = 1;
            while (true){
                try {
                    String nuevaurl = "https://jkanime.net/letra/0-9"+"/" + i + "/";
                    Log.d("MASTER LINK", nuevaurl);
                    Document doc = Connection.getDocOk(nuevaurl);
                    int size = flv_list.size();
                    //flv_list = Connection.getanimes(doc, 1, flv_list);
                    Connection.getanimes(doc, 1, flv_list,0);
                    if (size == flv_list.size()) break;
                    Log.d("LINK_SIZE", String.valueOf(size));
                } catch (NullPointerException  e) {
                    e.printStackTrace();
                }
                i++;
            }

            return flv_list;
        }
    }

    private class IDLinks implements Callable<ArrayList<MiniAnime>> {
        @Override
        public ArrayList<MiniAnime> call() throws IOException {

            ArrayList<MiniAnime> id_lists = new ArrayList<>();

            int i = 1;
            while (true){
                try {
                    String nuevaurl = "https://www.animeid.tv/series?sort=asc&pag=" + i;
                    if (i==1) nuevaurl = "https://www.animeid.tv/series";
                    builder.setContentText("Buscando Animes de ID").setProgress(100, i*100/56, false);
                    mNotifyManager.notify(0, builder.build());
                    Log.d("MASTER LINK", nuevaurl);
                    Document doc = Connection.getDocOk(nuevaurl);
                    int size = id_lists.size();
                    //id_lists = Connection.getanimes(doc, 2, id_lists);
                    Connection.getanimes(doc, 2, id_lists,0);
                    if (size == id_lists.size()) break;
                    Log.d("LINK_SIZE", String.valueOf(size));
                } catch (NullPointerException  e) {
                    e.printStackTrace();
                }
                i++;
            }

            i = 1;
            while (true){
                try {
                    String nuevaurl = "https://www.animeid.tv/peliculas?sort=asc&pag=" + i;
                    if (i==1) nuevaurl = "https://www.animeid.tv/peliculas";
                    builder.setContentText("Buscando Peliculas de ID").setProgress(100, i*100/10, false);
                    mNotifyManager.notify(0, builder.build());
                    Log.d("MASTER LINK", nuevaurl);
                    Document doc = Connection.getDocOk(nuevaurl);
                    int size = id_lists.size();
                    //id_lists = Connection.getanimes(doc, 2, id_lists);
                    Connection.getanimes(doc, 2, id_lists,1);
                    if (size == id_lists.size()) break;
                    Log.d("LINK_SIZE", String.valueOf(size));
                } catch (NullPointerException  e) {
                    e.printStackTrace();
                }
                i++;
            }

            i = 1;
            while (true){
                try {
                    String nuevaurl = "https://www.animeid.tv/ovas?sort=asc&pag=" + i;
                    if (i==1) nuevaurl = "https://www.animeid.tv/ovas";
                    builder.setContentText("Buscando Ovas de ID").setProgress(100, i*100/10, false);
                    mNotifyManager.notify(0, builder.build());
                    Log.d("MASTER LINK", nuevaurl);
                    Document doc = Connection.getDocOk(nuevaurl);
                    int size = id_lists.size();
                    //id_lists = Connection.getanimes(doc, 2, id_lists);
                    Connection.getanimes(doc, 2, id_lists,2);
                    if (size == id_lists.size()) break;
                    Log.d("LINK_SIZE", String.valueOf(size));
                } catch (NullPointerException  e) {
                    e.printStackTrace();
                }
                i++;
            }





            return id_lists;
        }
    }

    /*
    public static class RULinks implements Callable<ArrayList<MiniAnime>> {


        @Override
        public ArrayList<MiniAnime> call() {
            ArrayList<MiniAnime> id_lists = new ArrayList<>();
            String url = "https://animeflv.ac/animes?page=";

            int i = 1;
            while (true){
                try {
                    String nuevaurl = url+i;
                    Log.d("MASTER LINK", nuevaurl);
                    Document doc = Connection.getDocOk(nuevaurl);
                    int size = id_lists.size();
                    id_lists = Connection.getanimes(doc, 3, id_lists);
                    if (size == id_lists.size()) break;
                    Log.d("LINK_SIZE", String.valueOf(size));
                } catch (NullPointerException  e) {
                    //break;
                }
                i++;
            }
            return id_lists;
        }
    }
     */

    private class offlineFetch implements  Callable<ArrayList<MiniAnime>>{

        ArrayList<MiniAnime> animes1;
        ArrayList<MiniAnime> animes2;
        ArrayList<MiniAnime> animes3;
        //ArrayList<MiniAnime> animes4;


        public offlineFetch(ArrayList<MiniAnime> animes1, ArrayList<MiniAnime> animes2, ArrayList<MiniAnime> animes3) {
            this.animes1 = animes1;
            this.animes2 = animes2;
            this.animes3 = animes3;
        }

        @Override
        public ArrayList<MiniAnime> call() {
            return compareanimes(animes1,animes2,animes3);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);

    }

    private class SaveOnDB implements  Callable<Boolean>{

        ArrayList<MiniAnime> main;

        public SaveOnDB(ArrayList<MiniAnime> main) {
            this.main = main;
        }

        @Override
        public Boolean call() {
                MiniAnimeTable_Repo repo = new MiniAnimeTable_Repo(getApplication());
                builder.setContentText("TERMINANDO BASE DE DATOS").setProgress(0, 0, true);
                mNotifyManager.notify(0, builder.build());
                repo.reStock(main);
                new PrefManager(DatabaseFiller.this).putBoolean("FirstStart", false);
            return true;
        }
    }

    /*

    private ArrayList<MiniAnime> comparelogedanimes(ArrayList<MiniAnime> main) throws IOException {

        ArrayList<MiniAnime> mainfinal = new ArrayList<>();

        int size = main.size();
        Log.d("main", String.valueOf(size));

        MyAnimeListAPIService rss = MyAnimeListAPIAdapter.getApiService(DatabaseFiller.this);
        for (int i = 0; i <size ; i++) {
            Call<AnimeList> animesquery = rss.getanimelist(main.get(i).getTitle(), 1, 0);
            Response<AnimeList> response = animesquery.execute();
            if (response.isSuccessful()) {
                try {
                    MiniAnime possible = response.body().getData().get(0).getNode();
                    mainfinal.add(possible);
                }
                catch (IndexOutOfBoundsException ignored) {}

            } else {
                Log.d("ERROR", "NO RESPONSE");
            }
            builder.setContentText("Tageando Links (IMPORTANTE)").setProgress(100, i*100/size, false);
            mNotifyManager.notify(0, builder.build());
        }

        return  mainfinal;

    }
     */


    private ArrayList<MiniAnime> compareanimes(ArrayList<MiniAnime> FLV, ArrayList<MiniAnime> JK, ArrayList<MiniAnime> ID){



        Log.d("flv", String.valueOf(FLV.size()));
        Log.d("jk", String.valueOf(JK.size()));
        Log.d("id", String.valueOf(ID.size()));

        for (int i = 0; i < JK.size() ; i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                MiniAnime idelem2 = idelem.next();
                //int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(),JK.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if(
                        //sim<3 &&
                                idelem2.getShow_type()==JK.get(i).getShow_type() && StringUtils.compareAnimes(idelem2.getTitle(),JK.get(i).getTitle())){
                    JK.get(i).setLink(JK.get(i).getLink()+","+idelem2.getLink());
                    idelem.remove();
                }
            }
            builder.setContentText("Combinando links de JK").setProgress(100, i*100/JK.size(), false);
            mNotifyManager.notify(0, builder.build());
        }

        for (int i = 0; i < FLV.size() ; i++) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                MiniAnime idelem2 = idelem.next();
                //int sim = LevenshteinDistance.getDefaultInstance().apply(idelem2.getTitle().toUpperCase(),FLV.get(i).getTitle().toUpperCase());
                //if (idelem2.getTitle().contains("dungeon")){Log.d("ANIME SIMIL: ", String.valueOf(sim)); }

                if(idelem2.getShow_type()==FLV.get(i).getShow_type() && StringUtils.compareAnimes(idelem2.getTitle(),FLV.get(i).getTitle())){
                    FLV.get(i).setLink(FLV.get(i).getLink()+","+idelem2.getLink());
                    idelem.remove();
                }
            }
            Iterator<MiniAnime> jkelem = JK.iterator();
            while (jkelem.hasNext()){
                MiniAnime jkelem2 = jkelem.next();
                //int sim = LevenshteinDistance.getDefaultInstance().apply(jkelem2.getTitle().toUpperCase(),FLV.get(i).getTitle().toUpperCase());
                //Log.d("ANIME SIMIL: ", String.valueOf(sim));
                if(
                        //sim<3 &&
                                jkelem2.getShow_type()==FLV.get(i).getShow_type() && jkelem2.getTitle().replace(",", "").replace(":","").replace("(TV)","").replace("(","").replace(")","").toUpperCase().equals(FLV.get(i).getTitle().replace(",", "").replace(":","").replace("(TV)","").replace("(","").replace(")","").toUpperCase())){
                    FLV.get(i).setLink(FLV.get(i).getLink()+","+jkelem2.getLink());
                    jkelem.remove();
                }
            }

            builder.setContentText("Combinando links de FLV").setProgress(100, i*100/FLV.size(), false);
            mNotifyManager.notify(0, builder.build());
        }
/*
        for (MiniAnime flvelem: FLV) {
            Iterator<MiniAnime> idelem = ID.iterator();
            while (idelem.hasNext()){
                if(LevenshteinDistance.getDefaultInstance().apply(idelem.next().getTitle(),flvelem.getTitle())<5){
                    flvelem.setLink(flvelem.getLink()+","+idelem.next().getLink());
                    idelem.remove(); }
                //if(idelem.next().getTitle().equals(flvelem.getTitle())) {idelem.remove(); }
            }
            Iterator<MiniAnime> jkelem = JK.iterator();
            while (jkelem.hasNext()){
                if(LevenshteinDistance.getDefaultInstance().apply(jkelem.next().getTitle(),flvelem.getTitle())<5){
                    flvelem.setLink(flvelem.getLink()+","+jkelem.next().getLink());
                    jkelem.remove(); }
                //if(jkelem.next().getTitle().equals(flvelem.getTitle())) {jkelem.remove(); }
            }
        }


 */
        FLV.addAll(JK);
        FLV.addAll(ID);

        return FLV;
    }

    /*
    @Nullable
    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = "Application_name";
            // The user-visible description of the channel.
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;
            //            int channelLockscreenVisibility = Notification.;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            //notificationChannel.setVibrationPattern(new long[]{ 0 });
            notificationChannel.enableVibration(false);
            //            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }

     */
}