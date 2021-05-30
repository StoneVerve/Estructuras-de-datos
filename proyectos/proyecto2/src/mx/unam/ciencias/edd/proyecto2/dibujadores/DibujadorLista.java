package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;

/**
 * Clase publica que extiende la clase DibujadorSVG
 * La clase nos permite dibujar una lista en SVG a partir de la 
 * lista dada, la lista tiene que contener solo números enteros.
 */
public class DibujadorLista extends DibujadorSVG {
	
	protected Lista<Integer> estructura;
	protected int elementos;
	protected double tamañoX;
	protected double tamañoY;
	protected double alto;
	protected double largo;
	protected double largoFlecha;
	protected double coorX;
	protected double coorY;
	
	/**
	 * Constructor público que forma un nuevo dibujador a partir de una lista dada
	 * @param lista La lista que se quiere dibujar
	 * @param largoElemento el largo de los rectángulos que contendran a los 
	 * enteros
	 * @param altoElemento el alto de los rectángulos que contendran a los 
	 * enteros
	 * @param largoFlecha el largo de las flechas que apuntan entre los rectángulos
	 */
	public DibujadorLista(Lista<Integer> lista, double largoElemento, double altoElemento,
						  double largoFlecha) {
		this.estructura = lista;
		this.elementos = lista.getElementos();
		this.tamañoY = (40 + altoElemento);
		this.tamañoX = (50 + largoElemento) * elementos;
		this.largoFlecha = largoFlecha;
		alto = altoElemento;
		largo = largoElemento;
	}
		
	/**
	 * Método público que genera un Strin que representa la lista contenidad en 
	 * dibujador en SVG
	 * @return un String que representa la lista en SVG
	 */
	public String dibujaLista() {
		double coorX = 20;
		double coorY = 20;
		
		String salida = incializaArchivo(tamañoX, tamañoY) + '\n';
		salida = salida + "<g>" + '\n' + flechaD + '\n';
		
		while (!estructura.esVacio()) {
			Integer e = estructura.eliminaPrimero();
			/* Dibujamos el rectángulo */
			 salida = salida + dibujaElemento(e, coorX, coorY) + '\n'; 
			
			/*Modificamos valor x para el siguiente rectángulo*/
			coorX = coorX + largo + largoFlecha;
			 
			 
			 /* Dibujamos la línea */
			 if (!estructura.esVacio()) { 
			 double incY = calculaRectaY(coorY, alto);
			 salida = salida + dibujaRectaBidireccion((coorX - largoFlecha + 10), incY, coorX -10,
			 							   incY, "black", 3.0) + '\n';
			 			 
			 } 
		}
		salida = salida + "</g>" + '\n' + "</svg>";
		return salida;
	}
	
	/**
	 * Dibuja un rectángulo que contendra al elemento de la lista
	 * @param a el entero que estara en el rectángulo
	 * @param coorX la coordenada X inicial del rectángulo
	 * @param coorY la coordenada Y inicial del rectángulo
	 */
	protected String dibujaElemento(Integer a, double coorX, double coorY) {
		String figura = dibujaRectangulo(coorX, coorY, largo, alto,
					 					 "black", 3.0) + '\n'; 
		figura = figura + dibujaTexto("sans-serif", alto/2, coorX + largo/2,
			 						   coorY + (alto/3) * 2, "middle", a.toString(),
			 						   "black");
		return figura;
	}
	
	/**
	 * Calcula la coordenada Y de una recta a partir de un rectángulo para que esta
	 * sea igual la mitad del rectágulo
	 * @param coorY la coordenada Y del rectángulo
	 * @param alto el alto del rectángulo
	 * @return la coordenada Y de la recta
	 */
	protected double calculaRectaY(double coorY, double alto) {
		double y = alto / 2;
		y = y + coorY;
		return y;
	}
	
	/**
	 * Calcula la coordenada X de una recta a partir de un rectángulo para que esta
	 * sea igual la mitad del rectágulo
	 * @param coorY la coordenada X del rectángulo
	 * @param largo el largo del rectángulo
	 * @return la coordenada X de la recta
	 */
	protected double calculaRectaX(double coorX, double largo) {
		double x = largo / 2;
		x = x + coorX;
		return x;
	}
	
}
	
	
	
	