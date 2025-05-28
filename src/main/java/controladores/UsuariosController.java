package controladores;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Usuario;

import modelo.Moderador;
import sistema.AppMain;

public class UsuariosController {

    @FXML private TextField txtBuscar;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colCorreo;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRol()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colCorreo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));

        mostrarTodos();
    }

    @FXML
    public void mostrarTodos() {
        listaUsuarios.clear();
        listaUsuarios.addAll(AppMain.redSocial.getSistemaAutenticacion().getTodos().values());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    public void buscarUsuario() {
        String filtro = txtBuscar.getText().toLowerCase();
        if (filtro.isEmpty()) {
            mostrarTodos();
            return;
        }

        ObservableList<Usuario> filtrados = FXCollections.observableArrayList();
        for (Usuario u : AppMain.redSocial.getSistemaAutenticacion().getTodos().values()) {
            if (u.getNombre().toLowerCase().contains(filtro) || u.getCorreo().toLowerCase().contains(filtro)) {
                filtrados.add(u);
            }
        }
        tablaUsuarios.setItems(filtrados);
    }

    @FXML
    public void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debes seleccionar un usuario.");
            return;
        }

        if (seleccionado instanceof Moderador) {
            mostrarAlerta("No puedes eliminar moderadores.");
            return;
        }

        boolean confirmado = mostrarConfirmacion("¿Eliminar usuario " + seleccionado.getCorreo() + "?");
        if (!confirmado) return;

        AppMain.redSocial.getSistemaAutenticacion().getTodos().remove(seleccionado.getCorreo());
        AppMain.redSocial.getEstudiantes().remove(seleccionado.getId());
        AppMain.redSocial.guardarDatos();

        mostrarTodos();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
