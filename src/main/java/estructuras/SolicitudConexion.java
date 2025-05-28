package estructuras;

import java.io.Serializable;

public class SolicitudConexion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String correoEmisor, correoReceptor;
    public SolicitudConexion(String emisor, String receptor) {
        this.correoEmisor = emisor;
        this.correoReceptor = receptor;
    }
    public String getCorreoEmisor() { return correoEmisor; }
    public String getCorreoReceptor() { return correoReceptor; }
}
