package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.dibujadores.*;


/*
 * Clase abstracta que define métodos para inciar un archivo SVG asi como 
 * diferentes figuras básicas, cuadrados, circulos, etc
 */
public abstract class DibujadorSVG {

	protected String fill = " fill='white'";
	protected String startText = "<text fill='white' font-family='sans-serif' ";
	protected String flechaD = "<defs>" + '\n' + "<marker id=\"flechaD\" " + 
	"markerWidth=\"5\" markerHeight=\"5\" refX=\"0\" refY=\"2.5\" orient=\"auto\" " +
	"markerUnits=\"strokeWidth\">" + '\n' + "<path d=\"M0,0 L0,5 L2.8,2.5 z\" fill=\"black\" />" +
	'\n' + "</marker>" + '\n' + "</defs>" + '\n';
		
	/**
	 * Inicia un archivo se SVG con version 1.0, cifrado en UTF-8 y con los
	 * distancias deseadas tanto en largo como en alto
	 * @param largo El largo de la imagen
	 * @param alto El alto de la imagen
	 * @return La cadena que contiene el inicio de un archvio SVG con el largo y
	 * alto especificado
	 */
	protected String incializaArchivo(double largo, double alto) {
		String inicio = "<?xml version='1.0' encoding='UTF-8' ?>" + '\n';
		inicio = inicio + "<svg width='" + largo + "' height='" + alto + 
				 "' fill='white'>";
		return inicio;
	}
	
	/**
	 * Regresa la cadena para cerrar un archivo de SVG
	 * @return La cadena que cierra un SVG
	 */
	protected String cierraArchivo() {
		return "</svg>";
	}
	
	/**
	 * Genera una cadena que representa el ancho y color del borde de una figura
	 * @param color El color que tendra el borde
	 * @param ancho El ancho del borde
	 * @return Una cadena SVG que define el ancho y color de un borde
	 */
	protected String definirBorde(String color, double ancho) {
		String bordeColor = "stroke='" + color + "' ";
		String bordeAncho = "stroke-width='" + ancho + "' ";
		return bordeColor + bordeAncho;
	}
	
	/**
	 * Genera un rectangulo con los valores deseados
	 * @param coorX la coordenada X a partir de la cual se dibujara el rectangulo
	 * @param coorY la coordenada Y a partir de la cual se dibujara el rectangulo
	 * @param longitud El largo del rectángulo
	 * @param alto El alto del rectángulo
	 * @param color El color del borde del rectángulo
	 * @param ancho El ancho del borde del rectángulo
	 * @return Una cadena para dibujar el rectángulo deseado en SVG
	 */
	protected String dibujaRectangulo(double coorX, double coorY, double longitud,
									  double alto, String color, double ancho) {
		String rectangulo ="<rect " + "x='" + coorX + "' y='" + coorY + 
							"' width='" + longitud + "' " + "height='" +
							alto + "' " + definirBorde(color, ancho) + "fill='white'" +
							" />";
		return rectangulo;
	}
	
	/**
	 * Genera un círculo con los valores deseados
	 * @param centroX la coordenada x del centro del círculo
	 * @param centroY la coordenada y del centro del círculo
	 * @param radio el radio del círculo
	 * @param color El color del borde del círculo
	 * @param ancho El ancho del borde del círculo
	 * @return Una cadena para dibujar el círculo deseado en SVG
	 */
	protected String dibujaCirculo(double centroX, double centroY, double radio,
								   String color, double ancho, String fondo) {
		String circulo = "<circle cx='" + centroX + "' cy='" + centroY + "' r='" +
						 radio + "' " + definirBorde(color, ancho) + "fill='" +
						 fondo + "' />";
		return circulo;
	}
	
	/**
	 * Genera una recta con los valores deseados
	 * @param x1 la coordenada x donde iniciara la recta
	 * @param y1 la coordenada y donde iniciara la recta
	 * @param x2 la coordenada x donde terminara la recta
	 * @param y2 la coordenada y donde terminara la recta
	 * @param color El color del borde de la recta
	 * @param ancho El ancho del borde de la recta
	 * @return Una cadena para dibujar la recta deseada en SVG
	 */
	protected String dibujaRecta(double x1, double y1, double x2, double y2,
								 String color, double ancho) {
		String recta = "<line " + "x1='" + x1 + "' y1='" + y1 + "' x2='" + x2 +
					   "' y2='" + y2 + "' " + definirBorde(color, ancho) +
					   " />";
		return recta;
	}
	
	/**
	 * Genera una recta con los valores deseados y con una dirección
	 * @param x1 la coordenada x donde iniciara la recta
	 * @param y1 la coordenada y donde iniciara la recta
	 * @param x2 la coordenada x donde terminara la recta
	 * @param y2 la coordenada y donde terminara la recta
	 * @param color El color del borde de la recta
	 * @param ancho El ancho del borde de la recta
	 * @return Una cadena para dibujar la recta deseada en SVG
	 */
	protected String dibujaRectaDireccion(double x1, double y1, double x2,
										  double y2, String color, double ancho) {
		String recta = dibujaRecta(x1, y1, x2, y2, color, ancho);
		recta = recta.replace("/>", "");
		recta = recta + "marker-end=\"url(#flechaD)\" />";
		return recta;
	}
	
	/**
	 * Genera una recta con los valores deseados y cuyos extremos tienen apuntadores
	 * @param x1 la coordenada x donde iniciara la recta
	 * @param y1 la coordenada y donde iniciara la recta
	 * @param x2 la coordenada x donde terminara la recta
	 * @param y2 la coordenada y donde terminara la recta
	 * @param color El color del borde de la recta
	 * @param ancho El ancho del borde de la recta
	 * @return Una cadena para dibujar la recta deseada en SVG
	 */
	protected String dibujaRectaBidireccion(double x1, double y1, double x2,
										  	double y2, String color, double ancho) {
		String recta = dibujaRectaDireccion(x1, y1, x2, y2, color, ancho);
		recta = recta + '\n' + dibujaRectaDireccion(x2, y2, x1, y1, color, ancho);
		return recta;
	}
	
	/**
	 * Genera una cadena con el texto deseado en una posición del plano
	 * @param font El tipo de texto
	 * @param tamaño el tamaño del texto
	 * @param x La coordenada X donde estara desplegado el texto
	 * @param y La coordenada Y donde estara desplegado el texto
	 * @param anchor define si el texto estara centrado o al izquierda o a la derecha
	 * @param text el texto que se desea desplegar
	 * @param relleno el color del relleno del texto
	 */
	protected String dibujaTexto(String font, double tamaño, double x, double y,
								 String anchor, String text, String relleno) {
		String texto = "<text fill='" + relleno + "' font-family='" + font + "' font-size='" +
						tamaño + "' x='" + x + "' y='" + y + "' text-anchor='" +
						anchor + "'>" + text + "</text>";  
		return texto;
	}
	
}
