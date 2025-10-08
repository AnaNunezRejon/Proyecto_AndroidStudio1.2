package com.example.proyecto_androidstudio12.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto_androidstudio12.R;
import com.example.proyecto_androidstudio12.controller.LoginController;

public class MainActivity extends AppCompatActivity {

    // Referencias a los componentes de la interfaz
    private EditText etUsuario, etContrasena;
    private Button btnCrearUsuario;
    private LoginController controlador; // Nuestro objeto que maneja la lógica


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el Controlador
        controlador = new LoginController();

        // Conectar las variables de Java con los IDs del XML
        etUsuario = findViewById(R.id.et_username);
        etContrasena = findViewById(R.id.et_password);
        btnCrearUsuario = findViewById(R.id.btn_login);
        btnCrearUsuario.setText("CREAR USUARIO");

        // Colores iniciales
        establecerColorCampo(etUsuario, R.color.gris_neutro);
        establecerColorCampo(etContrasena, R.color.gris_neutro);

        // La Vista escucha eventos del usuario
        // Escucha en tiempo real para el feedback rojo/verde
        agregarEscuchadorTexto(etUsuario, true);
        agregarEscuchadorTexto(etContrasena, false);

        // Escucha del botón para la acción final
        btnCrearUsuario.setOnClickListener(v -> intentarRegistro());
    }


    private void intentarRegistro() {
        String usuario = etUsuario.getText().toString();
        String contrasena = etContrasena.getText().toString();

        // Pedir al Controller el mensaje de error final.
        String mensajeError = controlador.obtenerMensajeError(usuario, contrasena);

        if (mensajeError.isEmpty()) {
            // ÉXITO: Recibe el mensaje final del Controller y lo muestra.
            String mensajeExito = controlador.crearMensajeRegistroExitoso(usuario, contrasena);
            Toast.makeText(this, mensajeExito, Toast.LENGTH_LONG).show();

            // Actualizar UI y navegar
            establecerColorCampo(etUsuario, R.color.verde_correcto);
            establecerColorCampo(etContrasena, R.color.verde_correcto);

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Muestra el mensaje de error que le dio el Controller.
            Toast.makeText(this, " Error: " + mensajeError, Toast.LENGTH_LONG).show();

            // Pone en rojo el campo que falló según el mensaje de error.
            manejarColorErrorFinal(mensajeError);
        }
    }

    private void manejarColorErrorFinal(String mensajeError) {
        // Reiniciar los colores a gris
        establecerColorCampo(etUsuario, R.color.gris_neutro);
        establecerColorCampo(etContrasena, R.color.gris_neutro);

        // La lógica se basa en las palabras clave del Controller
        if (mensajeError.contains("usuario") || mensajeError.contains("ambos")) {
            establecerColorCampo(etUsuario, R.color.rojo_error);
        }
        if (mensajeError.contains("contraseña") || mensajeError.contains("ambos")) {
            establecerColorCampo(etContrasena, R.color.rojo_error);
        }
    }


    private void manejarColorCampo(EditText campoTexto, String texto, boolean esUsuario) {

        if (texto.isEmpty()) {
            establecerColorCampo(campoTexto, R.color.gris_neutro);
            return;
        }
        // Pide al Controller que valide solo este campo
        String mensajeError = controlador.validarCampoIndividual(texto, esUsuario);

        if (!mensajeError.isEmpty()) {
            // Si hay error de formato/longitud -> Rojo
            establecerColorCampo(campoTexto, R.color.rojo_error);
        } else {
            // Si no hay error -> Verde
            establecerColorCampo(campoTexto, R.color.verde_correcto);
        }
    }

    private void agregarEscuchadorTexto(final EditText campoTexto, final boolean esUsuario) {
        campoTexto.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                manejarColorCampo(campoTexto, s.toString(), esUsuario);
            }
        });
    }

    private void establecerColorCampo(EditText campoTexto, int idRecursoColor) {
        int color = ContextCompat.getColor(this, idRecursoColor);
        campoTexto.setTextColor(color);
        campoTexto.getBackground().setTint(color);
    }
}
/*

FUNCIONA-------------------------------------------------------------------------------

package com.example.proyecto_androidstudio12.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto_androidstudio12.R;
import com.example.proyecto_androidstudio12.controller.LoginController;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private LoginController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new LoginController();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setText("CREAR USUARIO");

        // Inicializar los colores a gris al inicio
        setEditTextColor(etUsername, R.color.gris_neutro);
        setEditTextColor(etPassword, R.color.gris_neutro);

        // AÑADIR LISTENERS DE TEXTO (TextWatcher)
        addTextWatcherToField(etUsername, true); // true indica que es el usuario
        addTextWatcherToField(etPassword, false); // false indica que es la contraseña

        btnLogin.setOnClickListener(v -> attemptRegistration());
    }


    private void validateField(EditText editText, String text, boolean isUsername) {

        // Si el campo está vacío, lo ponemos gris (neutro) y salimos.
        if (text.isEmpty()) {
            setEditTextColor(editText, R.color.gris_neutro);
            return;
        }

        // Usamos la función individual del Controller
        String errorMsg = controller.validarCampoIndividual(text, isUsername);

        // Si hay un error de formato/longitud, ROJO. Si está bien, VERDE.
        if (!errorMsg.isEmpty()) {
            // La validación FALLÓ
            setEditTextColor(editText, R.color.rojo_error);
        } else {
            // La validación PASÓ
            setEditTextColor(editText, R.color.verde_correcto);
        }
    }


    private void addTextWatcherToField(final EditText editText, final boolean isUsername) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No usado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No usado
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Llamamos a la función de validación con el texto actual
                validateField(editText, s.toString(), isUsername);
            }
        });
    }

    // El metodo setEditTextColor se mantiene igual (mueve esta función al final de la clase)
    private void setEditTextColor(EditText editText, int colorResId) {
        int color = ContextCompat.getColor(this, colorResId);
        editText.setTextColor(color);
        editText.getBackground().setTint(color);
    }

    // El metodo attemptRegistration se mantiene igual (solo se ejecuta al presionar el botón)
    private void attemptRegistration() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // 1. Validar si el registro fue exitoso (Controller)
        boolean registroExitoso = controller.validarRegistro(username, password);

        // Reiniciar colores a gris ANTES de la validación final para limpiarlo
        setEditTextColor(etUsername, R.color.gris_neutro);
        setEditTextColor(etPassword, R.color.gris_neutro);

        if (registroExitoso) {

            // Poner AMBOS en verde
            setEditTextColor(etUsername, R.color.verde_correcto);
            setEditTextColor(etPassword, R.color.verde_correcto);

            String mensajeToast = controller.crearMensajeRegistroExitoso(username, password);
            Toast.makeText(this, mensajeToast, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();

        } else {

            // Identificar cuál falló y ponerlo en rojo
            String mensajeError = controller.obtenerMensajeError(username, password);

            Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();


            // 1. Si el mensaje de error incluye "Usuario" o "rellenar"
            if (mensajeError.contains("Usuario") || (mensajeError.contains("rellenar") && username.isEmpty())) {
                setEditTextColor(etUsername, R.color.rojo_error);
            } else {
                // Si el error no es de usuario, y pasó el chequeo de usuario, lo dejamos en gris neutro
                setEditTextColor(etUsername, R.color.gris_neutro);
            }

            // 2. Si el mensaje de error incluye "Contraseña" o "rellenar"
            if (mensajeError.contains("Contraseña") || (mensajeError.contains("rellenar") && password.isEmpty())) {
                setEditTextColor(etPassword, R.color.rojo_error);
            } else {
                // Si el error no es de contraseña, y pasó el chequeo de contraseña, lo dejamos en gris neutro
                setEditTextColor(etPassword, R.color.gris_neutro);
            }
        }
    }
}
*/