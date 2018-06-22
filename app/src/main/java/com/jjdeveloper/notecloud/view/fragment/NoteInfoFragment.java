package com.jjdeveloper.notecloud.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;

import org.w3c.dom.Text;

public class NoteInfoFragment extends Fragment {
    public static View view;
    private NoteModel note;
    private TextView title, descricao, noteId;
    private Context activity;
    public NoteInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getContext();
        note = FeedActivity.clickedNote;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_note_info, container, false);
        initObjects();
        mostraNota();
        return view;
    }

    private void mostraNota(){
        title.setText(note.getTitle());
        descricao.setText(note.getBody());
        noteId.setText(String.valueOf(note.getNoteId()));
    }

    private void initObjects(){
        title = view.findViewById(R.id.txtTitleCard);
        descricao = view.findViewById(R.id.txtDescricaoCard);
        noteId = view.findViewById(R.id.noteIdCard);
    }
}
