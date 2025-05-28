package controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Estudiante;
import modelo.GrupoEstudio;
import modelo.Contenido;
import sistema.AppMain;

import java.util.*;
import java.util.stream.Collectors;

public class ConexionesController {

    @FXML private TableView<Estudiante> tablaConexiones;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colCorreo;
    @FXML private TableColumn<Estudiante, String> colIntereses;

    private ObservableList<Estudiante> conexiones = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));
        colIntereses.setCellValueFactory(data -> {
            String intereses = String.join(", ", data.getValue().getIntereses());
            return new javafx.beans.property.SimpleStringProperty(intereses);
        });

        cargarConexiones();
    }

    private void cargarConexiones() {
        Estudiante actual = AppMain.redSocial.getEstudianteActivo();
        String correoActual = actual.getCorreo();


        Set<String> correosConectados = new HashSet<>(AppMain.redSocial.getGrafo().obtenerConexiones(correoActual));

        for (GrupoEstudio grupo : AppMain.redSocial.getGruposEstudio().values()) {
            if (grupo.getMiembros().contains(actual)) {
                for (Estudiante miembro : grupo.getMiembros()) {
                    if (!miembro.getCorreo().equals(correoActual)) {
                        correosConectados.add(miembro.getCorreo());
                    }
                }
            }
        }

        Set<String> contenidosValorados = actual.getContenidos().stream()
                .map(Contenido::getTitulo) // o usa getId() si es más seguro
                .collect(Collectors.toSet());

        for (Estudiante e : AppMain.redSocial.getEstudiantes().values()) {
            if (!e.getCorreo().equals(correoActual)) {
                for (Contenido c : e.getContenidos()) {
                    if (contenidosValorados.contains(c.getTitulo())) {
                        correosConectados.add(e.getCorreo());
                        break;
                    }
                }
            }
        }

        List<Estudiante> amigos = AppMain.redSocial.getEstudiantes().values().stream()
                .filter(e -> correosConectados.contains(e.getCorreo()))
                .collect(Collectors.toList());

        conexiones.setAll(amigos);
        tablaConexiones.setItems(conexiones);
    }

    @FXML
    private void eliminarConexion() {
        Estudiante seleccionado = tablaConexiones.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            String actual = AppMain.redSocial.getEstudianteActivo().getCorreo();
            String otro = seleccionado.getCorreo();

            AppMain.redSocial.getGrafo().desconectar(actual, otro);
            AppMain.redSocial.guardarDatos();

            cargarConexiones();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Conexión eliminada con " + seleccionado.getNombre());
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un estudiante.");
            alert.show();
        }
    }
}


