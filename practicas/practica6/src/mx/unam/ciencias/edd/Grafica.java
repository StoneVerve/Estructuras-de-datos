package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
        	iterador = vertices.iteradorLista();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
        	return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
        	VerticeGrafica<T> vertice = iterador.next();
        	return vertice.getElemento();
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Vertices para gráficas; implementan la interfaz VerticeGrafica */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* Lista de vecinos. */
        public Lista<Grafica<T>.Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
        	this.elemento = elemento;
        	this.color = color.NINGUNO;
        	vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
        	return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
        	return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
        	return this.color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
        	this.color = color;
        }

        /* Regresa un iterador para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
        	return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
    	vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    public int getElementos() {
        return this.vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
    	return this.aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
    	if (contiene(elemento))
    		throw new IllegalArgumentException("Elemento repetido");
    	else
    		this.vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
    	if (sonVecinos(a, b) || a.equals(b)) {
    		throw new IllegalArgumentException("Los elementos ya estan cone" +
    											"ctados o son iguales");
    	} else {
    		Grafica<T>.Vertice v1 = buscaVertice(a);
    		Grafica<T>.Vertice v2 = buscaVertice(b);
    		v1.vecinos.agrega(v2);
    		v2.vecinos.agrega(v1);
    		aristas++;
    	}
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
    	if (sonVecinos(a, b)) {
    		buscaVertice(a).vecinos.elimina(buscaVertice(b));
    		buscaVertice(b).vecinos.elimina(buscaVertice(a));
    		aristas--;
    	} else
    		throw new IllegalArgumentException("Los vértices no estan conectados");
    	
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
    	for (Vertice e : vertices) {
    		if (e.getElemento().equals(elemento))
    			return true;
    	} 
    	return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
    	Vertice v = buscaVertice(elemento);
    	for (Vertice e : v.vecinos) {
    		e.vecinos.elimina(v);
    		aristas--;
    	}
    	vertices.elimina(v);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
    	if (buscaVertice(a).vecinos.contiene(buscaVertice(b)))
    		return true;
    	else
    		return false;
    }
    
    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    private Grafica<T>.Vertice buscaVertice(T elemento) {
    	for (Vertice e : vertices) {
    		if (e.getElemento().equals(elemento))
    			return e;
    	}
    	throw new NoSuchElementException("El elemento no esta en la gráfica");
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
    	for (Vertice e : vertices) {
    		if (e.getElemento().equals(elemento)) {
    			return e;
    		}
    	}
    	throw new NoSuchElementException("El elemento no esta en la gráfica");	
    }
    

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
    	for (Grafica<T>.Vertice e : vertices)
    		accion.actua(e);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
    	Cola<Vertice> col = new Cola<>();
    	recorrer(col, elemento, accion);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
    	Pila<Vertice> pil = new Pila<>();
    	recorrer(pil, elemento, accion);
    }


	/*
	 * Método auxiliar para bfs y dfs, recorre una gráfica mediante una estructura
	 * que implementa la interfaz MeteSaca y realiza la acción deseada en cada
	 * uno de los vértices.
	 * @param estructura Estrucutra que implementa la interfaz MeteSaca con la
	 * se realizara el recorrido
	 * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
	 */
	private void recorrer(MeteSaca<Grafica<T>.Vertice> estructura, T elemento,
							AccionVerticeGrafica<T> accion) {
		Grafica<T>.Vertice v = buscaVertice(elemento);
		v.setColor(Color.ROJO);
		estructura.mete(v);
		while (!estructura.esVacia()) {
			v = estructura.saca();
			accion.actua(v);
			for (Vertice e : v.vecinos) {
				if(e.getColor() != Color.ROJO) {
					e.setColor(Color.ROJO);
					estructura.mete(e);
				}
			}
		}
		paraCadaVertice((s) -> s.setColor(Color.NINGUNO));
	}

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacio() {
    	return vertices.esVacio();
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
