package modelo;

import java.io.Serializable;
import java.util.*;

public class Estudiante extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<String> intereses;
    private List<Contenido> contenidos;
    private List<SolicitudAyuda> solicitudes;

    public Estudiante() {
        super();
        this.intereses = new HashSet<>();
        this.contenidos = new ArrayList<>();
        this.solicitudes = new ArrayList<>();
    }

    public Estudiante(String id, String nombre, String correo, String contrasena) {
        super(id, nombre, correo, contrasena);
        this.intereses = new HashSet<>();
        this.contenidos = new ArrayList<>();
        this.solicitudes = new ArrayList<>();
    }

    public Estudiante(Estudiante otro) {
        super(otro.getId(), otro.getNombre(), otro.getCorreo(), otro.getContrasena());
        this.intereses = new HashSet<>(otro.getIntereses());
        this.contenidos = new ArrayList<>(otro.getContenidos());
        this.solicitudes = new ArrayList<>(otro.getSolicitudes());
        this.contenidosValorados = new HashSet<>(otro.getContenidosValorados());
    }









    private Set<String> contenidosValorados = new HashSet<>();

    public Set<String> getContenidosValorados() {
        if (contenidosValorados == null) {
            contenidosValorados = new HashSet<>();
        }
        return contenidosValorados;
    }

    private transient String origenSugerencia = "Interés"; // por defecto

    public String getOrigenSugerencia() {
        return origenSugerencia;
    }

    public void setOrigenSugerencia(String origenSugerencia) {
        this.origenSugerencia = origenSugerencia;
    }





    public void agregarInteres(String interes) {
        intereses.add(interes.toLowerCase());
    }

    public void eliminarInteres(String interes) {
        intereses.remove(interes.toLowerCase());
    }

    public Set<String> getIntereses() {
        return intereses;
    }

    public void publicarContenido(Contenido c) {
        contenidos.add(c);
    }

    public List<Contenido> getContenidos() {
        return contenidos;
    }

    public void valorarContenido(String idContenido) {
        // Guardar ID de contenido valorado
        contenidosValorados.add(idContenido);
    }

    public List<Contenido> getContenidosPublicados() {
        return contenidos;
    }


    public void agregarSolicitud(SolicitudAyuda s) {
        solicitudes.add(s);
    }

    public List<SolicitudAyuda> getSolicitudes() {
        return solicitudes;
    }

    @Override
    public String getRol() {
        return "Estudiante";
    }

    @Override
    public String toString() {
        return super.toString() + " | Intereses: " + intereses;
    }
}
