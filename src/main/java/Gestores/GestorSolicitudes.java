package Gestores;

import estructuras.GrafoEstudiantes;
import estructuras.SolicitudConexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestorSolicitudes implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SolicitudConexion> solicitudes;
    public GestorSolicitudes() { solicitudes = new ArrayList<>(); }
    public void enviarSolicitud(String emisor, String receptor) {
        solicitudes.add(new SolicitudConexion(emisor, receptor));
    }
    public List<SolicitudConexion> obtenerSolicitudesPara(String receptor) {
        List<SolicitudConexion> resultado = new ArrayList<>();
        for (SolicitudConexion s : solicitudes)
            if (s.getCorreoReceptor().equals(receptor)) resultado.add(s);
        return resultado;
    }
    public boolean aceptarSolicitud(String emisor, String receptor, GrafoEstudiantes grafo) {
        Iterator<SolicitudConexion> it = solicitudes.iterator();
        while (it.hasNext()) {
            SolicitudConexion s = it.next();
            if (s.getCorreoEmisor().equals(emisor) && s.getCorreoReceptor().equals(receptor)) {
                it.remove();
                grafo.conectar(emisor, receptor);
                return true;
            }
        }
        return false;
    }
    public void rechazarSolicitud(String emisor, String receptor) {
        solicitudes.removeIf(s -> s.getCorreoEmisor().equals(emisor) && s.getCorreoReceptor().equals(receptor));
    }

    public List<String> getSolicitudesPendientes(String correoEmisor) {
        List<String> pendientes = new ArrayList<>();
        for (SolicitudConexion s : solicitudes) {
            if (s.getCorreoEmisor().equals(correoEmisor)) {
                pendientes.add(s.getCorreoReceptor());
            }
        }
        return pendientes;
    }

    public List<String> getSolicitudesRecibidas(String correoReceptor) {
        List<String> recibidas = new ArrayList<>();
        for (SolicitudConexion s : solicitudes) {
            if (s.getCorreoReceptor().equals(correoReceptor)) {
                recibidas.add(s.getCorreoEmisor());
            }
        }
        return recibidas;
    }


    public List<SolicitudConexion> getSolicitudes() {
        return solicitudes;
    }
}