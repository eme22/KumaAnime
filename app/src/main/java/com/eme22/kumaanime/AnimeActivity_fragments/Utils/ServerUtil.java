package com.eme22.kumaanime.AnimeActivity_fragments.Utils;

import android.content.Context;
import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;
import com.eme22.kumaanime.AppUtils.NoSourceFoundException;
import com.eme22.kumaanime.AppUtils.Servers.Common.CommonServer;
import com.eme22.kumaanime.AppUtils.Servers.Fembed.MultiServer;
import com.eme22.kumaanime.AppUtils.Servers.Yu.Yu;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;

public class ServerUtil {

    public static class decode implements Runnable {

        Context context;
        Server server;
        Callback callback;

        public decode(Context context, Server server, Callback callback) {
            this.context = context;
            this.server = server;
            this.callback = callback;
        }

        @Override
        public void run() {

            noxGetterTry();

        }

        void noxGetterTry(){

            switch (server.getName()) {
                case "fembed": {
                    try {
                        MultiServer link = fembedDecoder(server.getSource());
                        callback.onSuccess(link);
                    } catch (IOException | JSONException e) {
                        callback.onError(e);
                    }
                    break;
                }
                case "gocdn": {
                    try {
                        CommonServer link = goCDNDecoder(server.getSource());
                        callback.onSuccess(link);
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                    break;
                }
                /*
                case "stape": {
                    try {
                        CommonServer link = StreamTapeDecoder(server.getSource());
                        callback.onSuccess(link);
                    } catch (IOException e) {
                        callback.onError(e);
                    }
                    break;
                }
                */
                case "yu":{
                    try {
                        Yu link = YuDecoder(server.getSource());
                        Log.d("YU HERE", link.getFile()+" "+link.getReferer());
                        callback.onSuccess(link);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError(e);
                    }
                    break;
                }
                default:
                    callback.onError(new NoSourceFoundException());
                    break;
            }
        }

    }

    private static Yu YuDecoder(String source) throws IOException {
        Pattern pattern = Pattern.compile("file: '(http.*vidcache.*mp4)'");
        Matcher matcher = pattern.matcher(Connection.getDocOk(source).outerHtml());
        if (matcher.find()){
            return new Yu(matcher.group(1),source);
        }
        else return null;
    }

    private static MultiServer fembedDecoder(String url) throws IOException, JSONException {
        Pattern pattern = Pattern.compile("(gcloud\\.live|fembed\\.com|feurl\\.com|vcdn\\.space|embedsito\\.com)/(v|api/source)/([^(?|#)]*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()) {
            for (int i = 0; i < matcher.groupCount() ; i++) {
                Log.d("MATCHER GROUPS", matcher.group(i));
            }
            Log.d("MATCH FOUND" ,url);
            String id = get_fEmbed_video_ID(url);
            Log.d("FEMBED VIDEO ID" ,id);
            Log.d("FEMBED VIDEO URL" ,"https://www.fembed.com/api/source/"+id);
            return JsonToObject(Connection.postConnection("https://www.fembed.com/api/source/"+id,new FormBody.Builder().build()), MultiServer.class);

        } else {
            Log.e("MATCH ERROR","Unable to get ID for url: "+url);
            return null;
        }
    }

    public static CommonServer goCDNDecoder(String url) throws IOException, JSONException {
        if (url.contains("https://streamium.xyz/gocdn.html#")){
            String[] a = url.split("#");
            String id = a[a.length-1];
            Log.d("URL: ",id);
            return JsonToObject(Connection.postConnection("https://streamium.xyz/gocdn.php?v="+id,new FormBody.Builder().build()), CommonServer.class);
        }
        else return null;
    }

    private static CommonServer StreamTapeDecoder(String source) throws IOException {
        Document  document = Connection.getDocOk(source);
        final String regex = "videolink['\"].+?innerHTML\\s*=\\s*['\"]([^'\"]+)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(document.html());
        String link = matcher.group(1);
        String real = Jsoup.connect(link).ignoreContentType(true).followRedirects(true).execute().url().toString();
        if (link == null || link.contains("streamtape_do_not_delete.mp4")) return null;
        return new CommonServer().withFile("https:"+link+"&stream=1");

    }

    private static String get_fEmbed_video_ID(String string){
        final String regex = "([vf])([/=])(.+)([/&])?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(3).replaceAll("[&/]","");
        }
        return null;
    }

    private static  <T>  T JsonToObject(JSONObject object, Class<T> tClass){
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());
        Gson gson = new Gson();

        return gson.fromJson(gsonObject, (Type) tClass);
    }


}


