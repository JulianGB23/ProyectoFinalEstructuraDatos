package modelo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrupoEstudio implements Serializable {
    private String id;
    private String nombre;
    private String tema;
    private List<Estudiante> miembros;

    public GrupoEstudio(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.tema = "";
        this.miembros = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public List<Estudiante> getMiembros() { return miembros; }

    public void agregarMiembro(Estudiante e) {
        if (!miembros.contains(e)) miembros.add(e);
    }

    public void removerMiembro(Estudiante e) {
        miembros.remove(e);
    }

    @Override
    public String toString() {
        return "GrupoEstudio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tema='" + tema + '\'' +
                ", miembros=" + miembros.size() +
                '}';
    }

    public void setNombre(String nombre) {
    }

    private Set<String> intereses = new HashSet<>();

    public Set<String> getIntereses() {
        return intereses;
    }

}