package com.jjdeveloper.notecloud.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjdeveloper.notecloud.R;
import com.jjdeveloper.notecloud.config.Config;
import com.jjdeveloper.notecloud.controller.ActionAdapter;
import com.jjdeveloper.notecloud.controller.ActionUser;
import com.jjdeveloper.notecloud.holder.NoteHolderTest;
import com.jjdeveloper.notecloud.model.NoteModel;
import com.jjdeveloper.notecloud.view.FeedActivity;
import com.jjdeveloper.notecloud.view.MainActivity;
import com.jjdeveloper.notecloud.view.NoteActivity;
import com.jjdeveloper.notecloud.view.fragment.FeedFragment;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolderTest> {
    private final List<NoteModel> noteList;
    private Context activity;
    public NoteAdapter(ArrayList<NoteModel> note, Context context) {
        noteList = note;
        this.activity = context;
    }


    public NoteHolderTest onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteHolderTest(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noteholder_view_test, parent, false));
    }


    public void onBindViewHolder(final NoteHolderTest holder, final int position) {
        holder.titulo.setText(noteList.get(position).getTitle());
        String body = noteList.get(position).getBody();

        if(noteList.get(position).getBody().length() > 92) {
            body = body.substring(0, 91);
            holder.descricao.setText(body + "...");
        }else holder.descricao.setText(body);
        holder.noteId.setText("#" + String.valueOf(noteList.get(position).getNoteId()));
        //holder.lblLike.setText(String.valueOf(noteList.get(position).getLikes()));
        //holder.lblFavorite.setText(String.valueOf(noteList.get(position).getFavorites()));
        //holder.lblShare.setText(String.valueOf(noteList.get(position).getShares()));
        //Log.e("user&note",String.valueOf(MainActivity.userLogado.getId()+" : " + noteList.get(position).getNoteId()));
        holder.btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, NoteActivity.class);
                Bundle params = new Bundle();
                String l = "0", f = "0";
                if((int)holder.btLike.getTag() == R.drawable.ic_action_like)
                    l = "1";
                if((int)holder.btFavorite.getTag() == R.drawable.ic_action_favorite)
                    f = "1";
                params.putString("like",l);
                params.putString("favorite",f);
                //params.putString("nivel", nivel);
                i.putExtras(params);
                FeedActivity.clickedNote = getNote(position);
                //setTitle("Criar Nota");
                //fragment.beginTransaction().replace(R.id.content_fragment, new NoteInfoFragment()).commit();
                activity.startActivity(i);
            }
        });


        holder.btFavorite.setImageResource(R.drawable.ic_action_unfavorite);
        if(ActionAdapter.getFavorite() != null) {
            for (int i = 0; i < ActionAdapter.getFavorite().length; i++) {
                String ch = ActionAdapter.getFavorite()[i];
                String str = String.valueOf(noteList.get(position).getNoteId());
                if (ch.equals(str)) {
                    holder.btFavorite.setImageResource(R.drawable.ic_action_favorite);
                    holder.btFavorite.setTag(R.drawable.ic_action_favorite);
                    break;
                }else{
                    holder.btFavorite.setTag(R.drawable.ic_action_unfavorite);
                }
            }
        }
        holder.btLike.setImageResource(R.drawable.ic_action_dislike);
        if(ActionAdapter.getLike() != null) {
            for (int i = 0; i < ActionAdapter.getLike().length; i++) {
                String ch = ActionAdapter.getLike()[i];
                String str = String.valueOf(noteList.get(position).getNoteId());
                if (ch.equals(str)) {
                    holder.btLike.setImageResource(R.drawable.ic_action_like);
                    holder.btLike.setTag(R.drawable.ic_action_like);
                    break;
                }else{
                    holder.btLike.setTag(R.drawable.ic_action_dislike);
                }
            }
        }

        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActionUser.action(MainActivity.userLogado.getId(),noteList.get(position).getNoteId(), Config.ACTION_LIKE, activity);
                //ActionAdapter.action(MainActivity.userLogado.getId(),activity);
                //int likes = Integer.parseInt(holder.lblLike.getText().toString());
                if((int)holder.btLike.getTag() == R.drawable.ic_action_dislike){
                    holder.btLike.setImageResource(R.drawable.ic_action_like);
                    holder.btLike.setTag(R.drawable.ic_action_like);

                    //holder.lblLike.setText(String.valueOf(likes + 1));
                }else {
                    holder.btLike.setImageResource(R.drawable.ic_action_dislike);
                    holder.btLike.setTag(R.drawable.ic_action_dislike);
                    /*if(likes > 0)
                        holder.lblLike.setText(String.valueOf(likes - 1));*/
                }
                //Toast.makeText(activity,"Você curtiu isto.",Toast.LENGTH_SHORT).show();
            }
        });


        holder.btFavorite.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActionUser.action(MainActivity.userLogado.getId(),noteList.get(position).getNoteId(), Config.ACTION_FAVORITE, activity);
                //ActionAdapter.action(MainActivity.userLogado.getId(),activity);
                //int favorites = Integer.parseInt(holder.lblFavorite.getText().toString());
                if((int)holder.btFavorite.getTag() == R.drawable.ic_action_unfavorite){
                    holder.btFavorite.setImageResource(R.drawable.ic_action_favorite);
                    holder.btFavorite.setTag(R.drawable.ic_action_favorite);
                    //holder.lblFavorite.setText(String.valueOf(favorites + 1));
                }else {
                    holder.btFavorite.setImageResource(R.drawable.ic_action_unfavorite);
                    holder.btFavorite.setTag(R.drawable.ic_action_unfavorite);
                    /*if(favorites > 0)
                        holder.lblFavorite.setText(String.valueOf(favorites - 1));*/
                }
            }
        });

        /*holder.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Compartilhando nota:\n" + noteList.get(position).getBody() + "\n - via NoteCloud";
                String shareSub = noteList.get(position).getTitle();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Compartilhe como mensagem para..."));

                ActionUser.action(MainActivity.userLogado.getId(),noteList.get(position).getNoteId(), Config.ACTION_SHARE, activity);
            }
        });*/

        /*holder.btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", holder.descricao.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity,"Nota copiada para área de transferência.",Toast.LENGTH_SHORT).show();
            }
        });*/
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

    public NoteModel getNote(int position){
        return noteList.get(position);
    }

    public List<NoteModel> getList(){
        return noteList;
    }

}
