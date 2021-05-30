package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends ArbolBinario<T>.Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
        	super(elemento);
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        public String toString() {
        	return (this.elemento.toString() + " " + altura + "/" + balance(this));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return super.equals(vertice) && this.altura == vertice.altura;
        }
    }
	
	/*
	 * Devuelve el hijo izquierdo de un vértice AVL
	 * @param vertice El vértice AVL del que se quiere conocer al hijo
	 * @return el hijo izquierdo del vértice (puede ser null)
	 */
	private VerticeAVL getHijoI(VerticeAVL vertice) {
		return verticeAVL(vertice.izquierdo);
	}
	
	/*
	 * Devuelve el hijo derecho de un vértice AVL
	 * @param vertice El vértice AVL del que se quiere conocer al hijo
	 * @return el hijo derecho del vértice (puede ser null)
	 */
	private VerticeAVL getHijoD(VerticeAVL vertice) {
		return verticeAVL(vertice.derecho);
	}
	
	/*
	 * Calcula el balance de un vértici
	 * @param vertice El vértice del cual se quiere conocer su balance
	 * @return el balance del vértice
	 */
	private int balance(VerticeAVL vertice) {
		return (getAltura(getHijoI(vertice)) - getAltura(getHijoD(vertice)));
	}
	
	/*
	 * Actualiza el atributo altura de un vértice
	 * @param vertice El vértice al que se le va a actualizar la altura
	 */
	private void actualizaAlt(VerticeAVL vertice) {
		if (vertice == null)
			return;
		int alturaI = getAltura(getHijoI(vertice));
		int alturaD = getAltura(getHijoD(vertice));
		vertice.altura = (alturaI >= alturaD) ? alturaI : alturaD;
		vertice.altura++;
	}
	
	/*
     * Método auxiliar que no dice si el vértice AVL es hijo izquierdo
     * @param vertice el vertice AVL que se quiere saber si es hijo izquierdo
     * o derecho
     * @return true si es hijo izquierdo y false en cualquier otro caso
     */
    private boolean esIzquierdo(VerticeAVL vertice) {
    	if (vertice == null)
    		return false;
    	return vertice.padre.izquierdo == vertice;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario. La complejidad en tiempo del método es <i>O</i>(log
     * <i>n</i>) garantizado.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
    	super.agrega(elemento);
    	VerticeAVL actual = verticeAVL(ultimoAgregado);
    	rebalanceo(actual);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo. La
     * complejidad en tiempo del método es <i>O</i>(log <i>n</i>) garantizado.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
    VerticeAVL adios = verticeAVL(busca(raiz, elemento));
    	if (adios == null)
    		return;
    	// Checamos si tiene dos hijos
    	if (adios.izquierdo != null && adios.derecho != null) {
    		VerticeAVL max;
    		max = verticeAVL(maximoEnSubarbol(adios.izquierdo));
    		intercambio(adios, max);
    		adios = max;
    	}
    	// Verificamos el caso en el que no tiene hijos
    	if (adios.izquierdo == null && adios.derecho == null)
    		if (adios == raiz)
    			raiz = null;
    		else {
    			if (esIzquierdo(adios))
    				adios.padre.izquierdo = null;
    			else
    				adios.padre.derecho = null;
    		rebalanceo(verticeAVL(adios.padre));
    		}
    	// Eliminamos el único hijo
    	if (adios.derecho != null) {
    		eliminaAux(getHijoD(adios));
    	} else if(adios.izquierdo != null) {
    		eliminaAux(getHijoI(adios));
    	}
    	elementos--;
    }
	
	/*
	 * Algoritmo recursivo que realancea el árbol AVL a través de un número 
	 * constante de operaciones los cuales incluyen giros,
	 * actualizaciones de altura, etc.
	 * @param actual el vértice a partir del cual inciara el algortimo el 
	 * rebalanceo
	 */ 
	private void rebalanceo(VerticeAVL actual) {
		if (actual == null)
			return;
		actualizaAlt(actual);
		if (balance(actual) < -1) {
			VerticeAVL hijoD = getHijoD(actual);
			if (balance(hijoD) == 1) {
				super.giraDerecha(hijoD);
				actualizaAlt(hijoD);
				actualizaAlt(verticeAVL(hijoD.padre));
			}
			super.giraIzquierda(actual);
		} else if (balance(actual) > 1) {
			VerticeAVL hijoI = getHijoI(actual);
			if (balance(hijoI) == -1) {
				super.giraIzquierda(hijoI);
				actualizaAlt(hijoI);
				actualizaAlt(verticeAVL(hijoI.padre));
			}
			super.giraDerecha(actual);
		}
		actualizaAlt(actual);
		actualizaAlt(verticeAVL(actual.padre));
		rebalanceo(verticeAVL(actual.padre));
	}
		
	
	
    /**
     * Regresa la altura del vértice AVL.
     * @param vertice el vértice del que queremos la altura.
     * @return la altura del vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    public int getAltura(VerticeArbolBinario<T> vertice) {
    	VerticeAVL tmp = verticeAVL(vertice);
    	return (tmp == null) ? -1 : tmp.altura;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeAVL}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice AVL.
     * @return el vértice recibido visto como vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    protected VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }
    
    /*
     * Método auxiliara para elimina(T elemento), se encarga de subir 
     * uno de los hijos del vértice a eliminar y llama al método de rebalanceo
     * @param vertice El vértice cuyo padre va a ser eliminado
     */
    private void eliminaAux(VerticeAVL vertice) {
    	VerticeAVL padre = verticeAVL(vertice.padre);
    	vertice.padre = padre.padre;
    	if (padre != raiz) {
    		if (esIzquierdo(padre))
    			padre.padre.izquierdo = vertice;
    		else
    			padre.padre.derecho = vertice;
    		actualizaAlt(verticeAVL(vertice.padre));
    	} else {
    		raiz = vertice;
    	}
    	actualizaAlt(vertice);
    	rebalanceo(vertice);
    }

    /*
     * Método auxilir que intercambia los elementos de dos vértices
     * @param a vértice a intercambiar elemento
     * @param b vértice a intercambiar elemento
	 */
    private void intercambio(VerticeAVL a, VerticeAVL b) {
    	T tmp = a.elemento;
    	a.elemento = b.elemento;
    	b.elemento = tmp;
    }
}