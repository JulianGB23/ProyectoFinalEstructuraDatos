package estructuras;



import modelo.Estudiante;

import java.util.*;


public class GrafoAfinidad {


    private Map<String, NodoEstudiante> nodos;


    public GrafoAfinidad() {
        this.nodos = new HashMap<>();
    }


    public void agregarEstudiante(Estudiante estudiante) {
        String correo = estudiante.getCorreo();
        if (!nodos.containsKey(correo)) {
            nodos.put(correo, new NodoEstudiante(estudiante));
        }
    }


    public boolean existeEstudiante(String correo) {
        return nodos.containsKey(correo);
    }


    public NodoEstudiante getNodoEstudiante(String correo) {
        return nodos.get(correo);
    }


    public void conectarEstudiantes(String correo1, String correo2) {
        NodoEstudiante nodo1 = nodos.get(correo1);
        NodoEstudiante nodo2 = nodos.get(correo2);
        if (nodo1 != null && nodo2 != null) {
            nodo1.agregarConexion(nodo2);
            nodo2.agregarConexion(nodo1);
        }
    }


    public void mostrarGrafo() {
        for (NodoEstudiante nodo : nodos.values()) {
            System.out.print(nodo.getEstudiante().getNombre() + " -> ");
            for (NodoEstudiante amigo : nodo.getConexiones()) {
                System.out.print(amigo.getEstudiante().getNombre() + ", ");
            }
            System.out.println();
        }
    }


    public Estudiante estudianteConMasConexiones() {
        int max = -1;
        Estudiante top = null;
        for (NodoEstudiante nodo : nodos.values()) {
            int conexiones = nodo.getConexiones().size();
            if (conexiones > max) {
                max = conexiones;
                top = nodo.getEstudiante();
            }
        }
        return top;
    }


    public List<String> caminoMasCorto(String correoOrigen, String correoDestino) {
        if (!nodos.containsKey(correoOrigen) || !nodos.containsKey(correoDestino)) {
            return Collections.emptyList();
        }


        Queue<NodoEstudiante> cola = new LinkedList<>();
        Map<String, String> predecesores = new HashMap<>();
        Set<String> visitados = new HashSet<>();


        cola.add(nodos.get(correoOrigen));
        visitados.add(correoOrigen);


        while (!cola.isEmpty()) {
            NodoEstudiante actual = cola.poll();
            for (NodoEstudiante vecino : actual.getConexiones()) {
                String correoVecino = vecino.getEstudiante().getCorreo();
                if (!visitados.contains(correoVecino)) {
                    visitados.add(correoVecino);
                    predecesores.put(correoVecino, actual.getEstudiante().getCorreo());
                    cola.add(vecino);
                    if (correoVecino.equals(correoDestino)) break;
                }
            }
        }


        List<String> camino = new LinkedList<>();
        String paso = correoDestino;
        while (paso != null && predecesores.containsKey(paso)) {
            camino.add(0, paso);
            paso = predecesores.get(paso);
        }
        if (paso != null && paso.equals(correoOrigen)) {
            camino.add(0, correoOrigen);
        }


        return camino;
    }
}
