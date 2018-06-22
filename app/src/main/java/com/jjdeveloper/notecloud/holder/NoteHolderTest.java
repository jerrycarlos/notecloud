package com.jjdeveloper.notecloud.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjdeveloper.notecloud.R;

public class NoteHolderTest extends RecyclerView.ViewHolder {
    public TextView titulo;
    public TextView descricao;
    public TextView noteId;
    public ImageButton btLike;
    public ImageButton btFavorite;
    public ImageButton btShare;
    public ImageButton btCopy;
    public ImageButton btInfo;
    public TextView lblLike, lblFavorite, lblShare;

    public NoteHolderTest(View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.txtTitleHolder);
        descricao = itemView.findViewById(R.id.txtDescricaoHolder);
        noteId = itemView.findViewById(R.id.noteIdHolder);
        btLike = itemView.findViewById(R.id.btLikeHolder);
        btFavorite = itemView.findViewById(R.id.btFavoriteHolder);
        btInfo = itemView.findViewById(R.id.btInfoHolder);

    }
}