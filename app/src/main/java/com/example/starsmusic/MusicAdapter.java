package com.example.starsmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    private int resourceId;
    public MusicAdapter(Context context, int textViewResourceId, List<Music> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 获取musicList的所有音乐项
        Music music = (Music) getItem(position);
        View view;
        // 缓存布局不存在，则加载布局
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }
        // 直接加载缓存布局
        else{
            view = convertView;
        }
        // 获取控件id
        ImageView imageView = view.findViewById(R.id.iv_list);
        TextView textView1 = view.findViewById(R.id.tv_list1);
        TextView textView2 = view.findViewById(R.id.tv_list2);
        // 对每一项的控件应用布局
        imageView.setImageResource(music.getPhoto());
        textView1.setText(music.getName());
        textView2.setText(music.getSinger());
        return view;
    }
}
