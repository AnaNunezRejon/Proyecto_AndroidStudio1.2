CONSTRUCCIÓN PASO A PASO DE LA APP
PASO 1: ESTRUCTURA DE PAQUETES
text
app/
├── src/main/java/com/example/proyecto_androidstudio12/
│   ├── controller/
│   │   └── LoginController.java    ← LÓGICA DE NEGOCIO
│   ├── vista/
│   │   └── MainActivity.java       ← INTERFAZ VISUAL
│   └── model/ (opcional, para futuras mejoras)
└── res/layout/
└── activity_main.xml           ← DISEÑO DE PANTALLA
PASO 2: CREAR EL LAYOUT (activity_main.xml)
Primero diseñamos la pantalla de login con:

2 EditText (usuario y contraseña)

1 Button (crear usuario)

Colores definidos en colors.xml (gris_neutro, rojo_error, verde_correcto)

PASO 3: CONSTRUIR EL CONTROLADOR (LoginController.java)
PASO 3.1 - Validación de Caracteres Válidos

java
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
PASO 3.2 - Validación Contra Campos Vacíos

java
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
PASO 3.3 - Conversión a ASCII

java
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
PASO 3.4 - Mensaje de Éxito

java
// En el controlador, método que crea el mensaje final
public String crearMensajeRegistroExitoso(String usuario, String contrasena) {
// Llamamos al método de conversión ASCII
String contrasenaASCII = convertirAASCII(contrasena);
return "✅ Usuario " + usuario + " con contraseña codificada:\n" +
contrasenaASCII + "\nha sido creado.";
}
PASO 4: CONSTRUIR LA VISTA (MainActivity.java)
PASO 4.1 - Configuración Inicial

java
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
PASO 4.2 - Validación en Tiempo Real (mientras el usuario escribe)

java
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
PASO 4.3 - Validación Final (al presionar el botón)

java
// Cuando el usuario presiona "CREAR USUARIO"
private void intentarRegistro() {
// Obtenemos el texto actual de los campos
String usuario = etUsuario.getText().toString();
String contrasena = etContrasena.getText().toString();

    // PASO CRÍTICO: Delegamos toda la validación al controlador
    String mensajeError = controlador.obtenerMensajeError(usuario, contrasena);

    if (mensajeError.isEmpty()) {
        // ✅ ÉXITO: Todos los campos son válidos
        
        // Pedimos al controlador el mensaje de éxito con la conversión ASCII
        String mensajeExito = controlador.crearMensajeRegistroExitoso(usuario, contrasena);
        
        // Mostramos el mensaje al usuario
        Toast.makeText(this, mensajeExito, Toast.LENGTH_LONG).show();
        
        // Navegamos a la siguiente pantalla
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        
    } else {
        // ❌ ERROR: Mostramos el mensaje que nos dio el controlador
        Toast.makeText(this, " Error: " + mensajeError, Toast.LENGTH_LONG).show();
    }
}
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
