package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase Actividad5
 *
 * Este programa copia un archivo desde una ruta de origen a una ruta de destino.
 * Utiliza comandos del sistema operativo para realizar la copia, adaptándose automáticamente
 * a Windows o Linux según el entorno en el que se ejecuta.
 *
 * Además de copiar el archivo, mide el tiempo que tarda en completarse la operación
 * y guarda ese tiempo en un archivo llamado "tiempo_copia.txt" dentro del mismo directorio
 * donde se ha guardado el archivo copiado.
 *
 * El programa requiere dos argumentos por consola: la ruta de origen y la ruta de destino.
 *
 * Autor: Alexis
 * Versión: 1.0
 */

public class Actividad5 {
    /**
     * Main que realiza la copia de un archivo y registra el tiempo que tarda.
     *
     * Requiere dos argumentos: la ruta del archivo de origen y la ruta del archivo de destino.
     * Detecta el sistema operativo y utiliza el comando adecuado para copiar:
     * - En Windows: usa el comando "copy"
     * - En Linux: usa el comando "cp"
     *
     * Después de copiar, calcula el tiempo total en milisegundos y lo guarda en un archivo
     * llamado "tiempo_copia.txt" en el mismo directorio que el archivo de destino.
     *
     *
     * @param args Argumentos de entrada. args[0] debe ser la ruta de origen, args[1] la ruta de destino.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Proporciona la ruta de origen y la ruta de destino");
            return;
        }

        String origen = args[0];
        String destino = args[1];

        // obtenemos el nombre del sistema operativo en minúsculas
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        // Investige para hacerlo sin que sea tiempo unix pero lo unico que encontre que entendia bien era esto, aunque queda un poco feo
        long inicio = System.currentTimeMillis();

        ProcessBuilder pb;

        // usamos el comando  según el sistema operativo
        if (sistemaOperativo.contains("win")) {
            pb = new ProcessBuilder("copy", origen, destino);
        } else {
            pb = new ProcessBuilder("cp", origen, destino);
        }


        try {
            Process proceso = pb.start();
            proceso.waitFor(); // esperamos que termine la copia

            long fin = System.currentTimeMillis();
            long tiempoTotal = fin - inicio;

            System.out.println("Copia completada en " + tiempoTotal + " milisegundos");

            // creamos el archivo de tiempo en el directorio de destino
            File destinoArchivo = new File(destino);
            File carpetaDestino = destinoArchivo.getParentFile(); //Usamos getParentFile para hacer la copia en la misma carpeta
            File archivoTiempo = new File(carpetaDestino, "tiempo_copia.txt");

            try (FileWriter fw = new FileWriter(archivoTiempo)) {
                fw.write("Tiempo de copia: " + tiempoTotal + " milisegundos");
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error al copiar el archivo");
            e.printStackTrace();
        }
    }
}