package com.jjdeveloper.notecloud.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.adapter.NoteAdapter;
import com.jjdeveloper.notecloud.controller.NoteControl;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;

import java.util.ArrayList;

public class MyNotesFragment extends Fragment {
    public static View view;
    View noteView;
    private Context activity;
    RecyclerView mRecyclerView;
    static SwipeRefreshLayout mSwipeRefresh;
    FloatingActionButton fab, fab2;
    private int id = 1;
    public static NoteAdapter mAdapter;

    public MyNotesFragment() {

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
        view = inflater.inflate(R.layout.fragment_my_notes, container, false);
        initObjects();
        setupRecycler();
        NoteControl.myNotes(activity);
        fab = FeedActivity.fab;
        fab2 = FeedActivity.fab2;
        fab.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        return view;
    }

    private void initObjects(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        mAdapter = new NoteAdapter(new ArrayList<NoteModel>(0),activity);
    }

    private void setupRecycler() {
        // Criando o StaggeredGridLayoutManager com duas colunas, descritas no primeiro argumento
        // e no sentido vertical (como uma lista).
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView = new RecyclerView(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        // Adiciona o adapter que irá anexar os objetos à lista.
        //mAdapter = new LineAdapter(new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        });

    }
}