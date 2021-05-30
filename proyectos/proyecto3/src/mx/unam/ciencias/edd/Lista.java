package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        public T elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        public Lista<T>.Nodo anterior;
        public Lista<T>.Nodo siguiente;

        public Iterador() {
            this.anterior = null;
            this.siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null)
                throw new NoSuchElementException("No hay elemento siguiente");
            this.anterior = this.siguiente;
            this.siguiente = this.siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null)
                throw new NoSuchElementException("No hay elemento anterior");
            this.siguiente = this.anterior;
            this.anterior = this.anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            siguiente = null;
            anterior = rabo;
        }

        /* No implementamos este método. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacio() {
       return longitud == 0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista. El método es idéntico
     * a {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("elemento no valido");
        } 
        Nodo nuevo = new Nodo(elemento);
        if (this.esVacio() == true) {
            cabeza = nuevo;
            rabo = nuevo;
        } else {
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;
        }
        longitud++;
            
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("elemento no valido");
        }
        Nodo n = new Nodo(elemento);
        if (this.esVacio() == true) {
            cabeza = n;
            rabo = n;
        } else {
            n.siguiente = cabeza;
            cabeza.anterior = n;
            cabeza = n;
        }
        longitud++;
    }
    
    /**
     * Busca un nodo en la lista
     */
     private Nodo busqueda(Nodo n, T elemento) {
        if (n == null)
            return null;
        if (n.elemento.equals(elemento))
            return n;
        return busqueda(n.siguiente, elemento);
    }
    
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica. Si un elemento de la lista es
     * modificado, el iterador se mueve al primer elemento.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        Nodo n = busqueda(cabeza, elemento);
        if (n == null) {
            return;
        } else if (cabeza == rabo) {
            cabeza = null;
            rabo = null;
        } else if (cabeza == n) {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        } else if (rabo == n) {
            rabo = rabo.anterior;
            rabo.siguiente = null;
        } else {
            n.siguiente.anterior = n.anterior;
            n.anterior.siguiente = n.siguiente;
        }
        longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (longitud == 0) {
            throw new NoSuchElementException("La lista es vacía");
        }
        T m = cabeza.elemento;
        if (cabeza.siguiente == null) {
            cabeza = null;
            rabo = null;
        } else {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }
        longitud--;
        return m;
        
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (longitud == 0) {
            throw new NoSuchElementException("La lista es vacía");
        }
        T m = rabo.elemento;
        if (cabeza.siguiente == null) {
            cabeza = null;
            rabo = null;
        } else {
            rabo.anterior.siguiente = null;
            rabo = rabo.anterior;
        }
        longitud--;
        return m;
            
    }

    /**
     * Nos dice si un elemento está en la lista. El iterador no se mueve.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        return busqueda(cabeza, elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> rev = new Lista<>();
        return reversa(rev, cabeza);
        
    }
    
    /* Método auxiliar recursivo para reversa */
    private Lista<T> reversa(Lista<T> a, Nodo nodo) {
        if (nodo == null)
            return a;
        a.agregaInicio(nodo.elemento);
        return reversa(a, nodo.siguiente);
    }
        
    
    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> nueva = new Lista<>();
        return copia(nueva, cabeza);
    }

    /* Método auxiliar recursivo para copia. */
    private Lista<T> copia(Lista<T> c, Nodo nodo) {
        if (nodo == null)
            return c;
        c.agregaFinal(nodo.elemento);
        return copia(c, nodo.siguiente);
    }

    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (longitud == 0)
            throw new NoSuchElementException("La lista es vacía");
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (longitud == 0)
            throw new NoSuchElementException("La lista es vacía");
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= longitud)
            throw new ExcepcionIndiceInvalido();
        return get(cabeza, i, 0);
    }
    
    /* Método auxiliar para get */
    private T get(Nodo nodo, int i, int j) {
        if (i == j)
            return nodo.elemento;
        return get(nodo.siguiente, i, ++j);
    }
            

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        return indiceDe(elemento, cabeza, 0);
    }

    /* Método auxiliar recursivo para indiceDe. */
    private int indiceDe(T elemento, Nodo nodo, int i) {
        if (nodo == null)
            return -1;
        if (nodo.elemento.equals(elemento))
            return i;
        return indiceDe(elemento, nodo.siguiente, i+1);
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (cabeza == null)
            return "[]";
        return "[" + cabeza.elemento.toString() + toString(cabeza.siguiente);
    }

    /* Método auxiliar recursivo para toString. */
    private String toString(Nodo nodo) {
        if (nodo == null)
            return "]";
        return ", " + nodo.elemento.toString() + toString(nodo.siguiente);
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Lista))
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        Nodo t1 = cabeza;
        Nodo t2 = lista.cabeza;
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

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
    
    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> l) {
        if (l.cabeza == l.rabo) 
            return l.copia();
        Lista<T> listai = new Lista<>();
        Lista<T> listad = new Lista<>();
        int i = 0;
        for (T e : l) {
            Lista<T> lm = (i++ < l.longitud / 2) ? listai : listad;
            lm.agrega(e);
        }
        return mezcla(mergeSort(listai), mergeSort(listad));
        
    }
    
    /* Método auxiliar para mergeSort */
    private static <T extends Comparable<T>> 
    Lista<T> mezcla(Lista<T> li, Lista<T> ld) {
        Lista<T> nueva = new Lista<>();
        Lista<T>.Nodo guia1 = li.cabeza;
        Lista<T>.Nodo guia2 = ld.cabeza;
            while ( !(guia1 == null || guia2 == null) ) {
                if (guia1.elemento.compareTo(guia2.elemento) <= 0) {
                    nueva.agrega(guia1.elemento);
                    guia1 = guia1.siguiente;
                } else {
                    nueva.agrega(guia2.elemento);
                    guia2 = guia2.siguiente;
                }
            }
        Lista<T>.Nodo guia3 = guia1 != null ? guia1 : guia2;
        while (guia3 != null) {
            nueva.agrega(guia3.elemento);
            guia3 = guia3.siguiente;
        }
            
        return nueva;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista donde se buscará.
     * @param e el elemento a buscar.
     * @return <tt>true</tt> si e está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>> 
    boolean busquedaLineal(Lista<T> l, T e) {
        Lista<T>.Nodo nodo = l.cabeza;
        while (nodo.elemento.compareTo(e) != 0) {
            if (nodo.siguiente == null) {
                return false;
            } else  
                nodo = nodo.siguiente;
        }
        return true;

    }
}
