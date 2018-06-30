package com.jjdeveloper.notecloud.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.model.UserModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginControl {

    private static Context activity;
    public static String imagem = null;
    public static void loginUser(String email, String senha, Context c){
        activity = c;
        JSONObject postData = new JSONObject();
        try {
            postData.put("email",email);
            postData.put("senha",senha);

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.IP_SERVIDOR +"/login.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Logando...");
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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            String nome = null, email = null, senha = null, login = null, instituicao = null,
                    curso = null, ocupacao = null, telefone = null;
            int id = -1, periodo = 0;
            Log.i("result",result);
            try {
                json = new JSONObject(result);
                codigo = json.getLong("status");
                if (codigo > 0) {
                    id = json.getInt("id");
                    nome = json.getString("nome");
                    email = json.getString("email");
                    login = json.getString("login");
                    telefone = json.getString("telefone");
                    imagem = json.getString("imagem");
                    if(imagem != null) {
                        String url = Config.IP_SERVIDOR + "/profiles/" + imagem + ".png";
                        CarregarImagem.baixarImagem(id, url, activity);
                    }
                    UserModel usr = new UserModel(nome, email, login, telefone);
                    usr.setId(id);
                    MainActivity.userLogado = usr;
                    msg = "Logado com sucesso.";
                    Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
                    mudaTela();
                }else{
                    titulo  = "Erro";
                    msg = json.getString("msg");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setMessage(msg)
                            .setTitle(titulo);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void mudaTela(){
        Intent i = new Intent(activity, FeedActivity.class);
        activity.startActivity(i);
    }
}
