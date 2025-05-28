package sistema;

import modelo.Estudiante;

public class DatosPrueba {

    public static void cargarDatos() {

        Estudiante e1 = new Estudiante("101", "Ana", "ana@email.com", "1234");
        e1.agregarInteres("Matemáticas");
        e1.agregarInteres("Física");

        Estudiante e2 = new Estudiante("102", "Luis", "luis@email.com", "1234");
        e2.agregarInteres("Programación");

        Estudiante e3 = new Estudiante("103", "Marta", "marta@email.com", "1234");
        e3.agregarInteres("Química");


        AppMain.redSocial.registrarEstudiante(e1.getId(), e1.getNombre(), e1.getCorreo(), e1.getContrasena());
        AppMain.redSocial.getEstudiante(e1.getId()).agregarInteres("Matemáticas");
        AppMain.redSocial.getEstudiante(e1.getId()).agregarInteres("Física");

        AppMain.redSocial.registrarEstudiante(e2.getId(), e2.getNombre(), e2.getCorreo(), e2.getContrasena());
        AppMain.redSocial.getEstudiante(e2.getId()).agregarInteres("Programación");

        AppMain.redSocial.registrarEstudiante(e3.getId(), e3.getNombre(), e3.getCorreo(), e3.getContrasena());
        AppMain.redSocial.getEstudiante(e3.getId()).agregarInteres("Química");


        AppMain.redSocial.guardarDatos();

        System.out.println("Datos de prueba cargados en memoria y guardados en red_social.dat.");
    }
}

