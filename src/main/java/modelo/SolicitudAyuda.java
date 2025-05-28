package modelo;

import java.io.Serializable;

public class SolicitudAyuda implements Serializable, Comparable<SolicitudAyuda> {

    private static final long serialVersionUID = 1L;

    private String estudianteId;
    private String tema;
    private int urgencia;
    private String estado;

    public SolicitudAyuda(String estudianteId, String tema, int urgencia) {
        this.estudianteId = estudianteId;
        this.tema = tema;
        this.urgencia = urgencia;
        this.estado = "Pendiente";
    }

    @Override
    public int compareTo(SolicitudAyuda o) {
        return Integer.compare(o.urgencia, this.urgencia); // Mayor urgencia primero
    }


    public String getEstudianteId() { return estudianteId; }
    public String getTema() { return tema; }
    public int getUrgencia() { return urgencia; }
    public String getEstado() { return estado; }

    public void setEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }
}

