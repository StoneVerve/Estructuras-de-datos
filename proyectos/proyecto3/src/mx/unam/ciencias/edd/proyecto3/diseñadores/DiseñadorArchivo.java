package mx.unam.ciencias.edd.proyecto3.diseñadores;

import mx.unam.ciencias.edd.proyecto3.dibujadores.*;
import mx.unam.ciencias.edd.*;

import java.text.Normalizer.Form;
import java.text.Normalizer;

/**
 * Clase que busca generar un archivo html a partir de un archivo de texto
 * con la siguiente información: Una tabla ordenada de las diferentes palabras
 * del archivo con las repeticiones de cada una, una gráfica de pastel donde se
 * desglose el porcentaje de apariciones de cada palabra con respecto al total, 
 * una árbol rojinegro con las 15 palabras más repetidas del archivo y un árbol
 * avl con los mismos datos que el rojinegro
 */
public class DiseñadorArchivo extends GeneradorHTML {

    private String nombre;
    private int elementos;
    private Diccionario<String, Palabra> contenido;
    private String rutaOrigen;
    private String rutaDestino;
    
    /**
     * Constructor que recibe el nombre del archivo y un diccinario con las diferentes
     * Palabras del archivo
     * @param nombre El nombre del archivo
     * @param contenido El diccionario con las diferentes Palabras del archivo
     * @param origen La ruta origen del archivo a diseñar
     */
    public DiseñadorArchivo(String nombre, Diccionario<String, Palabra> contenido,
                            String origen) {
        this.nombre = nombre;
        this.contenido = contenido;
        rutaOrigen = origen;
        rutaDestino = "";
        elementos = 0;
        for (Palabra e : contenido)
            elementos = elementos + e.repeticiones();
    }
    
    /**
     * Genera un String con el formato correcto de un archivo html donde se visua-
     * liza la información indicada en la descripción de a clase
     * @return Una cadena que representa el archivo html generado a partir de un
     * archivo dado
     */
    public String generaHTML() {
        String salida = inicializa(nombre, generaCSS()) + '\n';
        salida = salida + creaTitulo("1", "Analizador del archivo: " + nombre) + '\n';
        salida = salida + creaLineaH() + '\n';
        salida = salida + creaParrafo("El archivo contiene " + Integer.toString(elementos) + " palabras, " +
                                      "la siguiente tabla muestra las palabras en" +
                                      " orden (mayor a menor) con su respectivo número" +
                                      " de repeticiones en el archivo.", "P1") + '\n';
        Lista<Palabra> list = contenido.valores();
        list = Lista.mergeSort(list).reversa();
        salida = salida + generaTabla(list) + '\n';
        salida = salida + creaTitulo("2", "Gráfica de Pastel") + '\n';
        salida = salida + creaLineaH() + '\n';
        salida = salida + creaParrafo("Gráfica de pastel que despliega los " +
                                      "procentajes correspondientes a cada palabra " +
                                      "en base al número de veces que aparece en el " +
                                      "archivo, si el porcentaje de la palabra con " +
                                      "respecto al total es manor a 1.5% entonces la " +
                                      "palabra sera agregada a la sección *Otros", "P2") + '\n';
        salida = salida + "<div style=\"text-align:center;\">" + '\n';
        DibujadorPastel grafica = new DibujadorPastel(list, elementos);
        salida = salida + grafica.dibujaGrafica() + '\n';
        salida = salida + "</div>" + '\n';
        salida = salida + creaParrafo(grafica.getInformacion(), "P3") + '\n';
        ArbolRojinegro<Palabra> arb = new ArbolRojinegro<>();
        llenaArbol(list, arb);
        salida = salida + creaTitulo("2", "Árbol Rojinegro") + '\n';
        salida = salida + creaLineaH() + '\n';
        salida = salida + creaParrafo("Árbol rojinegro cuyos elementos son las " +
                                      "primeras 15 palabras más repetidas del " +
                                      "archivo, el formato de las palabras que " +
                                      "aparecen en los vértices es: \"Palabra\" " +
                                      "{No. de repeticiones}", "P4");
        DibujadorArbolRojinegro arbol1 = new DibujadorArbolRojinegro(arb, 20.00);
        salida = salida + "<div style=\"text-align:center;\">" + '\n';
        salida = salida + arbol1.dibujaArbolBinario() + '\n';
        salida = salida + "</div>" + '\n';
        ArbolAVL<Palabra> tex = new ArbolAVL<>();
        llenaArbol(list, tex);
        salida = salida + creaTitulo("2", "Árbol AVL") + '\n';
        salida = salida + creaLineaH() + '\n';
        salida = salida + creaParrafo("Árbol AVL cuyos elementos son las " +
                                      "primeras 15 palabras más repetidas del " +
                                      "archivo, el formato de las palabras que " +
                                      "aparecen en los vértices es: \"Palabra\" " +
                                      "{No. de repeticiones}. Seguido del balance" +
                                      " correspondiente al vértice", "P5");
        DibujadorArbolAVL arbol2 = new DibujadorArbolAVL(tex, 20.00);
        salida = salida + "<div style=\"text-align:center;\">" + '\n';
        salida = salida + arbol2.dibujaArbolBinario() + '\n';
        salida = salida + "</div>" + '\n';
        salida = salida + cierra();
        return salida;
    }
    
