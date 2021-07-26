package com.example.starsmusic;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class fragment_list extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MyViewModel myViewModel;
        myViewModel = ViewModelProviders.of(requireActivity()).get(MyViewModel.class);
        final MusicAdapter musicAdapter = new MusicAdapter(requireActivity(), R.layout.music_item,
                myViewModel.musicList);
        listView = requireView().findViewById(R.id.list_view);
        listView.setAdapter(musicAdapter);
        // 设置音乐列表单击播放事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = myViewModel.musicList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("music", music);
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.fragment_show, bundle);
            }
        });
        // 设置音乐列表长按删除事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(getString(R.string.delete_dialog_title));
                builder.setPositiveButton(R.string.delete_positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myViewModel.musicList.remove(position);
                        DataSupport.deleteAll(Music.class, "name = ?", String.valueOf(myViewModel.musicList.get(position).getName()));
                        Toast.makeText(requireActivity(), "音乐项删除成功", Toast.LENGTH_LONG).show();
                        musicAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.delete_negative_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
}
