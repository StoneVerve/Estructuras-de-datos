
package mx.unam.ciencias.edd.proyecto3.dibujadores;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.diseñadores.Palabra;
import java.lang.Math;

/**
 * Clase pública que extiende la clase DibujadorSVG
 * La clase nos permite dibujar un árbol binario en SVG a partir del 
 * árbol dado, el árbol tiene que contener Objetos de tipo Palabra
 */
public class DibujadorArbolBinario extends DibujadorSVG {

    /**
     * Clase interna privada que envuelve a un vértice del árbol para almacenar
     * más información cuando se dibuje
     */ 
    protected class VerticeDeDibujador {
        
        public VerticeArbolBinario<Palabra> vertice;
        public VerticeDeDibujador padre;
        public int indice;
        public int profundidad;
        public double coorX;
        public double coorY;
        double razon;
        
        /**
         * Contructor de la clase interna VerticeDeDibujador
         * @param vertice el vértice que va a envolver
         * @param padre el padre del vértice envuelto que también esta envuelto
         * en una instancia de VerticeDeDibujador
         * @param indice el índice del vértice para conocer su posición en un nivel
         * @param profundidad la profundidad del vértice para conocer en que nivel
         * del aŕbol se encuentra el vértice
         */
        public VerticeDeDibujador(VerticeArbolBinario<Palabra> vertice,
                                  VerticeDeDibujador padre, int indice,
                                  int profundidad) {
            
            this.vertice = vertice;
            this.padre = padre;
            this.indice = indice;
            this.profundidad = profundidad;
        }
        
    }
    
    
    protected ArbolBinario<Palabra> arbol;
    protected double radioElementos;
    
    /**
     * Constructor que recibe el árbol binario a dibujar y el radio que tendran
     * los vértices
     * @param arbol el árbol binario que se quiere dibujar
     * @param radioElemento el radio de los vértices que contedran los elementos
     */
    public DibujadorArbolBinario(ArbolBinario<Palabra> arbol, double radioElemento) {
        this.arbol = arbol;
        this.radioElementos = radioElemento;
    }
    
    /**
     * Método público principal que dibuja el árbol contenido en el dibujador a
     * partir de los datos ingresado en el constructor de la clase
     * @return Una cadena SVG que representa el arbol Binario que ha sido dibujado
     */
    public String dibujaArbolBinario() {
        int prof = arbol.profundidad();
        Lista<VerticeDeDibujador> listaElementos = new Lista<>();
        Cola<VerticeDeDibujador> cola = new Cola<>();
        VerticeDeDibujador raiz = new VerticeDeDibujador(arbol.raiz(), null, 1, 0);
        cola.mete(raiz);
        VerticeDeDibujador actual;
        while (!cola.esVacia()) {
            actual = cola.saca();
            if (actual.vertice.hayIzquierdo()) {
                cola.mete(new VerticeDeDibujador(actual.vertice.getIzquierdo(), actual, 
                                                 calculaIndiceIzq(actual.indice),
                                                 actual.profundidad + 1));
            }
            if (actual.vertice.hayDerecho()) {
                cola.mete(new VerticeDeDibujador(actual.vertice.getDerecho(), actual, 
                                                 calculaIndiceDer(actual.indice),
                                                 actual.profundidad + 1));
            }
            listaElementos.agrega(actual);
        }
        
        double tamañoX = (3 * radioElementos) * (Math.pow(2, prof));
        double tamañoY;
        if (prof == 0) 
            tamañoY = 4 * radioElementos;
        else
            tamañoY = 4 * ((2 * radioElementos) * prof);
        String salida = incializaArchivo(tamañoX, tamañoY) + '\n';
        salida = salida + "<g>" + '\n'; 
        /* Dibujamos las rectas y calculamos las coordenadas X y Y de cada vértice */
        for (VerticeDeDibujador e : listaElementos) {
            calculaCoorX(e, tamañoX);
            calculaCoorY(e, tamañoY, prof); 
            if (e.padre != null) {
                salida = salida + dibujaRecta(e.padre.coorX , e.padre.coorY,
                                              e.coorX, e.coorY, "black", 1.0) + '\n';
            }
        }   
        /* Dibujamos los vértices */
        for (VerticeDeDibujador e : listaElementos) 
            salida = salida + dibujaVertice(e, radioElementos);
        salida = salida + "</g>" + '\n' + "</svg>";
        return salida;
    }                   
                
    /**
     * Dibuja un vértice con su elemento en el plano
     * @param vertice El vértice de Dibujador que contiene al vértice que se
     * quiere dibujar
     * @param radioElementos el radio del vértice
     * @return Una cadena que representa al vértice en SVG
     */
    protected String dibujaVertice(VerticeDeDibujador vertice, double radioElementos) {
        String salida = "";
        salida = dibujaCirculo(vertice.coorX, vertice.coorY, radioElementos,
                                "black", 3.0, "white") + '\n';
        salida = salida + dibujaTexto("sans-serif", radioElementos / 2, 
                                      vertice.coorX, vertice.coorY,
                                      "middle", vertice.vertice.get().toString(), "black")
                                       + '\n';
        return salida;
    }
    
    
    /**
     * Calcula el índice del hijo izquierdo de un vértice de dibujador
     * @param indicePadre el índice del vértice cuyo hijo izquierdo se quiere 
     * conocer el índice
     * @return un entero con el valor del índice del hijo izquierdo
     */
    protected int calculaIndiceIzq(int indicePadre) {
        return (indicePadre * 2) - 1;
    }

    /**
     * Calcula el índice del hijo derecho de un vértice de dibujador
     * @param indicePadre el índice del vértice cuyo hijo derecho se quiere 
     * conocer el índice
     * @return un entero con el valor del índice del hijo derecho
     */
    protected int calculaIndiceDer(int indicePadre) {
        return (indicePadre * 2);
    }
    
    /**
     * Calcula la coordenada X de un vértice a partir del tamaño del plano
     * y de la distancia de la coorX de su padre con respecto al origen
     * @param vertice el vértice cuya coordenada X se quiere calcular
     * @param tamañoX el tamaño del eje X del plano
     */
    protected void calculaCoorX(VerticeDeDibujador vertice, double tamañoX) {
        if (vertice.padre == null) {
            vertice.coorX = tamañoX / 2;
            vertice.razon = vertice.coorX;
        } else {
            vertice.razon = vertice.padre.razon / 2;
            vertice.coorX = (vertice.indice % 2 == 0) ? vertice.razon : (-1) * vertice.razon;
            vertice.coorX = vertice.coorX + vertice.padre.coorX;
        }   
    }
    
    /**
     * Calcula la coordenada Y de un vértice a partir del tamaño del plano
     * y de su profundidad y la profundidad del árbol
     * @param vertice el vértice cuya coordenada Y se quiere calcular
     * @param tamañoY el tamaño del eje Y del plano
     * @param profundidadArbol la profundidad del árbol al cual pertenece
     * el vértice
     */
    protected void calculaCoorY(VerticeDeDibujador vertice, double tamañoY,
                                int profundidadArbol) {
        if (vertice.padre == null)
            vertice.coorY = radioElementos + 20.00;
        else
            vertice.coorY = (vertice.profundidad *
                            (tamañoY - (radioElementos + 10))) / profundidadArbol;
    }
}