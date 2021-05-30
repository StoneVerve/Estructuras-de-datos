package mx.unam.ciencias.edd.proyecto3.dibujadores;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.diseñadores.Palabra;
import java.lang.Math;

/**
 * Clase publica que extiende la clase DibujadorArbolBinario
 * La clase nos permite dibujar un arbol binario Rojinegro en SVG a partir del 
 * árbol dado, el árbol tiene que contener objetos de tipo Palabra.
 */
public class DibujadorArbolRojinegro extends DibujadorArbolBinario {
    
    protected ArbolRojinegro<Palabra> arbolRojinegro;
    
    /**
     * Constructor que recibe el árbol Rojinegro a dibujar y el radio que tendran
     * los vértices
     * @param est el árbol Rojiengro que se quiere dibujar
     * @param radioElementos el radio de los vértices que contedran los elementos
     */
    public DibujadorArbolRojinegro(ArbolRojinegro<Palabra> est, double radioElementos) {
        super(est, radioElementos);
        this.arbolRojinegro = est;
    }
    
    /**
     * Dibuja un vértice con su elemento y color en el plano
     * @param vertice El vértice de Dibujador que contiene al vértice que se
     * quiere dibujar
     * @param radioElementos el radio del vértice
     * @return Una cadena que representa al vértice en SVG
     */
    @Override protected String dibujaVertice(VerticeDeDibujador vertice,
                                             double radioElementos) {
        String salida = "";
        String color;
        Color col = arbolRojinegro.getColor(vertice.vertice);
        color = (Color.NEGRO == col) ? "black" : "red";
        if (vertice.vertice != null) {
            double tam = 0.0;
            tam = vertice.vertice.get().getPalabra().length() * 0.5;
            if ( vertice.vertice.get().getPalabra().length() <= 4)
                 tam = 3.0;
            salida = dibujaCirculo(vertice.coorX, vertice.coorY, radioElementos,
                                    "black", 1.0, color) + '\n';
            salida = salida + dibujaTexto("sans-serif", radioElementos / tam, 
                                          vertice.coorX, vertice.coorY,
                                          "middle", vertice.vertice.get().toString(), "white")
                                          + '\n';
            }
        return salida;
    }

}