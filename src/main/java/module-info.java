module vista {
    // JavaFX


    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // Librerías externas
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    // PDF (iText)
    requires kernel;
    requires layout;
    requires itextpdf;

    // Testing
    requires junit;
    requires org.testng;

    // Exportar/control de acceso
    opens vista to javafx.fxml;
    opens controladores to javafx.fxml;
    opens modelo to javafx.base;

    exports sistema to javafx.graphics;
    exports controladores;
    exports modelo;
    exports Utilidades;
    exports estructuras;


    // Si tienes pruebas en otro paquete y necesitas abrirlos:

}
