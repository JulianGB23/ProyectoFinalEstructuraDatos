package Gestores;




import modelo.Estudiante;

import java.util.Collection;
import java.util.HashMap;


public class GestorEstudiantes {


    private HashMap<String, Estudiante> estudiantesPorCorreo;


    public GestorEstudiantes() {
        estudiantesPorCorreo = new HashMap<>();
    }


    public boolean agregarEstudiante(Estudiante estudiante) {
        if (estudiantesPorCorreo.containsKey(estudiante.getCorreo())) {
            return false;
        }
        estudiantesPorCorreo.put(estudiante.getCorreo(), estudiante);
        return true;
    }


    public Estudiante buscar(String correo) {
        return estudiantesPorCorreo.get(correo);
    }


    public Collection<Estudiante> obtenerTodos() {
        return estudiantesPorCorreo.values();
    }


    public void mostrarEstudiantes() {
        for (Estudiante e : estudiantesPorCorreo.values()) {
            System.out.println(e.getNombre() + " - " + e.getCorreo());
        }
    }
}
