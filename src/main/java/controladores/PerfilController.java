package controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Estudiante;
import sistema.AppMain;

import java.util.Map;

public class PerfilController {

    @FXML private TextField campoInteres;
    @FXML private ListView<String> listaIntereses;
    @FXML private TextField txtNombre, txtCorreo;
    @FXML private Label lblPublicaciones, lblSolicitudes, lblIntereses;

    private ObservableList<String> interesesObservable;
    private Estudiante estudiante;

    @FXML
    public void initialize() {
        estudiante = AppMain.redSocial.getEstudianteActivo();

        txtNombre.setText(estudiante.getNombre());
        txtCorreo.setText(estudiante.getCorreo());

        lblPublicaciones.setText("Contenidos publicados: " + estudiante.getContenidos().size());
        lblSolicitudes.setText("Solicitudes de ayuda: " + estudiante.getSolicitudes().size());
        lblIntereses.setText("Cantidad de intereses: " + estudiante.getIntereses().size());

        interesesObservable = FXCollections.observableArrayList(estudiante.getIntereses());
        listaIntereses.setItems(interesesObservable);
    }

    @FXML
    public void agregarInteres() {
        String nuevo = campoInteres.getText().trim();
        if (!nuevo.isEmpty() && !interesesObservable.contains(nuevo.toLowerCase())) {
            interesesObservable.add(nuevo.toLowerCase());
            estudiante.agregarInteres(nuevo);
            AppMain.redSocial.guardarDatos();
            campoInteres.clear();
            actualizarContador();
        }
    }

    @FXML
    public void eliminarInteres() {
        String seleccionado = listaIntereses.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            interesesObservable.remove(seleccionado);
            estudiante.eliminarInteres(seleccionado);
            AppMain.redSocial.guardarDatos();
            actualizarContador();
        }
    }

    @FXML
    public void guardarCambios() {
        Estudiante actual = AppMain.redSocial.getEstudianteActivo();
        String correoAntiguo = actual.getCorreo();
        String nuevoNombre = txtNombre.getText().trim();
        String nuevoCorreo = txtCorreo.getText().trim().toLowerCase();

        if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty()) {
            mostrarAlerta("Debe llenar ambos campos.");
            return;
        }

        boolean correoCambio = !nuevoCorreo.equalsIgnoreCase(correoAntiguo);
        boolean correoYaExiste = AppMain.redSocial.getSistemaAutenticacion().existeCorreo(nuevoCorreo);

        if (correoCambio && correoYaExiste) {
            mostrarAlerta("Ese correo ya está en uso por otro estudiante.");
            return;
        }


        actual.setNombre(nuevoNombre);
        actual.setCorreo(nuevoCorreo);

        if (correoCambio) {
            System.out.println("✅ CORREO CAMBIADO: " + correoAntiguo + " → " + nuevoCorreo);


            AppMain.redSocial.getSistemaAutenticacion().actualizarCorreo(correoAntiguo, nuevoCorreo, actual);
            System.out.println("SistemaAutenticacion actualizado");


            AppMain.redSocial.getGrafo().actualizarCorreoEstudiante(correoAntiguo, nuevoCorreo);
            System.out.println("Grafo actualizado");


            AppMain.redSocial.getEstudiantes().put(actual.getId(), actual);
            System.out.println("Mapa de estudiantes actualizado");
        }


        AppMain.redSocial.guardarDatos();
        System.out.println("Datos guardados correctamente");

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText("Guardado");
        alerta.setContentText("Datos actualizados correctamente.");
        alerta.showAndWait();
    }


    private void actualizarContador() {
        lblIntereses.setText("Cantidad de intereses: " + interesesObservable.size());
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


