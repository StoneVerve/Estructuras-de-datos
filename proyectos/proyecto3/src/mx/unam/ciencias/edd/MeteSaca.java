package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a operaciones
 * mete/saca/mira.
 */
public abstract class MeteSaca<T> {

    /**
     * Clase Nodo protegida para uso interno de sus clases herederas.
     */
    protected class Nodo {
        /** El elemento del nodo. */
        public T elemento;
        /** El siguiente nodo. */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /** La cabeza de la estructura. */
    protected Nodo cabeza;
    /** El rabo de la estructura. */
    protected Nodo rabo;

    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        T elemen;
        if (esVacia()) {
            throw new NoSuchElementException("La estructura esta vacía");
        } else if (cabeza == rabo) {
            elemen = cabeza.elemento;
            cabeza = null;
            rabo = null;
            return elemen;
        } else {
            elemen = cabeza.elemento;
            cabeza = cabeza.siguiente;
            return elemen;
        }
    }

    /**
     * Nos permite ver el elemento en un extremo de la estructura, sin sacarlo
     * de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {
        if (esVacia()) {
            throw new NoSuchElementException("La estructura esta vacía");
        } else {
            return cabeza.elemento;
        }
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <tt>true</tt> si la estructura no tiene elementos,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacia() {
        if (cabeza == null || rabo == null)
            return true;
        else
            return false;
    }

    /**
     * Compara la estructura con un objeto.
     * @param o el objeto con el que queremos comparar la estructura.
     * @return <code>true</code> si el objeto recibido es una instancia de la
     *         misma clase que la estructura, y sus elementos son iguales en el
     *         mismo orden; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") MeteSaca<T> m = (MeteSaca<T>)o;
        Nodo t1 = cabeza;
        Nodo t2 = m.cabeza;
        while (t1 != null && t2 != null) {
            if (!t1.elemento.equals(t2.elemento))
                return false;
            t1 = t1.siguiente;
            t2 = t2.siguiente;
        }
        if (t1 != null || t2 != null)
            return false;
        return true;
    }
}
