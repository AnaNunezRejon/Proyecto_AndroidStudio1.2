CONSTRUCCIÓN PASO A PASO DE LA APP
PASO 1: ESTRUCTURA DE PAQUETES
```
app/
├── src/main/java/com/example/proyecto_androidstudio12/
│   ├── controller/
│   │   └── LoginController.java    ← LÓGICA DE NEGOCIO
│   ├── vista/
│   │   └── MainActivity.java       ← INTERFAZ VISUAL
│   │   └── SecondActivity.java       ← INTERFAZ VISUAL
│   └── model/
│   │   └── Alumno.java
└── res/layout/
    └── activity_main.xml           ← DISEÑO DE PANTALLA
```
PASO 2: CREAR EL LAYOUT (activity_main.xml)
Primero diseñamos la pantalla de login con:

2 EditText (usuario y contraseña)

1 Button (crear usuario)

Colores definidos en colors.xml (gris_neutro, rojo_error, verde_correcto)

PASO 3: CONSTRUIR EL CONTROLADOR (LoginController.java)
PASO 3.1 - Validación de Caracteres Válidos

```
// En el controlador creamos este método para verificar caracteres
public String validarCampoIndividual(String entrada, boolean esUsuario) {
// Primero verificamos que no esté vacío
if (entrada.isEmpty()) {
return "";
}

    // Configuramos parámetros según sea usuario o contraseña
    int largoMinimo;
    String nombreCampo;
    if (esUsuario) {
        largoMinimo = LARGO_MINIMO_USUARIO;  // 3 caracteres
        nombreCampo = "usuario";
    } else {
        largoMinimo = LARGO_MINIMO_CONTRASENA; // 6 caracteres  
        nombreCampo = "contraseña";
    }

    // Validación 1: Longitud mínima
    if (entrada.length() < largoMinimo) {
        return "El " + nombreCampo + " es muy corto (mín. " + largoMinimo + ").";
    }

    // Validación 2: Caracteres permitidos (sin caracteres raros)
    if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(entrada).matches()) {
        return "El " + nombreCampo + " no puede tener caracteres ^[a-zA-Z0-9._]+$.";
    }

    return ""; // Si pasa todas las validaciones
}
```
PASO 3.2 - Validación Contra Campos Vacíos

```
// En el controlador, método que verifica campos vacíos
public String obtenerMensajeError(String usuario, String contrasena) {
// PRIMERO: Verificar que no estén vacíos
if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
return "Debes rellenar ambos campos.";
}

    // SEGUNDO: Validar usuario individualmente
    String errorUsuario = validarCampoIndividual(usuario, true);
    if (!errorUsuario.isEmpty()) {
        return errorUsuario; // Si hay error en usuario, lo retornamos
    }

    // TERCERO: Validar contraseña individualmente  
    String errorContrasena = validarCampoIndividual(contrasena, false);
    if (!errorContrasena.isEmpty()) {
        return errorContrasena; // Si hay error en contraseña, lo retornamos
    }

    return ""; // ¡Todo válido!
}
```
PASO 3.3 - Conversión a ASCII

```
// En el controlador, método para convertir a código ASCII
public String convertirAASCII(String texto) {
if (texto.isEmpty()) {
return "";
}

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < texto.length(); i++) {
        // Convertimos cada carácter a su valor numérico ASCII
        sb.append((int) texto.charAt(i));
        if (i < texto.length() - 1) {
            sb.append("-"); // Separador entre números
        }
    }
    return sb.toString();
}
```
PASO 3.4 - Mensaje de Éxito

```
// En el controlador, método que crea el mensaje final
public String crearMensajeRegistroExitoso(String usuario, String contrasena) {
// Llamamos al método de conversión ASCII
String contrasenaASCII = convertirAASCII(contrasena);
return "Usuario " + usuario + " con contraseña codificada:\n" +
contrasenaASCII + "\nha sido creado.";
}
```
PASO 4: CONSTRUIR LA VISTA (MainActivity.java)
PASO 4.1 - Configuración Inicial

```
// En onCreate() vinculamos el XML con el código Java
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main); // ← Cargamos el diseño XML

    // Creamos el controlador (nuestra lógica de negocio)
    controlador = new LoginController();

    // Conectamos variables Java con elementos XML
    etUsuario = findViewById(R.id.et_username);
    etContrasena = findViewById(R.id.et_password);
    btnCrearUsuario = findViewById(R.id.btn_login);
}
```
PASO 4.2 - Validación en Tiempo Real (mientras el usuario escribe)

