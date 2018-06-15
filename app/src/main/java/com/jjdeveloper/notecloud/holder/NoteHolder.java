package com.jjdeveloper.notecloud.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjdeveloper.notecloud.R;

public class NoteHolder extends RecyclerView.ViewHolder {
    public TextView titulo;
    public TextView descricao;
    public TextView noteId;
    public ImageButton btLike;
    public ImageButton btFavorite;
    public ImageButton btShare;
    public ImageButton btCopy;
    public TextView lblLike, lblFavorite, lblShare;

    public NoteHolder(View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.txtTitleHolder);
        descricao = itemView.findViewById(R.id.txtDescricaoHolder);
        noteId = itemView.findViewById(R.id.noteIdHolder);
        btLike = itemView.findViewById(R.id.btLikeHolderMy);
        btFavorite = itemView.findViewById(R.id.btFavoriteHolderMy);
        btShare = itemView.findViewById(R.id.btShareHolder);
        btCopy = itemView.findViewById(R.id.btCopyHolder);
        lblLike = itemView.findViewById(R.id.lblLike);
        lblFavorite = itemView.findViewById(R.id.lblFavorite);
        lblShare = itemView.findViewById(R.id.lblShare);

    }
}