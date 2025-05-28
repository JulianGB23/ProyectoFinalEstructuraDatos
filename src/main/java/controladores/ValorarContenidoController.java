package controladores;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ValorarContenidoController {

    @FXML
    private ComboBox<Integer> comboPuntaje;

    private int puntajeSeleccionado = 0;
    private boolean confirmado = false;

    @FXML
    public void initialize() {
        comboPuntaje.getItems().addAll(1, 2, 3, 4, 5);
    }

    @FXML
    private void valorar() {
        Integer seleccion = comboPuntaje.getValue();
        if (seleccion == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un puntaje.");
            alert.show();
            return;
        }
        puntajeSeleccionado = seleccion;
        confirmado = true;
        cerrar();
    }

    @FXML
    private void cancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) comboPuntaje.getScene().getWindow();
        stage.close();
    }

    public boolean fueConfirmado() {
        return confirmado;
    }

    public int getPuntajeSeleccionado() {
        return puntajeSeleccionado;
    }
}
