package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.dibujadores.*;
import mx.unam.ciencias.edd.proyecto3.diseñadores.*;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File; 
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.text.DecimalFormat;
import java.lang.Math;

import java.text.Normalizer.Form;
import java.text.Normalizer;

import java.util.NoSuchElementException;

 
/** 
 * Clase que analiza el comportamiento de las palabras de  uno o varios archivos,
 * generando un archivo html a partir de la clase DiseñadorArchivo junto con un
 * index.html generado a partir de la clase DiseñadorIndex con ligas a cada uno 
 * de los archivos
 */
public class Analizador {
    
    public static void main(String[] args) {
        
        Lista<DiseñadorArchivo> elementos = new Lista<>();
        Lista<String> auxiliar = new Lista<>();
        boolean hayDirectorio = false;
        String directorio = "";
        int veces = 0;
        
        if (args.length <= 0)
            return;
        for (String x : args) {
            if ((veces != 0 && x.equals("-o")) ||
                (hayDirectorio && !directorio.equals(""))) {
                System.err.println("Formato de entrada no válido, por favor " +
                                   "revise los datos");
                return;
            }
            if (x.equals("-o")) {
                hayDirectorio = true;
                veces++;
            } else if (hayDirectorio) {
                directorio = x;
                hayDirectorio = false;
            } else {
                String[] palabras = leeArchivo(x);
                if (palabras != null && palabras.length > 0) {
                    File archivo = new File(x);
                    Diccionario<String, Palabra> dic = creaDiccionario(palabras);
                    DiseñadorArchivo arc = new DiseñadorArchivo(archivo.getName(),
                                                                dic, x);
                    if (!auxiliar.contiene(archivo.getPath())) {
                        auxiliar.agrega(archivo.getPath());
                        elementos.agrega(arc);
                    }
                }
            }
        }
        if (directorio.equals("")) {
            System.err.println("No se indico un directorio destino, por favor " +
                               "indique uno");
            return;
        }
        if (elementos.esVacio()) {
            System.err.println("No se ingresaron archivos");
            return;
        }
        File dirc = new File(directorio);
        if (!dirc.exists() || !dirc.isDirectory()) {
            System.err.println("El directorio no existe o la cadena dada no" +
                                   " es un directorio");
        }
        for (DiseñadorArchivo e : elementos) {
            if (e.getElementos() != 0)
                escribeArchivo(e, directorio);
        }
        escribeIndice(elementos, directorio);
        
        
    }
    /*
     * Escribe el archivo index.html en el directorio indicado
     * @param lista La lista de los archivos html
     * @param destino EL directorio destino
     */
    private static void escribeIndice(Lista<DiseñadorArchivo> lista, String destino) {
        try {
            File dir = new File(destino);
            BufferedWriter escritor = new BufferedWriter(new FileWriter(destino +
                                                         "index.html"));
            DiseñadorIndex index = new DiseñadorIndex(lista);
            escritor.write(index.generaIndex());
            escritor.flush();
            escritor.close();
        } catch (IOException e) {
            System.err.println("Ocurrio un error con la escritura del archivo" +
                               ", por favor revise" + " el directorio y los datos");
        }
    }
    
    /*
     * Escribe el archivo html en el directorio indicado correspondiente a uno 
     * de los archivos a analizar
     * @param aux El archivo a escribir
     * @param destino EL directorio destino
     */
    private static void escribeArchivo(DiseñadorArchivo aux, String destino) {
        try {
            DecimalFormat formatter = new DecimalFormat("#.####");
            String nombre = aux.getArchivo() + formatter.format(Math.random())
                              + ".html";
            aux.setRutaD(nombre);
            File dest = new File(destino + nombre);
            dest.createNewFile();
            BufferedWriter escritor = new BufferedWriter(new FileWriter(dest.getAbsoluteFile()));
            escritor.write(aux.generaHTML());
            escritor.flush();
            escritor.close();
        } catch (IOException e) {
            System.err.println("Ocurrio un error con la escritura del archivo " +
                               "(" + aux.getArchivo() + ")" + ", por favor revise" +
                               " el directorio y los datos");
        }
    }
    
    /*
     * Crea un diccionario de todas las palabras distintas de un archivo y a su 
     * vez calcula el número respectivo de repeticiones de cada palabra
     * @param arr El listado de las diferentes palabras de un archivo
     * @return Un diccionario con todas las palabas distintas de un archivo y las
     * repeticiones de cada una
     */
    private static Diccionario<String, Palabra> creaDiccionario(String[] arr) {
        String aux = "";
        Diccionario<String, Palabra> dic = new Diccionario<String, Palabra>(arr.length,
                                                          FabricaPicadillos.getInstancia(AlgoritmoPicadillo.GLIB_STRING));
            for (String m : arr) {
                aux = Normalizer.normalize(m.toLowerCase(),
                                           Normalizer.Form.NFD);
                aux = aux.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                aux = aux.replaceAll("[\\p{Punct}&&[^0-9]&&[^,]]", "");
                aux = aux.trim();
                if (!aux.equals("")) {
                    try {
                        Palabra actual = dic.get(aux);
                        actual.aumenta(1);
                    } catch (NoSuchElementException o) {
                        dic.agrega(aux, new Palabra(aux));
                    }
                }
            }
        return dic;
    }
    
    /*
     * Lee un archivo de texto separando cada palabra del archivo ignorando acentos
     * y eliminado caracteres especiales
     * @param x El archivo de texto
     * @return Un arreglo con las diferentes palabras del archivo
     */
    private static String[] leeArchivo(String x) {
        File archivo = new File(x);
        BufferedReader lector;
        String texto = "";
        try {
            lector = new BufferedReader(new FileReader(archivo));
            String linea;
            while((linea = lector.readLine()) != null)
                texto = texto + " " + linea;
            lector.close();
        } catch(FileNotFoundException e ) {
            System.err.println("No se pudo abrir o encontrar el archivo: " +
                               "(" + x.toString() + ")");
            return null;
        } catch(IOException i) {
            System.err.println("Ocurrio un error en la lectura de archivos, " +
                               "por favor reinicie el programa");
            return null;
        }
        if (!texto.equals(""))
            return texto.split(" ");
        else
            System.err.println("El archivo (" + x + ") no contiene elementos y por" +
                               " lo tanto no se proceso");
            return null;
    }
}