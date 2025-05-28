package controladores;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sistema.AppMain;

import java.util.*;

public class GrafoVisualController {

    @FXML private Canvas canvasGrafo;

    @FXML
    public void initialize() {
        dibujarGrafo();
    }

    private void dibujarGrafo() {
        GraphicsContext gc = canvasGrafo.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasGrafo.getWidth(), canvasGrafo.getHeight());

        Map<String, Set<String>> adyacencias = AppMain.redSocial.getGrafo().adyacencias;
        List<String> nodos = new ArrayList<>(adyacencias.keySet());

        Map<String, double[]> posiciones = new HashMap<>();
        int radio = 250;
        double centerX = canvasGrafo.getWidth() / 2;
        double centerY = canvasGrafo.getHeight() / 2;

        for (int i = 0; i < nodos.size(); i++) {
            double angle = 2 * Math.PI * i / nodos.size();
            double x = centerX + radio * Math.cos(angle);
            double y = centerY + radio * Math.sin(angle);
            posiciones.put(nodos.get(i), new double[]{x, y});
        }


        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(2);
        for (String a : adyacencias.keySet()) {
            double[] posA = posiciones.get(a);
            for (String b : adyacencias.get(a)) {
                double[] posB = posiciones.get(b);
                if (a.compareTo(b) < 0) { // Evitar duplicados
                    gc.strokeLine(posA[0], posA[1], posB[0], posB[1]);
                }
            }
        }


        gc.setFill(Color.CORNFLOWERBLUE);
        gc.setStroke(Color.BLACK);
        for (String nodo : posiciones.keySet()) {
            double[] pos = posiciones.get(nodo);
            gc.fillOval(pos[0] - 10, pos[1] - 10, 20, 20);
            gc.strokeText(nodo, pos[0] + 12, pos[1] + 4);
        }
    }
}

