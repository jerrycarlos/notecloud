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
import com.jjdeveloper.notecloud.controller.CarregarImagem;
import com.jjdeveloper.notecloud.controller.LoginControl;
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
    public static String imagem = null;
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
            LoginControl.loginUser(login, senha, activity);
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
        LoginControl.loginUser(userLogin,userSenha,activity);
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
}
