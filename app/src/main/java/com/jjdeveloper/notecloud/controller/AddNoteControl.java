package com.jjdeveloper.notecloud.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adfly.AdsSetting;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.fragment.FeedFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddNoteControl {
    private static Context activity;
    private static int operacao;
    private static NoteModel n;
    public static void addNote(NoteModel note, Context c){
        operacao = 4;
        activity = c;
        n = note;
        JSONObject postData = new JSONObject();
        try {
            postData.put("title", note.getTitle());
            postData.put("descricao", note.getBody());
            postData.put("author", note.getAuthor());
            postData.put("fk_user", note.getFk_user());
            postData.put("acess", note.getVisualization());
            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.IP_SERVIDOR +"/registerNote.php", postData.toString());
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
            this.progress.setMessage("Enviando sua nota...");
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

            JSONObject json = null;
            Long codigo = null;
            String msg = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                json = new JSONObject(result);
                codigo = json.getLong("status");
                msg = json.getString("msg");
                Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
                if(codigo == 1) {
                    mudaTela();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void mudaTela(){
        AdsSetting ads = new AdsSetting(activity);
        ads.start();
        FeedActivity.fragment.beginTransaction().replace(R.id.content_fragment, new FeedFragment()).commit();
        /*Snackbar.make(AddNote.view, "Nota criada com sucesso!", Snackbar.LENGTH_LONG)
                .setAction("Ver nota", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(activity, NoteActivity.class);
                        FeedActivity.clickedNote = n;
                        activity.startActivity(i);
                    }
                }).show();*/
    }
}
