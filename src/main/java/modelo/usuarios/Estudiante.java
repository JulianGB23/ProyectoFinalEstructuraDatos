package modelo.usuarios;

import modelo.usuarios.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Usuario {

    private List<String> contenidosPublicados;
    private List<String> valoracionesRealizadas;
    private List<String> solicitudesDeAyuda;

    public Estudiante(String id, String nombre, String correo, String contrasenia){

        super(id, nombre, correo, contrasenia);
        this.contenidosPublicados= new ArrayList<>();
        this.solicitudesDeAyuda= new ArrayList<>();
        this.valoracionesRealizadas= new ArrayList<>();

    }

    @Override
    public String getTipoUsuario(){

        return "Estudiante";
    }

    //métodos del Estudiante

    public void publicarContenido(String contenidoId){

        contenidosPublicados.add(contenidoId);
        }

    public void valorarContenido(String contenidoId){

        valoracionesRealizadas.add(contenidoId);
        }
    public void solicitarAyuda(String tema){

        solicitudesDeAyuda.add(tema);
        }

    //Getters


    public List<String> getContenidosPublicados() {
        return contenidosPublicados;
    }

    public List<String> getValoracionesRealizadas() {
        return valoracionesRealizadas;
    }

    public List<String> getSolicitudesDeAyuda() {
        return solicitudesDeAyuda;
    }
}