```
// Configuramos escuchadores que detectan cada letra que se escribe
private void agregarEscuchadorTexto(final EditText campoTexto, final boolean esUsuario) {
campoTexto.addTextChangedListener(new TextWatcher() {
@Override
public void afterTextChanged(Editable s) {
// Por cada letra que se escribe, llamamos a manejarColorCampo
manejarColorCampo(campoTexto, s.toString(), esUsuario);
}
// ... otros métodos del TextWatcher
});
}

private void manejarColorCampo(EditText campoTexto, String texto, boolean esUsuario) {
// CONSULTAMOS AL CONTROLADOR si lo que se escribió es válido
boolean esValido = controlador.campoEsValido(texto, esUsuario);

    // Aplicamos color basado en lo que nos dijo el controlador
    if (esValido) {
        establecerColorCampo(campoTexto, R.color.verde_correcto); // ✅ Verde
    } else {
        establecerColorCampo(campoTexto, R.color.rojo_error);     // ❌ Rojo
    }
}
```
PASO 4.3 - Validación Final (al presionar el botón)

```
// Cuando el usuario presiona "CREAR USUARIO"
private void intentarRegistro() {
// Obtenemos el texto actual de los campos
String usuario = etUsuario.getText().toString();
String contrasena = etContrasena.getText().toString();

    // PASO CRÍTICO: Delegamos toda la validación al controlador
    String mensajeError = controlador.obtenerMensajeError(usuario, contrasena);

    if (mensajeError.isEmpty()) {
        // ÉXITO: Todos los campos son válidos
        
        // Pedimos al controlador el mensaje de éxito con la conversión ASCII
        String mensajeExito = controlador.crearMensajeRegistroExitoso(usuario, contrasena);
        
        // Mostramos el mensaje al usuario
        Toast.makeText(this, mensajeExito, Toast.LENGTH_LONG).show();
        
        // Navegamos a la siguiente pantalla
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        
    } else {
        // ERROR: Mostramos el mensaje que nos dio el controlador
        Toast.makeText(this, " Error: " + mensajeError, Toast.LENGTH_LONG).show();
    }
}
```
PASO 5: FLUJO COMPLETO DE LA APP
Usuario abre la app → Se carga activity_main.xml

Usuario empieza a escribir →

Por cada letra, TextWatcher detecta el cambio

Se llama a controlador.campoEsValido()

El campo cambia a rojo/verde según sea válido

Usuario presiona "CREAR USUARIO" →

Se llama a controlador.obtenerMensajeError()

Si hay errores: se muestran y campos se ponen rojos

Si todo está bien:

Se llama a controlador.crearMensajeRegistroExitoso()

Que internamente llama a convertirAASCII()

Se muestra el mensaje con la contraseña codificada

Se navega a la siguiente pantalla

RESUMEN DE LLAMADAS ENTRE VISTA Y CONTROLADOR:
Vista → Controlador: "¿Este campo es válido?" (campoEsValido())

Vista → Controlador: "¿Hay errores en el formulario?" (obtenerMensajeError())

Vista → Controlador: "Dame el mensaje de éxito con ASCII" (crearMensajeRegistroExitoso())

Controlador → Vista: Retorna booleanos, mensajes y estados

# MainActivity.java

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
```

# LoginController.java

```
package com.example.proyecto_androidstudio12.controller;

import java.util.regex.Pattern;

public class LoginController {


    // CONSTANTES DE CONFIGURACIÓN ---------------------------------------------------------------------------
    // Patrón que define los caracteres permitidos: letras, números, punto y guión bajo
    private static final Pattern CARACTERES_PERMITIDOS_SEGUROS = Pattern.compile("^[a-zA-Z0-9._]+$");


    private static final int LARGO_MINIMO_USUARIO = 3;// Longitud mínima requerida para el usuario
    private static final int LARGO_MINIMO_CONTRASENA = 6; // Longitud mínima requerida para la contraseña


    // METODO 1: Validación individual de campos ---------------------------------------------------------------------------
    // Este metodo valida UN solo campo (usuario o contraseña) y retorna un mensaje de error si encuentra problemas
    public String validarCampoIndividual(String entrada, boolean esUsuario) {
        // Si el campo está vacío, no hay error (estado neutro)
        if (entrada.isEmpty()) {
            return "";
        }
        // Configurar parámetros según el tipo de campo
        int largoMinimo;
        String nombreCampo;

        // Determinar si estamos validando usuario o contraseña
        if (esUsuario) {
            largoMinimo = LARGO_MINIMO_USUARIO;
            nombreCampo = "usuario";
        } else {
            largoMinimo = LARGO_MINIMO_CONTRASENA;
            nombreCampo = "contraseña";
        }

        // PRIMERA VALIDACIÓN: Longitud del campo
        // Verifica que tenga al menos el mínimo de caracteres requerido
        if (entrada.length() < largoMinimo) {
            return "El " + nombreCampo + " es muy corto (mín. " + largoMinimo + ").";
        }

        // SEGUNDA VALIDACIÓN: Caracteres permitidos
        // Usa la expresión regular para verificar que solo tenga caracteres válidos
        if (!CARACTERES_PERMITIDOS_SEGUROS.matcher(entrada).matches()) {
            return "El " + nombreCampo + " no puede tener caracteres ^[a-zA-Z0-9._]+$.";
        }

        // Si pasa todas las validaciones, retorna string vacío (sin errores)
        return "";
    }


