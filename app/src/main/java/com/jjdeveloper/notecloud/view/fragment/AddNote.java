package com.jjdeveloper.notecloud.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adfly.AdsSetting;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.controller.AddNoteControl;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.MainActivity;


public class AddNote extends Fragment {
    public static View view;
    private EditText title, descricao;
    private RadioButton rdPublic, rdPrivate;
    private FloatingActionButton fabSend, fabBack;
    private AdView adView2;
    private Context activity;
    private AdView mAdView;
    public AddNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_note, container, false);
        initObjects();
        title.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (false == hasFocus) {
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            title.getWindowToken(), 0);
                }
            }
        });

        descricao.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (false == hasFocus) {
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            descricao.getWindowToken(), 0);
                }
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsSetting ads = new AdsSetting(activity);
                ads.start();
                FeedActivity.toolbar.setTitle("NoteCloud Feed");
                FeedActivity.fragment.beginTransaction().replace(R.id.content_fragment, new FeedFragment()).commit();
            }
        });

        MobileAds.initialize(activity, Config.app_pub);
        AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Config.banner_unit_id);

        mAdView = view.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F7FE7A31315748DB37FD0F08B7F254F9").build();
        mAdView.loadAd(adRequest);
        FeedActivity.fab.setVisibility(View.INVISIBLE);
        FeedActivity.fab2.setVisibility(View.INVISIBLE);
        return view;

    }


    private void addNote(){
        NoteModel note = new NoteModel();

        if(title.getText().toString().trim().equals("")) {
            title.setError("Insira o titulo da nota.");
            title.requestFocus();
            return;
        }
        if(title.getText().toString().length() < 5){
            title.setError("Título muito curto.");
            title.setHint("Titulo (min. 5 caracteres)");
            title.requestFocus();
            return;
        }
        note.setTitle(title.getText().toString().trim());

        if(descricao.getText().toString().trim().equals("")){
            descricao.setError("Insira a mensagem");
            descricao.requestFocus();
            return;
        }
        if(descricao.getText().toString().length() < 10){
            descricao.setError("Mensagem muito curta.");
            descricao.setHint("Mensagem (min. 10 caracteres)");
            descricao.requestFocus();
            return;
        }
        note.setBody(descricao.getText().toString().trim());

        note.setAuthor(MainActivity.userLogado.getLogin());

        note.setFk_user(MainActivity.userLogado.getId());

        if(rdPublic.isChecked())
            note.setVisualization(Config.MODE_PUBLIC);
        else if(rdPrivate.isChecked())
            note.setVisualization(Config.MODE_PRIVATE);
        else{
            rdPublic.setError("Escolha um dos modos!");
            Toast.makeText(activity,"Escolha o modo de visualização da nota.",Toast.LENGTH_SHORT).show();
            return;
        }

        AddNoteControl.addNote(note, activity);
    }

    private void initObjects(){
        title = view.findViewById(R.id.txtTitleAdd);
        descricao = view.findViewById(R.id.txtDescricaoAdd);
        rdPublic = view.findViewById(R.id.rdPublic);
        rdPrivate = view.findViewById(R.id.rdPrivate);
        adView2 = view.findViewById(R.id.adView2);
        fabSend = view.findViewById(R.id.fabSend);
        fabBack = view.findViewById(R.id.fabBack);
    }
}
