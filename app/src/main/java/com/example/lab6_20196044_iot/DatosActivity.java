package com.example.lab6_20196044_iot;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class DatosActivity extends AppCompatActivity {

    private String documentId;
    private TextView correo;
    private TextView salon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference reference;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        correo=findViewById(R.id.correo);
        salon=findViewById(R.id.salon);

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            db.collection("salones")
                    .whereEqualTo("correo", userEmail)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot document : querySnapshot) {
                            documentId = document.getId();
                            correo.setText(document.getString("correo"));
                            salon.setText(document.getString("nombre"));

                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}