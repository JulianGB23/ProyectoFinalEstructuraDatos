package controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sistema.AppMain;

import java.io.IOException;
import java.util.Objects;

public class PanelEstudianteController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    public void mostrarContenidos() {
        cargarVista("Contenidos.fxml");
    }

    @FXML
    public void mostrarGrupos() {
        cargarVista("GruposSugeridos.fxml");
    }
    public void mostrarSugerencias() {

        AppMain.redSocial.actualizarConexionesAutomaticas();
        cargarVista("Sugerencias.fxml");
    }

    @FXML
    public void mostrarMensajes() {
        cargarVista("Chat.fxml");
    }

    @FXML
    public void mostrarSolicitudes() {
        cargarVista("Solicitudes.fxml");
    }
    @FXML
    public void mostrarPerfil() {
        cargarVista("Perfil.fxml");
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
            Parent vista = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/vista/" + archivoFXML)));
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

