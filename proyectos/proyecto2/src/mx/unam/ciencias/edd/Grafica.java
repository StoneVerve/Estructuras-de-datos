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
    
    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Implementan VerticeGrafica. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice vecino;
        /* El peso de vecino conectando al vértice con el vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Grafica<T>.Vertice vecino, double peso) {
        	this.vecino = vecino;
        	this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T getElemento() {
        	return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
        	return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
        	return vecino.color;
        }

        /* Define el color del vecino. */
        @Override public void setColor(Color color) {
        	vecino.color = color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
        	return vecino.vecinos;
        }
    }



    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Grafica<T>.Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
        	this.elemento = elemento;
        	color = Color.NINGUNO;
        	vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
        	return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
        	return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
        	return color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
        	this.color = color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
        	return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
        	this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
        	return indice;
        }
        
        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
        	if (distancia == vertice.distancia)
        		return 0;
        	else if (distancia == -1)
        		return 1;
        	else if (distancia < vertice.distancia || vertice.distancia == -1)
        		return -1;
        	else 
        		return 1;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue de la vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
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
    	if (contiene(elemento) || elemento == null)
    		throw new IllegalArgumentException("Elemento repetido");
    	else
    		this.vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la vecino que conecte a los elementos será 1.
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
    		v1.vecinos.agrega(new Vecino(v2, 1));
    		v2.vecinos.agrega(new Vecino(v1, 1));
    		aristas++;
    	}
    }
    
    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b, double peso) {
    	if (sonVecinos(a, b) || a.equals(b)) {
    		throw new IllegalArgumentException("Los elementos ya estan cone" +
    											"ctados o son iguales");
    	} else {
    		Grafica<T>.Vertice v1 = buscaVertice(a);
    		Grafica<T>.Vertice v2 = buscaVertice(b);
    		v1.vecinos.agrega(new Vecino(v2, peso));
    		v2.vecinos.agrega(new Vecino(v1, peso));
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
    	Vertice verA = buscaVertice(a);
    	Vertice verB = buscaVertice(b);
    	if (sonVecinos(a, b)) {
    		verA.vecinos.elimina(buscaVecino(verA, verB));
    		verB.vecinos.elimina(buscaVecino(verB, verA));
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
    	for (Vecino e : v.vecinos) {
    		e.vecino.vecinos.elimina(buscaVecino(e.vecino, v));
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
    	Vertice prim = buscaVertice(a);
    	Vertice segun = buscaVertice(b);
    	Vecino aux = buscaVecino(prim, segun);
    	if (aux != null)
    		return true;
    	else
    		return false;
    }
    
    /*
     * Método auxiliar que buscan en la lista de vecinos de un vértices por 
     * otro vertice
     * @param vertice el vértice donde cuya lista de vecinos sera inspeccionada
     * @param vecino el vértices a buscar en la lista de vecinos
     * @return Un objeto de tipo vecino con el vértice que se buscaba si el vértices
     * es vecino y null en caso contrario
     */
    private Vecino buscaVecino(Vertice vertice, Vertice vecino) {
    	for (Vecino b : vertice.vecinos) {
    		if (b.vecino.equals(vecino))
    			return b;
    	}
    	return null;
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
			for (Vecino e : v.vecinos) {
				if(e.getColor() != Color.ROJO) {
					e.setColor(Color.ROJO);
					estructura.mete(e.vecino);
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
    
    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
    	MonticuloMinimo<Vertice> monticulo = inicializa(origen, destino);
    	Vertice dest = buscaVertice(destino);
    	Vertice ultimo;
   		while(!monticulo.esVacio()) {
    		ultimo = monticulo.elimina();
    		ultimo.color = Color.ROJO;
    		for (Vecino a : ultimo.vecinos) {
    			if (a.getColor() != Color.ROJO) {
    				a.setColor(Color.ROJO);
    				a.vecino.distancia = ultimo.distancia + 1;
					monticulo.reordena(a.vecino);
				}
    		}
    	}
    	Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
    	construyeTrayectoria(dest, trayectoria, "longitud");
    	return trayectoria;
    }
    
    /*
     * Método auxiliar que inicializa el montículo mínimo a partir del cual 
     * se buscara la trayectoria de menos peso o longitud
     * @param origen el vertices origen
     * @param destino el vertice destino
     * @return un montículo mínimo con los elementos de la gráfica
     */
    private MonticuloMinimo<Vertice> inicializa(T origen, T destino) {
    	Vertice ori = buscaVertice(origen);
    	asignaDistanciaInfinita();
    	ori.distancia = 0;
    	MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<>(vertices);
    	return monticulo;
    }
    
    /*
     * Construye una trayectoria de peso mínimo o de longitud mínima de forma
     * recursiva
     * @param ver el vertices a partir del cual se va a reconstruir la trayectoria
     * @param lista la lista donde estaran los vértices que conforman la trayectoria
     * @param tipo una cadena para saber si la trayectoria es de peso mínimo o de 
     * longitud mínima  
     */
    private void construyeTrayectoria(Vertice ver, Lista<VerticeGrafica<T>> lista,
    								 String tipo) {
    	lista.agregaInicio(ver);
    	double peso = 0;
    	for (Vecino a : ver.vecinos) {
    		if (tipo.equals("longitud")) {
    			peso = 1;
    		} else if (tipo.equals("peso")) {
    			peso = a.peso;
    		}
    		if (a.vecino.distancia == (ver.distancia - peso)) {
    			construyeTrayectoria(a.vecino, lista, tipo);
    			return;
    		}
    	}
    }
    
    /*
     * Asigna el valor de distancia inifinita (-1) a todos los vértices de la 
     * gráfica
     */
    private void asignaDistanciaInfinita() {
    	for (Vertice z : vertices)
    		z.distancia = -1;
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
    	MonticuloMinimo<Vertice> monticulo = inicializa(origen, destino);
    	Vertice dest = buscaVertice(destino);
    	Vertice ultimo;
    	while(!monticulo.esVacio()) {
    		ultimo = monticulo.elimina();
    		for (Vecino a : ultimo.vecinos) {
    		if (a.vecino.distancia > (ultimo.distancia + a.peso) ||
    			a.vecino.distancia == -1) {
    			
    			a.vecino.distancia = (ultimo.distancia + a.peso);
				monticulo.reordena(a.vecino);
    		}
    	}
    	}
    	Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
    	construyeTrayectoria(dest, trayectoria, "peso");
    	return trayectoria;
    }
}
