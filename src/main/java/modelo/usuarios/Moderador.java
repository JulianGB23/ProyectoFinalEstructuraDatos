package modelo.usuarios;

import modelo.usuarios.Usuario;

public class Moderador extends Usuario {

    public Moderador(String id, String nombre, String correo, String contrasenia){

        super(id,nombre,correo,contrasenia);
    }

    @Override
    public String getTipoUsuario(){
        return "Moderador";
    }

    // Métodos propios del moderador (pueden ser definidos más adelante)
    public void gestionarUsuario(String idUsuario) {

    }

    public void gestionarContenido(String idContenido) {

    }

    public void visualizarGrafo() {

    }

    public void generarReporte(String tipoReporte) {

    }
}

