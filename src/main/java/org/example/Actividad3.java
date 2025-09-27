package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Actividad3 - Programa que lista el contenido de un directorio y genera un archivo CSV con los detalles.
 * <p>
 * El programa recibe por consola la ruta de un directorio, ejecuta un comando del sistema operativo
 * para mostrar su contenido, y luego guarda en un archivo CSV el nombre, tipo, tamaño y permisos
 * de cada archivo.
 * </p>
 *
 * @author Alexis
 * @version 1.0
 */
public class Actividad3 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Proporciona un directorio como args");
            return;
        }

        String rutaDirectorio = args[0];
        File directorio = new File(rutaDirectorio);

        if (!directorio.exists() || !directorio.isDirectory()) {
            System.out.println("La ruta proporcionada no es un directorio");
            return;
        }
// no sabia como hacerlo para ambos S.O y encontre esto que te devuelve el nombre del S.O en minusculas
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();
        try {
            ProcessBuilder pb;
            if (sistemaOperativo.contains("win")) {
                pb = new ProcessBuilder("cmd", "/c", "dir", rutaDirectorio);
            } else {
                pb = new ProcessBuilder("ls", "-l", rutaDirectorio);
            }
            pb.start().waitFor(); // ejecutamos el proceso y esperamos que termine como dijo ivan
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //Asi conseguimos una lista de archivos con la que podremos trabajar
        File[] archivos = directorio.listFiles();
        if (archivos == null) {
            System.out.println("Error al acceder al directorio");
            return;
        }

        String nombreCSV = "listado.csv";
        try (FileWriter fw = new FileWriter(nombreCSV)) {
            // ponemos \n para que salte de linea y cada entrada este en otra fila
            fw.write("Nombre,Tipo,Tamaño,Permisos\n");

            for (File archivo : archivos) { //Recorremos cada archivo de la lista para poder trabajar con ellos
                String nombre = archivo.getName();

                String tipo;
                if (archivo.isDirectory()) {
                    tipo = "Directorio";
                } else {
                    tipo = "Archivo";
                }
// intente con int pero me daba error y me sugerio un long
                long tamaño = archivo.length();
                String permisos = obtenerPermisos(archivo);

                fw.write(nombre + "," + tipo + "," + tamaño + "," + permisos + "\n");
            }

            System.out.println("Listado guardado en: " + nombreCSV);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Obtiene los permisos de lectura, escritura y ejecución de un archivo o directorio
     * Devuelve una string donde cada letra representa un permiso.
     * Si no tiene el permiso, se muestra un guion "-".
     *
     *
     * @param archivo Archivo o directorio del que se quieren obtener los permisos.
     * @return Cadena con los permisos en formato "rwx".
     */
    private static String obtenerPermisos(File archivo) {
        String permisos = "";
// Cuando tiene permiso es r, w, x y cunado no tiene permiso es -, por ejemplo r--
        if (archivo.canRead()) {
            permisos = permisos + "r";
        } else {
            permisos = permisos + "-";
        }

        if (archivo.canWrite()) {
            permisos = permisos + "w";
        } else {
            permisos = permisos + "-";
        }

        if (archivo.canExecute()) {
            permisos = permisos + "x";
        } else {
            permisos = permisos + "-";
        }

        return permisos;
    }
}