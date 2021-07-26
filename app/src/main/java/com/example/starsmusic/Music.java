package com.example.starsmusic;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Music extends DataSupport implements Serializable {
    private int name;
    private int singer;
    private int photo;
    private int mp3;

    public Music(int name, int singer, int photo, int mp3) {
        this.name = name;
        this.singer = singer;
        this.photo = photo;
        this.mp3 = mp3;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getSinger() {
        return singer;
    }

    public void setSinger(int singer) {
        this.singer = singer;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getMp3() {
        return mp3;
    }

    public void setMp3(int mp3) {
        this.mp3 = mp3;
    }

}
