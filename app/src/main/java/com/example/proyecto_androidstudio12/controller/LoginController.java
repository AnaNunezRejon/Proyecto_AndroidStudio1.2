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
