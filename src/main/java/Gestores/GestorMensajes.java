package Gestores;

import mensajes.Mensaje;

import java.io.Serializable;
import java.util.*;

public class GestorMensajes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, List<Mensaje>> mensajesPorUsuario;

    public GestorMensajes() {
        mensajesPorUsuario = new HashMap<>();
    }

    public void enviarMensaje(String emisor, String receptor, String contenido) {
        Mensaje mensaje = new Mensaje(emisor, contenido);
        String claveConversacion = obtenerClaveConversacion(emisor, receptor);
        mensajesPorUsuario.computeIfAbsent(claveConversacion, k -> new ArrayList<>()).add(mensaje);
    }

    public List<Mensaje> obtenerConversacion(String correo1, String correo2) {
        String clave = obtenerClaveConversacion(correo1, correo2);
        return mensajesPorUsuario.getOrDefault(clave, new ArrayList<>());
    }

    private String obtenerClaveConversacion(String correo1, String correo2) {
        return (correo1.compareTo(correo2) < 0)
                ? correo1 + "-" + correo2
                : correo2 + "-" + correo1;
    }
}
