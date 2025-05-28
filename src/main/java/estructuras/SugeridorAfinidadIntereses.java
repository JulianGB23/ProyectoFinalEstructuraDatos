package estructuras;



import Gestores.GestorEstudiantes;
import Gestores.GestorSolicitudes;
import modelo.Estudiante;

import java.util.*;


public class SugeridorAfinidadIntereses {


    private GestorEstudiantes gestorEstudiantes;
    private GestorSolicitudes gestorSolicitudes;


    public SugeridorAfinidadIntereses(GestorEstudiantes gestorEstudiantes, GestorSolicitudes gestorSolicitudes) {
        this.gestorEstudiantes = gestorEstudiantes;
        this.gestorSolicitudes = gestorSolicitudes;
    }



    public List<Estudiante> sugerir(String correo) {
        Estudiante base = gestorEstudiantes.buscar(correo);
        if (base == null) return Collections.emptyList();


        Set<String> intereses = base.getIntereses();
        List<Estudiante> sugerencias = new ArrayList<>();


        for (Estudiante e : gestorEstudiantes.obtenerTodos()) {
            if (!e.getCorreo().equals(correo)) {
                for (String interes : e.getIntereses()) {
                    if (intereses.contains(interes)) {
                        sugerencias.add(e);
                        break;
                    }
                }
            }
        }
        return sugerencias;
    }



    public boolean enviarSolicitudSiCoinciden(String correoEmisor, String correoReceptor) {
        Estudiante e1 = gestorEstudiantes.buscar(correoEmisor);
        Estudiante e2 = gestorEstudiantes.buscar(correoReceptor);
        if (e1 == null || e2 == null) return false;


        for (String interes : e1.getIntereses()) {
            if (e2.getIntereses().contains(interes)) {
                gestorSolicitudes.enviarSolicitud(correoEmisor, correoReceptor);
                return true;
            }
        }
        return false;
    }
}


