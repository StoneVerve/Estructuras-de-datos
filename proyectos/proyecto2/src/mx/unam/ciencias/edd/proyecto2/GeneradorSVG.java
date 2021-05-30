package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.dibujadores.*;
import java.util.NoSuchElementException;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File; 
 
/**
 * Clase que dibuja diferente tipos de estructura de datos devolviendo un String
 * que representa la estructura indicada en SVG
 */
public class GeneradorSVG {
	

	public static void main(String[] args) {
		/* Verificamos si hay archivos o no*/
		String estructura = "";
		String[] elementos = new String[1];
		String[] conexiones = new String[1];
		try {
			if (args.length == 1)
				leeArchivo(args, estructura, elementos, conexiones);
			else
				leeEstandar(estructura, elementos, conexiones);
		} catch (NumberFormatException o) {
			System.err.println("No todos los elementos son enteros o la lista de " +
								"enteros no tiene la estructura correcta, por " +
								"favor revise los datos");
		}
 	}
 	
 	/*
 	 * Lee los datos sobre la estrucutra desde la entrada estandar*/
 	private static void leeEstandar(String estructura, String[] elementos,
 							 String[] conexiones) {
 		try {
			BufferedReader estandar;
			estandar = new BufferedReader(new InputStreamReader(System.in));
			String linea;
 		for (int i = 0; i < 3; i++) {
			linea = estandar.readLine();
			if (i == 0 && linea != null)
				estructura = linea;
			if (i == 1 && linea != null)
				elementos = linea.split(",");
			if (i == 2 && linea != null)
				conexiones = linea.split(";");
		}
			estandar.close();
			if (!estructura.startsWith("#"))
				System.err.println("El símbolo # no esta presente en el archivo");
			else {
				String salida = dibujaEstructura(estructura, elementos, conexiones);
				if (salida.equals("Estructura no soportada por el programa"))
					System.err.println("Estructura no soportada por el programa" +
										" revise que el nombre introducido sea el" +
										" correcto (consultar README)");
				else
					System.out.println(salida);
			}
		} catch(IOException io){
			System.err.println("Ocurrio un error, reinicie el programa");
		}
		
	}
	
	/*
 	 * Lee los datos sobre la estrucutra desde un archivo*/
 	private static void leeArchivo(String[] args, String estructura, String[] elementos,
 							 	   String[] conexiones) {
		String m = args[0]; 
		File archivo = new File(m);
		BufferedReader lector;
		try {
			lector = new BufferedReader(new FileReader(archivo));
			String linea;
 		for (int i = 0; i < 3; i++) {
			linea = lector.readLine();
			if (i == 0 && linea != null)
				estructura = linea;
			if (i == 1 && linea != null)
				elementos = linea.split(",");
			if (i == 2 && linea != null)
				conexiones = linea.split(";");
		}
			lector.close();
			if (estructura == null)
				return;
			if (!estructura.startsWith("#"))
				System.err.println("El símbolo # no esta presente en el archivo");
			else {
				String salida = dibujaEstructura(estructura, elementos, conexiones);
				if (salida.equals("Estructura no soportada por el programa"))
					System.err.println("Estructura no soportada por el programa" +
									   " revise que el nombre introducido sea el" +
									   " correcto (consultar README)");
				else
					System.out.println(salida);
			}
		} catch(FileNotFoundException e) {
			System.err.println("No se pudo abrir o encontrar el archivo: " +
								"\"" + m.toString() + "\"");
		} catch(IOException i) {
			System.err.println("Ocurrio un error, reinicie el programa");
		}
	}
 	/* Consigue los elementos */
	private static Integer[] conseguirElementos(String[] elementos) throws NumberFormatException {
		Integer[] datos = new Integer[elementos.length];
		for (int j = 0; j < datos.length; j++) {
			datos[j] = Integer.parseInt(elementos[j].replaceAll("\\s+",""));
		}
		return datos;
	}
	
