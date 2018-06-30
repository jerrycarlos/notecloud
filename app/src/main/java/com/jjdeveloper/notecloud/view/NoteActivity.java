package com.jjdeveloper.notecloud.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adfly.AdsSetting;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.controller.ActionUser;
import com.jjdeveloper.notecloud.model.NoteModel;

public class NoteActivity extends AppCompatActivity {
    private NoteModel note;
    private TextView title, descricao, noteId, author, date, lblLike, lblFavorite;
    private String paramLike, paramFavorite;
    private ImageButton btLike, btFavorite, btShare, btCopy;
    private Context activity;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, FeedActivity.class);
                startActivity(i);
            }
        });
        activity = getApplicationContext();
        note = FeedActivity.clickedNote;
        initObjects();
        mostraNota();
        MobileAds.initialize(this, Config.app_pub);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Config.banner_unit_id);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F7FE7A31315748DB37FD0F08B7F254F9").build();
        mAdView.loadAd(adRequest);


        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionUser.action(MainActivity.userLogado.getId(),note.getNoteId(), Config.ACTION_LIKE, activity);
                //ActionAdapter.action(MainActivity.userLogado.getId(),activity);
                //int likes = Integer.parseInt(holder.lblLike.getText().toString());
                if((int)btLike.getTag() == R.drawable.ic_action_dislike){
                    btLike.setImageResource(R.drawable.ic_action_like);
                    btLike.setTag(R.drawable.ic_action_like);

                    //holder.lblLike.setText(String.valueOf(likes + 1));
                }else {
                    btLike.setImageResource(R.drawable.ic_action_dislike);
                    btLike.setTag(R.drawable.ic_action_dislike);
                    /*if(likes > 0)
                        holder.lblLike.setText(String.valueOf(likes - 1));*/
                }
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActionUser.action(MainActivity.userLogado.getId(),note.getNoteId(), Config.ACTION_FAVORITE, activity);
                //ActionAdapter.action(MainActivity.userLogado.getId(),activity);
                //int favorites = Integer.parseInt(holder.lblFavorite.getText().toString());
                if((int)btFavorite.getTag() == R.drawable.ic_action_unfavorite){
                    btFavorite.setImageResource(R.drawable.ic_action_favorite);
                    btFavorite.setTag(R.drawable.ic_action_favorite);
                    //holder.lblFavorite.setText(String.valueOf(favorites + 1));
                }else {
                    btFavorite.setImageResource(R.drawable.ic_action_unfavorite);
                    btFavorite.setTag(R.drawable.ic_action_unfavorite);
                    /*if(favorites > 0)
                        holder.lblFavorite.setText(String.valueOf(favorites - 1));*/
                }
            }
        });

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Compartilhando nota:\n" + note.getBody() + "\n - via NoteCloud";
                String shareSub = note.getTitle();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Compartilhe como mensagem para..."));

                ActionUser.action(MainActivity.userLogado.getId(),note.getNoteId(), Config.ACTION_SHARE, activity);
            }
        });

        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", descricao.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity,"Nota copiada para área de transferência.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void mostraNota(){
        title.setText(note.getTitle());
        descricao.setText(note.getBody());
        descricao.setClickable(false);
        descricao.setFocusable(false);
        author.setText("@" + note.getAuthor());
        date.setText("Data criação: " + note.getDate_created());
        lblLike.setText(String.valueOf(note.getLikes()));
        lblFavorite.setText(String.valueOf(note.getFavorites()));

        if(paramLike.equals("1")) {
            btLike.setImageResource(R.drawable.ic_action_like);
            btLike.setTag(R.drawable.ic_action_like);
        }
        else {
            btLike.setImageResource(R.drawable.ic_action_dislike);
            btLike.setTag(R.drawable.ic_action_dislike);
        }

        if(paramFavorite.equals("1")) {
            btFavorite.setImageResource(R.drawable.ic_action_favorite);
            btFavorite.setTag(R.drawable.ic_action_favorite);
        }
        else {
            btFavorite.setImageResource(R.drawable.ic_action_unfavorite);
            btFavorite.setTag(R.drawable.ic_action_unfavorite);
        }
    }

    private void initObjects(){
        title = findViewById(R.id.txtTitleCard);
        descricao = findViewById(R.id.txtDescricaoCard);
        author = findViewById(R.id.noteAuthorCard);
        btLike = findViewById(R.id.btLikeCard);
        btFavorite = findViewById(R.id.btFavoriteCard);
        btShare = findViewById(R.id.btShareCard);
        btCopy = findViewById(R.id.btCopyCard);
        date = findViewById(R.id.noteDateCard);
        lblLike = findViewById(R.id.lblLike);
        lblFavorite = findViewById(R.id.lblFavorite);
        Intent it = getIntent();
        if(it != null) {
            Bundle params = it.getExtras();
            if (params != null) {
                paramLike = params.getString("like");
                paramFavorite = params.getString("favorite");
                //nivel = params.getString("nivel");
            }
        }
    }

}
