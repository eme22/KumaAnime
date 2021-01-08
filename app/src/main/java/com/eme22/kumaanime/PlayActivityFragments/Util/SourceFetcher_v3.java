package com.eme22.kumaanime.PlayActivityFragments.Util;

import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.eme22.kumaanime.AppUtils.Connection.getDocOk;

public class SourceFetcher_v3 implements Runnable{
    Callback callback;

    ArrayList<String> servers;

    public SourceFetcher_v3(String[] animes_sources,Callback callback) {
        this.callback = callback;
        this.servers = new ArrayList<>(Arrays.asList(animes_sources));
    }

    @Override
    public void run() {
        ArrayList<Server> data = new ArrayList<>();
        for (String source: servers){
            Log.d("SOURCE", source);
        }
        for (String source: servers) {
            int type = -1;
            if (source.contains("animeid")) type = 2;
            else if (source.contains("jkanime")) type = 1;
            else if (source.contains("animeflv")) type = 0;
            Document server;
            try {
                server = getDocOk(source);
            } catch (IOException e) {
                callback.onError(e);
                return;
            }
            try {
                data.addAll(Objects.requireNonNull(getMiniServers(server, type)));
            }
            catch (NullPointerException ignored){}
        }

        data.removeAll(Collections.singleton(null));

        for (Server s: data) {
            Log.d("SERVER", s.getName()+" "+s.getSource());
        }


        callback.onSuccess(data);
    }

