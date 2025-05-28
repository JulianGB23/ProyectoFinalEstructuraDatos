package controladores;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Utilidades.PDFExporter;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import modelo.Contenido;
import modelo.Estudiante;
import sistema.AppMain;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ReportesController {

    @FXML private TextArea resultadoArea;

    public void reporteParticipacion() {
        Map<String, Estudiante> estudiantes = AppMain.redSocial.getEstudiantes();

        StringBuilder sb = new StringBuilder("Estudiantes más participativos:\n\n");

        estudiantes.values().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getContenidos().size(), e1.getContenidos().size()))
                .limit(10)
                .forEach(e -> sb.append("").append(e.getNombre())
                        .append(" - Contenidos: ").append(e.getContenidos().size()).append("\n"));

        resultadoArea.setText(sb.toString());
    }

    public void reporteMasValorados() {
        List<Contenido> todos = AppMain.redSocial.getArbolContenidos().todos();

        StringBuilder sb = new StringBuilder("Contenidos más valorados:\n\n");

        todos.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getValoraciones(), c1.getValoraciones()))
                .limit(10)
                .forEach(c -> sb.append("").append(c.getTitulo())
                        .append(" (").append(c.getTipo()).append(")")
                        .append(" - Valoraciones: ").append(c.getValoraciones()).append("\n"));

        resultadoArea.setText(sb.toString());
    }

    public void reporteMasConectados() {
        Map<String, Integer> grados = AppMain.redSocial.getGrafo().obtenerGrados();

        StringBuilder sb = new StringBuilder("Estudiantes con más conexiones:\n\n");

        grados.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach(entry -> {
                    String correo = entry.getKey();
                    Estudiante e = AppMain.redSocial.getEstudiantePorCorreo(correo);
                    if (e != null) {
                        sb.append("").append(e.getNombre())
                                .append(" - Conexiones: ").append(entry.getValue()).append("\n");
                    }
                });

        resultadoArea.setText(sb.toString());
    }



    public void reporteComunidades() {
        Map<Integer, List<String>> comunidades = AppMain.redSocial.getGrafo().detectarComunidades();

        StringBuilder sb = new StringBuilder("Comunidades (clústeres):\n\n");

        for (Map.Entry<Integer, List<String>> entry : comunidades.entrySet()) {
            sb.append("Comunidad ").append(entry.getKey()).append(": ");
            List<String> nombres = entry.getValue().stream()
                    .map(correo -> {
                        Estudiante e = AppMain.redSocial.getEstudiantePorCorreo(correo);
                        return e != null ? e.getNombre() : correo;
                    })
                    .collect(Collectors.toList());
            sb.append(String.join(", ", nombres)).append("\n\n");
        }

        resultadoArea.setText(sb.toString());
    }


    @FXML
    public void exportarPDF() {
        String contenido = resultadoArea.getText();

        if (contenido == null || contenido.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportar PDF");
            alert.setHeaderText(null);
            alert.setContentText("No hay contenido para exportar.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte como PDF");
        fileChooser.setInitialFileName("reporte.pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

        File archivo = fileChooser.showSaveDialog(resultadoArea.getScene().getWindow());

        if (archivo != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(archivo));
                document.open();
                document.add(new Paragraph(contenido));
                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("El PDF fue exportado exitosamente.");
                alert.showAndWait();
            } catch (DocumentException | IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Ocurrió un error al generar el PDF.");
                alert.showAndWait();
            }
        }
    }

}
