package persistencia;


import estructuras.ABB;
import java.io.*;


public class ABBPersistencia {

    private static final String RUTA_ARCHIVO = "";

    public static void guardar(ABB abb) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
            oos.writeObject(abb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ABB cargar() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return new ABB();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ABB) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ABB();
        }
    }
}
