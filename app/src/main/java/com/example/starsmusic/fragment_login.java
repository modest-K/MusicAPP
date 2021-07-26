package com.example.starsmusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_login extends Fragment {
    private Button btn_login;
    private EditText userText;
    private EditText passWordText;
    private CheckBox checkBox;

    private String user, password;

    private SharedPreferences.Editor editor;

    public fragment_login() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_login = getView().findViewById(R.id.button_login);
        checkBox = getView().findViewById(R.id.checkBox);
        userText = getView().findViewById(R.id.user);
        passWordText = getView().findViewById(R.id.password);

        final SharedPreferences prefData = requireActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        checkBox.setChecked(prefData.getBoolean("isChecked", false));
        userText.setText(prefData.getString("name", ""));
        passWordText.setText(prefData.getString("password", ""));

        // 登录按钮响应事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户姓名和密码
                user = userText.getText().toString().trim();
                password = passWordText.getText().toString().trim();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)){
                    // 响应用户名或密码为空值
                    Toast.makeText(requireActivity(), "用户名或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if (user.equals("kells") && password.equals("123456")){
                    // 响应用户名和密码均正确
                    editor = prefData.edit();
                    if (checkBox.isChecked()){
                        editor.putString("name", user);
                        editor.putString("password", password);
                        editor.putBoolean("isChecked", true);
                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_fragment_login_to_fragment_list);
                }else { // 响应用户或密码错误
                    Toast.makeText(requireActivity(), "用户名或密码输入错误",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
