package sistema;

import Gestores.*;
import estructuras.*;
import mensajes.Mensaje;
import modelo.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedSocial implements Serializable {

    private static final long serialVersionUID = 1L;


    private static final String ARCHIVO_DATOS = "red_social.dat";

    private Estudiante estudianteActivo;

    private Map<String, Estudiante> estudiantes;
    private Map<String, Moderador> moderadores;
    private Map<String, GrupoEstudio> gruposEstudio;
    private ABB arbolContenidos;
    private ColaPrioridad colaSolicitudes;
    private GrafoEstudiantes grafo;
    private SistemaAutenticacion autenticacion;
    private GestorMensajes gestorMensajes;
    private GestorSolicitudes gestorSolicitudes;


    public RedSocial() {
        estudiantes = new HashMap<>();
        moderadores = new HashMap<>();
        gruposEstudio = new HashMap<>();
        arbolContenidos = new ABB();
        colaSolicitudes = new ColaPrioridad();
        grafo = new GrafoEstudiantes();
        autenticacion = new SistemaAutenticacion();
        gestorMensajes = new GestorMensajes();
        gestorSolicitudes = new GestorSolicitudes();
    }

    public void setEstudianteActivo(Estudiante estudiante) {
        this.estudianteActivo = estudiante;
    }

    public Estudiante getEstudianteActivo() {
        return estudianteActivo;
    }

    public void cerrarSesion() {
        this.estudianteActivo = null;
    }



    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(this);
            System.out.println("RedSocial guardada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar RedSocial: " + e.getMessage());
        }
    }

    public static RedSocial cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado. Creando nueva RedSocial.");
            return new RedSocial();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            RedSocial red = (RedSocial) ois.readObject();
            System.out.println("RedSocial cargada desde archivo.");
            red.actualizarConexionesAutomaticas(); // <- AQUI LO AÑADES
            return red;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar RedSocial: " + e.getMessage());
            return new RedSocial();
        }
    }



    public boolean registrarEstudiante(String id, String nombre, String correo, String contrasena) {
        if (estudiantes.containsKey(id) || autenticacion.existeCorreo(correo)) return false;
        Estudiante e = new Estudiante(id, nombre, correo, contrasena);
        estudiantes.put(id, e);
        grafo.agregarEstudiante(e);
        autenticacion.registrarUsuario(e);
        return true;
    }

    public boolean registrarModerador(String id, String nombre, String correo, String contrasena) {
        if (moderadores.containsKey(id) || autenticacion.existeCorreo(correo)) return false;
        Moderador m = new Moderador(id, nombre, correo, contrasena);
        moderadores.put(id, m);
        autenticacion.registrarUsuario(m);
        return true;
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        return autenticacion.iniciarSesion(correo, contrasena);
    }



    public Estudiante getEstudiante(String id) {
        return estudiantes.get(id);
    }

    public Map<String, Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public Map<String, GrupoEstudio> getGruposEstudio() {
        return gruposEstudio;
    }

    public ABB getArbolContenidos() {
        return arbolContenidos;
    }

    public GrafoEstudiantes getGrafo() {
        return grafo;
    }

    public ColaPrioridad getColaSolicitudes() {
        return colaSolicitudes;
    }

    public SistemaAutenticacion getSistemaAutenticacion() {
        return autenticacion;
    }

    public GestorMensajes getGestorMensajes() {
        return gestorMensajes;
    }

    public GestorSolicitudes getGestorSolicitudes() {
        return gestorSolicitudes;
    }

    public GestorEstudiantes getGestorEstudiantes() {
        GestorEstudiantes ge = new GestorEstudiantes();
        for (Estudiante e : estudiantes.values()) {
            ge.agregarEstudiante(e);
        }
        return ge;
    }

    public GestorGrupos getGestorGrupos() {
        return new GestorGrupos(getGestorEstudiantes());
    }



    public void publicarContenido(String idEstudiante, String tema, String tipo) {
        String idContenido = UUID.randomUUID().toString();
        Contenido c = new Contenido(tema, idEstudiante, tipo);

        arbolContenidos.insertar(c);
        Estudiante est = estudiantes.get(idEstudiante);
        if (est != null) {
            est.publicarContenido(c);
        }
    }

    public boolean valorarContenido(String idEstudiante, String idContenido) {
        Contenido c = arbolContenidos.buscarPorId(idContenido);
        if (c != null) {
            c.valorar();
            Estudiante est = estudiantes.get(idEstudiante);
            if (est != null) {
                est.valorarContenido(idContenido);
            }
            return true;
        }
        return false;
    }

    public List<Contenido> buscarContenidoPorTema(String titulo) {
        return arbolContenidos.buscarPorTitulo(titulo);
    }

    public List<Contenido> buscarContenidoPorAutor(String autorId) {
        return arbolContenidos.buscarPorAutor(autorId);
    }

    public List<Contenido> buscarContenidoPorTipo(String tipo) {
        return arbolContenidos.buscarPorTipo(tipo);
    }



    public boolean crearGrupoEstudio(String idGrupo, String nombre, List<String> idsMiembros) {
        if (gruposEstudio.containsKey(idGrupo)) return false;
        GrupoEstudio grupo = new GrupoEstudio(idGrupo, nombre);
        for (String idEst : idsMiembros) {
            Estudiante e = estudiantes.get(idEst);
            if (e != null) grupo.agregarMiembro(e);
        }
        gruposEstudio.put(idGrupo, grupo);
        return true;
    }

    public List<GrupoEstudio> listarGrupos() {
        return new ArrayList<>(gruposEstudio.values());
    }



    public void solicitarAyuda(String idEstudiante, String tema, int urgencia) {
        SolicitudAyuda s = new SolicitudAyuda(idEstudiante, tema, urgencia);
        colaSolicitudes.agregar(s);
        Estudiante est = estudiantes.get(idEstudiante);
        if (est != null) {
            est.agregarSolicitud(s);
        }
    }

    public SolicitudAyuda atenderSolicitud() {
        return colaSolicitudes.atender();
    }



    public List<Estudiante> sugerirCompañeros(String idEstudiante) {
        Estudiante base = estudiantes.get(idEstudiante);
        if (base == null) return Collections.emptyList();

        Set<String> interesesBase = base.getIntereses();
        List<Estudiante> sugerencias = new ArrayList<>();

        String correoBase = base.getCorreo();
        List<SolicitudConexion> solicitudesPendientes = gestorSolicitudes.getSolicitudes();

        for (Estudiante otro : estudiantes.values()) {
            if (!otro.getId().equals(idEstudiante)) {

                String correoOtro = otro.getCorreo();


                boolean yaConectados = grafo.obtenerConexiones(correoBase).contains(correoOtro);


                boolean yaSolicitado = solicitudesPendientes.stream().anyMatch(s ->
                        (s.getCorreoEmisor().equals(correoBase) && s.getCorreoReceptor().equals(correoOtro)) ||
                                (s.getCorreoEmisor().equals(correoOtro) && s.getCorreoReceptor().equals(correoBase))
                );


                Set<String> interesesOtro = otro.getIntereses();
                Set<String> interseccion = new HashSet<>(interesesBase);
                interseccion.retainAll(interesesOtro);

                if (!yaConectados && !yaSolicitado && !interseccion.isEmpty()) {
                    sugerencias.add(otro);
                }
            }
        }

        return sugerencias;
    }



    public boolean actualizarCorreoEstudiante(String correoAntiguo, String nuevoCorreo) {

        if (autenticacion.existeCorreo(nuevoCorreo)) return false;


        Estudiante est = estudiantes.values().stream()
                .filter(e -> e.getCorreo().equalsIgnoreCase(correoAntiguo))
                .findFirst()
                .orElse(null);

        if (est == null) return false;


        autenticacion.getTodos().remove(correoAntiguo);
        grafo.actualizarCorreoEstudiante(correoAntiguo, nuevoCorreo);


        est.setCorreo(nuevoCorreo);


        autenticacion.registrarUsuario(est);

        return true;
    }


    public List<String> rutaMasCorta(String idInicio, String idDestino) {
        Estudiante a = estudiantes.get(idInicio);
        Estudiante b = estudiantes.get(idDestino);
        if (a == null || b == null) return Collections.emptyList();
        return grafo.rutaMasCorta(a.getCorreo(), b.getCorreo());
    }



    public void enviarMensaje(String emisor, String receptor, String contenido) {
        gestorMensajes.enviarMensaje(emisor, receptor, contenido);
    }

    public List<Mensaje> obtenerConversacion(String correo1, String correo2) {
        return gestorMensajes.obtenerConversacion(correo1, correo2);
    }



    public void enviarSolicitudConexion(String emisor, String receptor) {
        gestorSolicitudes.enviarSolicitud(emisor, receptor);
    }

    public List<SolicitudConexion> obtenerSolicitudesPara(String receptor) {
        return gestorSolicitudes.obtenerSolicitudesPara(receptor);
    }

    public boolean aceptarSolicitudConexion(String emisor, String receptor) {
        return gestorSolicitudes.aceptarSolicitud(emisor, receptor, grafo);
    }

    public void rechazarSolicitudConexion(String emisor, String receptor) {
        gestorSolicitudes.rechazarSolicitud(emisor, receptor);
    }

    public List<GrupoEstudio> getGruposDelEstudiante(String idEstudiante) {
        List<GrupoEstudio> lista = new ArrayList<>();
        for (GrupoEstudio grupo : gruposEstudio.values()) {
            for (Estudiante e : grupo.getMiembros()) {
                if (e.getId().equals(idEstudiante)) {
                    lista.add(grupo);
                    break;
                }
            }
        }
        return lista;
    }

    public void conectarPorValoracionesCompartidas() {
        List<Estudiante> listaEstudiantes = new ArrayList<>(estudiantes.values());

        for (int i = 0; i < listaEstudiantes.size(); i++) {
            Estudiante e1 = listaEstudiantes.get(i);
            if (e1.getContenidosValorados() == null) continue;

            for (int j = i + 1; j < listaEstudiantes.size(); j++) {
                Estudiante e2 = listaEstudiantes.get(j);
                if (e2.getContenidosValorados() == null) continue;

                Set<String> contenidos1 = new HashSet<>(e1.getContenidosValorados());
                contenidos1.retainAll(e2.getContenidosValorados()); // intersección

                if (!contenidos1.isEmpty()) {
                    String correo1 = e1.getCorreo().toLowerCase().trim();
                    String correo2 = e2.getCorreo().toLowerCase().trim();

                    if (!grafo.estanConectados(correo1, correo2)) {
                        grafo.conectar(correo1, correo2);
                    }
                }
            }
        }
    }

    private void conectarPorGrupos() {
        for (GrupoEstudio grupo : gruposEstudio.values()) {
            List<Estudiante> miembros = grupo.getMiembros();
            for (int i = 0; i < miembros.size(); i++) {
                for (int j = i + 1; j < miembros.size(); j++) {
                    String a = miembros.get(i).getCorreo();
                    String b = miembros.get(j).getCorreo();
                    if (!grafo.obtenerConexiones(a).contains(b)) {
                        grafo.conectar(a, b);
                    }
                }
            }
        }
    }

    public void actualizarConexionesAutomaticas() {
        conectarPorGrupos();
        conectarPorValoracionesCompartidas();
    }

    private boolean tienenValoracionesEnComun(Estudiante e1, Estudiante e2) {
        if (e1.getContenidosValorados() == null || e2.getContenidosValorados() == null) return false;
        Set<String> comunes = new HashSet<>(e1.getContenidosValorados());
        comunes.retainAll(e2.getContenidosValorados());
        return !comunes.isEmpty();
    }

    public void formarGruposPorIntereses() {
        Map<Set<String>, List<Estudiante>> mapaIntereses = new HashMap<>();

        for (Estudiante e : estudiantes.values()) {
            List<String> lista = new ArrayList<>(e.getIntereses());
            Collections.sort(lista);


            for (int i = 0; i < lista.size(); i++) {
                for (int j = i + 1; j < lista.size(); j++) {
                    Set<String> combinacion = new TreeSet<>();
                    combinacion.add(lista.get(i));
                    combinacion.add(lista.get(j));

                    mapaIntereses.computeIfAbsent(combinacion, k -> new ArrayList<>()).add(e);
                }
            }
        }

        for (Map.Entry<Set<String>, List<Estudiante>> entry : mapaIntereses.entrySet()) {
            List<Estudiante> miembros = entry.getValue();

            if (miembros.size() >= 2) { // solo si hay al menos 2 estudiantes
                String nombreGrupo = "Grupo: " + String.join(", ", entry.getKey());
                String idGrupo = UUID.randomUUID().toString();


                boolean yaExiste = gruposEstudio.values().stream()
                        .anyMatch(g -> new HashSet<>(g.getMiembros()).equals(new HashSet<>(miembros)));

                if (!yaExiste) {
                    GrupoEstudio nuevo = new GrupoEstudio(idGrupo, nombreGrupo);
                    nuevo.setTema("Intereses comunes");

                    for (Estudiante e : miembros) {
                        nuevo.agregarMiembro(e);
                    }

                    nuevo.getIntereses().addAll(entry.getKey());
                    gruposEstudio.put(idGrupo, nuevo);
                }
            }
        }

        guardarDatos();
    }


    public List<Estudiante> sugerenciasCombinadas(String correoEstudiante) {
        Estudiante actual = getEstudiantePorCorreo(correoEstudiante);
        if (actual == null) return new ArrayList<>();

        Set<String> yaRelacionados = new HashSet<>();
        yaRelacionados.add(correoEstudiante);
        yaRelacionados.addAll(grafo.obtenerConexiones(correoEstudiante));
        yaRelacionados.addAll(gestorSolicitudes.getSolicitudes().stream()
                .filter(s -> s.getCorreoEmisor().equals(correoEstudiante) || s.getCorreoReceptor().equals(correoEstudiante))
                .flatMap(s -> Stream.of(s.getCorreoEmisor(), s.getCorreoReceptor()))
                .collect(Collectors.toSet()));

        Map<String, Estudiante> sugerencias = new LinkedHashMap<>();


        for (Estudiante e : estudiantes.values()) {
            if (!yaRelacionados.contains(e.getCorreo()) &&
                    !Collections.disjoint(e.getIntereses(), actual.getIntereses())) {
                e.setOrigenSugerencia("Interés");
                sugerencias.put(e.getCorreo(), e);
            }
        }


        for (Estudiante e : grafo.sugerencias(correoEstudiante)) {
            if (!yaRelacionados.contains(e.getCorreo())) {
                e.setOrigenSugerencia("Grafo");
                sugerencias.putIfAbsent(e.getCorreo(), e);
            }
        }

        return new ArrayList<>(sugerencias.values());
    }

    public Estudiante getEstudiantePorCorreo(String correo) {
        for (Estudiante e : estudiantes.values()) {
            if (e.getCorreo().equalsIgnoreCase(correo)) return e;
        }
        return null;
    }

    public List<Contenido> obtenerContenidosMasValorados(int limite) {
        return arbolContenidos.todos().stream()
                .sorted(Comparator.comparingInt(Contenido::getValoraciones).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    public List<Estudiante> obtenerEstudiantesMasConectados(int limite) {
        Map<String, Integer> grados = grafo.obtenerGrados();

        return estudiantes.values().stream()
                .sorted((a, b) -> Integer.compare(
                        grados.getOrDefault(b.getCorreo(), 0),
                        grados.getOrDefault(a.getCorreo(), 0)))
                .limit(limite)
                .collect(Collectors.toList());
    }

    public Map<Integer, List<String>> obtenerComunidades() {
        return grafo.detectarComunidades();
    }


    public List<Estudiante> obtenerEstudiantesMasParticipativos(int limite) {
        return estudiantes.values().stream()
                .sorted((a, b) -> {
                    int puntosA = a.getContenidosPublicados().size() + a.getContenidosValorados().size();
                    int puntosB = b.getContenidosPublicados().size() + b.getContenidosValorados().size();
                    return Integer.compare(puntosB, puntosA);
                })
                .limit(limite)
                .collect(Collectors.toList());
    }












}


