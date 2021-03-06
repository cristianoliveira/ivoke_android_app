package com.app.ivoke.objects;

public class Conversation {

    //Database mirror
    private int id;
    private int user_one_id;
    private int user_two_id;
    private int time;
    private int status;

    //Auxiliar
    private String user_one_nome;
    private String user_two_nome;
    private ConvesationMessage last_message;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserOneId() {
        return user_one_id;
    }
    public void setUserOneId(int user_one_id) {
        this.user_one_id = user_one_id;
    }
    public int getUserTwoId() {
        return user_two_id;
    }
    public void setUserTwoId(int user_two_id) {
        this.user_two_id = user_two_id;
    }
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getUserOneNome() {
        return user_one_nome;
    }
    public void setUserOneNome(String user_one_nome) {
        this.user_one_nome = user_one_nome;
    }
    public String getUserTwoNome() {
        return user_two_nome;
    }
    public void setUserTwoNome(String user_two_nome) {
        this.user_two_nome = user_two_nome;
    }
    public String getLastMessage() {
        return last_message.getMessage();
    }
    public void setLastMessage(ConvesationMessage last_message) {
        this.last_message = last_message;
    }

}
