package com.jjdeveloper.notecloud.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.controller.ActionUser;
import com.jjdeveloper.notecloud.holder.NoteHolder;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
    private final List<NoteModel> noteList;
    private Context activity;
    public NoteAdapter(ArrayList note, Context context) {
        noteList = note;
        this.activity = context;
    }


    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noteholder_view, parent, false));
    }


    public void onBindViewHolder(final NoteHolder holder, final int position) {
        holder.titulo.setText(noteList.get(position).getTitle());
        holder.descricao.setText(noteList.get(position).getBody());
        holder.noteId.setText("#" + String.valueOf(noteList.get(position).getNoteId()));
        holder.lblLike.setText(String.valueOf(noteList.get(position).getLikes()));
        holder.lblFavorite.setText(String.valueOf(noteList.get(position).getFavorites()));
        holder.lblShare.setText(String.valueOf(noteList.get(position).getShares()));
        //Log.e("user&note",String.valueOf(MainActivity.userLogado.getId()+" : " + noteList.get(position).getNoteId()));
        holder.btLike.setImageResource(R.drawable.ic_action_dislike);
        if(ActionAdapter.getLike() != null) {
            for (int i = 0; i < ActionAdapter.getLike().length; i++) {
                String ch = ActionAdapter.getLike()[i];
                String str = String.valueOf(noteList.get(position).getNoteId());
                if (ch.equals(str)) {
                    holder.btLike.setImageResource(R.drawable.ic_action_like);
                    break;
                }
            }
        }

        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActionUser.action(MainActivity.userLogado.getId(),noteList.get(position).getNoteId(),0,activity);
                if(holder.btLike.getImageAlpha() == R.drawable.ic_action_dislike){
                    holder.btLike.setImageResource(R.drawable.ic_action_like);
                }else holder.btLike.setImageResource(R.drawable.ic_action_dislike);
                //Toast.makeText(activity,"Você curtiu isto.",Toast.LENGTH_SHORT).show();
            }
        });

        holder.btFavorite.setImageResource(R.drawable.ic_action_unfavorite);
        if(ActionAdapter.getFavorite() != null) {
            for (int i = 0; i < ActionAdapter.getFavorite().length; i++) {
                String ch = ActionAdapter.getFavorite()[i];
                String str = String.valueOf(noteList.get(position).getNoteId());
                if (ch.equals(str)) {
                    holder.btFavorite.setImageResource(R.drawable.ic_action_favorite);
                    break;
                }
            }
        }

        holder.btFavorite.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActionUser.action(MainActivity.userLogado.getId(),noteList.get(position).getNoteId(),1,activity);
                if(holder.btFavorite.getImageAlpha() == R.drawable.ic_action_unfavorite){
                    holder.btFavorite.setImageResource(R.drawable.ic_action_favorite);
                }else holder.btFavorite.setImageResource(R.drawable.ic_action_unfavorite);
            }
        });

        holder.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Compartilhando nota:\n" + noteList.get(position).getBody() + "\n - via NoteCloud";
                String shareSub = noteList.get(position).getTitle();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Compartilhe como mensagem para..."));

                ActionUser.action(MainActivity.userLogado.getId(),noteList.get(position).getNoteId(),2,activity);
            }
        });

        holder.btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", holder.descricao.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity,"Nota copiada para área de transferência.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return noteList != null ? noteList.size() : 0;
    }

    public void updateList(NoteModel note) {
        insertItem(note);
    }

    public void clearList(){
        this.noteList.clear();
    }

    // Método responsável por inserir um novo usuário na lista
    //e notificar que há novos itens.

    private void insertItem(NoteModel note) {
        this.noteList.add(note);
        notifyDataSetChanged();
    }

    private void updateItem(int position) {
        NoteModel evento = this.noteList.get(position);
        //Toast.makeText(,"Registro efetuado!",Toast.LENGTH_LONG).show();

        notifyItemChanged(position);
    }

    public NoteModel getEvento(int position){
        return noteList.get(position);
    }

}
