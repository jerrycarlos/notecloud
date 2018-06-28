package com.jjdeveloper.notecloud.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jjdeveloper.notecloud.view.FeedActivity;


public class CarregarImagem extends Activity{

    private static Context activity;
    private static String url;
    public static Bitmap imagem;
    public static void baixarImagem(int id, String u, Context context){
        activity = context;
        url = u;
        DownloadImagemAsyncTask t = new DownloadImagemAsyncTask();
        t.execute(url);
    }


    static class DownloadImagemAsyncTask extends
            AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];

            try {
                URL url = new URL(urlString);
                HttpURLConnection conexao = (HttpURLConnection)
                        url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.connect();

                InputStream is = conexao.getInputStream();
                Bitmap imagem = BitmapFactory.decodeStream(is);
                return imagem;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            //Log.e("resultImg",result.toString());
            if (result != null) {
                FeedActivity.imgProfile.setImageBitmap(result);
            } else {
                String msg = "Imagem do perfil não pôde ser baixada ou você ainda não possui foto de perfil";
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}