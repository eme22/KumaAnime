package com.eme22.kumaanime.AppUtils.AnimeObjects;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.eme22.kumaanime.Databases.AnimeDataConverter;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(tableName = "anime_table")@TypeConverters(value = AnimeDataConverter.class)
public class FLVAnime_Object extends BaseAnime_Object {

    @Ignore
    public static JSONObject data;
    public int internalid;

    public FLVAnime_Object(String  url) throws IOException {
        super(url);
        data = getData(maindoc);
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



    private JSONObject getData(Document doc){
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

    private String getTitle(){
        try {

            JSONArray search = new JSONArray(data.getString("idandname"));
            if (search == null) return null;
            String title = StringEscapeUtils.unescapeHtml4((search.getString(1)));
            return title;
        }
        catch (JSONException e){
            return null;
        }
    }

    private String getPrefix() {
        try {
            JSONArray search = new JSONArray(data.getString("idandname"));
            String prefix = search.getString(2);
            return prefix;
        }
        catch (JSONException e){
            return null;
        }
    }

    private int getinternalid() {
        try {
            JSONArray search = new JSONArray(data.getString("idandname"));
            Integer prefix = Integer.parseInt( search.getString(0));
            return prefix;
        }
        catch (JSONException e){
            return 0;
        }

    }



    private String getDescription()  {
        String search = null;
        try {
            search = data.getString("description");
            return search;
        } catch (JSONException e) {
            return null;
        }

    }

    private String getImage(){
        String search = null;
        try {
            search = data.getString("image");
            return search;
        } catch (JSONException e) {
            return null;
        }

    }

    private int getEpisodesNumber() {
        try {
            JSONArray episodes = new JSONArray(data.getString("episodios"));
            JSONArray episodes2 = new JSONArray(episodes.getString(0));
            int search = Integer.parseInt(episodes2.getString(0));

            return search;
        }
        catch (NumberFormatException | JSONException e){
            JSONArray episodes = null;
            try {
                episodes = new JSONArray(data.getString("episodios"));
                JSONArray episodes2 = new JSONArray(episodes.getString(0));
                double value = Double.parseDouble(episodes2.getString(0));
                final int iValue = (int) value;
                return iValue;
            } catch (JSONException jsonException) {
                return 0;
            }

        }
    }

    private String getType(){
        try {
            String search = data.getString("type");
            return search;
        }
        catch (JSONException e){
            return null;
        }

    }

    private int getEpisodesNumber_orig()  {
        try {
            JSONArray episodes = new JSONArray(data.getString("episodios"));
            JSONArray episodes2 = new JSONArray(episodes.getString(0));
            int search = Integer.parseInt(episodes2.getString(0));

            return search;
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        catch (NumberFormatException e){
            JSONArray episodes = null;
            try {
                episodes = new JSONArray(data.getString("episodios"));
                JSONArray episodes2 = new JSONArray(episodes.getString(0));
                double value = Double.parseDouble(episodes2.getString(0));
                final int iValue = (int) value;
                return iValue;
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }
        return -1;
    }


    private JSONObject getEpisodes() {
        try {
            JSONObject episodes = new JSONObject();
            for(int i = 1; i<=getEpisodesNumber();i++){
                String epurl = url.replace("/anime/", "/ver/");
                epurl = epurl+"-"+i;
                episodes.put(Integer.toString(i), epurl);
            }
            return episodes;
        } catch (JSONException e){
            return null;
        }
    }

    private JSONArray getImage2(){
        JSONArray image = new JSONArray();
        String base = "https://cdn.animeflv.net/screenshots/";
        int id = getinternalid();
        for (int i = 1;i<=getEpisodesNumber();i++){
            String url = base+id+"/"+i+"/th_3.jpg";
            image.put(url);
        }
        return image;
    }

}
