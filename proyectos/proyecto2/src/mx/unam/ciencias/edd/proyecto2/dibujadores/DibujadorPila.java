package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;

/**
 * Clase pública que extiende la clase DibujadorLista
 * La clase nos permite dibujar una pila en SVG a partir de la 
 * pila dada, la pila tiene que contener solo números enteros.
 */
public class DibujadorPila extends DibujadorLista {
	
	private Pila<Integer> estructura;
	
	/**
	 * Constructor público que forma un nuevo dibujador a partir de una pila dada
	 * @param pila La pila que se quiere dibujar
	 * @param largoElemento el largo de los rectángulos que contendran a los 
	 * enteros
	 * @param altoElemento el alto de los rectángulos que contendran a los 
	 * enteros
	 * @param largoFlecha el largo de las flechas que apuntan entre los rectángulos
	 */
	public DibujadorPila(Pila<Integer> pila, double largoElemento, double altoElemento, double largoFlecha) {
		super(new Lista<Integer>(), largoElemento, altoElemento, largoFlecha);
		this.estructura = pila;
	}
	
	/**
	 * Método público que genera un String que representa la pila contenidad en 
	 * dibujador en SVG
	 * @return un String que representa la pila en SVG
	 */
	public String dibujaPila() {
		double coorX = 20;
		double coorY = 20;
		
		String salida = "<g>" + '\n' + flechaD + '\n';
		while (!estructura.esVacia()) {
			Integer e = estructura.saca();
			/* Dibujamos el rectángulo */
			 salida = salida + dibujaElemento(e, coorX, coorY) + '\n'; 
			
			/*Modificamos valor x para el siguiente rectángulo*/
			coorY = coorY + alto + largoFlecha;
			 
			 
			 /* Dibujamos la línea */
			 if (!estructura.esVacia()) { 
			 double incX = calculaRectaX(coorX, largo);
			 salida = salida + dibujaRectaDireccion(incX, coorY - 5, incX,
			 							   (coorY - largoFlecha + 10), "black", 3.0) + '\n';
			 
			 }
			 elementos++; 
		}
		this.tamañoY = (50 + alto) * elementos;
		this.tamañoX = (40 + largo);
		salida = incializaArchivo(tamañoX, tamañoY) + '\n' + salida;
		salida = salida + "</g>" + '\n' + "</svg>";
		return salida;
	}
	
	@Override public String dibujaLista() {
		return "Operacion no soportada";
	}

}