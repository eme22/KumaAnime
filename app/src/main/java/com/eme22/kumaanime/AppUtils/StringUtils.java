package com.eme22.kumaanime.AppUtils;

import java.security.SecureRandom;
import java.text.Format;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.internal.http2.Header;

public class StringUtils {

    private static final String[] KEYWORDS = {"FIRST","SECOND","THIRD", "FOURTH", "FIFTH"};
    private static final String[] KEYWORDS_INV = { "1ST","2ND","3RD", "4RD", "5TH"};

    private static String removetwopoints (String a) { return a.replace(":" , "");}

    private static String removecomma (String a) { return a.replace("," , "");}

    private static String removetv (String a) { return a.replace("(TV)" , "");}

    private static String searchforkeywords(String a){
        for (int i = 0; i < KEYWORDS.length; i++) {
            if (a.contains(KEYWORDS[i])) {
                a = a.replace(KEYWORDS[i],KEYWORDS_INV[i]);
            }
        }
        return a;
    }

    public static String normalizeAnime(String anime, boolean UpperCase){

        if (anime.contains(",")) anime = removecomma(anime);
        if (anime.contains(":")) anime = removetwopoints(anime);
        if (anime.contains("(TV)")) anime = removetv(anime);
        return UpperCase ? searchforkeywords(anime).toUpperCase() : searchforkeywords(anime);

    }

    public static String replacesforSQL(String data){
        if (data.contains(" ")) data = data.replace(" ","%");
        if (data.contains("-")) data = data.replace("-","%");
        return data;
    }

    public static String replacesforSQLMoreStrict(String data){
        if (data.contains(" ")) data = data.replace(" ","%");
        if (data.contains("-")) data = data.replace("-","%");
        if (data.contains(":")) data = data.replace(":","%");
        return data;
    }

    public static boolean compareAnimes(String anime1, String anime2){
        return normalizeAnime(anime1,true).equals(normalizeAnime(anime2,true));
    }

    public static Boolean compareEpisodes(String anime1, String anime2){
        if (anime1.contains("Final")) return true;
        else if (anime2.contains("Final")) return true;
        else return anime1.equals(anime2);
    }

    public static String getEpisodeFromJK(String actualUrl) {
        actualUrl = deleteLast(actualUrl);
        String[] bits = actualUrl.split("/");
        return bits[bits.length-1];
    }

    public static String deleteLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static ArrayList<String> getLinks(String data){
        return new ArrayList<>(Arrays.asList(data.split(",")));
    }

    public static String FormatFile(int id, String episode, String extension){
        return String.format(Locale.getDefault(), "%d_%s.%s",id,episode,extension);
    }
    public static String FormatFile(int id, String extension){
        return String.format(Locale.getDefault(), "%d.%s",id,extension);
    }

    public static int createRandomCode(int codeLength) {
        char[] chars = "1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return Integer.parseInt(sb.toString());
    }

    public static Headers getHeaders(List<Map.Entry<String,String>> pairList){
        Headers.Builder b = new Headers.Builder();
        for (Map.Entry<String, String> a : pairList) {
            b.add(a.getKey(),a.getValue());
        }
        return b.build();
    }

    public static List<Map.Entry<String,String>> getHeadersParcel(Headers headers){
        List<Map.Entry<String,String>> as = new ArrayList<>();
        for (Iterator<Pair<String, String>> it = headers.iterator(); it.hasNext(); ) {
            Pair<String, String> h = it.next();
            as.add(new AbstractMap.SimpleImmutableEntry<>(h.getFirst(), h.getSecond()));
        }
        return as;
    }
}
