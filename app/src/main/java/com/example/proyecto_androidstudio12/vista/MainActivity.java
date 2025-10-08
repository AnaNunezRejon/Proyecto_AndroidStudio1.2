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


    // DECLARACIÓN DE VARIABLES
    private EditText etUsuario;// Campo de texto para el nombre de usuario
    private EditText etContrasena; // Campo de texto para la contraseña
    private Button btnCrearUsuario;// Botón para crear usuario/iniciar sesión
    private LoginController controlador;    // Controlador que maneja la lógica de validación


    // METODO PRINCIPAL: onCreate ---------------------------------------------------------------------------
    // Este metodo se ejecuta cuando la actividad se crea por primera vez

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establece el diseño visual (layout) desde activity_main.xml
        setContentView(R.layout.activity_main);

        // INICIALIZAR CONTROLADOR - Maneja toda la lógica de negocio
        controlador = new LoginController();

        // VINCULAR COMPONENTES - Conecta variables Java con elementos XML
        etUsuario = findViewById(R.id.et_username);        // Campo de usuario
        etContrasena = findViewById(R.id.et_password);     // Campo de contraseña
        btnCrearUsuario = findViewById(R.id.btn_login);    // Botón de login

        // Personalizar texto del botón
        btnCrearUsuario.setText("CREAR USUARIO");

        // ESTABLECER COLORES INICIALES - Ambos campos en gris neutro
        establecerColorCampo(etUsuario, R.color.gris_neutro);
        establecerColorCampo(etContrasena, R.color.gris_neutro);

        // CONFIGURAR ESCUCHADORES EN TIEMPO REAL
        // Estos detectan cuando el usuario escribe en los campos
        agregarEscuchadorTexto(etUsuario, true);       // true = es campo usuario
        agregarEscuchadorTexto(etContrasena, false);   // false = es campo contraseña

        // CONFIGURAR BOTÓN - Qué pasa cuando se hace clic
        btnCrearUsuario.setOnClickListener(v -> intentarRegistro());
    }


    // METODO: intentarRegistro  ---------------------------------------------------------------------------
    // Se ejecuta cuando el usuario presiona el botón "CREAR USUARIO"
    private void intentarRegistro() {
        // OBTENER TEXTO ACTUAL de los campos
        String usuario = etUsuario.getText().toString();
        String contrasena = etContrasena.getText().toString();

        // DELEGAR VALIDACIÓN AL CONTROLADOR
        // El controlador decide si hay errores y cuáles son
        String mensajeError = controlador.obtenerMensajeError(usuario, contrasena);

        // VERIFICAR RESULTADO DE LA VALIDACIÓN
        if (mensajeError.isEmpty()) {
            // CASO ÉXITO: No hay errores, registro válido

            // Pedir al controlador el mensaje de éxito
            String mensajeExito = controlador.crearMensajeRegistroExitoso(usuario, contrasena);

            // Mostrar mensaje de éxito al usuario
            Toast.makeText(this, mensajeExito, Toast.LENGTH_LONG).show();

            // ACTUALIZAR INTERFAZ - Poner ambos campos en verde
            establecerColorCampo(etUsuario, R.color.verde_correcto);
            establecerColorCampo(etContrasena, R.color.verde_correcto);

            // NAVEGAR A SIGUIENTE PANTALLA
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);  // Inicia la nueva actividad
            finish();               // Cierra esta actividad actual

        } else {
            // CASO ERROR: Hay problemas en la validación
            // Mostrar mensaje de error al usuario
            Toast.makeText(this, " Error: " + mensajeError, Toast.LENGTH_LONG).show();
            // PEDIR AL CONTROLADOR qué campos específicamente fallaron
            boolean[] camposConError = controlador.obtenerCamposConError(mensajeError);
            // Actualizar colores de la interfaz basado en lo que dijo el controlador
            manejarColorErrorFinal(camposConError);
        }
    }


    // METODO: manejarColorErrorFinal  ---------------------------------------------------------------------------
    // Aplica colores rojos a los campos que el controlador marcó como erróneos
    private void manejarColorErrorFinal(boolean[] camposConError) {
        // PRIMERO: Reiniciar todos los campos a color gris neutro
        establecerColorCampo(etUsuario, R.color.gris_neutro);
        establecerColorCampo(etContrasena, R.color.gris_neutro);

        // LUEGO: Aplicar rojo solo a los campos que tienen error
        // camposConError[0] = usuario, camposConError[1] = contraseña
        if (camposConError[0]) {
            establecerColorCampo(etUsuario, R.color.rojo_error);
        }
        if (camposConError[1]) {
            establecerColorCampo(etContrasena, R.color.rojo_error);
        }
    }


    // METODO: manejarColorCampo ---------------------------------------------------------------------------
    // Maneja el color de los campos MIENTRAS el usuario escribe
    private void manejarColorCampo(EditText campoTexto, String texto, boolean esUsuario) {
        // Si el campo está vacío, poner color gris (estado neutro)
        if (texto.isEmpty()) {
            establecerColorCampo(campoTexto, R.color.gris_neutro);
            return;
        }
        // CONSULTAR AL CONTROLADOR si el campo actual es válido
        boolean esValido = controlador.campoEsValido(texto, esUsuario);

        // Aplicar color basado en la respuesta del controlador
        if (esValido) {
            // Verde si el controlador dice que es válido
            establecerColorCampo(campoTexto, R.color.verde_correcto);
        } else {
            // Rojo si el controlador dice que no es válido
            establecerColorCampo(campoTexto, R.color.rojo_error);
        }
    }


    // METODO: agregarEscuchadorTexto ---------------------------------------------------------------------------
    // Configura un "escuchador" que detecta cuando el usuario escribe en un campo
    private void agregarEscuchadorTexto(final EditText campoTexto, final boolean esUsuario) {
        campoTexto.addTextChangedListener(new TextWatcher() {
            // Se ejecuta ANTES de que el texto cambie
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // Se ejecuta MIENTRAS el texto está cambiando
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // Se ejecuta DESPUÉS de que el texto ha cambiado
            @Override
            public void afterTextChanged(Editable s) {
                // Llama al metodo que maneja el color del campo
                manejarColorCampo(campoTexto, s.toString(), esUsuario);
            }
        });
    }


    // METODO: establecerColorCampo ---------------------------------------------------------------------------
    // Metodo utilitario que aplica color al texto y borde de un campo
    private void establecerColorCampo(EditText campoTexto, int idRecursoColor) {
        // Obtener el color real desde el archivo de recursos colors.xml
        int color = ContextCompat.getColor(this, idRecursoColor);

        // Aplicar el color al TEXTO del campo
        campoTexto.setTextColor(color);

        // Aplicar el color al BORDE/FONDO del campo
        campoTexto.getBackground().setTint(color);
    }
}