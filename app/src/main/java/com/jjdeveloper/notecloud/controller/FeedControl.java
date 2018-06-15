package com.jjdeveloper.notecloud.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjdeveloper.notecloud.adapter.NoteAdapter;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.holder.NoteHolder;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FeedControl {
    private static Context activity;
    private static int operacao;
    public static void listaEvento(Context c){
        operacao = 1;
        activity = c;
        ActionAdapter.action(MainActivity.userLogado.getId(),activity);
        JSONObject postData = new JSONObject();
        try {
            postData.put("email","teste");
            postData.put("senha","teste");
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/buscaNotas.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void myNotes(Context c){
        operacao = 2;
        activity = c;
        ActionAdapter.action(MainActivity.userLogado.getId(),activity);
        JSONObject postData = new JSONObject();
        try {
            postData.put("userId",MainActivity.userLogado.getId());
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/minhasNotas.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private static class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Carregando notas...");
            this.progress.show();
        }

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
                int responseCode=httpURLConnection.getResponseCode();

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
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            if (progress.isShowing()) {
                progress.dismiss();
            }
            String titulo = "Sucesso";

            JSONArray json = null;
            Long codigo = null;
            String msg = null;
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                if(result.length()>50) {
                    json = new JSONArray(result);
                    if (operacao == 1){
                        FeedActivity.mAdapter.clearList();
                        if (json.length() > 0) {
                            //listNotte = new ArrayList<NoteModel>();
                            FeedActivity.mAdapter.clearList();
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject xx = json.getJSONObject(i);
                                NoteModel note = new NoteModel();
                                note.setNoteId(xx.getInt("id"));
                                note.setTitle(xx.getString("title"));
                                note.setBody(xx.getString("descricao"));
                                note.setLikes(xx.getInt("likes"));
                                note.setFavorites(xx.getInt("favorites"));
                                note.setShares(xx.getInt("shares"));
                                note.setAuthor(xx.getString("author"));
                                //FeedActivity.mAdapter.;
                                FeedActivity.mAdapter.updateList(note);
                            }
                            //Swipe load complete
                            FeedActivity.onItemsLoadComplete();
                        }
                    }else{
                        FeedActivity.mAdapter.clearList();
                        if (json.length() > 0) {
                            //listNotte = new ArrayList<NoteModel>();
                            FeedActivity.mAdapter.clearList();
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject xx = json.getJSONObject(i);
                                NoteModel note = new NoteModel();
                                note.setNoteId(xx.getInt("id"));
                                note.setTitle(xx.getString("title"));
                                note.setBody(xx.getString("descricao"));
                                note.setLikes(xx.getInt("likes"));
                                note.setFavorites(xx.getInt("favorites"));
                                note.setShares(xx.getInt("shares"));
                                //FeedActivity.mAdapter.;
                                FeedActivity.mAdapter.updateList(note);
                            }
                            //Swipe load complete
                            FeedActivity.onItemsLoadComplete();
                        }
                    }
                }else
                    Toast.makeText(activity,"Nenhuma nota no momento!",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