    // METODO 2: Validación completa del formulario  ---------------------------------------------------------------------------
    // Este mEtodo valida AMBOS campos juntos y retorna un mensaje de error general
    public String obtenerMensajeError(String usuario, String contrasena) {
        // PRIMERA VALIDACIÓN: Campos vacíos
        // Verifica que ninguno de los dos campos esté vacío
        if (usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
            return "Debes rellenar ambos campos.";
        }

        // SEGUNDA VALIDACIÓN: Usuario individual
        // Valida solo el campo de usuario
        String errorUsuario = validarCampoIndividual(usuario, true);
        // Si hay error en usuario, lo retorna inmediatamente
        if (!errorUsuario.isEmpty()) {
            return errorUsuario;
        }

        // TERCERA VALIDACIÓN: Contraseña individual
        // Valida solo el campo de contraseña
        String errorContrasena = validarCampoIndividual(contrasena, false);
        // Si hay error en contraseña, lo retorna
        if (!errorContrasena.isEmpty()) {
            return errorContrasena;
        }

        // Si pasa todas las validaciones, retorna string vacío (éxito)
        return ""; // ¡Registro exitoso!
    }


    // METODO 3: Conversión a código ASCII ---------------------------------------------------------------------------
    // Convierte una cadena de texto a su representación numérica ASCII
    public String convertirAASCII(String texto) {
        // Si el texto está vacío, retorna cadena vacía
        if (texto.isEmpty()) {
            return "";
        }

        // StringBuilder para construir el resultado eficientemente
        StringBuilder sb = new StringBuilder();

        // Recorre cada carácter del texto
        for (int i = 0; i < texto.length(); i++) {
            // Convierte el carácter a su valor ASCII (número)
            sb.append((int) texto.charAt(i));

            // Agrega guión entre números, excepto en el último
            if (i < texto.length() - 1) {
                sb.append("-");
            }
        }

        // Retorna la cadena ASCII resultante
        return sb.toString();
    }


    // METODO 4: Crear mensaje de éxito  ---------------------------------------------------------------------------
    // Genera el mensaje que se muestra cuando el registro es exitoso
    public String crearMensajeRegistroExitoso(String usuario, String contrasena) {
        // Convierte la contraseña a ASCII para mostrarla codificada
        String contrasenaASCII = convertirAASCII(contrasena);

        // Construye y retorna el mensaje de éxito
        return "Usuario " + usuario + " con contraseña codificada:\n" +
                contrasenaASCII + "\nha sido creado.";
    }


    // METODO 5: Determinar qué campos tienen error  ---------------------------------------------------------------------------
    // Analiza el mensaje de error y determina qué campos fallaron
    // Retorna un arreglo booleano: [usuario_error, contraseña_error]
    public boolean[] obtenerCamposConError(String mensajeError) {
        // Arreglo para indicar errores: posición 0 = usuario, posición 1 = contraseña
        boolean[] camposError = new boolean[2];

        // Si el mensaje menciona "usuario", marca el campo usuario como erróneo
        if (mensajeError.contains("usuario")) {
            camposError[0] = true;
        }

        // Si el mensaje menciona "contraseña", marca el campo contraseña como erróneo
        if (mensajeError.contains("contraseña")) {
            camposError[1] = true;
        }

        // Si el mensaje menciona "ambos", marca ambos campos como erróneos
        if (mensajeError.contains("ambos")) {
            camposError[0] = true;
            camposError[1] = true;
        }

        // Retorna el arreglo con la información de qué campos fallaron
        return camposError;
    }


    // METODO 6: Validación para feedback en tiempo real  ---------------------------------------------------------------------------
    // Usado mientras el usuario escribe para dar feedback inmediato
    public boolean campoEsValido(String texto, boolean esUsuario) {
        // Si el campo está vacío, retorna false (estado neutro/gris)
        if (texto.isEmpty()) {
            return false;
        }

        // Si no está vacío, valida el campo y retorna true si es válido
        return validarCampoIndividual(texto, esUsuario).isEmpty();
    }
}

```

# SecondActivity.java

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
# Alumno.java

```
package com.example.proyecto_androidstudio12.model;

public class Alumno {
    // Para este ejemplo simple, almacenamos un usuario "válido"
    // En una aplicación real, esto provendría de una base de datos.
    private static final String USUARIO_VALIDO = "admin";
    private static final String CONTRASENA_VALIDA = "123456";

    // En este ejemplo, el modelo se mantiene simple y solo expone
    // la lógica para verificar credenciales (que es una lógica de datos).

}
```
