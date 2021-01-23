package com.eme22.kumaanime.AppUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;

import androidx.annotation.Nullable;

import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.Common.MainPicture;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.models.MiniAnime;
import com.eme22.kumaanime.AppUtils.AnimeObjects.episodes.MiniEpisode;
import com.eme22.kumaanime.R;
import com.eme22.kumaanime.Services.MALSessionJobIntentService;
import com.google.android.material.snackbar.Snackbar;
import com.tingyik90.prefmanager.PrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


public class Connection {  private static final String USER_AGENT = "Mozilla/5.0";
private static final String baseurl = "https://www3.animeflv.net";

    static ArrayList<String> urlList;

    private static final OkHttpClient okHttp = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private static PrefManager prefs;
    private static SharedPreferences prefsenc;
    private static final String AUTH_ERROR_IDENTIFIER = "invalid_token";

    public static <T> T getConnection(String url) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("URL", String.valueOf(request.url()));
        Response response = okHttp.newCall(request).execute();
        //Log.d("RESPONSE ", response.body().string());
        if (response.isSuccessful()) {
            String responsebody = Objects.requireNonNull(response.body()).string();
            if (responsebody.startsWith("[")) {
                return (T) new JSONArray(responsebody);
            } else {
                return (T) new JSONObject(responsebody);
            }
        }
        else return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T postConnection(String url , RequestBody rs) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(url)
                .post(rs)
                .build();
        //Log.d("URL", String.valueOf(request.url()));
        Response response = okHttp.newCall(request).execute();
        //Log.d("RESPONSE ", response.body().string());
        if (response.isSuccessful()) {
            String responsebody = Objects.requireNonNull(response.body()).string();
            if (responsebody.startsWith("[")) {
                return (T) new JSONArray(responsebody);
            } else {
                return (T) new JSONObject(responsebody);
            }
        }
        else return null;

    }

    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static void postConnection(String url , RequestBody rs, Callback callback) throws JSONException {
        Request request = new Request.Builder()
                .url(url)
                .post(rs)
                .build();
        okHttp.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.onSuccess(response.body());
            }
        });
    }


    public static String gettoken(Context context){
        JSONObject creds = getUserCredentials(context);
        Log.d("DATA", creds.toString());
        String token = gettoken2(creds);



        if (check_mal_session(context)) {
            return token;
        }
        else {
            String token2 = token;
            Intent intent = new Intent(context, MALSessionJobIntentService.class);
            intent.putExtra(MALSessionJobIntentService.ACTION, MALSessionJobIntentService.ACTION_3);
            MALSessionJobIntentService.enqueueWork(context,intent);
            if (!token2.equals(token)) return token;
            else {Log.e("ERROR MIO: ","ME DOY"); return null; }
        }
    }

    private static String gettoken2 (JSONObject creds) {

        try {
            return creds.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static JSONObject getUserCredentials(Context context){
        prefsenc = new ObscuredPreferences(context, context.getSharedPreferences("MAL_USERDATA", 0) );
        String userdata = prefsenc.getString("USERJSON", null);
        JSONObject userdata2 = null;
        try {
            userdata2 = new JSONObject(userdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userdata2;
    }



    public static Document getDocOk(String site) throws IOException {
        Log.d("EPISODE LINK:", site);
        Request request2 = new Request.Builder().url(site).get().build();
        Document doc = null;
        doc = Jsoup.parse(Objects.requireNonNull(okHttp.newCall(request2).execute().body()).string());

        return doc;
    }

    public static Document getDocOkWithCustomClient(OkHttpClient client,String site) throws IOException {
        Request request2 = new Request.Builder().url(site).build();
        Document doc = null;
        doc = Jsoup.parse(Objects.requireNonNull(client.newCall(request2).execute().body()).string());

        return doc;
    }


    public static String getCookie(String siteName,String cookieName){
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains(cookieName)){
                String[] temp1=ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }

    public static ArrayList<MiniEpisode> geteps(Document document,int type, MiniAnime anime, String location){
        ArrayList<MiniEpisode> episodes = new ArrayList<>();
        switch (type){
            default: return episodes;
            case 0:{
                Elements script = document.getElementsByTag("script");
                for (Element scripts : script) {
                    if (scripts.data().contains("var episodes = ")) {
                        //Log.d("DOCUMENT", scripts.data());
                        //Pattern pattern = Pattern.compile(".*var anime_info = \\[\"([^,]*)\".*");
                        Pattern pattern = Pattern.compile("var anime_info = \\[\\\"(.*?)\\\"");
                        Matcher matcher = pattern.matcher(scripts.data());
                        String internalid = null;
                        if (matcher.find()) internalid = matcher.group(1);
                        else break;
                        pattern = Pattern.compile("var episodes = \\[(.*)\\];");
                        matcher = pattern.matcher(scripts.data());
                        if (matcher.find()) {
                            String[] data = Objects.requireNonNull(matcher.group(1)).split("\\](,)\\[");
                            for (String episode: data) {
                                String[] spisode = episode.replace("]", "").replace("[","").split(",");
                                Log.d("EPISODES SCRAP: ", String.valueOf(spisode.length));
                                if (!(spisode.length == 2)) {
                                    Log.e("EPISODES SCRAP: ","EPISODES NOT SAME");
                                    break;
                                }
                                MiniEpisode temp = new MiniEpisode();
                                temp.setAnimeID(anime.getId());
                                temp.setName(anime.getTitle());
                                temp.setEpisode(spisode[0]);
                                temp.setLink(location.replace("/anime/", "/ver/")+"-"+Integer.parseInt(spisode[0]));
                                MainPicture temp2 = new MainPicture();
                                temp2.setMedium(episodeToImage(internalid, Integer.parseInt(spisode[0])));
                                temp.setMainPicture(temp2);
                                episodes.add(temp);

                            }
                            break;
                        } else {
                            Log.e("EPISODE SCRAPPER: ", "No match found!");
                            return episodes;
                        }
                    }
                    else Log.e("EPISODE SCRAPPER: ", "No data found!");

                }
                return episodes;
            }
            case 1: {
                Elements scripts = document.getElementsByTag("script");
                for (Element script: scripts) {
                    Pattern pattern = Pattern.compile("ajax\\/pagination_episodes\\/(.*?)\\/");
                    Matcher matcher = pattern.matcher(script.data());
                    if (matcher.find()){
                        int i = 1;
                        while (true){
                            try {
                                JSONArray animes = Connection.getConnection("https://jkanime.net/index.php/ajax/pagination_episodes/"+matcher.group(1)+"/"+i);
                                if (animes != null) {
                                    if (animes.length()== 0) break;
                                    for (int j = 0; j < animes.length() ; j++) {

                                        JSONObject animes2 = animes.getJSONObject(j);
                                        Log.d("LENGTH", String.valueOf(animes2.length()));
                                        MiniEpisode temp = new MiniEpisode();
                                        String number = animes2.getString("number");
                                        temp.setAnimeID(anime.getId());
                                        temp.setName(anime.getTitle());
                                        temp.setEpisode(number);
                                        temp.setLink(location+number+"/");
                                        Log.d("LINK JK", document.location()+number+"/");
                                        MainPicture temp2 = new MainPicture();
                                        temp2.setMedium(anime.getMainPicture().getMedium());
                                        temp.setMainPicture(temp2);
                                        episodes.add(temp);

                                    }

                                }
                                else break;

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            i++;
                        }
                        break;
                    }
                }

                return episodes;
            }
            case 2:{
                Elements items = document.select("ul#listado li");
                for (Element item: items) {
                    String episode = item.select("strong").text().replace("Capítulo ", "");
                    Log.d("EPISODE", episode);
                    MiniEpisode temp = new MiniEpisode();
                    temp.setAnimeID(anime.getId());
                    temp.setName(anime.getTitle());
                    temp.setEpisode(episode);
                    temp.setLink("https://www.animeid.tv"+item.select("a").attr("href"));
                    MainPicture temp2 = new MainPicture();
                    temp2.setMedium(anime.getMainPicture().getMedium());
                    temp.setMainPicture(temp2);
                    episodes.add(temp);
                }
                return episodes;
            }
        }
    }


    public static ArrayList<MiniEpisode> geteps(Document document, int type, int animeid, String animelink){
        ArrayList<MiniEpisode> episodes = new ArrayList<>();
        switch (type) {
            default: return null;
            case 0: {
                Elements script = document.getElementsByTag("script");
                for (Element scripts : script) {

                    if (scripts.data().contains("var episodes = ")) {
                        Pattern pattern = Pattern.compile(".*var anime_info = \\[\"([^,]*)\".*");
                        Matcher matcher = pattern.matcher(scripts.data());
                        String internalid = matcher.group(1);
                        pattern = Pattern.compile(".*var episodes = \\[(.*)\\];");
                        matcher = pattern.matcher(scripts.data());
                        if (matcher.find()) {
                            String[] data = Objects.requireNonNull(matcher.group(1)).split("\\](,)\\[");
                            for (String episode: data) {
                                String[] spisode = episode.replace("]", "").replace("[","").split(",");
                                Log.e("EPISODES SCRAP: ", String.valueOf(spisode.length));
                                if (!(spisode.length == 2)) {
                                    Log.e("EPISODES SCRAP: ","EPISODES NOT SAME");
                                    break;
                                }
                                MiniEpisode temp = new MiniEpisode();
                                temp.setAnimeID(animeid);
                                temp.setEpisode(spisode[0]);
                                temp.setLink(animeLinktoEpisode(animelink, Integer.parseInt(spisode[0])));
                                MainPicture temp2 = new MainPicture();
                                temp2.setMedium(episodeToImage(internalid, Integer.parseInt(spisode[0])));
                                temp.setMainPicture(temp2);

                                episodes.add(temp);

                            }
                        } else {
                            Log.e("EPISODE SCRAPPER: ", "No match found!");
                        }

                        return episodes;
                    }
                }
            }
            case 1:{
                return null;
            }
            case 2:{
                return null;
            }
        }

    }

    public static ArrayList<MiniEpisode> getneweps(Document doc, int type){
        ArrayList<MiniEpisode> urlList = new ArrayList<>();
        switch (type){
            case 0: {
                String baseurl = "https://www3.animeflv.net";
                Elements eles = doc.select("ul.ListEpisodios ").select("a[href]");
                int i = 0;
                for (Element ele : eles) {
                    MiniEpisode episode = new MiniEpisode();
                    MainPicture picture = new MainPicture();
                    String actualUrl = baseurl + ele.attr("href");
                    String image = baseurl + ele.select("img").attr("src");
                    String episode_num = ele.select("span.Capi").text();
                    if (episode_num.contains("Episodio ")) episode_num = episode_num.replace("Episodio ","");
                    if (episode_num.equals("")) episode_num = "1";
                    picture.setMedium(image);
                    String  title = ele.getElementsByClass("Title").text();
                    episode.setEpisode(episode_num);
                    episode.setLink(actualUrl);
                    episode.setMainPicture(picture);
                    episode.setName(title);
                    Log.d("NEW ANIME FLV EPS: ", actualUrl+" "+title+" "+episode_num+" "+ image);
                    if (!urlList.contains(episode) && (actualUrl.contains("/ver/"))){
                        urlList.add(episode);
                        i++;
                    }
                    if (i>15) break;
                }
                break;
            }
            case 1: {
                String baseurl = "https://jkanime.net/";
                Elements eles = doc.select("div#programacion").select("a[href]");
                int i = 0;
                for (Element ele : eles) {
                    MiniEpisode episode = new MiniEpisode();
                    MainPicture picture = new MainPicture();
                    String actualUrl = ele.attr("href");
                    String image =  ele.select("img").attr("src");
                    String title = ele.select("img").attr("title");
                    String episode_num = ele.getElementsByClass("episode").text().replace(" ", "");
                    if (episode_num.contains("Episodio")) episode_num = episode_num.replace("Episodio","");
                    if (episode_num.equals("Final")) episode_num = StringUtils.getEpisodeFromJK(actualUrl);
                    if (episode_num.equals("")) episode_num = "1";
                    episode.setName(title);
                    picture.setMedium(image);
                    episode.setMainPicture(picture);
                    episode.setLink(actualUrl);
                    episode.setEpisode(episode_num);
                    Log.d("NEW ANIME JK EPS: ", actualUrl+" "+title+" "+episode_num+" "+ image);
                    if (!urlList.contains(episode) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length())){
                        urlList.add(episode);
                        i++;
                    }
                    if (i>15) break;
                }
                break;
            }
            case 2: {
                String baseurl = "https://www.animeid.tv";
                Elements eles = doc.select("section.lastcap").select("a[href]");
                int i = 0;
                for (Element ele : eles) {
                    MiniEpisode episode = new MiniEpisode();
                    MainPicture picture = new MainPicture();

                    String actualUrl = baseurl+ele.attr("href");
                    String image =  ele.select("img").attr("src");
                    String title = ele.select("img").attr("alt");
                    String episode_num = ele.select("header").text();
                    episode_num = episode_num.split("#")[1];
                    if (episode_num.equals("")) episode_num = "1";

                    episode.setName(title);
                    picture.setMedium(image);
                    episode.setMainPicture(picture);
                    episode.setLink(actualUrl);
                    episode.setEpisode(episode_num);

                    Log.d("NEW ANIME ID EPS: ", actualUrl+" "+title+" "+episode_num+" "+ image);
                    if (!urlList.contains(episode) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length())){
                        urlList.add(episode);
                        i++;
                    }
                    if (i>15) break;
                }
                break;
            }
        }
        return urlList;
    }

    public static ArrayList<MiniAnime> getnewanimes(Document doc, int type){
        ArrayList<MiniAnime> urlList = new ArrayList<>();
        switch (type){
            case 0: {
                String baseurl = "https://www3.animeflv.net";
                Elements eles = doc.select("ul.ListAnimes ").select("a[href]");
                for (Element ele : eles) {
                    MiniAnime actualanime = new MiniAnime();
                    MainPicture actualanimemainpicture = new MainPicture();
                    String actualUrl = baseurl + ele.attr("href");
                    String title = ele.select("h3.Title").text();
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String image = baseurl +  ele.select("img").attr("src");
                    String type2 = ele.select("span.Type ").text();
                    if (type2.contains("Anime")) actualanime.setShow_type(0);
                    else if (type2.contains("Película")) actualanime.setShow_type(1);
                    else if (type2.contains("OVA")) actualanime.setShow_type(2);
                    else if (type2.contains("Especial")) actualanime.setShow_type(3);
                    if (title.contains("Latino")) actualanime.setShow_type(5);
                    actualanime.setTitle(title);
                    actualanimemainpicture.setMedium(image);
                    actualanime.setMainPicture(actualanimemainpicture);
                    actualanime.setLink(actualUrl);
                    if (!urlList.contains(actualanime) && (actualUrl.contains("/anime/")) && (!title.equals("") && image.length()> baseurl.length())){
                        urlList.add(actualanime);
                        //Log.d("NEW ANIME FLV ANIMES: ", actualUrl+", "+title+", "+image);
                    }
                }
                break;
            }
            case 1: {
                String baseurl = "https://jkanime.net/";
                Elements eles = doc.select("div.content-box").select("a[href]");
                for (Element ele : eles) {
                    MiniAnime actualanime = new MiniAnime();
                    MainPicture actualanimemainpicture = new MainPicture();
                    String actualUrl = ele.attr("href");
                    String title = ele.attr("title");
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String image = ele.select("img").attr("src");
                    String typea = ele.select("span.eps-num").text();
                    if (typea.contains("Serie")) actualanime.setShow_type(0);
                    else if (typea.contains("Pelicula")) actualanime.setShow_type(1);
                    else if (typea.contains("OVA") || typea.contains("ONA")) actualanime.setShow_type(2);
                    else if (typea.contains("Especial")) actualanime.setShow_type(3);
                    if (title.contains("Latino")) actualanime.setShow_type(5);
                    actualanime.setTitle(title);
                    actualanimemainpicture.setMedium(image);
                    actualanime.setMainPicture(actualanimemainpicture);
                    actualanime.setLink(actualUrl);
                    if (!urlList.contains(actualanime) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length()) && (!title.equals("") && image.length()> baseurl.length())){
                        urlList.add(actualanime);
                        //Log.d("NEW ANIME JK EPS: ", actualUrl+", "+title+", "+image);
                    }
                }
                break;
            }
            case 2: {
                //System.out.println(doc.body().toString());
                String baseurl = "https://www.animeid.tv";
                Elements eles = doc.getElementsByClass("emision tab").select("a[href]");
                //Elements eles = doc.select("ol.emision tab").select("a[href]");
                for (Element ele : eles) {
                    //Log.d("ELEMENT:", ele.toString());
                    MiniAnime actualanime = new MiniAnime();
                    String actualUrl = baseurl + ele.attr("href");
                    String title = ele.select("strong").text();
                    if (title.length() >= 47) title = convertepisode(ele.attr("href"));
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String typea = ele.select("span.tipo").text();
                    if (typea.contains("Serie")) actualanime.setShow_type(0);
                    else if (typea.contains("Peli")) actualanime.setShow_type(1);
                    else if (typea.contains("OVA") || typea.contains("ONA")) actualanime.setShow_type(2);
                    else if (typea.contains("Especial")) actualanime.setShow_type(3);
                    if (title.contains("Latino")) actualanime.setShow_type(5);
                    actualanime.setTitle(title);
                    actualanime.setLink(actualUrl);
                    if (!urlList.contains(actualanime) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length()) && !title.equals("")){
                        //Log.d("NEW ANIME ID EPS: ", actualUrl+", "+title);
                        urlList.add(actualanime);
                    }
                }
                break;
            }
        }
        return urlList;
    }

    public static void getanimes(Document doc, int type, ArrayList<MiniAnime> animes, int subtype){
        switch (type){
            case 0: {
                String baseurl = "https://www3.animeflv.net";
                Elements eles = doc.select("ul.ListAnimes ").select("a[href]");
                for (Element ele : eles) {
                    MiniAnime actualanime = new MiniAnime();
                    MainPicture actualanimemainpicture = new MainPicture();
                    String actualUrl = baseurl + ele.attr("href");
                    String title = ele.select("h3.Title").text();
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String image = ele.select("img").attr("src");
                    String type2 = ele.select("span.Type ").text();
                    if (type2.contains("Anime")) actualanime.setShow_type(0);
                    else if (type2.contains("Película")) actualanime.setShow_type(1);
                    else if (type2.contains("OVA")) actualanime.setShow_type(2);
                    else if (type2.contains("Especial")) actualanime.setShow_type(3);
                    if (title.contains("Latino")) actualanime.setShow_type(5);
                    actualanime.setTitle(title);
                    actualanimemainpicture.setMedium(image);
                    actualanime.setMainPicture(actualanimemainpicture);
                    actualanime.setLink(actualUrl);
                    boolean part1 = !animes.contains(actualanime);
                    boolean part2 = actualUrl.contains("/anime/");
                    boolean part3 = !title.equals("");
                    boolean part4 = image.length()> baseurl.length();
                    if (part1 && part2 && part3 && part4) {
                        animes.add(actualanime);
                    }
                }
                break;
            }
            case 1: {
                String baseurl = "https://jkanime.net/";
                Elements eles = doc.select("div.content-box").select("div.portada-box");
                for (Element ele : eles) {
                    MiniAnime actualanime = new MiniAnime();
                    MainPicture actualanimemainpicture = new MainPicture();
                    Element a = ele.select("a").first();
                    String actualUrl = a.attr("href");
                    String title = a.attr("title");
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String image = ele.select("img").attr("src");
                    String typea = ele.select("span.eps-num").text();
                    if (typea.contains("Serie") || typea.contains("ONA")) actualanime.setShow_type(0);
                    else if (typea.contains("Pelicula")) actualanime.setShow_type(1);
                    else if (typea.contains("OVA")) actualanime.setShow_type(2);
                    else if (typea.contains("Especial")) actualanime.setShow_type(3);
                    if (title.contains("Latino")) actualanime.setShow_type(5);

                    actualanime.setTitle(title);
                    actualanimemainpicture.setMedium(image);
                    actualanime.setMainPicture(actualanimemainpicture);
                    actualanime.setLink(actualUrl);
                    //Log.d("ELEMENT: ", actualUrl+", "+title+", "+image);
                    if (!animes.contains(actualanime) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length()) && (!title.equals("") && image.length()> baseurl.length())){
                        animes.add(actualanime);
                        //Log.d("NEW ANIME JK EPS: ", actualUrl+", "+title+", "+image+", "+typea+", "+actualanime.getShow_type());
                    }
                }
                break;
            }
            case 2: {
                //System.out.println(doc.body().toString());
                String baseurl = "https://www.animeid.tv";
                Elements eles = doc.select("section#result > article.item");
                for (Element ele : eles) {
                    //Log.d("ELEMENT:", ele.toString());
                    MiniAnime actualanime = new MiniAnime();
                    String actualUrl = baseurl + ele.select("a").attr("href");
                    String title = ele.select("header").text();
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String image = ele.select("img").attr("src");
                    switch (subtype) {
                        default:
                        case 0:
                            actualanime.setShow_type(0);
                            break;
                        case 1:
                            actualanime.setShow_type(1);
                            break;
                        case 2:
                            actualanime.setShow_type(2);
                            break;
                    }
                    if (title.contains("Latino")) actualanime.setShow_type(5);
                    actualanime.setTitle(title);
                    MainPicture actualanimemainpicture = new MainPicture();
                    actualanimemainpicture.setMedium(image);
                    actualanime.setMainPicture(actualanimemainpicture);
                    actualanime.setLink(actualUrl);
                    if (!animes.contains(actualanime) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length()) && title != null){
                        animes.add(actualanime);
                        //Log.d("NEW ANIME ID EPS: ", actualUrl+", "+title+", "+image);
                    }
                }
                break;
            }
            case 3: {
                String baseurl = "https://animeflv.ac/anime/";

                Elements eles = doc.select("div.mosaicos_layout > article.Anime");
                for (Element ele: eles) {
                    MiniAnime actualanime = new MiniAnime();
                    String actualUrl = ele.select("a").attr("href");
                    String image = ele.select("img").attr("src");
                    String title = ele.select("figure").attr("title");
                    if (title.contains("(TV)")) title = title.replace("(TV)","");
                    String type2 = ele.select("span.Type ").attr("class");
                    if (type2.contains("tv")) actualanime.setShow_type(0);
                    else if (type2.contains("Pelicula")) actualanime.setShow_type(1);
                    else if (type2.contains("OVA") || type2.contains("ONA")) actualanime.setShow_type(2);
                    else if (type2.contains("Especial")) actualanime.setShow_type(3);
                    actualanime.setTitle(title);
                    MainPicture actualanimemainpicture = new MainPicture();
                    actualanimemainpicture.setMedium(image);
                    actualanime.setMainPicture(actualanimemainpicture);
                    actualanime.setLink(actualUrl);
                    if (!animes.contains(actualanime) && (actualUrl.contains(baseurl)) && (actualUrl.length() > baseurl.length()) && title != null){
                        animes.add(actualanime);
                    }
                }
                break;
            }
        }
    }


    public static ArrayList<String> scrape_newp2(Document doc, int type){
        urlList = new ArrayList<>();
        int length = 0;
        int end = 0;
        boolean esanime = false;
        if (type == 1) {end = 30; length =26;}
        else if (type == 2) {end = 60; length =24;}
        else if (type == 3) {end = 150; length =23;}
        else if (type ==4) {end = 88; length =20;}

        Elements eles = doc.select("ul.ListAnimes ").select("a[href]");
        for (Element ele : eles){

            String actualUrl = baseurl+ele.attr("href");

            if (type == 3) {
                esanime = (!urlList.contains(actualUrl)) && !(actualUrl.contains("/letra/") || actualUrl.contains("/genero/") || actualUrl.contains("/especiales") || actualUrl.contains("facebook") || actualUrl.contains("youtube") || actualUrl.contains("google.com") || actualUrl.contains("/series") || actualUrl.contains("/ovas") || actualUrl.contains("/peliculas")) && !(actualUrl.length() <= length);
            } else if (type == 4) {
                esanime = (!urlList.contains(actualUrl)) && !(actualUrl.contains("/letra/") || actualUrl.contains("/genero/") || actualUrl.contains("/terminos") || actualUrl.contains("discord.gg") || actualUrl.contains("/horario/") || actualUrl.contains("tipo")) && !(actualUrl.length() <= length);
            } else {
                esanime = (!urlList.contains(actualUrl)) && (actualUrl.contains("/anime/") && !(actualUrl.length() <= length));
            }

            if (esanime) {
                urlList.add(actualUrl);
            }

        }

        return urlList;
    }


    public static  JSONArray scrape_newep(Document doc,int type){

        JSONArray nuevosepisodios = new JSONArray();
        try {
            Elements links = doc.select("a.fa-play");
            int i = 1;
            for (Element todos : links) {
                JSONObject anime = new JSONObject();
                String link = todos.attr("href");
                String image = todos.select("img").first().attr("src");
                String number = convernumber(todos.getElementsByClass("Capi").first().text());
                String title = todos.getElementsByClass("Title").first().text();
                String anime_ref = convertepisode(link);
                anime.put("ref",baseurl+anime_ref);
                anime.put("link",baseurl+link);
                anime.put("image",baseurl+image);
                anime.put("number",number);
                anime.put("title",title);
                nuevosepisodios.put(anime);
            }

            return nuevosepisodios;

        }  catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList<String> scrape(Document doc, int type){
        urlList = new ArrayList<>();
        int length = 0;
        int end = 0;

        if (type == 1) {end = 30; length =26;}
        else if (type == 2) {end = 60; length =24;}
        else if (type == 3) {end = 150; length =23;}
        else if (type ==4) {end = 88; length =20;}
            Elements links = doc.select("a[href]");
            boolean esanime;
            for (Element todos : links) {
                if (links.size() <= end) {
                    return null;
                }
                else {
                    String actualUrl = baseurl+todos.attr("href");

                    if (type == 3) {
                        esanime = (!urlList.contains(actualUrl)) && !(actualUrl.contains("/letra/") || actualUrl.contains("/genero/") || actualUrl.contains("/especiales") || actualUrl.contains("facebook") || actualUrl.contains("youtube") || actualUrl.contains("google.com") || actualUrl.contains("/series") || actualUrl.contains("/ovas") || actualUrl.contains("/peliculas")) && !(actualUrl.length() <= length);
                    } else if (type == 4) {
                        esanime = (!urlList.contains(actualUrl)) && !(actualUrl.contains("/letra/") || actualUrl.contains("/genero/") || actualUrl.contains("/terminos") || actualUrl.contains("discord.gg") || actualUrl.contains("/horario/") || actualUrl.contains("tipo")) && !(actualUrl.length() <= length);
                    } else {
                        esanime = (!urlList.contains(actualUrl)) && (actualUrl.contains("/anime/") && !(actualUrl.length() <= length));
                    }

                    if (esanime && !urlList.contains(actualUrl)) {
                        urlList.add(actualUrl);
                    }
                }
            }

        return urlList;
    }

    private static String convertepisode(String str) {
        while (str != null && str.length() > 0 && str.charAt(str.length() - 1) != '-'){
            Log.d("EPISODEA???", str);
            str = str.replace("/", "");
            str = str.substring(0, str.length() - 1);
            Log.d("EPISODEB???", str);
        }
        return str;
    }

    private static boolean check_mal_session(Context context) {
        prefs = new PrefManager(context);
        long current = System.currentTimeMillis();
        long next_inlong = Long.parseLong(prefs.getString("expiration", null));
        long diff = next_inlong - current;
        Log.d("MAL EXPIRE: ", String.valueOf(diff));
        return diff > 0;
    }

    private static String convernumber(String str){
return str.replaceAll("[^0-9.]", "");
    }

    private static String animeLinktoEpisode(String link, int episode){
        return link.replace("https://www3.animeflv.net/anime/", "https://www3.animeflv.net/ver/")+"-"+episode;
    }

    private static String episodeToImage(String internal_id, int episode){
        return "https://cdn.animeflv.net/screenshots/"+internal_id+"/"+episode+"/th_3.jpg";
    }

    public static void hideError(Snackbar snackbar){
        //errorText.setVisibility(View.GONE);
        if(snackbar!=null&&snackbar.isShown())snackbar.dismiss();
        snackbar=null;
    }

    @SuppressLint("ShowToast")
    public static void showError(View masterLayout, Snackbar snackbar, @Nullable String text, @Nullable View.OnClickListener listener){
        if(text==null){
            hideError(snackbar);
            return;
        }
        if(listener==null){
            snackbar = Snackbar.make(masterLayout,text,Snackbar.LENGTH_SHORT);
        }else{
            snackbar = Snackbar.make(masterLayout,text,Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.retry, listener);
        }
        snackbar.show();
    }



}