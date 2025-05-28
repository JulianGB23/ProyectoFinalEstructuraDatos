package controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Estudiante;
import sistema.AppMain;

import java.util.stream.Collectors;

public class SugerenciasController {

    @FXML private TableView<Estudiante> tablaSugerencias;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colCorreo;
    @FXML private TableColumn<Estudiante, String> colIntereses;
    @FXML private TableColumn<Estudiante, String> colOrigen;

    private ObservableList<Estudiante> sugeridos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        colIntereses.setCellValueFactory(data -> {
            String intereses = data.getValue().getIntereses().stream()
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .distinct()
                    .sorted()
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(intereses);
        });

        colOrigen.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getOrigenSugerencia())
        );

        cargarSugerencias();
    }

    private void cargarSugerencias() {
        sugeridos.clear();

        var lista = AppMain.redSocial.sugerenciasCombinadas(
                AppMain.redSocial.getEstudianteActivo().getCorreo()
        );


        System.out.println("Sugerencias visibles:");
        for (Estudiante e : lista) {
            System.out.println(" - " + e.getCorreo());
        }

        sugeridos.addAll(lista);
        tablaSugerencias.setItems(sugeridos);
    }

    @FXML
    private void enviarSolicitud() {
        Estudiante seleccionado = tablaSugerencias.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String emisor = AppMain.redSocial.getEstudianteActivo().getCorreo();
            String receptor = seleccionado.getCorreo(); // YA NO usamos replace aquí

            AppMain.redSocial.enviarSolicitudConexion(emisor, receptor);
            AppMain.redSocial.guardarDatos();

            sugeridos.remove(seleccionado);

            new Alert(Alert.AlertType.INFORMATION,
                    "Solicitud enviada a " + seleccionado.getNombre()).show();
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Selecciona un estudiante.").show();
        }
    }

    @FXML
    private void verSolicitudesPendientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/SolicitudAmistad.fxml"));
            Scene escena = new Scene(loader.load());
            Stage ventana = new Stage();
            ventana.setTitle("Solicitudes de conexión recibidas");
            ventana.setScene(escena);
            ventana.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de solicitudes.").show();
        }
    }

    @FXML
    private void verConexiones() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Conexiones.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Conexiones");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de conexiones.").show();
        }
    }
}