	/* Verifica que estructura va a dibujar y la dibuja */			
	private static String dibujaEstructura(String estructura, String[] elementos,
 							 				String[] conexiones) {
		if (elementos[0] == null)
			return "";
		Integer[] nodos = conseguirElementos(elementos);
		switch (estructura.toLowerCase().replaceAll("\\s+","")) {
			case "#lista":
				Lista<Integer> lista = new Lista<>();
				llenar(nodos, lista);
				DibujadorLista dibujador1 = new DibujadorLista(lista, 200.0,
															  100.0, 50.0);
				return dibujador1.dibujaLista();
			case "#cola":
				Cola<Integer> cola = new Cola<>();
				for (Integer a : nodos)
					cola.mete(a);
				DibujadorCola dibujador2 = new DibujadorCola(cola, 200.0,
															  100.0, 50.0);
				return dibujador2.dibujaCola();
			case "#pila":
				Pila<Integer> pila = new Pila<>();
				for (Integer a : nodos)
					pila.mete(a);
				DibujadorPila dibujador3 = new DibujadorPila(pila, 200.0,
															  100.0, 50.0);
				return dibujador3.dibujaPila();
			case "#arbolbinariocompleto":
				ArbolBinarioCompleto<Integer> arbol = new ArbolBinarioCompleto<>();
				llenar(nodos, arbol);
				DibujadorArbolBinario dibujador4 = new DibujadorArbolBinario(arbol, 50.00);
				return dibujador4.dibujaArbolBinario();
			case "#arbolbinarioordenado":
				ArbolBinarioOrdenado<Integer> arbol1 = new ArbolBinarioOrdenado<>();
				llenar(nodos, arbol1);
				DibujadorArbolBinario dibujador5 = new DibujadorArbolBinario(arbol1, 20.00);
				return dibujador5.dibujaArbolBinario();
			case "#arbolrojinegro":
				ArbolRojinegro<Integer> arbol2 = new ArbolRojinegro<>();
				llenar(nodos, arbol2);
				DibujadorArbolRojinegro dibujador6 = new DibujadorArbolRojinegro(arbol2, 50.00);
				return dibujador6.dibujaArbolBinario();
			case "#arbolavl":
				ArbolAVL<Integer> arbol3 = new ArbolAVL<>();
				llenar(nodos, arbol3);
				DibujadorArbolAVL dibujador7 = new DibujadorArbolAVL(arbol3, 50.00);
				return dibujador7.dibujaArbolBinario();
			case "#monticulominimo":
				Lista<VerticeMonticulo> list = new Lista<>();
				for (Integer l : nodos)
					list.agrega(new VerticeMonticulo(l));
				MonticuloMinimo<VerticeMonticulo> mont = new MonticuloMinimo<>(list);
				DibujadorMonticuloMin dibujador8 = new DibujadorMonticuloMin(mont, 50.00);
				return dibujador8.dibujaMonticulo();
			case "#grafica":
				if (conexiones[0] == null)
					return "";
				Grafica<VerticeGraficaSVG> grafica = new Grafica<>();
				for (Integer y : nodos) {
					grafica.agrega(new VerticeGraficaSVG(y));
				}
				conectaGrafica(conexiones, grafica);
				DibujadorGrafica dibujador9 = new DibujadorGrafica(grafica, 50.00);
				return dibujador9.dibujaGrafica();
			default:
				return "Estructura no soportada por el programa";
		}
	}
	
	/* Consigue las adyacencias para gráfica y le idica a la gráfica que vérice
	 * conectar */
	private static void conectaGrafica(String[] conexiones,
									   Grafica<VerticeGraficaSVG> grafica) {
		for (int i = 0; i < conexiones.length; i++) {
			if (conexiones[i] != null) {
			String[] pareja = conexiones[i].split(",");
			try {
				if (pareja.length == 2) {
					grafica.conecta(new VerticeGraficaSVG(Integer.parseInt(pareja[0].replaceAll("\\s+",""))),
									new VerticeGraficaSVG(Integer.parseInt(pareja[1].replaceAll("\\s+",""))));
				} else {
					System.err.println("El formato de las conexiones es incorrecto");
					return;
				}
			} catch (NoSuchElementException o) {
				System.err.println("Se intento conectar los elementos " + 
									pareja[0].replaceAll("\\s+","") + 
									pareja[1].replaceAll("\\s+","") +
									" de los cuales alguno no existe");
			} catch (IllegalArgumentException a) {
				System.err.println("Se intento conectar los elementos " +
								   pareja[0].replaceAll("\\s+","") + 
								   pareja[1].replaceAll("\\s+","") +
								   " los cuales ya estaban conectados" + 
								   " o son iguales");
			}
		}
		}
	}
	/* Método auxiliar para llenar algunas estructuras */		
	private static void llenar(Integer[] nodos, Coleccion<Integer> estructura) {
		for (Integer a : nodos)
			estructura.agrega(a);
	}
				
 }