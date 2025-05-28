package controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Usuario;
import modelo.Estudiante;


import sistema.AppMain;

public class LoginController {

    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;

    @FXML
    private void handleLogin(ActionEvent event) {
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Por favor ingresa correo y contraseña.");
            return;
        }

        Usuario usuario = AppMain.redSocial.getSistemaAutenticacion().iniciarSesion(correo, contrasena);

        if (usuario == null) {
            mostrarAlerta("Correo o contraseña incorrectos.");
            return;
        }

        try {
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            FXMLLoader loader;

            if (usuario instanceof Estudiante) {
                AppMain.redSocial.setEstudianteActivo((Estudiante) usuario);


                AppMain.redSocial.actualizarConexionesAutomaticas();

                loader = new FXMLLoader(getClass().getResource("/vista/PanelEstudiante.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/vista/PanelModerador.fxml"));
            }


            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar el panel.");
        }
    }

    @FXML
    private void irRegistro(ActionEvent event) {
        try {
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Registro.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir el registro.");
        }
    }
    @FXML
    private void cargarDatosPrueba() {
        try {
            sistema.DatosPrueba.cargarDatos();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Carga completada");
            alert.setHeaderText(null);
            alert.setContentText("Datos de prueba cargados correctamente.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar los datos de prueba.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
