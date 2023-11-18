package com.example.lab6_20196044_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private Button loginButton;

    private EditText correo;
    private EditText contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            redirectUserBasedOnRole(user.getEmail());
        } else {
            setupLoginButton();

        }
    }

    private void setupLoginButton() {
        loginButton=findViewById(R.id.loginButton);
        correo=findViewById(R.id.correo);
        contrasena=findViewById(R.id.contrasena);

        loginButton.setOnClickListener(v -> {
            String email =correo.getText().toString();
            String password =contrasena.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                performLogin(email, password);
            } else {
                Toast.makeText(MainActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        redirectUserBasedOnRole(email);
                    } else {
                        Toast.makeText(MainActivity.this, "Error en el inicio de sesión.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectUserBasedOnRole(String email) {
        Query query = db.collection("usuarios").whereEqualTo("correo", email);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    // El documento existe, puedes obtener los datos
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    Map<String, Object> data = document.getData();
                    String rol = (String) data.get("rol");
                    String estadoStr = (String) data.get("estado");

                    SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tipoUsuario", rol);
                    editor.apply();

                    if("activo".equals(estadoStr)){
                        switch (rol) {
                            case "gestor":
                                startActivity(new Intent(MainActivity.this, GestorActivity.class));
                                break;

                            case "cliente":
                                startActivity(new Intent(MainActivity.this, ClienteActivity.class));
                                break;


                        }
                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "La cuenta no se encuentra habilitada, comuníquese con el administrador.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // El documento no existe
                    Toast.makeText(MainActivity.this, "Usuario no encontrado en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error al obtener el documento
                Toast.makeText(MainActivity.this, "Error al obtener datos del usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}