package mx.unam.ciencias.edd;

/**
 * Clase para manipular arreglos genéricos de elementos comparables.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] a) {
        if (a == null) 
        	return;
        realQuickSort(a, 0, a.length - 1);
    }
    
    /* Método auxiliar para quickSort */
    private static <T extends Comparable<T>> void realQuickSort(T[] a, int ini,
    															int fin) {
		if (fin <= ini || a.length == 0) {
			return;
		}
		int izq = ini + 1;
		int der = fin;
		int pivote = ini;
		while(izq < der) {
			if (a[izq].compareTo(a[pivote]) > 0 &&
				a[pivote].compareTo(a[der]) >= 0) {
				
				intercambio(a, izq, der);
				der--;
				izq++;
			} else if (a[izq].compareTo(a[pivote]) <= 0) {
				izq++;
			} else if (a[der].compareTo(a[pivote]) > 0) {
			  	der--;
			}
		}
		if (a[izq].compareTo(a[pivote]) > 0) 
			izq--;
			
		intercambio(a, pivote, izq);
		realQuickSort(a, ini, izq - 1);
		realQuickSort(a, izq + 1, fin);
	}

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] a) {
        if (a == null || a.length <= 1)
        	return;
        for (int i = 0; i < a.length - 1; i++) {
        	int guia = i;
        	for (int j = i + 1; j < a.length; j++) {
        		if (a[guia].compareTo(a[j]) >= 0) {
        			guia = j;
        		}
        	}
        	intercambio(a, i, guia);
        }		
    }
    /* Método auxiliar para intercambiar dos elementos en un arreglo*/
    private static <T extends Comparable<T>> void intercambio(T[] a, int b,
    														  int c) {
    	T temp = a[b];
    	a[b] = a[c];
    	a[c] = temp;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e) {
    	if (a == null || a.length == 0) {
    		return -1;
    	} else
    		return particionar(a, e, 0, a.length - 1);
    }
    /** Metodo auxiliar para busquedaBinaria */
    private static <T extends Comparable<T>> int particionar(T[] a, T e, int ini,
    							     int fin) {
    	if (ini == fin || ini > fin) {
    		if (a[ini].compareTo(e) == 0) 
    			return ini;
    		else 
    			return -1;
    	} else {
    		int half = (int)((fin - ini) / 2);
    		if (a[ini + half].compareTo(e) == 0) {
    			return (ini + half);
    		} else if (a[ini + half].compareTo(e) < 0) {
    			return particionar(a, e, (ini + half) + 1, fin);
    		} else  
    			return particionar(a, e, ini, (ini + half) - 1);
    	}
    } 									  															
}
