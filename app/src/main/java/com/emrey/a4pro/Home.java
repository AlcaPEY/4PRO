package com.emrey.a4pro;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private Button button_login_jav;
    private Button button_add_user_jav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button_add_user_jav = findViewById(R.id.button_add_users);
        button_add_user_jav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity_add();
            }
        });
        button_login_jav = findViewById(R.id.button_login);
        button_login_jav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity_login();
            }
        });
    }
    public void OpenActivity_login(){

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    public void OpenActivity_add(){
        Intent intent2 = new Intent(this, Add.class);
        startActivity(intent2);
    }
}

