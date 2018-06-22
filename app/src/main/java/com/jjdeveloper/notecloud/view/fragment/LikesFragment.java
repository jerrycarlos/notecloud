package com.jjdeveloper.notecloud.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adapter.NoteAdapter;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.MainActivity;

import java.util.ArrayList;

public class LikesFragment extends Fragment {
    public static View view;
    View noteView;
    private static Context activity;
    static RecyclerView mRecyclerView;
    static SwipeRefreshLayout mSwipeRefresh;
    private int id = 1;
    public static NoteAdapter mAdapter;

    public LikesFragment() {

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
        view = inflater.inflate(R.layout.fragment_likes, container, false);
        initObjects();
        setupRecycler();
        ActionAdapter.operacao = 1;
        ActionAdapter.action(MainActivity.userLogado.getId(), activity);
        //noteLikes();
        return view;
    }

    private void initObjects(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        mAdapter = new NoteAdapter(new ArrayList<NoteModel>(0),activity);
    }

    private void setupRecycler() {
        // Criando o StaggeredGridLayoutManager com duas colunas, descritas no primeiro argumento
        // e no sentido vertical (como uma lista).
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView = new RecyclerView(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        // Adiciona o adapter que irá anexar os objetos à lista.
        //mAdapter = new LineAdapter(new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);

        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                    fab2.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                    fab2.show();
                }
            }
        });*/

    }

    public static void noteLikes(){
        ProgressDialog progress = new ProgressDialog(activity);
        progress.setMessage("Carregando curtidas...");
        progress.show();

        NoteAdapter likeAdapter = new NoteAdapter(new ArrayList(0),activity);
        //FeedFragment.mAdapter;
        if(ActionAdapter.getLike() != null) {
            for (int i = 0; i < ActionAdapter.getLike().length; i++) {
                for(NoteModel l : FeedFragment.mAdapter.getList()){
                    //if()
                    if(l.getNoteId() == Integer.parseInt(ActionAdapter.getLike()[i]))
                        likeAdapter.updateList(l);
                }
            }
        }
        if (progress.isShowing()) {
            progress.dismiss();
        }
        mRecyclerView.setAdapter(likeAdapter);

    }
}