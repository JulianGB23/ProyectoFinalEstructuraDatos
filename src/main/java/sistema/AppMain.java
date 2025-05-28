package sistema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

    public static RedSocial redSocial;

    @Override
    public void start(Stage stage) {
        try {
            redSocial = RedSocial.cargarDatos();

            redSocial.formarGruposPorIntereses();


            redSocial.actualizarConexionesAutomaticas();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Red Social Educativa - Iniciar Sesión");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void stop() {

        if (redSocial != null) {
            redSocial.guardarDatos();
            System.out.println("Archivo guardado antes de salir.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
