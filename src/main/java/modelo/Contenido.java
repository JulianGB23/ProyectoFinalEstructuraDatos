package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Contenido implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String titulo;
    private String autor;
    private String tipo;
    private int valoraciones;
    private String descripcion;
    private LocalDate fechaPublicacion;

    public Contenido(String titulo, String autor, String tipo) {
        this.id = java.util.UUID.randomUUID().toString();
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.valoraciones = 0; // siempre inicia en 0
        this.descripcion = "Sin descripción";
        this.fechaPublicacion = LocalDate.now();
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getTipo() { return tipo; }
    public int getValoraciones() { return valoraciones; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaPublicacion(LocalDate fecha) {
        this.fechaPublicacion = fecha;
    }

    public void valorar() {
        this.valoraciones += 1;
    }
}
