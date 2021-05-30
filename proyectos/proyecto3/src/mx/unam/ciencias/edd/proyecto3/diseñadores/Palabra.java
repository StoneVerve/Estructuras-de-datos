package mx.unam.ciencias.edd.proyecto3.diseñadores;

import java.text.Normalizer.Form;
import java.text.Normalizer; 

/**
 * Envoltura para una cadena que busca guardar el número de repeticiones de la 
 * cadena en un archivo de texto
 */
public class Palabra implements Comparable<Palabra> {
    
    private String palabra;
    private int repeticion;
    
    /**
     * Constructor que recibe una palabra
     * @param palabra La palabra que se quiere envolver en la clase Palabra
     */
    public Palabra(String palabra) {
        String aux = Normalizer.normalize(palabra.toLowerCase(),
                                          Normalizer.Form.NFD);
        this.palabra = palabra;
        this.repeticion++;;
    }
    
    /**
     * Aumenta el número de repeticiones de la palabra en 'j' unidades
     * @param j Las unidades que se quiere aumentar el valor repetición de 
     * palabra
     */
    public void aumenta(int j) {
        repeticion = repeticion + j;
    }
    
    /**
     * Regresa la palabra contenida en el objeto como una cadena
     * @return la palabra contenida en la envoltura
     */
    public String getPalabra() {
        return this.palabra;
    }
    
    /**
     * Regresa el número de veces que la palabra se ha repetido
     * @return el número de repeticiones de la palabra
     */
    public int repeticiones() {
        return this.repeticion;
    }
    
    /**
     * Compara el número de repeticiones de dos objetos de tipo palabra
     * @param a La palabra con la uqe se quiere comparar
     * @return un valor positivo de el objeto tiene mas repeticiones que el del 
     * parametro, 0 si tienen el mismo número de repeticiones y un valor negativo
     * en cualquier otro caso
     */
    public int compareTo(Palabra a) {
        if (this.repeticion > a.repeticion)
            return 1;
        else if (this.repeticion == a.repeticion)
            return 0;
        else
            return -1;
    }
    /**
     * Regresa el String contenido en la clase Palabra junto que su número de 
     * repeticiones
     * @return El String contenido en la clase con sus repeticiones
     */
    public String toString() {
        return this.palabra + "{" + Integer.toString(this.repeticion) + "}";
    }

}