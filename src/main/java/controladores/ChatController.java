package controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mensajes.Mensaje;
import sistema.AppMain;

import java.util.List;

public class ChatController {

    @FXML private ListView<String> listaAmigos;

    @FXML private TextField campoMensaje;
    @FXML private Label labelChatCon;
    @FXML private VBox contenedorMensajes;
    @FXML private ScrollPane scrollMensajes;


    private String amigoSeleccionado = null;

    @FXML
    public void initialize() {

        List<String> conexiones = AppMain.redSocial.getGrafo().obtenerConexiones(AppMain.redSocial.getEstudianteActivo().getCorreo());
        listaAmigos.setItems(FXCollections.observableArrayList(conexiones));

        listaAmigos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                amigoSeleccionado = newVal;
                labelChatCon.setText("Chateando con: " + newVal);
                cargarConversacion();
            }
        });
    }

    private void cargarConversacion() {
        if (amigoSeleccionado == null) return;

        contenedorMensajes.getChildren().clear();
        List<Mensaje> conversacion = AppMain.redSocial.getGestorMensajes().obtenerConversacion(
                AppMain.redSocial.getEstudianteActivo().getCorreo(), amigoSeleccionado);

        String yo = AppMain.redSocial.getEstudianteActivo().getCorreo();

        for (Mensaje mensaje : conversacion) {
            Label label = new Label(mensaje.getContenido());
            label.setWrapText(true);
            label.setMaxWidth(300);
            label.setPadding(new Insets(8));
            label.setStyle("-fx-background-color: " +
                    (mensaje.getRemitente().equals(yo) ? "#dcf8c6" : "#ffffff") + ";" +
                    "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: lightgray;");

            HBox burbuja = new HBox(label);
            burbuja.setMaxWidth(Double.MAX_VALUE);
            burbuja.setAlignment(
                    mensaje.getRemitente().equals(yo)
                            ? javafx.geometry.Pos.CENTER_RIGHT
                            : javafx.geometry.Pos.CENTER_LEFT
            );
            contenedorMensajes.getChildren().add(burbuja);
        }

        scrollMensajes.setVvalue(1.0); // auto scroll al final
    }


    @FXML
    private void enviarMensaje() {
        if (amigoSeleccionado == null) return;

        String texto = campoMensaje.getText().trim();
        if (!texto.isEmpty()) {
            String emisor = AppMain.redSocial.getEstudianteActivo().getCorreo();
            AppMain.redSocial.getGestorMensajes().enviarMensaje(emisor, amigoSeleccionado, texto);
            campoMensaje.clear();
            AppMain.redSocial.guardarDatos();
            cargarConversacion();
        }
    }
}
