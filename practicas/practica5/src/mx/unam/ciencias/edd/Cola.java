package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     */
    @Override public void mete(T elemento) {
    	if (elemento == null)
    		throw new IllegalArgumentException();
    	Nodo nodo = new Nodo(elemento);
    	if (esVacia()) {
    		cabeza = nodo;
    		rabo = nodo;
    	} else 
    		rabo.siguiente = nodo;
    		rabo = nodo;
    }
    		
}
