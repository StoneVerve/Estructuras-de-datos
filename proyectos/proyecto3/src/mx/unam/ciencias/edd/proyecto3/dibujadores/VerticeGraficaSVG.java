
package mx.unam.ciencias.edd.proyecto3.dibujadores;

import mx.unam.ciencias.edd.proyecto3.diseñadores.DiseñadorArchivo;
import mx.unam.ciencias.edd.*;

/**
 * Clase auxiliar que envuelve a un DiseñadorArchivo para su manejor en la clase 
 * DibujadorGrafica para guardar la posisción del vértice en el plano
 */ 
public class VerticeGraficaSVG {
        
    private DiseñadorArchivo elemento;
    private double coorX;
    private double coorY;
        
    /**
     * Contructor de la clase VerticeGraficaSVG
     * @param elemento el DiseñadorArchivo que va a envolver
     */
    public VerticeGraficaSVG(DiseñadorArchivo elemento) {
        this.elemento = elemento;
    }
    
    /**
     * Devuleve el DiseñadorArchivo envuelto en el clase
     * @return El DiseñadorArchivo envuelto en la clase
     */
    public DiseñadorArchivo getDiseñador() {
        return this.elemento;
    }
    
    /**
     * Método get para el elemento
     * @return el nombre del DiseñadorArchivo envuelto por la clase
     */ 
    public String getElemento() {
        return this.elemento.getArchivo();
    }
    
    /**
     * Método set para la coordenada X del elemento en el plano
     * @param coorX la coordenada X del vertice
     */
    public void setCoorX(double coorX) {
        this.coorX = coorX;
    }
    
    /**
     * Método get para obtener la coordenada X del vértice
     * @return la coordenada X del vértice
     */
    public double getCoorX() {
        return this.coorX;
    
    }
    
    /**
     * Método set para la coordenada Y del elemento en el plano
     * @param coorY la coordenada Y del vertice
     */
    public void setCoorY(double coorY) {
        this.coorY = coorY;
    }
    
    /**
     * Método get para obtener la coordenada Y del vértice
     * @return la coordenada Y del vértice
     */
    public double getCoorY() {
        return this.coorY;
    }
    
    /**
     * Indica si una palabra esta contenida en el DiseñadorArchivo
     * @param tex La palabra a buscar
     * @return true si la contiene y false en cualquier otro caso
     */
    public boolean contiene(String tex) {
        return elemento.contienePalabra(tex);
    }
    
    /**
     * Compara este objeto de tipo VerticeGraficaSVG con otro de distinto o mismo
     * tipo
     * @param o el objeto a comparar con este
     * @return true si son instancias de la misma clase y envuelven al mismo DiseñadorArchivo
     * false en cualquier otro caso
     */
    @Override public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        VerticeGraficaSVG vertice = (VerticeGraficaSVG)o;
        return vertice.elemento.getArchivo().equals(this.elemento.getArchivo());
    }
}
