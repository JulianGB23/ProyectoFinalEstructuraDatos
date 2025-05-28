package controladores;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import estructuras.SolicitudConexion;
import sistema.AppMain;

public class SolicitudAmistadController {

    @FXML private TableView<SolicitudConexion> tablaSolicitudes;
    @FXML private TableColumn<SolicitudConexion, String> colEmisor;
    private ObservableList<SolicitudConexion> solicitudes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colEmisor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreoEmisor()));
        cargarSolicitudes();
    }

    private void cargarSolicitudes() {
        String receptor = AppMain.redSocial.getEstudianteActivo().getCorreo();
        solicitudes.setAll(AppMain.redSocial.obtenerSolicitudesPara(receptor));
        tablaSolicitudes.setItems(solicitudes);
    }

    @FXML
    private void aceptarSolicitud() {
        SolicitudConexion seleccionada = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            String receptor = AppMain.redSocial.getEstudianteActivo().getCorreo();
            AppMain.redSocial.aceptarSolicitudConexion(seleccionada.getCorreoEmisor(), receptor);
            AppMain.redSocial.guardarDatos();
            cargarSolicitudes();
            mostrarMensaje("Conexión aceptada.");
        }
    }

    @FXML
    private void rechazarSolicitud() {
        SolicitudConexion seleccionada = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            String receptor = AppMain.redSocial.getEstudianteActivo().getCorreo();
            AppMain.redSocial.rechazarSolicitudConexion(seleccionada.getCorreoEmisor(), receptor);
            AppMain.redSocial.guardarDatos();
            cargarSolicitudes();
            mostrarMensaje("Solicitud rechazada.");
        }
    }

    private void mostrarMensaje(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText(null);
        alert.show();
    }
}