    private ArrayList<Server> getMiniServers(Document doc, int type) {
        ArrayList<Server> server = new ArrayList<>();
        if (type == 0){
            Elements scripts =  doc.getElementsByTag("script");
            for (Element todos: scripts){
                //Log.d("DATA", todos.outerHtml());
                Pattern pattern = Pattern.compile(".*var videos = \\{\"SUB\":([^;]*)\\};");
                Matcher matcher = pattern.matcher(todos.data());
                //Log.d("MATCHER", String.valueOf(matcher.find()));
                if (matcher.find()) {
                    JSONArray jsonArr;
                    try {
                        //Log.d("MATCH: ", matcher.group(1));
                        jsonArr = new JSONArray(matcher.group(1));
                        return convertFLVJSONtoServer(jsonArr);
                    } catch (JSONException e) {
                        callback.onError(e);
                        return null;
                    }
                }
                //else {
                 //   Log.d("DATA", "NOT MATCHED");
                    //callback.onError(new NullPointerException());
                    //return null;
                //}
            }
            return null;
        }
        else if (type == 1){
            Elements scripts =  doc.getElementsByTag("script");
            Elements a =  doc.select("td ").select("a[href]");
            for (Element todos : scripts) {
                if(todos.data().contains("var video = []")){
                    Pattern pattern = Pattern.compile("video\\[[0-9]\\] = '(.*)'");
                    Matcher matcher = pattern.matcher(todos.data());
                    while (matcher.find()){
                        //Log.d("MATCHED", matcher.group(1));
                        String link = Jsoup.parse(matcher.group(1)).getElementsByTag("iframe").first().attr("src");
                        try {
                            Server link2 = decodeJK(link);
                            server.add(link2);
                        }
                        catch (NullPointerException ignored){ }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
            }
            for (Element links: a) {
                try {
                    Server link = decodeJK(links.attr("href"));
                    server.add(link);
                }
                catch (IOException e){e.printStackTrace();}
                catch (NullPointerException ignored){}

            }

            //for (Server s: server) {
            //    Log.d("SERVER", s.getName()+" "+s.getSource());
            //}

            return server;
        }
        else if (type == 2){
            Log.d("DATA ID:", doc.html());
            Elements elements = doc.getElementsByClass("subtab");
            for (Element element: elements) {
                String source = element.select("div").attr("src").replace("\\u0022","").replace("\\","");
                Log.d("Source" , source);
                try {
                    Server link2 = decodeID(source);
                    server.add(link2);
                }
                catch (NullPointerException ignored){ }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return server;

        }
        else return null;
    }

    /*
    private ArrayList<Server> getMiniServers(Document doc, int type) {
        if (type == 0){
            Elements scripts =  doc.getElementsByTag("script");
            for (Element todos : scripts) {
                if (todos.data().contains("var videos =")) {
                    //Log.d("DATA", todos.html());
                    Pattern pattern = Pattern.compile(".*var videos = \\{\"SUB\":([^;]*)\\};");
                    Matcher matcher = pattern.matcher(todos.data());
                    Log.d("MATCHER", String.valueOf(matcher.find()));
                    if (matcher.find()) {
                        JSONArray jsonArr = null;
                        try {
                            jsonArr = new JSONArray(matcher.group(1));
                            return convertFLVJSONtoServer(jsonArr);
                        } catch (JSONException e) {
                            callback.onError(e);
                            return null;
                        }

                    }
                    else {
                        Log.d("DATA", "NOT MATCHED");
                        callback.onError(new NullPointerException());
                        return null;
                    }
                }
                else return null;
            }
        }
        else if (type == 1){
            ArrayList<Server> server = new ArrayList<>();
            Elements scripts =  doc.getElementsByTag("script");
            Elements a =  doc.select("td ").select("a[href]");
            for (Element todos : scripts) {
                if(todos.data().contains("var video = []")){
                    Pattern pattern = Pattern.compile("video\\[[0-9]\\] = '(.*)'");
                    Matcher matcher = pattern.matcher(todos.data());
                    if (matcher.find()){
                        Log.d("MATCHED", String.valueOf(matcher.groupCount()));
                        for (int j = 1; j < matcher.groupCount() ; j++) {
                            String link = Jsoup.parse(matcher.group(j)).getElementsByTag("iframe").first().attr("src");
                            try {
                                Server link2 = decodeJK(link);
                                server.add(link2);
                            }
                            catch (NullPointerException ignored){
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
            for (Element links: a) {
                try {
                    Server link = decodeJK(links.attr("href"));
                    server.add(link);
                }
                catch (IOException e){e.printStackTrace();}
                catch (NullPointerException ignored){}

            }
            return server;
        }
        else return null;
        return null;
    }

    /*
    private ArrayList<Server> getMiniServers(Document Doc, int i) {
        switch (i){
            default: return null;
            case 0:{
                Elements scripts =  Doc.getElementsByTag("script");
                for (Element todos : scripts) {
                    if (todos.data().contains("var videos =")) {
                        //Log.d("DATA", todos.html());
                        Pattern pattern = Pattern.compile(".*var videos = \\{\"SUB\":([^;]*)\\};");
                        Matcher matcher = pattern.matcher(todos.data());
                        Log.d("MATCHER", String.valueOf(matcher.find()));
                        if (matcher.find()) {
                            JSONArray jsonArr = null;
                            try {
                                jsonArr = new JSONArray(matcher.group(1));
                                return convertFLVJSONtoServer(jsonArr);
                            } catch (JSONException e) {
                                callback.onError(e);
                                return null;
                            }

                        }
                        else {
                            Log.d("DATA", "NOT MATCHED");
                            callback.onError(new NullPointerException());
                            return null;
                        }
                    }
                    break;
                }
            }
            case 1: {
                ArrayList<Server> server = new ArrayList<>();
                Elements scripts =  Doc.getElementsByTag("script");
                Elements a =  Doc.select("td ").select("a[href]");
                for (Element todos : scripts) {
                    if(todos.data().contains("var video = []")){
                        Pattern pattern = Pattern.compile("video\\[[0-9]\\] = '(.*)'");
                        Matcher matcher = pattern.matcher(todos.data());
                        if (matcher.find()){
                            Log.d("MATCHED", String.valueOf(matcher.groupCount()));
                            for (int j = 1; j < matcher.groupCount() ; j++) {
                                String link = Jsoup.parse(matcher.group(j)).getElementsByTag("iframe").first().attr("src");
                                try {
                                    Server link2 = decodeJK(link);
                                    server.add(link2);
                                }
                                catch (NullPointerException | IOException e){e.printStackTrace();}
                            }

                        }
                        break;
                    }
                }
                for (Element links: a) {
                    try {
                        Server link = decodeJK(links.attr("href"));
                        server.add(link);
                    }
                    catch (IOException e){e.printStackTrace();}
                    catch (NullPointerException ignored){}

                }
                return server;
            }

            case 2: return null;


        }
    }

     */

    private Server decodeID(String link) throws IOException {
        if (link.contains("netu.php")){
            link = Jsoup.parse(new URL(link),3000).select("iframe").attr("src");
            return new Server().withSource(link).withName("netu");
        }
        else if (link.contains("streamtape.com")){
            return new Server().withSource(link).withName("stape");
        }
        else if (link.contains("animeid.tv/?vid=")){
            link = Jsoup.parse(new URL(link),3000).select("IFRAME").attr("src");
            return new Server().withSource(link).withName("clipwatching.com");
        }
        else return null;
    }

    private Server decodeJK(String link) throws IOException {
        if (link.contains("mega.nz")){
            Log.d("NEW LINK: ", link);
            return new Server().withName("Mega").withSource(link);}
        else if (link.contains("zippyshare.com")){
            Log.d("NEW LINK: ", link);
            return new Server().withName("ZippyShare").withSource(link);
        }
        else if (link.contains("um.php")){
            Log.d("NEW LINK: ", link);
            Elements document = Connection.getDocOk(link).getElementsByTag("script");
            Pattern pattern = Pattern.compile("swarmId: '(.*)'");
            Matcher matcher = pattern.matcher(document.html());
            if (matcher.find()){
                link = matcher.group(1);
                Log.d("NEW LINK: ", link);
                return new Server().withName("UM").withSource(link);
            }
            else {
                //callback.onError(new NoSourceFoundException());
                return null;
            }

        }
        else if (link.contains("jkfembed.php")){
            Document document = Connection.getDocOk(link);
            link = document.select("iframe").first().attr("src");
            return new Server().withName("fembed").withSource(link);
        }
        else if (link.contains("jkokru.php")){
            Document document = Connection.getDocOk(link);
            link = document.select("iframe").first().attr("src");
            return new Server().withName("OKRu").withSource(link);
        }
        else if (link.contains("jkvmixdrop.php")){
            Document document = Connection.getDocOk(link);
            link = "https:"+ document.select("iframe").first().attr("src");
            return new Server().withName("MixDrop").withSource(link);
        }
        else if (link.contains("jkmedia")){
            Document document = Connection.getDocOk(link);
            link = document.select("video > source").first().attr("src");
            return new Server().withName("JKMedia").withSource(link);
        }
        else {
            //callback.onError(new NoSourceFoundException());
            return null;
        }

    }

    private ArrayList<Server> convertFLVJSONtoServer(JSONArray jsonArr) {
        Log.d("WORKING HERE", "");
        ArrayList<Server> servers = new ArrayList<>();
        for (int i = 0; i < jsonArr.length() ; i++) {
            Server server = new Server();
            JSONObject serverJSON;
            try {
                serverJSON = (JSONObject) jsonArr.get(i);
                server.setName(serverJSON.getString("server"));
                server.setSource(serverJSON.getString("code"));
                Log.d("SERVER: ", serverJSON.getString("server") + " "+ serverJSON.getString("code"));
            } catch (JSONException e) {
                callback.onError(e);
                return null;
            }
            servers.add(server);
        }
        return servers;
    }
}
