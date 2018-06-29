package com.jjdeveloper.notecloud.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.controller.ActionUser;
import com.jjdeveloper.notecloud.controller.CarregarImagem;
import com.jjdeveloper.notecloud.model.UserModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.MainActivity;

public class ProfileFragment extends Fragment {
    public View view;
    private Context activity;

    private TextView txtNome, txtEmail, txtLogin, txtTelefone, lblLike, lblFavorite;
    private ImageView imageView;
    private UserModel u;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getContext();
        u = MainActivity.userLogado;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initObjects();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mostraDados();
    }

    private void initObjects(){
        txtNome = view.findViewById(R.id.labelNome);
        txtEmail = view.findViewById(R.id.labelEmail);
        txtLogin = view.findViewById(R.id.labelLogin);
        txtTelefone = view.findViewById(R.id.labelTelefone);
        imageView = view.findViewById(R.id.imageView);
        lblLike = view.findViewById(R.id.lblLike);
        lblFavorite = view.findViewById(R.id.lblFavorite);
    }

    private void mostraDados(){
        txtNome.setText(u.getNome());
        txtEmail.setText(u.getEmail());
        txtLogin.setText(u.getLogin());
        txtTelefone.setText(u.getTelefone());
        imageView.setImageBitmap(CarregarImagem.imagem);
        lblLike.setText(String.valueOf(ActionAdapter.getLike().length));
        lblFavorite.setText(String.valueOf(ActionAdapter.getFavorite().length));
    }
}

