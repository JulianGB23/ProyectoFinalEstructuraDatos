package sistema;

import estructuras.GrafoEstudiantes;
import modelo.Contenido;
import modelo.Estudiante;

import java.util.*;

public class RedSocialExtras {


    public static List<Contenido> obtenerContenidosMasValorados(List<Contenido> todos, int n) {
        return todos.stream()
                .sorted(Comparator.comparingInt(Contenido::getValoraciones).reversed())
                .limit(n)
                .toList();
    }


    public static List<String> estudiantesConMasConexiones(GrafoEstudiantes grafo, int n) {
        Map<String, Integer> conexiones = new HashMap<>();
        for (String id : grafo.adyacencias.keySet()) {
            conexiones.put(id, grafo.adyacencias.get(id).size());
        }
        return conexiones.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }


    public static Map<String, Integer> nivelesDeParticipacion(Map<String, Estudiante> estudiantes) {
        Map<String, Integer> participacion = new HashMap<>();
        for (Estudiante e : estudiantes.values()) {
            int nivel = e.getContenidos().size() + e.getSolicitudes().size();
            participacion.put(e.getCorreo(), nivel);
        }
        return participacion;
    }

    public static List<Set<String>> detectarComunidades(GrafoEstudiantes grafo) {
        Set<String> visitados = new HashSet<>();
        List<Set<String>> comunidades = new ArrayList<>();
        for (String nodo : grafo.adyacencias.keySet()) {
            if (!visitados.contains(nodo)) {
                Set<String> componente = new HashSet<>();
                Queue<String> cola = new LinkedList<>();
                cola.add(nodo);
                while (!cola.isEmpty()) {
                    String actual = cola.poll();
                    if (!visitados.contains(actual)) {
                        visitados.add(actual);
                        componente.add(actual);
                        cola.addAll(grafo.adyacencias.getOrDefault(actual, new HashSet<>()));
                    }
                }
                comunidades.add(componente);
            }
        }
        return comunidades;
    }


    public static void cargarDatosPrueba(RedSocial red) {
        red.registrarEstudiante("1", "Ana", "ana@uni.edu", "123");
        red.registrarEstudiante("2", "Luis", "luis@uni.edu", "456");
        red.registrarEstudiante("3", "Carlos", "carlos@uni.edu", "789");

        red.publicarContenido("1", "Matemáticas", "pdf");
        red.publicarContenido("2", "Física", "video");
        red.publicarContenido("1", "Historia", "enlace");

        red.solicitarAyuda("2", "Física", 4);
        red.solicitarAyuda("3", "Matemáticas", 5);

        red.enviarSolicitudConexion("ana@uni.edu", "luis@uni.edu");
        red.aceptarSolicitudConexion("ana@uni.edu", "luis@uni.edu");
        red.enviarSolicitudConexion("luis@uni.edu", "carlos@uni.edu");
        red.aceptarSolicitudConexion("luis@uni.edu", "carlos@uni.edu");
    }


    public static List<Contenido> contenidosPublicadosPor(Map<String, Estudiante> estudiantes, String correo) {
        Estudiante e = estudiantes.get(correo);
        return e != null ? (List<Contenido>) e.getContenidos() : new ArrayList<>();
    }
}

