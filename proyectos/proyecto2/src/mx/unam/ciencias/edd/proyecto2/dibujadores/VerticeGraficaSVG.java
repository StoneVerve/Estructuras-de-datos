
package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;

/**
 * Clase auxiliar que envuelve a un integer para su manejor en la clase 
 * DibujadorGrafica para guardar la posisción del vértice en el plano
 */ 
public class VerticeGraficaSVG {
		
	private int elemento;
	private double coorX;
	private double coorY;
		
	/**
	 * Contructor de la clase VerticeGraficaSVG
	 * @param elemento el integer que va a envolver
	 */
	public VerticeGraficaSVG(Integer elemento) {
		this.elemento = elemento.intValue();
	}
	
	/**
	 * Método get para el elemento
	 * @return el entero envuelto por la clase
	 */ 
	public int getElemento() {
		return this.elemento;
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
	 * Compara este objeto de tipo VerticeGraficaSVG con otro de distinto o mismo
	 * tipo
	 * @param o el objeto a comparar con este
	 * @return true si son instancias de la misma clase y envuelven al mismo entero
	 * false en cualquier otro caso
	 */
	@Override public boolean equals(Object o) {
     	if (o == null)
        	return false;
        if (getClass() != o.getClass())
        	return false;
        VerticeGraficaSVG vertice = (VerticeGraficaSVG)o;
        if (vertice.elemento == this.elemento)
        	return true;
        else 
        	return false;
   	}
}
