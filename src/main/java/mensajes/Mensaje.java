package mensajes;



import java.io.Serializable;

public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String remitente;
    private final String contenido;

    public Mensaje(String remitente, String contenido) {
        this.remitente = remitente;
        this.contenido = contenido;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getContenido() {
        return contenido;
    }


}

