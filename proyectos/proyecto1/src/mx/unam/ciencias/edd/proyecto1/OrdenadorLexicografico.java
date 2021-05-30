/*
 * Clase OrdenadorLexicografico
 *
 * Autor:Dimitri Semenov Flores
 * 
 * This file is part of Proyecto1.
 *
 * OrdenadorLexicografico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OrdenadorLexicografico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File; 

 
/** 
 * Clase que ordena uno o varios archivos de texto de manera lexicográfica
 * también ordenar texto recibido a partir de la entrada estándar, se ultiliza el 
 * algoritmo Merge Sort para ordenar el o los texto(s)
 */
public class OrdenadorLexicografico {
	
	public static void main(String[] args) {
		
	    boolean reversa = false;
	    boolean hayArchivos = false;
		Lista<SpecialString> texto = new Lista<>();
		
		/* Si hay al menos un string en el arreglo args lo recorremos en busca de
		 * archivos o el parámetro "-r". Si se encuetran archivos los agregamos 
		 * a una lista 
		 */
		if (args.length >= 1) {
			for (String m : args) {
				if (m.equals("-r")) {
					reversa = true;
				} else {
					File archivo = new File(m);
					BufferedReader lector;
					try {
						lector = new BufferedReader(new FileReader(archivo));
						String linea;
						while ((linea = lector.readLine()) != null) {
							SpecialString nueva = new SpecialString(linea);
							texto.agrega(nueva);
						}
						lector.close();
					} catch(FileNotFoundException e) {
						System.out.println("No se pudo abrir o encontrar el archivo: " +
											"(" + m.toString() + ")");
					} catch(IOException i) {
						System.out.println("Ocurrio un error, reinicie el programa");
					} finally {
						hayArchivos = true;
					}
				}
			}
		}
		
		/* Sino se envió algun archivo como parámetro se utiliza la entrada
		 * estandar
		 */
		if (hayArchivos == false) {		
			try {
				BufferedReader estandar;
				estandar = new BufferedReader(new InputStreamReader(System.in));
				String input;
				while((input = estandar.readLine()) != null){
					SpecialString nueva = new SpecialString(input);
					texto.agrega(nueva);
				}
				estandar.close();
			}catch(IOException io){
				System.out.println("Ocurrio un error, reinicie el programa");
			}
		}
		
		/* Organiza el texto usando Merge Sort implementado en lista
		 * y en caso de detectar el parámetro '-r' regresa el texto en 
		 * orden invertido
		 */
		Lista<SpecialString> salida = Lista.mergeSort(texto);
		if (reversa == true) {
			salida = salida.reversa();
		}
		
		/* Imprime el texto ordenado */
		for (SpecialString m : salida) {
			System.out.println(m.toString());
		}
		
	}	
}