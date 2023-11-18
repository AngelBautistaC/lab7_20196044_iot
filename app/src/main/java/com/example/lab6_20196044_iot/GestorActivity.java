package com.example.lab6_20196044_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class GestorActivity extends AppCompatActivity {

    private Button cerrar;
    private Button datos;
    private Button fotos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor);


        cerrar=findViewById(R.id.Cerrar);
        datos=findViewById(R.id.Buttondatos);
        fotos=findViewById(R.id.Buttonfotos);

        cerrar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        datos.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), DatosActivity.class));
        });

        fotos.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), FotosActivity.class));
        });
    }
}