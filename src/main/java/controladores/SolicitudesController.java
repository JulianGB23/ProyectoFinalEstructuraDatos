package controladores;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Estudiante;
import modelo.SolicitudAyuda;
import sistema.AppMain;

public class SolicitudesController {

    @FXML
    private TableView<SolicitudAyuda> tablaSolicitudes;

    @FXML
    private TableColumn<SolicitudAyuda, String> colTema;

    @FXML
    private TableColumn<SolicitudAyuda, String> colUrgencia;

    @FXML
    private TableColumn<SolicitudAyuda, String> colEstado;

    @FXML
    private TextField campoTema;

    @FXML
    private ComboBox<String> comboUrgencia;

    private ObservableList<SolicitudAyuda> solicitudes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboUrgencia.getItems().addAll("Alta", "Media", "Baja");

        colTema.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTema()));
        colUrgencia.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getUrgencia())));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));

        Estudiante actual = AppMain.redSocial.getEstudianteActivo();
        if (actual != null) {
            solicitudes.setAll(actual.getSolicitudes());
        }

        tablaSolicitudes.setItems(solicitudes);
    }

    @FXML
    private void enviarSolicitud() {
        String tema = campoTema.getText().trim();
        String urgenciaTexto = comboUrgencia.getValue();

        if (!tema.isEmpty() && urgenciaTexto != null) {
            int urgenciaValor = switch (urgenciaTexto) {
                case "Alta" -> 3;
                case "Media" -> 2;
                case "Baja" -> 1;
                default -> 1;
            };

            Estudiante actual = AppMain.redSocial.getEstudianteActivo();
            if (actual == null) {
                new Alert(Alert.AlertType.ERROR, "No hay un estudiante autenticado.").show();
                return;
            }

            SolicitudAyuda nueva = new SolicitudAyuda(actual.getCorreo(), tema, urgenciaValor);
            actual.agregarSolicitud(nueva);
            solicitudes.add(nueva);

            AppMain.redSocial.guardarDatos(); // Persistencia

            campoTema.clear();
            comboUrgencia.setValue(null);
        } else {
            new Alert(Alert.AlertType.WARNING, "Completa el tema y la urgencia.").show();
        }
    }
}
