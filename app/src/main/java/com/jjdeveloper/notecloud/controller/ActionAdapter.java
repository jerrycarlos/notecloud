package com.jjdeveloper.notecloud.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jjdeveloper.notecloud.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class ActionAdapter {

    //private static int userId, noteId;
    private static Context context;
    private static JSONObject json;
    private static String[] like = null, favorite = null;

    /**
     *
     * @param userId id User do action
     * @param context
     */
    public static void action(int userId, Context context) {
        ActionAdapter.context = context;
        JSONObject postData = new JSONObject();
        try {
            postData.put("userId",userId);
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/userActions.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class SendDeviceDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                httpURLConnection.setReadTimeout(15000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();


                //pega o codigo da requisicao http
                int responseCode = httpURLConnection.getResponseCode();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("result", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            String titulo = "Sucesso";
            JSONObject json = null;
            int codigo = 0;
            String msg = null;
            try {
                if(result.length() > 27) {
                    json = new JSONObject(result);
                    //JSONArray likes = json.getJSONArray("like");
                    //JSONArray favorites = json.getJSONArray("favorites");
                    String like = json.getString("likes");
                    String favorite = json.getString("favorites");
                    like = like.replace("[", "");
                    like = like.replace("]", "");
                    like = like.replace("\"", "");
                    ActionAdapter.like = like.split(",");
                    favorite = favorite.replace("[", "");
                    favorite = favorite.replace("]", "");
                    favorite = favorite.replace("\"", "");
                    ActionAdapter.favorite = favorite.split(",");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] getLike(){
        return like;
    }

    public static String[] getFavorite(){
        return favorite;
    }


}
