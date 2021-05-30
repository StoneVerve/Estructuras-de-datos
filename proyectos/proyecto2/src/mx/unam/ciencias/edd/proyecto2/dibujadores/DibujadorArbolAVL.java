package mx.unam.ciencias.edd.proyecto2.dibujadores;

import mx.unam.ciencias.edd.*;
import java.lang.Math;

/**
 * Clase publica que extiende la clase DibujadorArbolBinario
 * La clase nos permite dibujar un arbol binario AVL en SVG a partir del 
 * árbol dado, el árbol tiene que contener solo números enteros.
 */
public class DibujadorArbolAVL extends DibujadorArbolBinario {
	
	protected ArbolAVL<Integer> arbolAVL;
	
	/**
	 * Constructor que recibe el arbol AVl a dibujar y el radio que tendran
	 * los vértices
	 * @param est el árbol AVl que se quiere dibujar
	 * @param radioElementos el radio de los vértices que contedran los elementos
	 */
	public DibujadorArbolAVL(ArbolAVL<Integer> est, double radioElementos) {
		super(est, radioElementos);
		this.arbolAVL = est;
	}
	
	/**
	 * Dibuja un vértice con su elemento, balance y altura en el plano
	 * @param vertice El vértice de Dibujador que contiene al vértice que se
	 * quiere dibujar
	 * @param radioElementos el radio del vértice
	 * @return Una cadena que representa al vértice en SVG
	 */
	@Override protected String dibujaVertice(VerticeDeDibujador vertice,
											 double radioElementos) {
		String salida = "";
		if (vertice.vertice != null) {
			int altura = arbolAVL.getAltura(vertice.vertice);
			int balance = balance(vertice.vertice);
			salida = dibujaCirculo(vertice.coorX, vertice.coorY, radioElementos,
									"black", 3.0, "white") + '\n';
			salida = salida + dibujaTexto("sans-serif", radioElementos / 2, 
										  vertice.coorX, vertice.coorY,
			 						   	  "middle", vertice.vertice.get().toString(), "black")
			 						   	  + '\n';
			salida = salida + dibujaTexto("sans-serif", radioElementos / 2, 
										  vertice.coorX, vertice.coorY + 30.00,
			 						   	  "middle", altura + "/" + balance,
			 						   	  "black") + '\n';
			}
		return salida;
	}
	/*
	 * Método auxiliar que nos permite calcular el balance del vértice
	 * @param ver el vértice del cual se quiere conocer el balance
	 * @return un entero con el valor del balance
	 */
	private int balance(VerticeArbolBinario<Integer> ver) {
		int alturaHijoI = 0;
		int alturaHijoD = 0;
		if (!ver.hayIzquierdo())
			alturaHijoI = -1;
		else
			alturaHijoI = arbolAVL.getAltura(ver.getIzquierdo());
		if (!ver.hayDerecho())
			alturaHijoD = -1;
		else
			alturaHijoD = arbolAVL.getAltura(ver.getDerecho());
		return alturaHijoI - alturaHijoD;
	}
			
}
