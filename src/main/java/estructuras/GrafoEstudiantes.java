package estructuras;

import modelo.Estudiante;

import java.io.Serializable;
import java.util.*;

public class GrafoEstudiantes implements Serializable {
    private static final long serialVersionUID = 1L;
    public Map<String, Set<String>> adyacencias = new HashMap<>();
    private Map<String, Estudiante> nodos = new HashMap<>();

    public void agregarEstudiante(Estudiante e) {
        nodos.put(e.getCorreo(), e);
        adyacencias.putIfAbsent(e.getCorreo(), new HashSet<>());
    }

    public void conectar(String a, String b) {
        adyacencias.putIfAbsent(a, new HashSet<>());
        adyacencias.putIfAbsent(b, new HashSet<>());
        adyacencias.get(a).add(b);
        adyacencias.get(b).add(a);
    }

    public List<Estudiante> sugerencias(String id) {
        Set<String> amigos = adyacencias.getOrDefault(id, new HashSet<>());
        Set<String> sugeridos = new HashSet<>();
        for (String amigo : amigos) {
            for (String amigoDeAmigo : adyacencias.getOrDefault(amigo, new HashSet<>())) {
                if (!amigos.contains(amigoDeAmigo) && !amigoDeAmigo.equals(id)) {
                    sugeridos.add(amigoDeAmigo);
                }
            }
        }
        List<Estudiante> resultado = new ArrayList<>();
        for (String s : sugeridos) resultado.add(nodos.get(s));
        return resultado;
    }

    public List<String> rutaMasCorta(String inicio, String destino) {
        Queue<String> cola = new LinkedList<>();
        Map<String, String> previo = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        cola.add(inicio);
        visitados.add(inicio);
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (actual.equals(destino)) break;
            for (String vecino : adyacencias.getOrDefault(actual, new HashSet<>())) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    previo.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }
        List<String> camino = new LinkedList<>();
        for (String at = destino; at != null; at = previo.get(at)) camino.add(0, at);
        return camino.isEmpty() || !camino.get(0).equals(inicio) ? new ArrayList<>() : camino;
    }

    public void mostrarGrafo(Object o) {
    }

    public Map<String, Integer> obtenerGrados() {
        Map<String, Integer> grados = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : adyacencias.entrySet()) {
            grados.put(entry.getKey(), entry.getValue().size());
        }
        return grados;
    }

    public void actualizarCorreoEstudiante(String antiguo, String nuevo) {
        if (!adyacencias.containsKey(antiguo)) return;

        Set<String> conexiones = adyacencias.remove(antiguo);
        adyacencias.put(nuevo, conexiones);

        for (Set<String> vecinos : adyacencias.values()) {
            if (vecinos.remove(antiguo)) {
                vecinos.add(nuevo);
            }
        }

        Estudiante e = nodos.remove(antiguo);
        if (e != null) nodos.put(nuevo, e);
    }

    public List<String> obtenerConexiones(String correo) {
        return new ArrayList<>(adyacencias.getOrDefault(correo, new HashSet<>()));
    }

    public void desconectar(String a, String b) {
        if (adyacencias.containsKey(a)) {
            adyacencias.get(a).remove(b);
        }
        if (adyacencias.containsKey(b)) {
            adyacencias.get(b).remove(a);
        }
    }

    public boolean estanConectados(String a, String b) {
        return adyacencias.getOrDefault(a, Collections.emptySet()).contains(b);
    }

    public Map<Integer, List<String>> detectarComunidades() {
        Map<Integer, List<String>> comunidades = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        int id = 1;

        for (String estudiante : adyacencias.keySet()) {
            if (!visitados.contains(estudiante)) {
                List<String> comunidad = new ArrayList<>();
                dfs(estudiante, visitados, comunidad);
                comunidades.put(id++, comunidad);
            }
        }

        return comunidades;
    }

    private void dfs(String actual, Set<String> visitados, List<String> comunidad) {
        visitados.add(actual);
        comunidad.add(actual);

        for (String vecino : adyacencias.getOrDefault(actual, new HashSet<>())) {
            {
                if (!visitados.contains(vecino)) {
                    dfs(vecino, visitados, comunidad);
                }
            }
        }


    }

}

