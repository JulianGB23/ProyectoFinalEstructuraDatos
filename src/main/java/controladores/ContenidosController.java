package controladores;

import estructuras.ABB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Contenido;
import modelo.Estudiante;
import sistema.AppMain;

import java.io.IOException;

public class ContenidosController {

    @FXML
    private TextField campoBusqueda;

    @FXML
    private TableView<Contenido> tablaContenidos;

    @FXML
    private TableColumn<Contenido, String> colTitulo;

    @FXML
    private TableColumn<Contenido, String> colAutor;

    @FXML
    private TableColumn<Contenido, String> colTipo;

    @FXML
    private TableColumn<Contenido, Integer> colValoracion;

    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colValoracion.setCellValueFactory(new PropertyValueFactory<>("valoraciones"));

        tablaContenidos.setRowFactory(tv -> {
            TableRow<Contenido> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Contenido seleccionado = row.getItem();
                    mostrarDetalle(seleccionado);
                }
            });
            return row;
        });

        actualizarTabla(AppMain.redSocial.getArbolContenidos().todos());
    }

    @FXML
    public void buscarContenido() {
        String filtro = campoBusqueda.getText().trim().toLowerCase();
        ABB arbol = AppMain.redSocial.getArbolContenidos();

        if (filtro.isEmpty()) {
            actualizarTabla(arbol.todos());
            return;
        }

        ObservableList<Contenido> filtrado = FXCollections.observableArrayList();
        for (Contenido c : arbol.todos()) {
            if (c.getTitulo().toLowerCase().contains(filtro) ||
                    c.getAutor().toLowerCase().contains(filtro) ||
                    c.getTipo().toLowerCase().contains(filtro)) {
                filtrado.add(c);
            }
        }

        actualizarTabla(filtrado);
    }

    @FXML
    public void publicarContenido() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PublicarContenido.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Publicar Contenido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            PublicarContenidoController controller = loader.getController();
            if (controller.fuePublicado()) {
                Contenido nuevo = controller.getContenidoGenerado();

                AppMain.redSocial.getArbolContenidos().insertar(nuevo);

                Estudiante actual = AppMain.redSocial.getEstudianteActivo();
                if (actual != null) {
                    actual.getContenidos().add(nuevo);
                }

                AppMain.redSocial.guardarDatos();
                actualizarTabla(AppMain.redSocial.getArbolContenidos().todos());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir el formulario.");
            alert.show();
        }
    }

    @FXML
    public void valorarContenido() {
        Contenido seleccionado = tablaContenidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un contenido primero.");
            alert.show();
            return;
        }

        // Verificar que no sea el autor
        String correoActual = AppMain.redSocial.getEstudianteActivo().getCorreo();
        if (seleccionado.getAutor().equalsIgnoreCase(correoActual)) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION, "No puedes valorar tu propio contenido.");
            alerta.show();
            return;
        }
        Estudiante actual = AppMain.redSocial.getEstudianteActivo();
        if (actual.getContenidosValorados().contains(seleccionado.getId())) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Ya has valorado este contenido.");
            alerta.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/ValorarContenido.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Valorar Contenido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            ValorarContenidoController controller = loader.getController();
            if (controller.fueConfirmado()) {
                int puntaje = controller.getPuntajeSeleccionado();

                for (int i = 0; i < puntaje; i++) {
                    seleccionado.valorar();
                }

                actual.getContenidosValorados().add(seleccionado.getId()); // ✅ se marca como valorado

                AppMain.redSocial.actualizarConexionesAutomaticas(); // ✅ conecta automáticamente

                AppMain.redSocial.guardarDatos();
                tablaContenidos.refresh();
            }



        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de valoración.");
            alert.show();
        }
    }


    private void mostrarDetalle(Contenido contenido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VerDetallesContenido.fxml"));
            Parent root = loader.load();

            VerDetalleContenidoController controller = loader.getController();
            controller.setContenido(contenido);

            Stage stage = new Stage();
            stage.setTitle("Detalle del Contenido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo mostrar el detalle.");
            alert.show();
        }
    }

    @FXML
    public void eliminarContenido() {
        Contenido seleccionado = tablaContenidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un contenido para eliminar.");
            alert.show();
            return;
        }

        Estudiante actual = AppMain.redSocial.getEstudianteActivo();
        if (actual == null || !seleccionado.getAutor().equalsIgnoreCase(actual.getCorreo())) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Solo puedes eliminar tus propios contenidos.");
            alerta.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que deseas eliminar este contenido?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean eliminadoDelABB = AppMain.redSocial.getArbolContenidos().eliminar(seleccionado.getId());
            boolean eliminadoDelEstudiante = actual.getContenidos().removeIf(c -> c.getId().equals(seleccionado.getId()));

            if (eliminadoDelABB && eliminadoDelEstudiante) {
                AppMain.redSocial.guardarDatos();
                actualizarTabla(AppMain.redSocial.getArbolContenidos().todos());

                Alert exito = new Alert(Alert.AlertType.INFORMATION, "Contenido eliminado correctamente.");
                exito.show();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "No se pudo eliminar el contenido.");
                error.show();
            }
        }
    }

    private void actualizarTabla(java.util.List<Contenido> lista) {
        tablaContenidos.setItems(FXCollections.observableArrayList(lista));
    }
}
