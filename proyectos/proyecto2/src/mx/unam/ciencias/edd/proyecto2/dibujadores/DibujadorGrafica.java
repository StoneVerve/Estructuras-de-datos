
package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;
import java.lang.Math;

/**
 * Clase pública que extiende la clase DibujadorSVG
 * La clase nos permite dibujar una gráfica en SVG a partir de la 
 * gráfica dada, la gráfica tiene que contener solo números enteros que tendran
 * que estar envueltos en una instancia de la clase VerticeGraficaSVG.
 */
public class DibujadorGrafica extends DibujadorSVG {
	
	protected Grafica<VerticeGraficaSVG> grafica;
	protected double radioElementos;
	
	/**
	 * Constructor que recibe la gráfia a dibujar y el radio que tendran
	 * los vértices
	 * @param grafica la gráfica que se quiere dibujar
	 * @param radioElemento el radio de los vértices que contedran los elementos
	 */
	public DibujadorGrafica(Grafica<VerticeGraficaSVG> grafica, double radioElemento) {
		this.grafica = grafica;
		this.radioElementos = radioElemento;
	}
	
	/**
	 * Método público principal que dibuja la gráfica contenido en el dibujador a
	 * partir de los datos ingresado en el constructor de la clase
	 * @return Una cadena SVG que representa la gráfica que ha sido dibujado
	 */
	public String dibujaGrafica() {
		int elementos = grafica.getElementos();
		double radioCirCentral = calcularRadioCircunferencia(elementos);
		double tamañoX = (2 * radioCirCentral) + 200.00;
		double tamañoY = (2 * radioCirCentral) + 200.00;
		double coorCircCentral = 100.00 + radioCirCentral;
		double anguloBase = (2 * Math.PI) / elementos;
		int posicion = 1;
		for (VerticeGraficaSVG e : grafica) {
			calculaCoorX(e, posicion * anguloBase, radioCirCentral, coorCircCentral);
			calculaCoorY(e, posicion * anguloBase, radioCirCentral, coorCircCentral);
			posicion++;
		}
		Lista<VerticeGrafica<VerticeGraficaSVG>> lista = new Lista<>();
		String salida = incializaArchivo(tamañoX, tamañoY) + '\n';
		salida = salida + "<g>" + '\n';	
		grafica.paraCadaVertice((v) -> lista.agrega(v));
		for (VerticeGrafica<VerticeGraficaSVG> e : lista) {
			for (VerticeGrafica<VerticeGraficaSVG> vecino : e.vecinos()) {
				salida = salida + dibujaRecta(e.getElemento().getCoorX(),
											  e.getElemento().getCoorY(),
										 	  vecino.getElemento().getCoorX(), 
										  	  vecino.getElemento().getCoorY(),
										   	  "black", 3.0) + '\n';
			}
		}
		for (VerticeGrafica<VerticeGraficaSVG> s : lista) 
			salida = salida + dibujaVertice(s.getElemento(), radioElementos);
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
	protected String dibujaVertice(VerticeGraficaSVG vertice, double radioElementos) {
		String salida = "";
		Integer elemento =  Integer.valueOf(vertice.getElemento());
		salida = dibujaCirculo(vertice.getCoorX(), vertice.getCoorY(), radioElementos,
								"black", 3.0, "lightblue") + '\n';
		salida = salida + dibujaTexto("sans-serif", radioElementos / 2, 
									  vertice.getCoorX(), vertice.getCoorY(),
			 						  "middle", elemento.toString(), "black")
			 						   + '\n';
		return salida;
	}
	
	/*
	 * Calcula el radio de la circunferencia principal alrededor de la cual
	 * seran colocados los vérices
	 * @param elementos el número de elementos de la gráfica
	 * @return el radio de la circunferencia central
	 */ 
	private double calcularRadioCircunferencia(int elementos) {
		return (6 * radioElementos) * elementos / (2 * Math.PI);
	}
	
	/*
	 * Calcula la coordenada X del vértice en el círculo principal usando
	 * identidaes trigonométricas
	 * @param vertice el vértice a cuya coordenada se quiere calcular
	 * @param angulo el ángulo que tendra la recta cuya intersección
	 * con la circunferencia describe la posición del vértice
	 * @param radioCir el radio de la circunferencia central
	 * @param centroCir el centro de la circunferencia central
	 */ 
	private void calculaCoorX(VerticeGraficaSVG vertice, double angulo,
							  double radioCir, double centroCir) {
		vertice.setCoorX((Math.cos(angulo) * radioCir) + centroCir );
	}
	
	/*
	 * Calcula la coordenada Y del vértice en el círculo principal usando
	 * identidaes trigonométricas
	 * @param vertice el vértice a cuya coordenada se quiere calcular
	 * @param angulo el ángulo que tendra la recta cuya intersección
	 * con la circunferencia describe la posición del vértice
	 * @param radioCir el radio de la circunferencia central
	 * @param centroCir el centro de la circunferencia central
	 */
	private void calculaCoorY(VerticeGraficaSVG vertice, double angulo,
							  double radioCir, double centroCir) {
		vertice.setCoorY((Math.sin(angulo) * radioCir) + centroCir);
	}

}