package com.jjdeveloper.notecloud.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.jjdeveloper.notecloud.config.Config.PREF_NAME;

public class MainActivity extends AppCompatActivity {
    public final Context activity = MainActivity.this;
    private TextView userEmail, userPass;
    private CheckBox chkManter;
    public static UserModel userLogado;
    public static long result = -1;
    private String userLogin, userSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObjects();
        SharedPreferences credenciais = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        //for debug mode
        String login = "jerry@gmail.com";
        String senha = "123456";
        //String login = credenciais.getString("login", "");
        //String senha = credenciais.getString("senha", "");
        if(!login.equals("") && !senha.equals(""))
            loginUser(login,senha);
    }
    public void registrar(View v){
        //Intent i = new Intent(MainActivity.this, CadastroActivity.class);
        //startActivity(i);
        //finishAffinity();
        //finish();
    }

    public void abrirFeed(View v){
        this.userLogin = userEmail.getText().toString();
        this.userSenha = userPass.getText().toString();
        if(userLogin.trim().equals("")){
            userEmail.setError("Insira seu login!");
            userEmail.requestFocus();
            //Toast.makeText(getApplicationContext(),"Insira seu login!", Toast.LENGTH_SHORT).show();
            return;
        }else if(userSenha.trim().equals("")){
            userPass.setError("Insira sua senha!");
            userPass.requestFocus();
            //Toast.makeText(getApplicationContext(),"Insira sua senha!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(chkManter.isChecked())
            salvarLogin();
        loginUser(userLogin,userSenha);
    }

    private void salvarLogin(){
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", this.userLogin);
        editor.putString("senha", this.userSenha);
        editor.commit();
    }

    private long backPressedTime = 0;
    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 segundos para sair
            backPressedTime = t;
            Toast.makeText(this, "Clique 2x voltar para sair!",
                    Toast.LENGTH_SHORT).show();
        } else {    // se pressionado novamente encerrar app
            // clean up
            finish();//super.onBackPressed();       // bye
        }
    }

    private void initObjects(){
        userEmail = (TextView) findViewById(R.id.txtSingin);
        userPass = (TextView) findViewById(R.id.txtPasswordMain);
        chkManter = (CheckBox) findViewById(R.id.checkBoxLembrar);
    }

    private void loginUser(String email, String senha){
        JSONObject postData = new JSONObject();
        try {
            postData.put("email",email);
            postData.put("senha",senha);

            SendDeviceDetails t = new SendDeviceDetails();
            t.execute(Config.ip_servidor+"/login.php", postData.toString());
            //ip externo http://179.190.193.231/cadastro.php
            //ip interno 192.168.0.21 minha casa
            //ip interno hotspot celular 192.168.49.199[
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        private ProgressDialog progress = new ProgressDialog(activity);

        protected void onPreExecute() {
            //display progress dialog.
            this.progress.setMessage("Entrando...");
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
                if (json.length() > 2) {
                    id = json.getInt("id");
                    nome = json.getString("nome");
                    email = json.getString("email");
                    login = json.getString("login");
                    telefone = json.getString("telefone");
                    UserModel usr = new UserModel(nome, email, login, telefone);
                    usr.setId(id);
                    MainActivity.userLogado = usr;
                    msg = "Logado com sucesso.";
                    Toast.makeText(activity,msg,Toast.LENGTH_LONG).show();
                    mudaTela();
                }else{
                    titulo  = "Erro";
                    codigo = json.getLong("erro");
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
    private void mudaTela(){
        Intent i = new Intent(MainActivity.this, FeedActivity.class);
        startActivity(i);
        finishAffinity();
    }
}
