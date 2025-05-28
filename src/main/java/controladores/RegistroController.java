package controladores;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Estudiante;
import modelo.Moderador;
import sistema.AppMain;
import sistema.RedSocial;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cmbRol;

    @FXML
    private void initialize() {
        cmbRol.getItems().clear();
        cmbRol.getItems().addAll("Estudiante", "Moderador");
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim().toLowerCase();
        String contrasena = txtContrasena.getText().trim();
        String rol = cmbRol.getValue();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || rol == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        String id = correo; // puedes cambiarlo por UUID si deseas
        boolean exito = false;

        if (rol.equals("Estudiante")) {
            exito = AppMain.redSocial.registrarEstudiante(id, nombre, correo, contrasena);
        } else {
            exito = AppMain.redSocial.registrarModerador(id, nombre, correo, contrasena);
        }

        if (exito) {
            AppMain.redSocial.guardarDatos();
            AppMain.redSocial = RedSocial.cargarDatos();
            mostrarInfo("Usuario registrado correctamente.");
            irLogin(null);
        } else {
            mostrarAlerta("El correo ya está registrado.");
        }
    }

    @FXML
    private void irLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo volver al login.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
