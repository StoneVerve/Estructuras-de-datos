

package mx.unam.ciencias.edd.proyecto3.dibujadores;

import mx.unam.ciencias.edd.*;
import java.lang.Math;

/**
 * Clase publica que extiende la clase DibujadorSVG
 * La clase nos permite dibujar un montículo mínimo en SVG a partir del 
 * min hip dado, el montículo tiene que contener solo números enteros.
 */
public class DibujadorMonticuloMin extends DibujadorSVG {

    
    private MonticuloMinimo<VerticeMonticulo> monticulo;
    private double radioElementos;
    
    /**
     * Contruye un dibujador montículo a partir de un min hip
     * @param mont el montículo mínimmo
     * @param radioElementos el radio de los vértices
     */
    public DibujadorMonticuloMin(MonticuloMinimo<VerticeMonticulo> mont, 
                                                 double radioElementos) {
        monticulo = mont;
        this.radioElementos = radioElementos;
    }
    
    /**
     * Método público principal que dibuja el montículo contenido en el dibujador a
     * partir de los datos ingresado en el constructor de la clase
     * @return Una cadena SVG que representa el montículo que ha sido dibujado
     */
    public String dibujaMonticulo() {
        Lista<VerticeMonticulo> listaElementos = new Lista<>();
        for (VerticeMonticulo e : monticulo) {
            if (e.getIndice() == 0) {
                e.posicion = 1;
                e.profundidad = 0;
                e.padre = null;
            } else {
                calculaPadre(e);
                calculaPosicion(e);
                e.profundidad = e.padre.profundidad + 1;
            }
            listaElementos.agrega(e);
        }
        int prof = listaElementos.getUltimo().profundidad;
        double tamañoX = (3 * radioElementos * listaElementos.getLongitud()) + 30.00;
        double tamañoY = 4 * ((2 * radioElementos) * prof);
        String salida = incializaArchivo(tamañoX, tamañoY) + '\n';
        salida = salida + "<g>" + '\n'; 
        /* Dibujamos las rectas y calculamos las coordenadas X y Y de cada vértice */
        for (VerticeMonticulo e : listaElementos) {
            calculaCoorX(e, tamañoX);
            calculaCoorY(e, tamañoY, prof); 
            if (e.padre != null) {
                salida = salida + dibujaRecta(e.padre.coorX , e.padre.coorY,
                                              e.coorX, e.coorY, "black", 3.0) + '\n';
            }
        }   
        /* Dibujamos los vértices */
        for (VerticeMonticulo e : listaElementos) 
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
    protected String dibujaVertice(VerticeMonticulo vertice, double radioElementos) {
        String salida = "";
        salida = dibujaCirculo(vertice.coorX, vertice.coorY, radioElementos,
                                "black", 3.0, "white") + '\n';
        salida = salida + dibujaTexto("sans-serif", radioElementos / 2, 
                                      vertice.coorX, vertice.coorY,
                                      "middle", vertice.getElemento().toString(), "black")
                                       + '\n';
        return salida;
    }
    
    /**
     * Calcula la posición del padre de un vértice
     * @param vir el vértice cuya posición del padre se quiere calcular
     */
    private void calculaPadre(VerticeMonticulo ver) {
        if (ver.getIndice() != 0) {
            int padre = (ver.getIndice() - 1) / 2;
            ver.padre = monticulo.get(padre);
        } else
            ver.padre = null;
    }
    
    /**
     * Calcula la posición de un vértice
     * @param vir el vértice cuya posición se quiere calcular
     */
    private void calculaPosicion(VerticeMonticulo vir) {
        if ((2 * vir.padre.getIndice()) + 1 == vir.getIndice()) 
            vir.posicion = calculaPosicionIzq(vir.padre.posicion);
        else
            vir.posicion = calculaPosicionDer(vir.padre.posicion);
    }
        
    
    /**
     * Calcula el índice del hijo izquierdo de un vértice de dibujador
     * @param posicionPadre el índice del vértice cuyo hijo izquierdo se quiere 
     * conocer el índice
     * @return un entero con el valor del índice del hijo izquierdo
     */
    protected int calculaPosicionIzq(int posicionPadre) {
        return (posicionPadre * 2) - 1;
    }

    /**
     * Calcula el índice del hijo derecho de un vértice de dibujador
     * @param posicionPadre el índice del vértice cuyo hijo derecho se quiere 
     * conocer el índice
     * @return un entero con el valor del índice del hijo derecho
     */
    protected int calculaPosicionDer(int posicionPadre) {
        return (posicionPadre * 2);
    }
    
    /**
     * Calcula la coordenada X de un vértice a partir del tamaño del plano
     * y de la distancia de la coorX de su padre con respecto al origen
     * @param vertice el vértice cuya coordenada X se quiere calcular
     * @param tamañoX el tamaño del eje X del plano
     */
    protected void calculaCoorX(VerticeMonticulo vertice, double tamañoX) {
        if (vertice.padre == null) {
            vertice.coorX = tamañoX / 2;
            vertice.razon = vertice.coorX;
        } else {
            vertice.razon = vertice.padre.razon / 2;
            vertice.coorX = (vertice.posicion % 2 == 0) ? vertice.razon : (-1) * vertice.razon;
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
    protected void calculaCoorY(VerticeMonticulo vertice, double tamañoY,
                                int profundidadArbol) {
        if (vertice.padre == null)
            vertice.coorY = radioElementos + 20.00;
        else
            vertice.coorY = (vertice.profundidad *
                            (tamañoY - (radioElementos + 10))) / profundidadArbol;
    }

}