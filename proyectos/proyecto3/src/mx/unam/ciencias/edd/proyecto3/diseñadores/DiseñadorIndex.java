package mx.unam.ciencias.edd.proyecto3.diseñadores;

import mx.unam.ciencias.edd.proyecto3.dibujadores.*;
import mx.unam.ciencias.edd.*;

/**
 * Clase que busca generar un index.html con las direcciones a diversos archivos
 * html generados por la clase DiseñadorArchivo, donde se indica el número de 
 * palabras de cada archivo, y además contiene una gráfica donde cada archivo
 * es un vértice y dos archivos son adyacentes si contienen al menos 10 palabras 
 * en común donde cada una tiene al menos cinco caracteres
 */
public class DiseñadorIndex extends GeneradorHTML {

    private Lista<DiseñadorArchivo> archivos;
    
    /**
     * Constructor que recibe una Lista de DiseñadoreArchivo con la cual se 
     * se generar el index.html y la gráfica
     * @param lista la lista de archivos
     */
    public DiseñadorIndex(Lista<DiseñadorArchivo> lista) {
        this.archivos = lista;
    }
    
     /**
     * Genera un String con el formato correcto de un archivo html donde se visua-
     * liza la información indicada en la descripción de a clase
     * @return Una cadena que representa el archivo html generado a partir de un
     * archivo dado
     */
    public String generaIndex() {
        String salida = inicializa("Analizador de Archvios (Index)", generaCSS()) + '\n';
        salida = salida + creaTitulo("1", "Index") + '\n';
        salida = salida + creaLineaH() + '\n';
        salida = salida + creaParrafo("Estos son los diferentes archivos:", "P1") + '\n';
        salida = salida + generaLista(archivos) + '\n';
        Grafica<VerticeGraficaSVG> grafica = new Grafica<>();
        for (DiseñadorArchivo e : archivos)
            grafica.agrega(new VerticeGraficaSVG(e));
        try {
            conectaGrafica(grafica);
            DibujadorGrafica dib = new DibujadorGrafica(grafica, 30.00);
            salida = salida + creaTitulo("2", "Gráfica de archivos") + '\n';
            salida = salida + creaLineaH() + '\n';
            salida = salida + creaParrafo("La gráfica muestra la relación entre " +
                                          "los diferentes archivos, cada archivo" +
                                          " es un vértice y dos archivos son adya" + 
                                          "centes si ambos contienen al menos 10" +
                                          " palabras de mínimo 5 caracteres cada" +
                                          " una.", "P2") + '\n';
            salida = salida + "<div style=\" text-align:center\">" + '\n';
            salida = salida + dib.dibujaGrafica() + '\n';
            salida = salida + "</div>" + '\n';
        } catch (IllegalArgumentException a) {
            System.err.println("La gráfica no se dibujo ya que se ingreso " +
                               "múltiples veces el mismo archivo");
        }
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
        salida = salida + "#links li a {" + '\n' + "color:black;" + '\n' + 
                 "text-decoration:underline;" + '\n' + "}" + '\n';
        salida = salida + "#links li a:visited {color:darkred;}" + '\n';
        salida = salida + "#links li a:hover {color:darkred;}" + '\n';
        salida = salida + "</style>";
        return salida;
    }
    
    /*
     * Conecta una gráfica de DiseñadoreArchvio si dos archivos contienen 10 
     * palabras en común de al menos 5 caracteres
     * @param grafica La gráfica a conectar
     */
    private void conectaGrafica(Grafica<VerticeGraficaSVG> grafica) {
        DiseñadorArchivo arch;
        Lista<Palabra> aux;
        Lista<VerticeGraficaSVG> vertices = new Lista<>();
        int i;
        for (VerticeGraficaSVG e : grafica) 
            vertices.agrega(e);
        for (VerticeGraficaSVG e : grafica) {
            arch = e.getDiseñador();
            aux = arch.getPalabras();
            vertices.elimina(e);
            for (VerticeGraficaSVG s : vertices) {
                i = 0;
                for(Palabra m : aux) {
                    if(m.getPalabra().length() >= 5 && s.contiene(m.getPalabra()))
                        i++;
                }
                if (i >= 10)
                    grafica.conecta(e, s);
            }
        }
    }
    
    /*
     * Genara una cadena formateada correctamenete como una lista de html a 
     * partir de una lista de DiseñadoreArchivo
     * @param lista La lista de DiseñadorArchivo
     * @return Un String con la lista de DiseñadorArchivo formateada para un html
     */
    private String generaLista(Lista<DiseñadorArchivo> lista) {
        String salida = "<div id=\"links\">" + '\n' + "<ul>";
        for (DiseñadorArchivo e : lista) {
            salida = salida + '\n' + "<li>" + creaDireccion(e.getRutaD(), e.getArchivo()) +
                     " -No. de Palabras: " + Integer.toString(e.getElementos()) +
                     "</li>" + '\n';
        }
        salida = salida + "</ul>" + '\n' + "</div>";
        return salida;
    }
}