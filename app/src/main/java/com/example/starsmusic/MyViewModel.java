package com.example.starsmusic;



import androidx.lifecycle.ViewModel;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    List<Music> musicList = new ArrayList<>();

    private int[] song = new int[]{R.string.ara_song,R.string.jlc_song,R.string.hsgddj_song,R.string.jh_song,
            R.string.lx_song, R.string.nz_song,R.string.qjf_song,R.string.lgj_song,R.string.xiang_song,
            R.string.yggs_song};
    private int[] singer = new int[]{R.string.ara_singer,R.string.jlc_singer,R.string.hsgddj_singer,R.string.jh_singer,
            R.string.lx_singer, R.string.nz_singer,R.string.qjf_singer,R.string.lgj_singer,R.string.xiang_singer,
            R.string.yggs_singer};
    private int[] photo = new int[]{R.drawable.ara,R.drawable.jlc,R.drawable.hsgddj,R.drawable.jh,R.drawable.lx,
            R.drawable.nz,R.drawable.qjf,R.drawable.lgj,R.drawable.xiang,R.drawable.yggs};
    private int[] mp3 = new int[]{R.raw.ara, R.raw.jlc,R.raw.hsgddj,R.raw.jh,R.raw.lx,R.raw.nz,R.raw.qjf,R.raw.lgj,
            R.raw.xiang,R.raw.yggs};

    void initMusic(){
        Connector.getDatabase();
        for(int i=0; i < mp3.length; i++){
            Music music = new Music(song[i], singer[i], photo[i], mp3[i]);
            music.save();
            musicList.add(music);
        }
    }

    Music preMusic(List<Music> musicList, Music music){
        int i = musicList.indexOf(music);
        if (i == 0){
            return musicList.get(musicList.size() - 1);
        }else {
            return musicList.get(i- 1);
        }
    }

    Music nextMusic(List<Music> musicList, Music music){
        int i = musicList.indexOf(music);
        if (i == musicList.size() - 1){
            return musicList.get(0);
        }else {
            return musicList.get(i + 1);
        }
    }

}
