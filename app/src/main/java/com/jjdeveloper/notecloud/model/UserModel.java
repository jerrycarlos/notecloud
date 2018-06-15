package com.jjdeveloper.notecloud.model;

public class UserModel {
    private int id;
    private String nome, email, login, telefone;

    public UserModel() {
    }

    public UserModel(String nome, String email, String login, String telefone) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
