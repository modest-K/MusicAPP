# MusicApp

An easy app with music  

## 实现功能  

登录  
查看音乐列表  
删除音乐  
播放/暂停/停止音乐  
切换上一首/下一首  
显示乐曲名称  
显示当前播放时长和总时长  
进度条下显示播放进度  

## 关键技术  

Viewmodel框架来管理数据、Navigation导航  

## 重难点  

设计过程中遇到的困难是进度条和当前播放时间的实时更新。起初用的是Timer和TimeMask构成的定时器，每过一段时间便执行更新语句，但是Timer执行周期任务时依赖系统时间，如果当前系统时间发生变化会出现一些执行上的变化，其中ScheduledExecutorService基于时间的延迟，不会由于系统时间的改变发生执行变化，因此会报错。
上述问题解决办法是：改用Handler和Runnable，启用一个新线程，大致代码结构如下：
``` Handler handler = new Handler();
Runnable updateProgress = new Runnable() {
    @Override
    public void run() {
        Progress1;
        Progress2；
        Progress3；
    }
};
handler.post(updateProgress); // 启动线程
handler.removeCallbacks(updateProgress); // 取消线程
