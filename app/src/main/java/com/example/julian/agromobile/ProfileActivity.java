package com.example.julian.agromobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userTxt;
    Button btnCloseSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnCloseSession = (Button) findViewById(R.id.btnCloseSession);
        btnCloseSession.setOnClickListener(this);
        userTxt = (TextView) findViewById(R.id.txt_name_user);
    }

    @Override
    public void onClick(View v) {

    }
}
