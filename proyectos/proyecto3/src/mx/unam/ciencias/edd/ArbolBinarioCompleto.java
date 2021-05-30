package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        private Cola<ArbolBinario<T>.Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<>();
            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            Vertice actual = cola.saca();
            if (actual.izquierdo != null) 
                cola.mete(actual.izquierdo);
            if (actual.derecho != null) 
                cola.mete(actual.derecho);
            return actual.elemento;
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
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice vertice = nuevoVertice(elemento);
        if (raiz == null) {
            raiz = vertice;
        } else if (ultimoAgregado == raiz) {
            raiz.izquierdo = vertice;
            raiz.izquierdo.padre = raiz;
        } else if (ultimoAgregado.padre.izquierdo == ultimoAgregado) {
            ultimoAgregado.padre.derecho = vertice;
            vertice.padre = ultimoAgregado.padre;
        } else if (ultimoAgregado.padre.derecho == ultimoAgregado) {
            Vertice aux = ultimoAgregado;
            while (aux.padre != null && aux.padre.izquierdo != aux) {
                aux = aux.padre;
            }
            aux = (aux.padre == null) ? aux : aux.padre.derecho;
            while (aux.izquierdo != null && aux.derecho != null) {
                aux = aux.izquierdo;
            }
            if (aux.izquierdo == null) {
                aux.izquierdo = vertice;
                aux.izquierdo.padre = aux;
            } else {
                aux.derecho = vertice;
                aux.derecho.padre = aux;
            }
        }
        
        this.ultimoAgregado = vertice;
        this.elementos++;
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (esVacio())
            return;
        Cola<ArbolBinario<T>.Vertice> cola = new Cola<>();
        cola.mete(raiz);
        Vertice aux;
        boolean cambio = false;
        do {
            aux = cola.saca();
            if (aux.izquierdo != null)
                cola.mete(aux.izquierdo);
            if (aux.derecho != null)
                cola.mete(aux.derecho);
            if (aux.elemento.equals(elemento) && cambio == false) {
                aux.elemento = ultimoAgregado.elemento;
                cambio = true;
            }
        } while ((!cola.esVacia()) && (cola.mira() != ultimoAgregado));
        
        if (cambio == true && raiz == ultimoAgregado) {
            raiz = null;
            aux = raiz;
        } else if (cambio == true || ultimoAgregado.elemento.equals(elemento)) {
            if (ultimoAgregado.padre.izquierdo == ultimoAgregado) 
                ultimoAgregado.padre.izquierdo = null;
            else  
                ultimoAgregado.padre.derecho = null;
        } else
            return;
        ultimoAgregado = aux;
        elementos--;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
