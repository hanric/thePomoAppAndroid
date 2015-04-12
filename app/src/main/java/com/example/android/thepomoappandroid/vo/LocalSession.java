package com.example.android.thepomoappandroid.vo;

/**
 * Created by Enric on 11/04/2015.
 */
public class LocalSession {

    private int id;
    private int num;
    private String description;

    public LocalSession(int id, int num, String description) {
        super();
        this.id = id;
        this.num = num;
        this.description = description;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
