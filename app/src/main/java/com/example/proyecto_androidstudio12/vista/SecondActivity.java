package com.example.proyecto_androidstudio12.vista;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.proyecto_androidstudio12.R; // Asegúrate que tu paquete R sea correcto

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnVolver = findViewById(R.id.btn_volver);

        // Listener para el botón de "Volver al Inicio"
        btnVolver.setOnClickListener(v -> {
            // Crea un Intent para volver a MainActivity
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra esta Activity para liberar recursos
        });
    }
}