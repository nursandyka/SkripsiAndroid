package com.example.projekskripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    ImageView ambilfoto, unggah, tentang, keluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ambilfoto = findViewById(R.id.ambil);
        unggah = findViewById(R.id.unggah);
        tentang = findViewById(R.id.tentang);
        keluar = findViewById(R.id.keluar);

        ambilfoto.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, GetFromCameraActivity.class);
                startActivity(i);
            }
        }));

        unggah.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, SelectFromGalleryActivity.class);
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
