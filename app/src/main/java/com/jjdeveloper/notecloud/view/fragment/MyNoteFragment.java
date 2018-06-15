package com.jjdeveloper.notecloud.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.controller.FeedControl;
import com.jjdeveloper.notecloud.view.FeedActivity;

public class MyNoteFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_notes, container);
        //FeedControl.myNotes();

        return view;
    }

}
