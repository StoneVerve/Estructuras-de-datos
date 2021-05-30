
package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;

/**
 * Clase que envuelve un Integer en un ComparableIndexable para poder utilizarlos
 * en Montículos mínimos
 */
public class VerticeMonticulo implements ComparableIndexable<VerticeMonticulo> {

	private int indiceArreglo;
	private Integer elemento;
	int profundidad;
	int posicion;	
	double coorX;
	double coorY;
	double razon;
	VerticeMonticulo padre;
	
	
	/**
	 * Constructor que envuelve un Integer
	 * @param a El entero que se quiere envolver
	 */
	public VerticeMonticulo(Integer a) {
		this.elemento = a;
	}
	
	public Integer getElemento() {
		return elemento;
	}
	
	/**
     * Regresa el índice del objeto.
     * @return el índice del objeto.
     */
	@Override public int getIndice() {
		return this.indiceArreglo;
	}
	
	/**
     * Actualiza el índice del objeto.
     * @param indice el nuevo índice.
     */	
	@Override public void setIndice(int b) {
		this.indiceArreglo = b;
	}
	
	/**
	 * Compara los enteros que envuelven los vértices montículos
	 * @param ver el vértice con el que se va a comparar
	 * @return un valor negativo si el vertice que manda a llamar tiene un entero
	 * menor que la otra instancia, 0 si son iguales y un entero positivo si el 
	 * vértice que invoco al método tiene un entero mayor
	 */
	@Override public int compareTo(VerticeMonticulo ver) {
		return this.elemento.compareTo(ver.elemento);
	}
	
	
}