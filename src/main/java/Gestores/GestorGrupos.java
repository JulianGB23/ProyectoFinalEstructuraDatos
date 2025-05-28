package Gestores;



import modelo.Estudiante;

import java.util.*;


public class GestorGrupos {

    private static final long serialVersionUID = 1L;


    private GestorEstudiantes gestorEstudiantes;


    public GestorGrupos(GestorEstudiantes gestorEstudiantes) {
        this.gestorEstudiantes = gestorEstudiantes;
    }



    public Map<String, List<Estudiante>> formarGruposPorMateria() {
        Map<String, List<Estudiante>> grupos = new HashMap<>();


        for (Estudiante e : gestorEstudiantes.obtenerTodos()) {
            for (String interes : e.getIntereses()) {
                grupos.putIfAbsent(interes, new ArrayList<>());
                grupos.get(interes).add(e);
            }
        }


        return grupos;
    }
}
