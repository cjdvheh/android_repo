package com.cookandroid.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Memo")
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private int id =0; // 고유값

    private String date; // 생성 날짜
    private String content; //내용
    private String title;//제목

    private boolean isabled;

    private boolean isfind;

    public int getId(){return id;}

    public void setId(int id) {this.id = id;}

    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
    }
    public String getContent(){return content;}

    public void setTitle(String title){
        this.title=title;
    }

    public void setDate(String date){
        this.date=date;
    }
    public void setContent(String content){this.content = content;}

    public void setIsabled(boolean isabled){this.isabled=isabled;}

    public boolean getIsabled(){return isabled;}
    public boolean isEmpty() {
        return title.isEmpty() && content.isEmpty();
    }

    public void setIsfind(boolean isfind) {
       this.isfind=isfind;
    }

    public boolean getIsfind(){
        return isfind;
    }

    public Memo(String date, String title, String context){
        this.date=date;
        this.title=title;
        this.content =context;
        setIsfind(false);
        setIsabled(true);
    }

    public Memo(){

    }
}
