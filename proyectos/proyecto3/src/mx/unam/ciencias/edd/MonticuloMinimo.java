package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear un montículo
 * mínimo con <em>n</em> elementos en tiempo <em>O</em>(<em>n</em>), y podemos
 * agregar y actualizar elementos en tiempo <em>O</em>(log <em>n</em>). Eliminar
 * el elemento mínimo también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            if (indice < siguiente)
                return true;
            else
                return false;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (hasNext()) {
                indice++;
                return arbol[indice - 1];
            } else 
                throw new NoSuchElementException();
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* El siguiente índice dónde agregar un elemento. */
    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Lista)}, pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = creaArregloGenerico(1);
    }

    /**
     * Constructor para montículo mínimo que recibe una lista. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param lista la lista a partir de la cuál queremos construir el
     *              montículo.
     */
    public MonticuloMinimo(Lista<T> lista) {
        arbol = creaArregloGenerico(lista.getLongitud());
        int i = 0;
        for (T e : lista) {
            arbol[i] = e;
            arbol[i].setIndice(i);
            i++;
        }
        siguiente = lista.getLongitud();
        reordena(arbol[0]);
        for (int j = arbol.length / 2; j > 0; j--)
            ordenaAbajo(j - 1);
    }
    
    /*
     * Nos dice a partir de un vértice cual es el vértice de menor valor entre
     * el y sus hijos
     * @param a el índice del vértice
     * return el índice del vértice menor
     */
    private int menorDeTres(int a) {
        int aux = a;
        int hijoI = (2 * a) + 1;
        int hijoD = (2 * a) + 2;
        if (hijoI < siguiente && hijoD < siguiente) 
            aux = (arbol[hijoI].compareTo(arbol[hijoD]) <= 0) ? hijoI : hijoD;
        else if (hijoI < siguiente)
            aux = hijoI;
        else if (hijoD < siguiente)
            aux = hijoD;
        aux = (arbol[a].compareTo(arbol[aux]) <= 0) ? a : aux;
        return aux;
    }
    
        
    /*
     * Intercambia dos elementos de posición y acutaliza sus índices
     * @param i el índice del primer elemento a cambiar
     * @param j el índice del segundo elemento a cambiar
     */ 
    private void intercambio(int i, int j) {
        T aux = arbol[i];
        arbol[i] = arbol[j];
        arbol[j] = aux;
        arbol[i].setIndice(i);
        arbol[j].setIndice(j);
    }
        
    
    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (siguiente < arbol.length) {
            arbol[siguiente] = elemento;
        } else {
            T[] aux = creaArregloGenerico(arbol.length * 2);
            int i = 0;
            for (T e : arbol) {
                aux[i] = e;
                i++;
            }
            arbol = aux;
        }
        arbol[siguiente] = elemento;
        arbol[siguiente].setIndice(siguiente);
        ordenaArriba(siguiente);
        siguiente++;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
        if (siguiente == 0) {
            throw new IllegalStateException();
        } else {
            intercambio(0, siguiente - 1);
        }
        siguiente--;
        ordenaAbajo(0);
        arbol[siguiente].setIndice(-1);
        return arbol[siguiente];
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int i = elemento.getIndice();
        if (i >= 0)
            intercambio(i, siguiente - 1);
            siguiente--;
            reordena(arbol[i]);
            arbol[siguiente].setIndice(-1);
    }
    
    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (T e : arbol) {
            if (e.equals(elemento))
                return true;
        }
        return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacio() {
        if (siguiente == 0)
            return true;
        else
            return false;
    }
    
    /*
     * Pregunta recursivamente si el padre del vértice es mayor, intercambiandolos
     * si es cierto
     * @param k el índice del vértice en el cual se empezara el algoritmo
     */
    private void ordenaArriba(int k) {
        int padre = (k - 1) / 2; 
        if (padre >= 0 && arbol[padre].compareTo(arbol[k]) > 0) {
            intercambio(k, padre);
            ordenaArriba(padre);
        } else
            return;
    }
    
   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    public void reordena(T elemento) {
        int i = elemento.getIndice(); 
        if (i >= 0 && i < siguiente) {
            ordenaAbajo(i);
            if (arbol[i].equals(elemento))
                ordenaArriba(i);
        }
    }
    
    /*
     * Pregunta recursivamente si alguno de los hijos es menor que el vértice,
     * si es cierto intercambia el menor con el vértice y se llama recursivamente
     * sobre el vértice que ocupa el lugar del hijo
     * @param l el índice del vértice en el cual se empezara el algoritmo
     */
    private void ordenaAbajo(int l) {
        int j = menorDeTres(l);
        if (j != l) {
            intercambio(j, l);
            ordenaAbajo(j);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    public T get(int i) {
        if (i < 0 || i >= arbol.length)
            throw new NoSuchElementException();
        else
            return arbol[i];
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
