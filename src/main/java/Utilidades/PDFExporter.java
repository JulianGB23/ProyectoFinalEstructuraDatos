package Utilidades;


import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PDFExporter {

    public static void exportarReporte(String titulo, List<String> lineas, File destino) {
        try {
            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(titulo).setBold().setFontSize(16).setMarginBottom(10));

            for (String linea : lineas) {
                document.add(new Paragraph(linea));
            }

            document.close();
            System.out.println("PDF generado en: " + destino.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo generar el PDF: " + e.getMessage());
        }
    }
}

