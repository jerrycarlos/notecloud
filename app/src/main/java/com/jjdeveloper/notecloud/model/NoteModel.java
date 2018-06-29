package com.jjdeveloper.notecloud.model;

public class NoteModel {
    private String title;
    private String body;
    private String author;
    private int likes, favorites, shares, fk_user;
    private int visualization; // public, private, anonymous
    private int noteId;
    private String date_created;

    public NoteModel(){    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNoteId(){
        return noteId;
    }

    public void setNoteId(int id){
        this.noteId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getFk_user() {
        return fk_user;
    }

    public void setFk_user(int fk_user) {
        this.fk_user = fk_user;
    }

    public int getVisualization() {
        return visualization;
    }

    public void setVisualization(int visualization) {
        this.visualization = visualization;
    }

    public String getDate_created() { return date_created; }

    public void setDate_created(String date_created) { this.date_created = date_created; }
}
