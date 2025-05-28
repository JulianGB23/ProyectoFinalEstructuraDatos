package controladores;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PanelModeradorController {

    @FXML private AnchorPane contentPane;

    @FXML
    public void mostrarUsuarios() {
        cargarVista("Usuarios.fxml");
    }

    @FXML
    public void mostrarContenidos() {
        cargarVista("ContenidosModerador.fxml");
    }

    @FXML
    public void mostrarGrafo() {
        cargarVista("Grafo.fxml");
    }

    @FXML
    public void mostrarReportes() {
        cargarVista("Reportes.fxml");
    }

    @FXML
    public void cerrarSesion(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Iniciar Sesión");
        } catch (IOException ex) {
            mostrarError("Error al cerrar sesión");
        }
    }

    private void cargarVista(String archivoFXML) {
        try {
            AnchorPane vista = FXMLLoader.load(getClass().getResource("/vista/" + archivoFXML));
            contentPane.getChildren().setAll(vista);
        } catch (IOException e) {
            mostrarError("No se pudo cargar la vista: " + archivoFXML);
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.show();
    }
}

