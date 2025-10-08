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

