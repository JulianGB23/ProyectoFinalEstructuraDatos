package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String nombre;
    protected String correo;
    protected String contrasena;
    protected ArrayList solicitudesAyuda;

    public Usuario(String id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public Usuario() {
        // Constructor vacío requerido para serialización y subclases
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean autenticar(String contrasena) {
        return this.contrasena != null && this.contrasena.equals(contrasena);
    }

    public abstract String getRol(); // "Estudiante" o "Moderador"

    @Override
    public String toString() {
        return nombre + " (" + correo + ")";
    }
}

