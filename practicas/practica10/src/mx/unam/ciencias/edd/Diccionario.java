package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, permitiendo (en general, dependiendo de qué tan bueno
 * sea su método para generar picadillos) agregar, eliminar, y buscar valores en
 * tiempo <i>O</i>(1) (amortizado) en cada uno de estos casos.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Clase para las entradas del diccionario. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
        	this.llave = llave;
        	this.valor = valor;
        }
    }

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador implements Iterator<V> {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Diccionario<K,V>.Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
        	iterador = conseguirElem().iterator();
        	
        }
        
        /* Nos dice si hay un siguiente elemento. */
        public boolean hasNext() {
        	return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        public V next() {
        	return iterador.next().valor;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Tamaño mínimo; decidido arbitrariamente a 2^6. */
    private static final int MIN_N = 64;

    /* Máscara para no usar módulo. */
    private int mascara;
    /* Picadillo. */
    private Picadillo<K> picadillo;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores*/
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private Lista<Entrada>[] nuevoArreglo(int n) {
        Lista[] arreglo = new Lista[n];
        return (Lista<Entrada>[])arreglo;
    }

    /**
     * Construye un diccionario con un tamaño inicial y picadillo
     * predeterminados.
     */
    public Diccionario() {
    	this(MIN_N, (K o) -> o.hashCode());
    }

    /**
     * Construye un diccionario con un tamaño inicial definido por el usuario, y
     * un picadillo predeterminado.
     * @param tam el tamaño a utilizar.
     */
    public Diccionario(int tam) {
    	this(tam, (K o) -> o.hashCode());
    }

    /**
     * Construye un diccionario con un tamaño inicial predeterminado, y un
     * picadillo definido por el usuario.
     * @param picadillo el picadillo a utilizar.
     */
    public Diccionario(Picadillo<K> picadillo) {
    	this(MIN_N, picadillo);
    }

    /**
     * Construye un diccionario con un tamaño inicial, y un método de picadillo
     * definidos por el usuario.
     * @param tam el tamaño del diccionario.
     * @param picadillo el picadillo a utilizar.
     */
    public Diccionario(int tam, Picadillo<K> picadillo) {
    	this.picadillo = picadillo;
		mascara = (tam < MIN_N) ? mascara(MIN_N) : mascara(tam);
		entradas = nuevoArreglo(mascara + 1);
    }
    
    /*
     * Calcula la mascara a partir de un tamaño
     * @param n el tamaño
     * @return la mascara correspondiente al tamaño
     */
    private int mascara(int m) {
		int i = 1;
		while(i <= m + 1)
			i = (i<<1) | 1;
		return i;
	}
	
	/*
	 * Calcula el indice que le corresponde a la entrada en el arreglo
	 * @param llave la llave de la Entrada
	 * @return la posición de la entrada en el arreglo
	 */
	private int calculaIndice(K llave) {
		return picadillo.picadillo(llave) & mascara;
	}
	
	/*
	 * Busca una entrada en un lista con la llave deseada
	 * @param llave la llave de la entrada
	 * @param l la lista donde se va a buscar
	 * @return la entrada con el valor de la llave buscado, null en caso contrario
	 */
	private Entrada buscaEntrada(K llave, Lista<Entrada> l) {
		if (l != null) {
			for (Entrada e : l) {
				if (e.llave.equals(llave))
					return e;
			}
		}
		return null;
	}
	
	/* 
	 * Devuleva la lista correspondiente a una entrada del arreglo
	 * en caso de ser nulo inicializa una lista vacia
	 * @param n el indice del arreglo
	 * @return La lista correspondiente a la entrada del arreglo
	 */
	private Lista<Entrada> getLista(int n) {
		if (entradas[n] == null) {
			Lista<Entrada> aux = new Lista<>();
			entradas[n] = aux;
		}
		return entradas[n];
	}
	
	/*
	 * Devuelve una lista con todas las entrada del diccionario
	 * @return la lista con todas las entradas del diccionario
	 */
	private Lista<Entrada> conseguirElem() {
		Lista<Entrada> aux = new Lista<>();
		for(int i = 0; i < entradas.length; i++) {
			if (entradas[i] != null) {
				for (Entrada e : entradas[i])
					aux.agrega(e);
			}
		}
		return aux;
	}
	
	/*
	 * Aumenta el tamaño del arreglo por 2, recalcula la mascara y copia
	 * todas las entrada del original
	 */
	private void crecer() {
		this.mascara = mascara(mascara);
		Lista<Entrada> aux = conseguirElem();
		entradas = nuevoArreglo(mascara + 1);
		elementos = 0;
		for (Entrada e : aux)
			agrega(e.llave, e.valor);
	}
					
    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
    	if (llave == null || valor == null)
    		throw new IllegalArgumentException();
		int indice = calculaIndice(llave);
		Lista<Entrada> l = getLista(indice);
		Entrada e = buscaEntrada(llave, l);
		if (e != null)
			e.valor = valor;
		else {
			e = new Entrada(llave, valor);
			l.agregaFinal(e);
			elementos++;
		}
		if (Double.compare(carga(), MAXIMA_CARGA) >= 0)
			crecer();
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
    	Lista<Entrada> lista = entradas[calculaIndice(llave)];
    	Entrada e = buscaEntrada(llave, lista);
    	if (e != null)
    		return e.valor;
    	else
    		throw new NoSuchElementException();
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
    	try {
    		get(llave);
    		return true;
    	} catch (NoSuchElementException o) {
    		return false;
    	}
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
    	int pos = calculaIndice(llave);
    	Lista<Entrada> aux = entradas[pos];
    	Entrada e = buscaEntrada(llave, aux);
    	if (e == null)
    		throw new NoSuchElementException();
    	else
    		aux.elimina(e);
    	if (aux.esVacio())
    		entradas[pos] = null;
    	elementos--;
    }

    /**
     * Regresa una lista con todas las llaves con valores asociados en el
     * diccionario. La lista no tiene ningún tipo de orden.
     * @return una lista con todas las llaves.
     */
    public Lista<K> llaves() {
    	Lista<K> aux = new Lista<>();
    	Lista<Entrada> elems = conseguirElem();
    	for (Entrada e : elems)
    		aux.agrega(e.llave);
    	return aux; 
    }

    /**
     * Regresa una lista con todos los valores en el diccionario. La lista no
     * tiene ningún tipo de orden.
     * @return una lista con todos los valores.
     */
    public Lista<V> valores() {
    	Lista<V> aux = new Lista<>();
    	for (V e : this)
    		aux.agrega(e);
    	return aux;
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int aux = 0;
        for (int i = 0; i < entradas.length; i++) {
        	if (entradas[i] != null)
        		aux = aux + (entradas[i].getElementos() - 1);
        }
        return aux;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int aux = 0;
        for (int i = 0; i < entradas.length; i++) {
        	if (entradas[i] != null)
        		aux = (aux >= (entradas[i].getElementos() - 1)) ? aux : entradas[i].getElementos() - 1;
        }
        return aux;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
    	double d = (double)elementos;
    	double c = (double)entradas.length;
    	double a = (d / c);
    	return a;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
    	return this.elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacio() {
        return elementos == 0;
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Diccionario))
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d = (Diccionario<K, V>)o;
        if (this.elementos != d.elementos || this.entradas.length != d.entradas.length)
        	return false;
        Lista<Entrada> aux = this.conseguirElem();
        for (Entrada e : aux) {
        	if (!d.contiene(e.llave))
        		return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar el diccionario.
     */
    @Override public Iterator<V> iterator() {
    	return new Iterador();
    }
}
