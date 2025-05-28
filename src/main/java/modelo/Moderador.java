package modelo;


public class Moderador extends Usuario {
    public Moderador(String id, String nombre, String correo, String contraseña) {
        super(id, nombre, correo, contraseña);
    }

    @Override
    public String getRol() {
        return "Moderador";
    }

    @Override
    public String toString() {
        return "Moderador{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}


