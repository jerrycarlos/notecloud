package com.jjdeveloper.notecloud.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.MobileAds;
import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adapter.NoteAdapter;
import com.jjdeveloper.notecloud.adfly.AdsSetting;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.controller.NoteControl;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.MainActivity;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    public static View view;
    View noteView;
    private static Context activity;
    public static RecyclerView mRecyclerView;
    static SwipeRefreshLayout mSwipeRefresh;
    public static int swipeRefresh = 0;
    private int id = 1;
    public static NoteAdapter mAdapter;
    public FeedFragment() {

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
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        initObjects();
        mAdapter = new NoteAdapter(new ArrayList<NoteModel>(0),activity);
        setupRecycler();
        ActionAdapter.operacao = 0;
        ActionAdapter.action(MainActivity.userLogado.getId(),activity);
        NoteControl.buscaNotas(activity);
        return view;
    }

    private void initObjects(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_feedlayout);
    }

    private void setupRecycler() {
        // Criando o StaggeredGridLayoutManager com duas colunas, descritas no primeiro argumento
        // e no sentido vertical (como uma lista).
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
        swipeRefresh = 0;
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                swipeRefresh = 1;
                refreshContent();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //swipeRefresh = 0;
    }

    private void refreshContent(){
        NoteControl.buscaNotas(activity);
    }

    public static void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefresh.setRefreshing(false);
        MobileAds.initialize(activity, Config.app_pub);
        AdsSetting ads = new AdsSetting(activity);
        ads.start();
    }

    public static NoteModel noteInfo(View v){
        return mAdapter.getNote(mRecyclerView.getChildAdapterPosition(v));
    }
}
