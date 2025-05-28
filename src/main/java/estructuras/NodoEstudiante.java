package estructuras;




import modelo.Estudiante;

import java.util.HashSet;
import java.util.Set;


public class NodoEstudiante {
    private Estudiante estudiante;
    private Set<NodoEstudiante> conexiones;


    public NodoEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.conexiones = new HashSet<>();
    }


    public Estudiante getEstudiante() {
        return estudiante;
    }


    public void agregarConexion(NodoEstudiante otro) {
        conexiones.add(otro);
    }


    public Set<NodoEstudiante> getConexiones() {
        return conexiones;
    }
}

