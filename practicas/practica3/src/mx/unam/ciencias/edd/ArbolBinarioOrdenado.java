package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
        	if (raiz == null)
        		return;
        	pila = new Pila<>();
        	pila.mete(raiz);
        	Vertice aux = raiz;
        	while (aux.izquierdo != null) {
        		pila.mete(aux.izquierdo);
        		aux = aux.izquierdo;
        	}
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
        	return !pila.esVacia();
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
        	if (hasNext()) {
        		Vertice aux = pila.saca();
        		T e = aux.elemento;
        		aux = aux.derecho;
        		while (aux != null) {
        			pila.mete(aux);
        			aux = aux.izquierdo;
        		}
        		return e;
        	} else
        		return null;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
    	if (elemento == null)
    		throw new IllegalArgumentException();
    	if (raiz == null) {
    		raiz = nuevoVertice(elemento);
    		ultimoAgregado = raiz;
    		elementos++;
    	} else 
    		agrega(raiz, elemento);
    		
    }
    
    /* Método auxiliar para agrega */
    private void agrega (Vertice vertice, T elemento) {
    	Vertice aux = vertice;
    	if (aux.elemento.compareTo(elemento) > 0) {
    		if (aux.izquierdo == null) {
    			Vertice nuevo = nuevoVertice(elemento);
    			aux.izquierdo = nuevo;
    			nuevo.padre = aux;
    			ultimoAgregado = nuevo;
    			elementos++;
    		} else 
    			agrega(aux.izquierdo, elemento);
    	} else {
    		if (aux.derecho == null) {
    			Vertice nuevo = nuevoVertice(elemento);
    			aux.derecho = nuevo;
    			nuevo.padre = aux;
    			ultimoAgregado = nuevo;
    			elementos++;
    		} else {
    			agrega(aux.derecho, elemento);
    		}
    	}
    }
    	

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
    	if (esVacio())
    		return;
    	Vertice aux = buscaElimina(raiz, elemento);
    	if (aux != null) {
    		if (aux.izquierdo != null) {
    			Vertice act = maximoEnSubarbol(aux.izquierdo);
    			aux.elemento = act.elemento;
    			aux = act;
    			if (aux.izquierdo != null) {
    				aux.izquierdo.padre = aux.padre;
    				if (aux.padre.izquierdo == aux) 
    					aux.padre.izquierdo = aux.izquierdo;
    				else 
    					aux.padre.derecho = aux.izquierdo;
    				aux = null;
    			}
    		}
    		if (aux != null && (aux.izquierdo == null && aux.derecho == null)) {
    			if (aux == raiz) {
    				raiz = null;
    			} else
    				if (aux.padre.izquierdo == aux) 
    					aux.padre.izquierdo = null;
    				else 
    					aux.padre.derecho = null;
    		} else if (aux != null) {
    			aux.derecho.padre = aux.padre;
    			if (aux.padre != null) {
    				if (aux.padre.izquierdo == aux)
    					aux.padre.izquierdo = aux.derecho;
    				else
    					aux.padre.derecho = aux.derecho;
    			} else
    				raiz = aux.derecho;
    		}
    		ultimoAgregado = null;
    		elementos--;
    	} else
    		return;
    }
	
	/* Método auxiliar para elimina que busca el vértice con el elemento a 
	 * eliminar por DFS
	 */
	private Vertice buscaElimina(Vertice vertice, T elemento) {
		Pila<ArbolBinario<T>.Vertice> pila = new Pila<>();
		pila.mete(vertice);
        Vertice aux = vertice;
        while (aux.izquierdo != null) {
        	pila.mete(aux.izquierdo);
        	aux = aux.izquierdo;
		}
		Vertice act = aux;
		do {
        	act = pila.saca();
        	aux = act;
        	aux = aux.derecho;
        	while (aux != null) {
        		pila.mete(aux);
        		aux = aux.izquierdo;
        	}			
		} while (!pila.esVacia() && !act.elemento.equals(elemento));
		if (act.elemento.equals(elemento))
			return act;
		else
			return null;
	}
			
    /**
     * Busca recursivamente un elemento, a partir del vértice recibido.
     * @param vertice el vértice a partir del cuál comenzar la búsqueda. Puede
     *                ser <code>null</code>.
     * @param elemento el elemento a buscar a partir del vértice.
     * @return el vértice que contiene el elemento a buscar, si se encuentra en
     *         el árbol; <code>null</code> en otro caso.
     */
    @Override protected Vertice busca(Vertice vertice, T elemento) {
    	if (vertice == null)
    		return null;
    	Vertice aux = vertice;
    	if (aux.elemento.equals(elemento))
    		return aux;
    	else if (aux.elemento.compareTo(elemento) > 0)
    		return busca(aux.izquierdo, elemento);
    	else
    		return busca(aux.derecho, elemento);
    }

    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el vértice que
     * recibe.
     * @param vertice el vértice raíz del subárbol del que queremos encontrar el
     *                máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice que recibe.
     */
    protected Vertice maximoEnSubarbol(Vertice vertice) {
    	Vertice aux = vertice;
    	while (aux.derecho != null)
    		aux = aux.derecho;
    	return aux;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
    	Vertice original = vertice(vertice);
    	Vertice nuevo = original.derecho;
    	if (!original.hayIzquierdo())
    		return;
    	nuevo = original.izquierdo;
    	original.izquierdo = nuevo.derecho;
    	nuevo.derecho = original;
    	if (original.izquierdo != null)
    		original.izquierdo.padre = original;
    	if (original.padre != null) {
    		if (original.padre.derecho == original)
    			original.padre.derecho = nuevo;
    		else 
    			original.padre.izquierdo = nuevo;
    	}
    	nuevo.padre = original.padre;
    	original.padre = nuevo;	
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
    	Vertice original = vertice(vertice);
    	Vertice nuevo = original.izquierdo;
    	if (!original.hayDerecho())
    		return;
    	nuevo = original.derecho;
    	original.derecho = nuevo.izquierdo;
    	nuevo.izquierdo = original;
    	if (original.derecho != null)
    		original.derecho.padre = original;
    	if (original.padre != null) {
    		if (original.padre.izquierdo == original)
    			original.padre.izquierdo = nuevo;
    		else 
    			original.padre.derecho = nuevo;
    	}
    	nuevo.padre = original.padre;
    	original.padre = nuevo;	
    }
}
