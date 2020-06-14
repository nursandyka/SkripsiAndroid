package com.example.projekskripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Main2Activity extends AppCompatActivity {

    ImageView ambilfoto, unggah, tentang, keluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ambilfoto = (ImageView) findViewById(R.id.ambil);
        unggah = (ImageView) findViewById(R.id.unggah);
        tentang = (ImageView) findViewById(R.id.tentang);
        keluar = (ImageView) findViewById(R.id.keluar);

        ambilfoto.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(i);
            }
        }));

        unggah.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Main4Activity.class);
                startActivity(i);
            }
        }));

        tentang.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Main5Activity.class);
                startActivity(i);
            }
        }));

    }
}
