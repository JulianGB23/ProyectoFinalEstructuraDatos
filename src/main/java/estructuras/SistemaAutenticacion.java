package estructuras;

import modelo.Usuario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SistemaAutenticacion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Usuario> usuarios;

    public SistemaAutenticacion() {
        usuarios = new HashMap<>();
    }

    public boolean registrarUsuario(Usuario usuario) {
        String correo = usuario.getCorreo().toLowerCase();
        if (usuarios.containsKey(correo)) return false;
        usuarios.put(correo, usuario);
        return true;
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        Usuario u = usuarios.get(correo.toLowerCase());
        if (u != null && u.getContrasena() != null && u.getContrasena().equals(contrasena)) {
            return u;
        }
        return null;
    }

    public boolean existeCorreo(String correo) {
        return usuarios.containsKey(correo.toLowerCase());
    }

    public void actualizarCorreo(String correoAnterior, String nuevoCorreo, Usuario usuario) {
        usuarios.remove(correoAnterior.toLowerCase());
        usuarios.put(nuevoCorreo.toLowerCase(), usuario);
    }

    public Map<String, Usuario> getTodos() {
        return usuarios;
    }
}
