package controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sistema.AppMain;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GrafoController {

    @FXML private ListView<String> listaConexiones;
    @FXML private TextField txtInicio;
    @FXML private TextField txtDestino;
    @FXML private TextArea txtRuta;
    @FXML private TextArea txtComunidades;

    @FXML
    public void initialize() {
        mostrarConexiones();
        mostrarComunidades();
    }

    private void mostrarConexiones() {
        listaConexiones.getItems().clear();
        AppMain.redSocial.getGrafo().adyacencias.forEach((e, conexiones) -> {
            listaConexiones.getItems().add(e + " → " + conexiones);
        });
    }

    private void mostrarComunidades() {
        Map<Integer, List<String>> comunidades = AppMain.redSocial.getGrafo().detectarComunidades();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<String>> entry : comunidades.entrySet()) {
            sb.append("Grupo ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        txtComunidades.setText(sb.toString());
    }

    @FXML
    private void calcularRuta() {
        String inicio = txtInicio.getText().trim();
        String destino = txtDestino.getText().trim();
        if (inicio.isEmpty() || destino.isEmpty()) {
            txtRuta.setText("Por favor ingresa ambos correos.");
            return;
        }

        List<String> ruta = AppMain.redSocial.getGrafo().rutaMasCorta(inicio, destino);
        if (ruta.isEmpty()) {
            txtRuta.setText("No hay conexión entre los estudiantes.");
        } else {
            txtRuta.setText(String.join(" → ", ruta));
        }
    }

    @FXML
    public void abrirVentanaVisualGrafo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/GrafoVisual.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Visualización Gráfica del Grafo");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la visualización gráfica del grafo.");
            alert.show();
        }
    }

}


