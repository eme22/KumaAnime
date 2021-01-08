package com.eme22.kumaanime.PlayActivityFragments.Util;

import android.util.Log;

import com.eme22.kumaanime.AppUtils.AnimeObjects.server.Server;
import com.eme22.kumaanime.AppUtils.Callback;
import com.eme22.kumaanime.AppUtils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceFetcher_v2 implements Runnable {

        String FLV;
        String JK;
        String ID;
        Callback callback;

        public SourceFetcher_v2(String FLV, String JK, String ID, Callback callback) {
            this.FLV = FLV;
            this.JK = JK;
            this.ID = ID;
            this.callback = callback;
        }

        @Override
        public void run() {
            Document FLVDoc = null;
            if (FLV !=null){
                try {
                    FLVDoc = Connection.getDocOk(FLV);
                } catch (IOException e) {
                    callback.onError(e);
                    return;
                }
            }
            Document JKDoc = null;
            if (JK !=null){
                try {
                    JKDoc = Connection.getDocOk(JK);
                } catch (IOException e) {
                    callback.onError(e);
                    return;
                }
            }
            Document IDDoc = null;
            if (ID != null){
                try {
                    IDDoc = Connection.getDocOk(ID);
                } catch (IOException e) {
                    callback.onError(e);
                    return;
                }
            }
            ArrayList<Server> servers1 = new ArrayList<>();
            try {
                servers1 = getMiniServers(FLVDoc,0);
            }
            catch (NullPointerException e){
                e.printStackTrace();
                Log.d("NO SERVERS A", FLV);
            }
            /*
            ArrayList<Server> servers2 = new ArrayList<>();
            try {
                servers2 = getMiniServers(JKDoc,1);
            }
            catch (NullPointerException e){
                Log.d("NO SERVERS A", JK);
            }
            ArrayList<Server> servers3 = new ArrayList<>();
            try {
                servers3 = getMiniServers(IDDoc,2);
            }
            catch (NullPointerException e){
                Log.d("NO SERVERS A", ID);
            }


            ArrayList<Server> servers =new ArrayList<>();
            try {
                servers.addAll(servers1);
            }
            catch (NullPointerException e){
                e.printStackTrace();
                Log.d("NO SERVERS B", FLV);
            }
            try {
                servers.addAll(servers2);
            }
            catch (NullPointerException e){
                Log.d("NO SERVERS B", JK);
            }
            try {
                servers.addAll(servers3);
            }
            catch (NullPointerException e){
                Log.d("NO SERVERS B", ID);
            }

*/

            //callback.onSuccess(servers);
            callback.onSuccess(servers1);

        }

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
                            }
                        }
                    }
                    return null;
                }
                case 1: return null;

                case 2: return null;


            }
        }

        private ArrayList<Server> convertFLVJSONtoServer(JSONArray jsonArr) {
            ArrayList<Server> servers = new ArrayList<>();
            for (int i = 0; i < jsonArr.length() ; i++) {
                Server server = new Server();
                JSONObject serverJSON = null;
                try {
                    serverJSON = (JSONObject) jsonArr.get(i);
                    server.setName(serverJSON.getString("server"));
                    server.setSource(serverJSON.getString("code"));
                } catch (JSONException e) {
                    callback.onError(e);
                    return null;
                }
                servers.add(server);
            }
            return servers;
        }





}
