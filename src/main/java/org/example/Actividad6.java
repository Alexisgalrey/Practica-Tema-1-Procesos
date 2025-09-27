package org.example;

import java.io.*;
import java.util.Scanner;
/**
 * Clase Actividad6
 *
 * Este programa muestra un menú interactivo en consola con cinco opciones:
 * 1. Listar el contenido de un directorio
 * 2. Copiar un fichero o directorio
 * 3. Comprimir un fichero o directorio
 * 4. Descargar un fichero desde una URL
 * 5. Salir del programa
 *
 * El usuario selecciona una opción y el programa ejecuta comandos del sistema operativo
 *
 * Autor: Alexis
 * Versión: 1.0
 */
public class Actividad6 {
    /**
     * Main que muestra el menu de opciones y gestiona la interacción con el usuario.
     * Según la opción elegida, llama al metodo correspondiente para realizar la acción.
     *
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Menu de opciones:");
        System.out.println("1. Listar un directorio");
        System.out.println("2. Copiar fichero o directorio");
        System.out.println("3. Comprimir fichero o directorio");
        System.out.println("4. Descargar fichero desde URL");
        System.out.println("5. Salir");

        System.out.print("Selecciona una opcion: ");
        String opcion = sc.nextLine();

        switch (opcion) {
            case "1":
                listarDirectorio(sc);
                break;
            case "2":
                copiar(sc);
                break;
            case "3":
                comprimir(sc);
                break;
            case "4":
                descargar(sc);
                break;
            case "5":
                System.out.println("Me hiciste perder el tiempo :@.");
                break;
            default:
                System.out.println("Opcion no valida.");
        }
    }
    /**
     * Lista el contenido de un directorio según la opción elegida por el usuario.
     * Las opciones disponibles son:
     * -s para listado simple
     * -d para listado detallado
     * -t para mostrar tipo (añade "/" a carpetas)
     * -m para ordenar por fecha de modificación
     *
     * @param sc Scanner para leer la entrada del usuario
     */
    // Listar el directorio con opciones
    private static void listarDirectorio(Scanner sc) {
        System.out.print("Introduce la ruta del directorio: ");
        String ruta = sc.nextLine();
        File dir = new File(ruta);

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("La ruta no es un directorio valido.");
            return;
        }

        System.out.println("opciones:");
        System.out.println("-s: Listado simple");
        System.out.println("-d: Listado detallado");
        System.out.println("-t: Listado por tipos");
        System.out.println("-m: Listado ordenado por fecha");

        System.out.print("Selecciona opcion: ");
        String opciones = sc.nextLine();

        ProcessBuilder pb;
        switch (opciones) {
            case "-s":
                pb = new ProcessBuilder("ls", ruta);
                break;
            case "-d":
                pb = new ProcessBuilder("ls", "-l", ruta); // al detalle
                break;
            case "-t":
                pb = new ProcessBuilder("ls", "-p", ruta); // por tipo
                break;
            case "-m":
                pb = new ProcessBuilder("ls", "-lt", ruta); // ordenado por fecha
                break;
            default:
                System.out.println("opcion no válida.");
                return;
        }

        ejecutar(pb); //llama al metodo complementario ejecutar
    }
    /**
     * Copia un fichero o directorio desde una ruta origen a una ruta destino.
     * Las opciones disponibles son:
     * -f para copiar un fichero
     * -ds para copiar un directorio de forma simple
     * -dr para copiar un directorio de forma recursiva
     *
     * @param sc Scanner para leer la entrada del usuario
     */
    // Copiar fichero o directorio
    private static void copiar(Scanner sc) {
        System.out.print("Ruta origen: ");
        String origen = sc.nextLine();
        System.out.print("Ruta destino: ");
        String destino = sc.nextLine();

        File archivo = new File(origen);
        if (!archivo.exists()) {
            System.out.println("La ruta origen no existe.");
            return;
        }

        System.out.println("opciones:");
        System.out.println("-f: Copiar fichero");
        System.out.println("-ds: Copia simple de directorio");
        System.out.println("-dr: Copia recursiva de directorio");

        System.out.print("Selecciona opcion: ");
        String opcion = sc.nextLine();

        ProcessBuilder pb;
        switch (opcion) {
            case "-f":
                pb = new ProcessBuilder("cp", origen, destino);
                break;
            case "-ds":
                pb = new ProcessBuilder("cp", "-R", origen, destino);
                break;
            case "-dr":
                pb = new ProcessBuilder("cp", "-r", origen, destino);
                break;
            default:
                System.out.println("opcion no valida.");
                return;
        }

        ejecutar(pb);
    }

    /**
     * Comprime un fichero o directorio en formato ZIP.
     * El usuario puede elegir si quiere guardar el archivo comprimido en otra ruta
     * o dejarlo en el directorio actual.
     *
     * @param sc Scanner para leer la entrada del usuario
     */
    // Comprimir fichero o directorio
    private static void comprimir(Scanner sc) {
        System.out.print("Ruta a comprimir: ");
        String ruta = sc.nextLine();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            System.out.println("La ruta no existe.");
            return;
        }

        System.out.println("¿Quieres que se comprima? ( -c para sí, cualquier otra cosa para que no )");
        String opcion = sc.nextLine();

        String destino;
        if (opcion.equals("-c")) {
            System.out.print("Introduce ruta destino: ");
            destino = sc.nextLine();
        } else {
            destino = "."; // en linux al poner "." equivale a la misma carpeta donde estamos
        }

        String nombreZip = archivo.getName() + ".zip";
        ProcessBuilder pb = new ProcessBuilder("zip", "-r", destino + "/" + nombreZip, ruta);

        // -r es compresion recursiva en linux, el destino le ponemos barra para que con el nombre zip complete la ruta de destino
        // ejemplo: home/escritorio (destino)  "/" medievil.zip (nombrezip)

        ejecutar(pb);
    }
    /**
     * Descarga un archivo desde una URL utilizando el comando curl.
     *
     * El usuario debe introducir la dirección web del archivo, el directorio donde desea guardarlo
     * y el nombre con el que se almacenará.
     *
     * Si la ruta no es válida, se muestra un mensaje de error y se cancela la operación.
     *
     * @param sc Scanner utilizado para leer la entrada del usuario por teclado
     */
    // Descargar fichero desde URL con curl
    // Intentar lo de la descarga en el futuro porque no soy capaz de hacerlo ahora
    private static void descargar(Scanner sc) {
        System.out.print("Introduce la URL del archivo: ");
        String url = sc.nextLine();

        System.out.print("Introduce el directorio donde guardarlo: ");
        String destino = sc.nextLine();

        File dir = new File(destino);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("La ruta destino no es valida");
            return;
        }

        System.out.print("Introduce el nombre del archivo a guardar: ");
        String nombre = sc.nextLine();

        ProcessBuilder pb = new ProcessBuilder("curl", destino + "/" + nombre, url);
        ejecutar(pb);
    }
    /**
     * Ejecuta un comando del sistema utilizando ProcessBuilder.
     *
     * Este metodo inicia el proceso, lee su salida línea por línea y la muestra por consola.
     * Si ocurre un error al iniciar el proceso, se muestra el error por pantalla.
     *
     * @param pb Objeto ProcessBuilder que contiene el comando a ejecutar
     */
    // Metodo complementario ejecutar para no tener que ponerlo varias veces por apartado
    private static void ejecutar(ProcessBuilder pb) {
        try {
            Process proceso = pb.start();
            BufferedReader salida = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            while ((linea = salida.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}