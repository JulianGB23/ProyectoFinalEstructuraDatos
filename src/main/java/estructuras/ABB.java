package estructuras;

import modelo.Contenido;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ABB implements Serializable {
    private static final long serialVersionUID = 1L;



    private class Nodo implements Serializable {
        Contenido contenido;
        Nodo izq, der;

        Nodo(Contenido c) {
            this.contenido = c;
        }
    }

    private Nodo raiz;


    public void insertar(Contenido c) {
        raiz = insertarRec(raiz, c);
    }

    private Nodo insertarRec(Nodo actual, Contenido c) {
        if (actual == null) return new Nodo(c);

        if (c.getTitulo().compareToIgnoreCase(actual.contenido.getTitulo()) < 0)
            actual.izq = insertarRec(actual.izq, c);
        else
            actual.der = insertarRec(actual.der, c);

        return actual;
    }


    public Contenido buscarPorId(String id) {
        for (Contenido c : inOrden()) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }


    public List<Contenido> inOrden() {
        List<Contenido> lista = new ArrayList<>();
        inOrdenRec(raiz, lista);
        return lista;
    }

    private void inOrdenRec(Nodo actual, List<Contenido> lista) {
        if (actual != null) {
            inOrdenRec(actual.izq, lista);
            lista.add(actual.contenido);
            inOrdenRec(actual.der, lista);
        }
    }


    public List<Contenido> buscarPorTitulo(String titulo) {
        List<Contenido> resultado = new ArrayList<>();
        for (Contenido c : inOrden()) {
            if (c.getTitulo().equalsIgnoreCase(titulo)) {
                resultado.add(c);
            }
        }
        return resultado;
    }


    public List<Contenido> buscarPorAutor(String autorCorreo) {
        List<Contenido> resultado = new ArrayList<>();
        for (Contenido c : inOrden()) {
            if (c.getAutor().equalsIgnoreCase(autorCorreo)) {
                resultado.add(c);
            }
        }
        return resultado;
    }


    public List<Contenido> buscarPorTipo(String tipo) {
        List<Contenido> resultado = new ArrayList<>();
        for (Contenido c : inOrden()) {
            if (c.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(c);
            }
        }
        return resultado;
    }


    public List<Contenido> todos() {
        return inOrden();
    }


    public boolean eliminar(String id) {
        if (buscarPorId(id) == null) return false;

        List<Contenido> todos = inOrden();
        raiz = null;

        for (Contenido c : todos) {
            if (!c.getId().equals(id)) {
                insertar(c);
            }
        }

        return true;
    }
}