    /*
     * Genera una cadena que representa un CSS correspondiete al archivo html 
     * @return Un String con un CSS formateado correctamente de forma "internal" 
     * para el archivo html
     */
    private String generaCSS() {
        String salida = "<style>" + '\n';
        salida = salida + "body {background-color: #FFFFFF;}" + '\n';
        salida = salida + "h1 {" + '\n' + "text-align: center;" + '\n' + "}" + '\n';
        for (int i = 1; i < 6; i++)
            salida = salida + defineEstiloP("P" + Integer.toString(i)) + '\n';
        salida = salida + "table {border-collapse: collapse;" +
                 '\n' + "width:610px;" + '\n' + "}" + '\n' +
                 "table, th, td {border: 1px solid black;}" + '\n' +
                 "th {" + '\n' + "background-color: black;" + '\n' + "color: white;" +
                 '\n' + "text-align: center;" + '\n' + "height: 20px" + '\n' +"}" +
                 '\n' + "th, td {padding: 2px;}" +
                 '\n' + "tr:hover {background-color: #ffff33}" + '\n';
        salida = salida + "</style>";
        return salida;
    }
    
    private String defineEstiloP(String id) {
        String salida = "#" + id + " {" + '\n' + "border-style: double;" + '\n' +
                 "border-width: 3px;" + '\n' + "border-color: #480000;" +
                 '\n' + "}";
        return salida;
    }
    
    /*
     * LLena un Árbol Binario de Palabras con una lista de Palabras
     * @param lista La lista de palabras
     * @param arbol El árbol a llenar
     */
    private void llenaArbol(Lista<Palabra> lista, ArbolBinario<Palabra> arbol) {
        int i = 0;
        int fin = lista.getElementos();
        if (fin >= 16) {
            IteradorLista<Palabra> iter = lista.iteradorLista();
            Palabra aux;
            while (i < 15) {
                aux = iter.next();
                arbol.agrega(aux);
                i++;
            }
        } else {
            for (Palabra e : lista) 
                arbol.agrega(e);
        }
    }
    
    /*
     * Genera una tabla de objetos de tipo Palabra formateada correctamente para 
     * un archivo html estándar
     * @param lista La lista de objetos de tipo Palabra que seran formateados en
     * la tabla
     * @return Un String con la tabla de objetos tipo Palabra formateada correctamente
     * para un archivo html estándar
     */
    private String generaTabla(Lista<Palabra> lista) {
        String salida = "<div style=\"text-align:center; height:420px;" + 
                        " width: 626px; overflow:auto; margin:auto;" + 
                        " border-style:solid; border-width:0.5px; margin-bottom" +
                        ":50px;\">" + '\n' + 
                        "<table>";
        int i = 1;
        salida = salida + '\n' + "<tr>" + '\n' + "<th> Posición </th>" + '\n' +
                 "<th> Palabra </th>" + '\n' + "<th> Repeticiones </th>" + '\n' +
                 "</tr>";
        for (Palabra e : lista) {
            salida = salida + '\n' + "<tr>" + '\n' + "<td> " + Integer.toString(i) +
                     "</td>" + '\n' + "<td>" + e.getPalabra() + "</td>" + '\n' +
                     "<td>" + Integer.toString(e.repeticiones()) + "</td>" + '\n' +
                     "</tr>";
            i++;
        }
        salida = salida + '\n' + "</table>" + '\n' + "</div>";
        return salida;
    }
    
    /**
     * Asigna la dirección donde esta alojado el archivo
     * @param ruta La ruta donde esta alojado el archivo
     */
    public void setRutaD(String ruta) {
        this.rutaDestino = ruta;
    }
    
    /**
     * Devuelve el número de palabras contenidas en el archivo que se busca diseñar
     * @return EL número de palabras
     */
    public int getElementos() {
        return this.elementos;
    }
    
    /**
     * Devuleve el nombre del archivo
     * @return El nombre del archivo como un String
     */
    public String getArchivo() {
        return this.nombre;
    }
    
    /**
     * Devuleva la ruta donde sera alojado el archivo
     * @return La ruta donde sera alojado el archivo como un String
     */
    public String getRutaD() {
        return this.rutaDestino;
    }
    
    /**
     * Devuleva la ruta donde esta alojado el archivo
     * @return La ruta donde esta alojado el archivo como un String
     */
    public String getRutaO() {
        return this.rutaOrigen;
    }
    
    /**
     * Devuelve una lista de objetos tipo Palabra que representan todas las 
     * palabras diferentes del archivo
     * @return Una lista de Palabras
     */
    public Lista<Palabra> getPalabras() {
        return contenido.valores();
    }
    
    /**
     * Indica si el archivo contiene un palabra
     * @param tex La palabra que se quiere conocer si esta contenida en el archivo
     * @return true si la palabra esta contenida en el archivo y false en cualquier
     * otro caso
     */
    public boolean contienePalabra(String tex) {
        return contenido.contiene(tex);
    }
}