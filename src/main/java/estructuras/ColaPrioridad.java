package estructuras;

import modelo.SolicitudAyuda;

import java.io.Serializable;
import java.util.PriorityQueue;

public class ColaPrioridad implements Serializable {
    private static final long serialVersionUID = 1L;

    private PriorityQueue<SolicitudAyuda> cola = new PriorityQueue<>();
    public void agregar(SolicitudAyuda s) { cola.offer(s); }
    public SolicitudAyuda atender() { return cola.poll(); }
    public boolean estaVacia() { return cola.isEmpty(); }
}