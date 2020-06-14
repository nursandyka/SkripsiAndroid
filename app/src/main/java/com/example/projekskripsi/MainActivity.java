package com.example.projekskripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView mainimg;
    TextView welcometext, desctext;
    Button mulai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainimg = (ImageView) findViewById(R.id.MainImg);
        welcometext = (TextView) findViewById(R.id.WelcomeText);
        desctext = (TextView) findViewById(R.id.deskripsi);
        mulai = (Button) findViewById(R.id.mulai);

        mulai.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(i);
            }
        }));

    }
}
