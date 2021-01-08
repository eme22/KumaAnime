package com.eme22.kumaanime.AppUtils.AnimeObjects;

import androidx.room.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JKAnime_Object extends BaseAnime_Object {

    @Ignore
    public static JSONObject data;
    @Ignore
    public static JSONArray data2;
    public int internalid;

    public JKAnime_Object(String url) throws IOException {
        super(url);
        data = getData(maindoc);
        data2 = getEpisodesData();
        this.title = getTitle();
        this.description = getDescription();
        this.prefix = getPrefix();
        this.episodelist=getEpisodes();
        this.episodes=getEpisodesNumber();
        this.image=getImage();
        this.miniatures=getImage2();
        this.type=getType();
        this.internalid=getinternalid();
    }

    private String getType() {
        String r = null;
        try {
            r = data.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    private int getinternalid() {
        Integer r = null;
        try {
            r = data.getInt("internalid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    private JSONArray getImage2() {
        JSONArray image = new JSONArray();
        for (int i = 1;i<=getEpisodesNumber();i++){
            String url = null;
            try {
                url = data.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            image.put(url);
        }
        return image;
    }

    private String getImage() {
        String r = null;
        try {
            r = data.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    private JSONObject getEpisodes() {
        try {
            JSONObject episodes = new JSONObject();
            for(int i = 1; i<=getEpisodesNumber();i++){
                String epurl = url+i+"/";
                episodes.put(Integer.toString(i), epurl);
            }
            return episodes;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getDescription() {
        String r = null;
        try {
            r = data.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    private String getTitle() {
        String r = null;
        try {
            r = data.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    private JSONObject getData(Document doc){
        JSONObject data666 = new JSONObject();
        if (doc == null) return null;
        try {
            Elements info = doc.getElementsByClass("info-field pc");
            String image = doc.getElementsByClass("cap-portada").select("img").first().attr("src");
            data666.put("image",image);
            Elements script = doc.select("script");

        for ( Element info1 : info){
            if (info1.getElementsByClass("info-title").text().contains("Tipo:")){
                String type = info1.select("info-value").text();
                data666.put("type",type);
                break;
            }
        }

        for (Element script0 : script){

            Pattern p = Pattern.compile("\\$.get\\('\\/ajax\\/pagination_episodes\\/(.*)\\/\\'\\+ ");
            Matcher m = p.matcher(script0.data());
            if (m.find()){
                String internalid = m.group(1);
                data666.put("internalid",internalid);
                break;
            }


        }


        String title = doc.getElementsByClass("sinopsis-box").select("h2").first().text();
            data666.put("title",title);
        String description = doc.getElementsByClass("sinopsis-box").select("p").first().text();
            data666.put("description",description);

        } catch (Exception e) {
           e.printStackTrace();
        }

        return data666;
    }

    private JSONArray getEpisodesData(){

        String url = "https://jkanime.net/index.php/ajax/pagination_episodes/"+getinternalid();
        JSONArray episodes = new JSONArray();

        int i = 0;
        while (true) {

            String url2 = url +"/"+ i;

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url2)
                        .build();
                Response responses = null;
                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                JSONArray episodespage = new JSONArray(jsonData);

                if (episodespage.length()==0 || episodespage == null) break;

                for (int j = 0 ; j <episodespage.length();j++){
                    episodes.put(episodespage.get(j));
                }
            } catch (IOException | JSONException e) {
                break;
            }

            i++;
}
return  episodes;
    }

    public String getPrefix(){
        String prefix = url.replace("https://jkanime.net/" ,"");

        if (prefix != null && prefix.length() > 0 && prefix.charAt(prefix.length() - 1) == '/') {
            prefix = prefix.substring(0, prefix.length() - 1);
        }

        return prefix;
    }



    private int getEpisodesNumber(){
        return data2.length();
    }


    private JSONObject getData_test(Document doc){
        JSONObject data2 = new JSONObject();
        if (doc == null) return null;
        try {
            Elements metaTags =  doc.getElementsByTag("meta");
            Elements script =  doc.getElementsByTag("script");
            Elements type =  doc.getElementsByTag("span");
            String type2 = doc.select("span[class*=\"Type \"]").text();

            for (Element metaTag : metaTags) {

                String content = metaTag.attr("content");
                String name = metaTag.attr("property");
                String descname = metaTag.attr("name");

                if("og:image".equals(name)) {
                    data2.put("image",content);
                }
                if("description".equals(descname)) {
                    data2.put("description",content);
                }
            }
            for (Element scripts : script){
                if (scripts.data().contains("var episodes = ")) {
                    Pattern pattern = Pattern.compile(".*var episodes = ([^;]*);");
                    Matcher matcher = pattern.matcher(scripts.data());
                    if (matcher.find()) {
                        data2.put("episodios", matcher.group(1));
                    } else {
                        System.err.println("No match found!");
                    }
                    //pattern = Pattern.compile(".*var anime_info = ([^;]*);");
                    pattern = Pattern.compile(".*var anime_info = (\\[.*?]);");
                    matcher = pattern.matcher(scripts.data());
                    if (matcher.find()) {
                        data2.put("idandname", matcher.group(1));
                    } else {
                        System.err.println("No match found!");
                    }

                    break;
                }
            }

            data2.put("type", type2);


            return data2;
        }
        catch (JSONException e){
            return null;
        }
    }



}
