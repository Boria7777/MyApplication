package com.snydu.icuvideo.icuvideoapp.presenter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.snydu.icuvideo.icuvideoapp.MainActivity;
import com.snydu.icuvideo.icuvideoapp.R;

/**
 * Created by Boria on 2016/3/9.
 */
public class MainPresenter {
    private MainActivity view;
    private EditText loginIdEdittext;
    private EditText loginPasswordEdittext;
    private Button loginButton;
    private String tag = "Main-Presenter";

    public void onGetView(MainActivity view) {
        this.view = view;
        Log.d(tag, "66666666:");
        loginIdEdittext = (EditText) view.findViewById(R.id.Login_Id_edittext_mainactivity);
        loginPasswordEdittext = (EditText) view.findViewById(R.id.Login_password_edittext_mainactivity);
        loginButton = (Button) view.findViewById(R.id.Login_button_mainactivity);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Get protocol length:");
                getText();
            }
        });

    }


    private void getText() {
        String id = loginIdEdittext.getText().toString();
        String password = loginPasswordEdittext.getText().toString();
        Log.d(tag, id);
        Log.d(tag, password);
    }

}
