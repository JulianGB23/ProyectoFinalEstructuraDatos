package controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import modelo.Estudiante;
import modelo.GrupoEstudio;
import sistema.AppMain;

import java.util.*;

public class GruposSugeridosController {

    @FXML private ListView<String> listaGrupos;
    @FXML private ListView<String> listaMiembros;
    @FXML private Label lblNombreGrupo;
    @FXML private Label lblTemaGrupo;
    @FXML private Label lblMiembrosGrupo;

    private Estudiante estudianteActual;
    private Map<String, GrupoEstudio> gruposSugeridos;
    private boolean mostrandoMisGrupos = false;

    @FXML
    public void initialize() {
        mostrandoMisGrupos = false;
        cargarGruposSugeridos();
    }

    private void cargarGruposSugeridos() {
        estudianteActual = AppMain.redSocial.getEstudianteActivo();
        if (estudianteActual == null) {
            mostrarAlerta("No hay estudiante activo para sugerencias.");
            return;
        }

        gruposSugeridos = new LinkedHashMap<>();
        listaGrupos.getItems().clear();
        listaMiembros.getItems().clear();
        limpiarDetalles();

        Set<String> yaAgregados = new HashSet<>();

        for (GrupoEstudio grupo : AppMain.redSocial.getGruposEstudio().values()) {
            if (grupo.getMiembros().contains(estudianteActual)) continue;

            String interesComun = null;
            for (Estudiante miembro : grupo.getMiembros()) {
                for (String interes : miembro.getIntereses()) {
                    if (estudianteActual.getIntereses().contains(interes)) {
                        interesComun = interes;
                        break;
                    }
                }
                if (interesComun != null) break;
            }

            if (interesComun != null && !yaAgregados.contains(grupo.getId())) {
                gruposSugeridos.put(grupo.getId(), grupo);
                listaGrupos.getItems().add(grupo.getNombre() + " - Tema: " + grupo.getTema() + " (Interés en común: " + interesComun + ")");
                yaAgregados.add(grupo.getId());
            }
        }
    }


    @FXML
    public void unirseGrupo() {
        int index = listaGrupos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            mostrarAlerta("Selecciona un grupo para unirte.");
            return;
        }

        GrupoEstudio grupo = new ArrayList<>(gruposSugeridos.values()).get(index);
        grupo.agregarMiembro(estudianteActual);

        AppMain.redSocial.guardarDatos();
        mostrarInfo("Te has unido al grupo: " + grupo.getNombre());
        recargarVista();
    }

    @FXML
    public void crearGrupo() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Grupo de Estudio");
        dialog.setHeaderText("Crear grupo con nombre y tema");

        TextField nombreField = new TextField();
        TextField temaField = new TextField();
        nombreField.setPromptText("Nombre del grupo");
        temaField.setPromptText("Tema del grupo");

        VBox contenido = new VBox(10, new Label("Nombre:"), nombreField, new Label("Tema:"), temaField);
        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) return new Pair<>(nombreField.getText(), temaField.getText());
            return null;
        });

        Optional<Pair<String, String>> resultado = dialog.showAndWait();
        resultado.ifPresent(par -> {
            String idGrupo = UUID.randomUUID().toString();
            GrupoEstudio nuevo = new GrupoEstudio(idGrupo, par.getKey());
            nuevo.setTema(par.getValue());
            nuevo.agregarMiembro(estudianteActual);

            AppMain.redSocial.getGruposEstudio().put(idGrupo, nuevo);
            AppMain.redSocial.guardarDatos();
            mostrarInfo("Grupo creado exitosamente.");
            recargarVista();
        });
    }

    @FXML
    public void mostrarMiembros() {
        int index = listaGrupos.getSelectionModel().getSelectedIndex();
        listaMiembros.getItems().clear();

        if (index < 0) {
            limpiarDetalles();
            return;
        }

        GrupoEstudio grupo = new ArrayList<>(gruposSugeridos.values()).get(index);
        for (Estudiante e : grupo.getMiembros()) {
            listaMiembros.getItems().add(e.getNombre() + " (" + e.getCorreo() + ")");
        }

        lblNombreGrupo.setText("Nombre: " + grupo.getNombre());
        lblTemaGrupo.setText("Tema: " + grupo.getTema());
        lblMiembrosGrupo.setText("Miembros: " + grupo.getMiembros().size());
    }

    @FXML
    public void salirseGrupo() {
        int index = listaGrupos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            mostrarAlerta("Selecciona un grupo del cual salir.");
            return;
        }

        GrupoEstudio grupo = new ArrayList<>(gruposSugeridos.values()).get(index);
        if (!grupo.getMiembros().contains(estudianteActual)) {
            mostrarAlerta("No perteneces a este grupo.");
            return;
        }

        grupo.removerMiembro(estudianteActual);
        AppMain.redSocial.guardarDatos();
        mostrarInfo("Has salido del grupo: " + grupo.getNombre());
        recargarVista();
    }

    @FXML
    public void verGruposSugeridos() {
        mostrandoMisGrupos = false;
        cargarGruposSugeridos();
    }

    @FXML
    public void verMisGrupos() {
        mostrandoMisGrupos = true;
        listaGrupos.getItems().clear();
        listaMiembros.getItems().clear();
        gruposSugeridos.clear();
        limpiarDetalles();

        List<GrupoEstudio> misGrupos = AppMain.redSocial.getGruposDelEstudiante(estudianteActual.getId());
        for (GrupoEstudio grupo : misGrupos) {
            gruposSugeridos.put(grupo.getId(), grupo);
            listaGrupos.getItems().add(grupo.getNombre() + " - Tema: " + grupo.getTema());
        }
    }

    @FXML
    public void editarGrupo() {
        int index = listaGrupos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            mostrarAlerta("Selecciona un grupo.");
            return;
        }

        GrupoEstudio grupo = new ArrayList<>(gruposSugeridos.values()).get(index);

        if (!grupo.getMiembros().contains(estudianteActual)) {
            mostrarAlerta("Solo los miembros pueden editar el grupo.");
            return;
        }

        TextInputDialog dialogNombre = new TextInputDialog(grupo.getNombre());
        dialogNombre.setTitle("Editar Grupo");
        dialogNombre.setHeaderText("Editar nombre del grupo");
        dialogNombre.setContentText("Nuevo nombre:");

        Optional<String> nuevoNombre = dialogNombre.showAndWait();
        nuevoNombre.ifPresent(nombre -> grupo.setNombre(nombre));

        TextInputDialog dialogTema = new TextInputDialog(grupo.getTema());
        dialogTema.setTitle("Editar Grupo");
        dialogTema.setHeaderText("Editar tema del grupo");
        dialogTema.setContentText("Nuevo tema:");

        Optional<String> nuevoTema = dialogTema.showAndWait();
        nuevoTema.ifPresent(tema -> grupo.setTema(tema));

        AppMain.redSocial.guardarDatos();
        mostrarInfo("Grupo actualizado.");
        recargarVista();
    }

    private void recargarVista() {
        if (mostrandoMisGrupos) {
            verMisGrupos();
        } else {
            cargarGruposSugeridos();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarDetalles() {
        lblNombreGrupo.setText("Nombre: -");
        lblTemaGrupo.setText("Tema: -");
        lblMiembrosGrupo.setText("Miembros: -");
    }
}
