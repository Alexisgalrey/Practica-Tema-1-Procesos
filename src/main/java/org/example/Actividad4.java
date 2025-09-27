package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase Actividad4
 *
 * Este programa lee un archivo CSV llamado "listado.csv" que contiene información sobre archivos y directorios.
 * Para cada entrada, determina si es un directorio, un archivo de texto o un ejecutable.
 * Si es un directorio, muestra su contenido usando comandos del sistema.
 * Si es un archivo de texto, muestra su contenido en consola.
 * Si es un ejecutable, muestra su nombre y ruta absoluta.
 *
 * El programa detecta el sistema operativo y adapta los comandos utilizados (Windows o Linux).
 * Utiliza ProcessBuilder para ejecutar comandos como "dir", "ls", "type" o "cat".
 *
 * Autor: Alexis
 * Versión: 1.0
 */

public class Actividad4 {
    /**
     * Main que procesa el archivo CSV y muestra información de cada archivo o directorio.
     * Utiliza el s.o para decidir qué comando ejecutar.
     */
    public static void main(String[] args) {
        String nombreCSV = "listado.csv";

        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreCSV))) {
            String linea = br.readLine(); // ignorar cabezera

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");

                String nombre = partes[0];
                String tipo = partes[1];
                // String tamaño = partes [2] No lo necesitamos en este ejercicio
                String permisos = partes[3];

                File archivo = new File(nombre);
// comprobamos que el tipo sea directorio, lo recorrermos y con el forEach obtenemos el nombre de cada fichero
                if (tipo.equals("Directorio")) {
                    System.out.println("Contenido del directorio: " + nombre);

                    // usamos ProcessBuilder para listar el contenido del directorio
                    ProcessBuilder pb;
                    if (sistemaOperativo.contains("win")) {
                        pb = new ProcessBuilder("cmd", "/c", "dir", nombre);
                    } else {
                        pb = new ProcessBuilder("ls", nombre);
                    }

                    try {
                        Process proceso = pb.start();
                        BufferedReader salida = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                        String lineaSalida;
                        while ((lineaSalida = salida.readLine()) != null) {
                            System.out.println(lineaSalida); // listar los archivos
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else { // si no es un directorio comprobamos si es ejecutable o texto
                    if (esTexto(nombre)) {
                        System.out.println("Contenido del archivo de texto: " + nombre);

                        // usamos ProcessBuilder para mostrar el contenido del archivo
                        ProcessBuilder pb;
                        if (sistemaOperativo.contains("win")) {
                            pb = new ProcessBuilder("cmd", "/c", "type", nombre);
                        } else {
                            pb = new ProcessBuilder("cat", nombre); // cat en linux muestra el texto del archivo
                        }

                        try {
                            Process proceso = pb.start();
                            BufferedReader salida = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                            String lineaSalida;
                            while ((lineaSalida = salida.readLine()) != null) {
                                System.out.println(lineaSalida);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (esEjecutable(archivo, permisos, sistemaOperativo)) {
                        System.out.println("  Nombre: " + archivo.getName());
                        System.out.println("  Ruta: " + archivo.getAbsolutePath());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Comprueba si el nombre del archivo indica que es un archivo de texto.
     * Se considera texto si termina en ".txt" o ".csv".
     *
     * @param nombre Nombre del archivo
     * @return true si es texto, false en caso contrario
     */
    private static boolean esTexto(String nombre) {
        return nombre.endsWith(".txt") || nombre.endsWith(".csv"); // podriamos poner mas tipoos de archivos de texto
    }

    /**
     * Determina si un archivo es ejecutable según el sistema operativo y sus permisos.
     * En Windows se considera ejecutable si termina en ".exe".
     * En Linux se considera ejecutable si tiene permiso de ejecución.
     *
     * @param archivo Objeto File que representa el archivo
     * @param permisos Cadena con los permisos del archivo (por ejemplo "rwx")
     * @param sistemaOperativo Nombre del SO
     * @return true si es ejecutable, false en caso contrario
     */
    // con esto nos aseguramos si es de windows o linux y si es eun ejecutable
    private static boolean esEjecutable(File archivo, String permisos, String sistemaOperativo) {
        if (sistemaOperativo.contains("win")) {
            return archivo.getName().endsWith(".exe");
        } else {
            return permisos.contains("x"); // linux o IOS
            // no se si con "x" ya se considera que es un ejecutable, pero no encontre otra manera que pueda entender de hacerlo
        }
    }
}