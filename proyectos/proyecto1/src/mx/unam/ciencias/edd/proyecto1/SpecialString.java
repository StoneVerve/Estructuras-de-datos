/*
 * Clase SpecialString
 *
 * Autor:Dimitri Semenov Flores
 * 
 * This file is part of Proyecto1.
 *
 * SpecialString is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpecialString is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package mx.unam.ciencias.edd.proyecto1;

import java.text.Normalizer.Form;
import java.text.Normalizer; 

/**
 * Clase que envuelve un Objeto String para definir un método compareTo() entre
 * Strings diferente al implementado por Oracle
 */
public class SpecialString implements Comparable<SpecialString> {

	private String cadena;
	
	/** 
	 * Construye una instancia de SpecialString a partir de un String
	 * @param cadena La cadena que va a ser envuelta por el objeto SpecialString
	 */
	public SpecialString(String cadena) {
		this.cadena = cadena;
	}
	
	/** 
	 * Regresa la cadena contenida en el SpecialString 
	 * @return La cadena contenida en el Objeto SpecialString
	 */
	public String getCadena() {
		return this.cadena;
	}
	/**
	 * Regresa un representación en cadena de SpecialString
	 * @return Una cadena que representa al objeto SpecialString 
	 */
	@Override public String toString() {
		return this.cadena.toString();
	}
	
	/**
	 * Compara un objeto de tipo SpecialString con otro SpecialString.
	 * Se eliminar todos los caracteres especiales de ambas cadenas 
	 * resultando en dos Strings compuestas de letras minúsculas,
	 * finalmente se manda a llamar el método compareTo() de la clase String
	 * con las nuevas cadenas
	 * @param a El objeto de tipo SpecialString con el que se busca comparar
	 * @return un entero negativo si el objeto que llama al método es menor que
	 * el parámetro, 0 si son iguales y un entero positivo si es mayor
	 */
	@Override public int compareTo(SpecialString a) {
		if (a.cadena == null)
			return -1;
		String primera = Normalizer.normalize(this.cadena.toLowerCase(),
											  Normalizer.Form.NFD);
		String segunda = Normalizer.normalize(a.cadena.toLowerCase(),
		                                      Normalizer.Form.NFD);
		primera = regex(primera);
		segunda = regex(segunda);
		return primera.compareTo(segunda);
	}
	
	/*
	 * Método auxiliar privado que elimina cualquier caracter que no sea 
	 * una minúscula haciendo uso de los valores en ascii de los caracteres
	 */
	private static String regex(String a) {
		String nuevo = "";
		for (int i = 0; i < a.length(); i++) {
			int valor = (int)a.charAt(i);
			if (valor >= 97 && valor <= 122) {
				nuevo = nuevo + a.charAt(i);
			} else {
				nuevo = nuevo + "";
			}
		}
		return new String(nuevo);
	} 
}