package controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.Contenido;
import sistema.AppMain;

public class PublicarContenidoController {

    @FXML
    private TextField campoTitulo;

    @FXML
    private ComboBox<String> comboTipo;

    @FXML
    private TextArea campoDescripcion;

    private Contenido contenidoGenerado;
    private boolean publicado = false;

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("PDF", "Video", "Enlace", "Otro");
    }

    @FXML
    public void publicar() {
        String titulo = campoTitulo.getText().trim();
        String tipo = comboTipo.getValue();
        String descripcion = campoDescripcion.getText().trim();

        if (titulo.isEmpty() || tipo == null) {
            mostrarAlerta("Por favor completa todos los campos obligatorios.");
            return;
        }

        String autor = AppMain.redSocial.getEstudianteActivo().getId();

        contenidoGenerado = new Contenido(titulo, autor, tipo);
        contenidoGenerado.setDescripcion(descripcion.isEmpty() ? "Sin descripción" : descripcion);
        publicado = true;

        cerrarVentana();
    }

    @FXML
    public void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) campoTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public boolean fuePublicado() {
        return publicado;
    }

    public Contenido getContenidoGenerado() {
        return contenidoGenerado;
    }
}
