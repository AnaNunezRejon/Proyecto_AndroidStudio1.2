#  MainActivity

```
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
```
# SecondActivity
```
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
```
# public class LoginController

```

package com.example.proyecto_androidstudio12.controller;

import java.util.regex.Pattern;

public class LoginController {

    private static final Pattern CARACTERES_PERMITIDOS_SEGUROS =
            Pattern.compile("^[a-zA-Z0-9._]+$");
    private static final int LARGO_MINIMO_USUARIO = 3;
    private static final int LARGO_MINIMO_CONTRASENA = 6;


    public String validarCampoIndividual(String entrada, boolean esUsuario) {
        if (entrada.isEmpty()) {
            return "";
        }

        int largoMinimo = esUsuario ? LARGO_MINIMO_USUARIO : LARGO_MINIMO_CONTRASENA;
        String nombreCampo = esUsuario ? "usuario" : "contraseña";

        // Chequeo de Longitud
        if (entrada.length() < largoMinimo) {
            return "El " + nombreCampo + " es muy corto (mín. " + largoMinimo + ").";
        }

        // Chequeo de Caracteres
        if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(entrada).matches()) {
            return "El " + nombreCampo + " no puede tener caracteres ^[a-zA-Z0-9._]+$.";
        }

        return "";
    }


    public String obtenerMensajeError(String usuario, String contrasena) {
        // 1. Campos Vacíos
        if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
            return "Debes rellenar ambos campos.";
        }

        // 2. Errores de Usuario (Longitud y Caracteres)
        String errorUsuario = validarCampoIndividual(usuario, true);
        if (!errorUsuario.isEmpty()) {
            // El mensaje ya es claro
            return errorUsuario;
        }

        // 3. Errores de Contraseña (Longitud y Caracteres)
        String errorContrasena = validarCampoIndividual(contrasena, false);
        if (!errorContrasena.isEmpty()) {
            // El mensaje ya es claro
            return errorContrasena;
        }

        return ""; // ¡Registro exitoso!
    }


    public String convertirAASCII(String texto) {
        if (texto == null || texto.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            sb.append((int) texto.charAt(i));
            if (i < texto.length() - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public String crearMensajeRegistroExitoso(String usuario, String contrasena) {
        String contrasenaASCII = convertirAASCII(contrasena);
        return "✅ Usuario " + usuario + " con contraseña codificada:\n" +
                contrasenaASCII + "\nha sido creado.";
    }
}

/*
package com.example.proyecto_androidstudio12.controller;

import java.util.regex.Pattern;

public class LoginController {

    private static final Pattern CARACTERES_PERMITIDOS_SEGUROS =
            Pattern.compile("^[a-zA-Z0-9._]+$");


    public boolean validarRegistro(String username, String password) {

        // 1. Validar campos vacíos
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        // 2. Validar longitud mínima
        if (username.length() < 3 || password.length() < 6) {
            return false;
        }

        // 3. Validar caracteres no permitidos (SEGURIDAD)
        if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(username).matches() ||
                !CARACTERES_PERMITIDOS_SEGUROS.matcher(password).matches()) {
            return false;
        }

        // Si ha pasado todas las validaciones de formato y seguridad,
        // SIMULAMOS que el registro fue exitoso.
        return true;
    }

    // Este metodo obtiene el mensaje de error específico si el registro falla
    public String obtenerMensajeError(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            return "Debes rellenar todos los campos.";
        }
        if (username.length() < 3) {
            return "El usuario debe tener al menos 3 caracteres.";
        }
        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(username).matches()) {
            return "El usuario solo puede contener letras, números, puntos (.) y guiones bajos (_).";
        }
        if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(password).matches()) {
            return "La contraseña solo puede contener letras, números, puntos (.) y guiones bajos (_).";
        }

        return "";
    }

    public String convertirAASCII(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            int valorASCII = (int) caracter;

            sb.append(valorASCII);

            if (i < texto.length() - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public String crearMensajeRegistroExitoso(String username, String password) {
        String passwordASCII = convertirAASCII(password);

        // Formato solicitado: Usuario [username] con contraseña codificada [ASCII]
        return "Usuario " + username + " con contraseña codificada:\n" +
                passwordASCII + "\nha sido creado.";
    }

    public String validarCampoIndividual(String input, boolean esUsuario) {
        if (input.isEmpty()) {
            // Si está vacío, color gris.
            return "";
        }

        // 1. Validar longitud mínima
        int minLength = esUsuario ? 3 : 6;
        String campoNombre = esUsuario ? "El usuario" : "La contraseña";

        if (input.length() < minLength) {
            return "El"+ campoNombre + " debe tener al menos " + minLength + " caracteres.";
        }

        // 2. Validar caracteres no permitidos
        if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(input).matches()) {
            String tipoCampo = esUsuario ? "usuario" : "contraseña";
            return "El " + tipoCampo + " solo puede contener letras, números, puntos (.) y guiones bajos (_).";
        }

        return ""; // Correcto
    }

}
*/


```
# public class Usuario
```
package com.example.proyecto_androidstudio12.model;

public class Usuario {
    // Para este ejemplo simple, almacenamos un usuario "válido"
    // En una aplicación real, esto provendría de una base de datos.
    private static final String USUARIO_VALIDO = "admin";
    private static final String CONTRASENA_VALIDA = "123456";

    // En este ejemplo, el modelo se mantiene simple y solo expone
    // la lógica para verificar credenciales (que es una lógica de datos).

}
```
# activity_main.xml
```

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp"
    tools:context=".vista.MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="30dp"
        android:text="INICIO DE SESIÓN"
        android:textColor="#0D0D0D"
        android:textSize="24sp"
        android:textStyle="" />

    <EditText
        android:id="@+id/et_username"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Usuario (Solo letras y números)"
        android:inputType="textPersonName"
        android:maxLength="15"
        android:minHeight="48dp" />

    <EditText
        android:id="@+id/et_password"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Contraseña (Mínimo 6 caracteres)"
        android:inputType="textPassword"
        android:maxLength="15"
        android:minHeight="48dp" />

    <Button
        android:id="@+id/btn_login"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Acceder"
        android:layout_marginTop="20dp" />

</LinearLayout>
```
# activity_second.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp"
    tools:context=".vista.SecondActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="20dp"
        android:text="¡ACCESO CONCEDIDO!"
        android:textSize="28sp"
        android:textStyle="bold" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Usuario Creado Correctamente (Simulación de registro exitoso)."
        android:textSize="18sp"
        android:gravity="center"
        android:layout_marginBottom="50dp" />

    <Button
        android:id="@+id/btn_volver"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Volver al Inicio para Logarte" />

</LinearLayout>
```

