package com.example.starsmusic;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;




/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_show extends Fragment {

    private MediaPlayer mediaPlayer;
    private Music currentMusic; // 当前播放音乐
    private boolean isPause = false; // 是否暂停标志
    private  boolean isStop = false; // 是否停止标志
    // 界面控件
    private ImageView imageView;
    private TextView musicName;
    private TextView musicTime;
    private TextView currentTime;
    private Button buttonPause;
    private Button buttonStop;
    private Button buttonPre;
    private Button buttonNext;
    private SeekBar seekBar;

    public fragment_show() {}

    // 更新MediaPlayer
    private MediaPlayer loadMusic(Music music){
        mediaPlayer = MediaPlayer.create(requireActivity(), music.getMp3());
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    // 格式化歌曲时间
    private String getTime(int time){
        String min, sec;
        int m = time /1000 / 60;
        int s = time /1000 % 60;
        if (m > 9) {
            min = String.valueOf(m);
        }else {
            min = '0' + String.valueOf(m);
        }
        if (s > 9) {
            sec = String.valueOf(s);
        }else {
            sec = '0' + String.valueOf(s);
        }
        return min + ':' + sec;
    }

    // 实时获取歌曲进度
    Handler handler = new Handler();
    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            currentTime.setText(getTime(mediaPlayer.getCurrentPosition()));
            handler.postDelayed(updateProgress, 100);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MyViewModel myViewModel;
        myViewModel = ViewModelProviders.of(requireActivity()).get(MyViewModel.class);

        // 接收并准备音乐对象
        currentMusic = (Music) getArguments().getSerializable("music");
        mediaPlayer = loadMusic(currentMusic);

        // 获取控件并设置界面
        buttonPause = getView().findViewById(R.id.pauseButton);
        buttonStop = getView().findViewById(R.id.stopButton);
        buttonPre = getView().findViewById(R.id.preButton);
        buttonNext = getView().findViewById(R.id.nextButton);
        seekBar = getView().findViewById(R.id.seekBar);
        imageView = getView().findViewById(R.id.showView);
        musicName = getView().findViewById(R.id.mName);
        musicTime = getView().findViewById(R.id.mTime);
        currentTime = getView().findViewById(R.id.mTime2);

        musicName.setText(currentMusic.getName());
        musicTime.setText(getTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        imageView.setImageResource(currentMusic.getPhoto());

        // imageView旋转动画
        final ObjectAnimator animator; // 属性动画对象
        // 用imageView初始化动画对象，并设置动画方式为"rotation"，角度从0°-360°
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0.0f, 360.0f);
        animator.setDuration(30000); // 设置旋转一圈时间为30s
        animator.setInterpolator(new LinearInterpolator()); // 设置旋转速度-linearInterpolator为线性匀速
        animator.setRepeatCount(ObjectAnimator.INFINITE); // 无穷次旋转
        animator.start();
        mediaPlayer.start();
        handler.post(updateProgress);

        // 设置按钮点击事件
        buttonPause.setOnClickListener(new View.OnClickListener() {
            // 暂停
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){ // 处于播放状态时暂停
                    mediaPlayer.pause();
                    animator.pause();
                    buttonPause.setText("播放");
                    isPause = true;
                } else { // 从暂停状态继续播放或停止状态重新播放
                    isPause = false;
                    isStop = false;
                    mediaPlayer.start();
                    animator.resume();
                    handler.post(updateProgress);
                    buttonPause.setText("暂停");
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            // 停止
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (!isStop) {
                    mediaPlayer.reset();
                    mediaPlayer = loadMusic(currentMusic);
                    animator.pause();
                    buttonPause.setText("播放");
                    isStop = true;
                }
            }
        });

        buttonPre.setOnClickListener(new View.OnClickListener() {
            // 上一首
            @Override
            public void onClick(View v) {
                currentMusic = myViewModel.preMusic(myViewModel.musicList, currentMusic);
                imageView.setImageResource(currentMusic.getPhoto());
                musicName.setText(currentMusic.getName());
                if (!mediaPlayer.isPlaying()){ // 处于暂停状态或停止状态切上一首音乐
                    buttonPause.setText("暂停");
                    animator.start();
                    isPause = false;
                    isStop = false;
                }
                mediaPlayer.reset();
                mediaPlayer = loadMusic(currentMusic);
                seekBar.setMax(mediaPlayer.getDuration());
                musicTime.setText(getTime(mediaPlayer.getDuration()));
                mediaPlayer.start();
                handler.post(updateProgress);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            // 下一首
            @Override
            public void onClick(View v) {
                currentMusic = myViewModel.nextMusic(myViewModel.musicList, currentMusic);
                imageView.setImageResource(currentMusic.getPhoto());
                musicName.setText(currentMusic.getName());
                if (!mediaPlayer.isPlaying()){ // 处于暂停状态或停止状态切下一首音乐
                    buttonPause.setText("暂停");
                    animator.start();
                    isPause = false;
                    isStop = false;
                }
                mediaPlayer.reset();
                mediaPlayer = loadMusic(currentMusic);
                seekBar.setMax(mediaPlayer.getDuration());
                musicTime.setText(getTime(mediaPlayer.getDuration()));
                mediaPlayer.start();
                handler.post(updateProgress);
            }
        });

        // 设置进度条事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateProgress);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.post(updateProgress);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(updateProgress);
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
