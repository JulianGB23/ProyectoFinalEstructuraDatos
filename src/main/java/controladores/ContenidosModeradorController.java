package controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Contenido;
import sistema.AppMain;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ContenidosModeradorController {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Contenido> tablaContenidos;
    @FXML private TableColumn<Contenido, String> colTitulo;
    @FXML private TableColumn<Contenido, String> colAutor;
    @FXML private TableColumn<Contenido, String> colTipo;
    @FXML private TableColumn<Contenido, String> colValoraciones;
    @FXML private TableColumn<Contenido, String> colFecha;

    private ObservableList<Contenido> listaContenidos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colAutor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAutor()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colValoraciones.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getValoraciones())));
        colFecha.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFechaPublicacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        mostrarTodos();
    }

    @FXML
    public void mostrarTodos() {
        listaContenidos.clear();
        List<Contenido> todos = AppMain.redSocial.getArbolContenidos().todos();
        listaContenidos.addAll(todos);
        tablaContenidos.setItems(listaContenidos);
    }

    @FXML
    public void buscarContenido() {
        String filtro = txtBuscar.getText().toLowerCase();
        if (filtro.isEmpty()) {
            mostrarTodos();
            return;
        }

        listaContenidos.clear();
        for (Contenido c : AppMain.redSocial.getArbolContenidos().todos()) {
            if (c.getTitulo().toLowerCase().contains(filtro) ||
                    c.getAutor().toLowerCase().contains(filtro)) {
                listaContenidos.add(c);
            }
        }
        tablaContenidos.setItems(listaContenidos);
    }

    @FXML
    public void eliminarContenido() {
        Contenido seleccionado = tablaContenidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debes seleccionar un contenido para eliminar.");
            return;
        }

        boolean confirmado = mostrarConfirmacion("¿Eliminar el contenido '" + seleccionado.getTitulo() + "'?");
        if (!confirmado) return;

        AppMain.redSocial.getArbolContenidos().eliminar(seleccionado.getId());
        AppMain.redSocial.guardarDatos();
        mostrarTodos();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}


