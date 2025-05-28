package controladores;



import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Contenido;

public class VerDetalleContenidoController {

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblAutor;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblValoracion;

    @FXML private Label lblDescripcion;
    @FXML private Label lblFecha;


    private Contenido contenido;

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;

        lblTitulo.setText("Título: " + contenido.getTitulo());
        lblAutor.setText("Autor: " + contenido.getAutor());
        lblTipo.setText("Tipo: " + contenido.getTipo());
        lblValoracion.setText("Valoraciones: " + contenido.getValoraciones());
        lblDescripcion.setText("Descripción: " + contenido.getDescripcion());
        lblFecha.setText("Publicado: " + contenido.getFechaPublicacion());
    }


    @FXML
    private void cerrar() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }
}

