package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las operaciones de
 * inserción, eliminación y búsqueda pueden realizarse en <i>O</i>(log
 * <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends ArbolBinario<T>.Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            this.color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            String color;
            color = (this.color == Color.NEGRO) ? "N" : "R";
            return color + "{" + elemento.toString() + "}"; 
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeRojinegro vertice = (VerticeRojinegro)o;
            return super.equals(vertice) && this.color == vertice.color;
        }
    }
    
    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del
     *         mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeRojinegro}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice
     *                rojinegro.
     * @return el vértice recibido visto como vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro u = verticeRojinegro(vertice);
        return (u == null) ? Color.NEGRO : u.color;
    }
    
    /*
     * Método auxiliar que no dice si el vértice roji-negro es hijo izquierdo
     * @param vertice el vertice roji-negro que se quiere saber si es hijo izquierdo
     * o derecho
     * @return true si es hijo izquierdo y false en cualquier otro caso
     */
    private boolean esIzquierdo(VerticeRojinegro vertice) {
        if (vertice == null)
            return false;
        return vertice.padre.izquierdo == vertice;
    }
    
    /*
     * Método auxiliar que nos regresa el hermano de un vértice roji-negro
     * @param vertice El vértice roji-negro del cual queremos conocer su hermano
     * @return el hermano del vertice o null si el hermano es null
     */
    private VerticeRojinegro getHermano(VerticeRojinegro vertice) {
        if (esIzquierdo(vertice)) {
            return verticeRojinegro(vertice.padre.derecho);
        } else
            return verticeRojinegro(vertice.padre.izquierdo);
    }
    
    /*
     * Método auxiliar que nos regresa el tío de un vértice roji-negro
     * @param vertice El vértice roji-negro del cual queremos conocer su tío
     * @return el tío del vértice roji-negro o null si el tío es null
     */
    private VerticeRojinegro getTio(VerticeRojinegro vertice) {
        VerticeRojinegro padre = verticeRojinegro(vertice.padre);
        if (esIzquierdo(padre))
            return verticeRojinegro(padre.padre.derecho);
        else 
            return verticeRojinegro(padre.padre.izquierdo);
    }
    
    /*
     * Método auxiliar que nos regresa el hijo derecho de un vértice roji-negro
     * @param vertice El vértice roji-nergro del cual queremos conocer su hijo
     * derecho
     * @return el hijo derecho del vértice
     */
    private VerticeRojinegro getHijoD(VerticeRojinegro vertice) {
        return verticeRojinegro(vertice.derecho);
    }
    
    /*
     * Método auxiliar que nos regresa el hijo izquierdo de un vértice roji-negro
     * @param vertice El vértice roji-nergro del cual queremos conocer su hijo
     * izquierdo
     * @return el hijo izquierdo del vértice
     */
    private VerticeRojinegro getHijoI(VerticeRojinegro vertice) {
        return verticeRojinegro(vertice.izquierdo);
    }
    
    /*
     * Método auxiliar que nos dice si un vértice roji-negro es de color negro
     * @param vertice El vértice roji-nergro del cual queremos conocer si su 
     * color es negro
     * @return true si es negro, false en cualquier otro caso
     */
    private boolean esNegro(VerticeRojinegro vertice) {
        if (vertice == null)
            return true;
        if (vertice.color == Color.NEGRO)
            return true;
        else
            return false;
    }
    
    /*
     * Método auxiliar que nos dice si los sobrino de un vértice son de color 
     * negro
     * @param vertice El vértice roji-nergro del cual queremos conocer si su 
     * sobrinos son de color negro
     * @return true si son de color negro, false en cualquier otro caso
     */
    private boolean sonSobrinoNegros(VerticeRojinegro vertice) {
        VerticeRojinegro hermano = getHermano(vertice);
        if (hermano == null)
            return true;
        if (esNegro(getHijoI(hermano)) && esNegro(getHijoD(hermano))) {
            return true;
        } else
            return false;
    }
    
    /*
     * Método auxiliar que nos regresa el sobrino cruzado de un vértice roji-negro
     * @param vertice El vértice roji-nergro del cual queremos conocer su sobri-
     * no cruzado
     * @return el sobrino cruzado del vértice
     */
    private VerticeRojinegro getSobrinoCruzado(VerticeRojinegro vertice) {
        VerticeRojinegro hermano = getHermano(vertice);
        VerticeRojinegro sobrinoI = getHijoI(hermano);
        VerticeRojinegro sobrinoD = getHijoD(hermano);
        if (esIzquierdo(vertice))
            return sobrinoD;
        else
            return sobrinoI;
    }
    
    /*
     * Método auxiliar que nos regresa el sobrino no cruzado de un vértice roji-negro
     * @param vertice El vértice roji-nergro del cual queremos conocer su sobri-
     * no no cruzado
     * @return el sobrino cruzado del vértice
     */
    private VerticeRojinegro getSobrinoNoCruzado(VerticeRojinegro vertice) {
        VerticeRojinegro hermano = getHermano(vertice);
        if (hermano != null) {
            VerticeRojinegro sobrinoI = getHijoI(hermano);
            VerticeRojinegro sobrinoD = getHijoD(hermano);
            if (esIzquierdo(vertice))
                return sobrinoI;
            else
                return sobrinoD;
        } else
            return null;
    }
    
    /*
     * Método auxiliar que nos dice si los sobrino de un vértice son bicoloreados
     * cruzados (que el sobrino cruzado tiene el mismo color que el vértice)
     * @param vertice El vértice roji-nergro del cual queremos conocer si sus 
     * sobrinos son bicoloreados cruzados
     * @return true si son bicoloreados cruzados, false en cualquier otro caso
     */
    private boolean sonBicoloreadosCruzados(VerticeRojinegro vertice) {
        VerticeRojinegro hermano = getHermano(vertice);
        if (hermano == null)
            return false;
        else
            if (getColor(vertice) == getColor(getSobrinoCruzado(vertice)) &&
                getColor(vertice) != getColor(getSobrinoNoCruzado(vertice)))
                return true;
            else
                return false;
    }
                
        

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro actual = verticeRojinegro(ultimoAgregado);
        actual.color = Color.ROJO;
        rebalanceoAgrega(actual);
    }
    
    /*
     * Algoritmo recursivo que realancea el árbol roji-negro a través de un número 
     * constante de operaciones los cuales incluyen giros,
     * cambios de color, etc.
     * @param actual el vértice a partir del cual inciara el algortimo el 
     * rebalanceo
     */
    private void rebalanceoAgrega(VerticeRojinegro actual) {
        VerticeRojinegro padre = verticeRojinegro(actual.padre);
        // Caso 1
        if (padre == null) {
            actual.color = Color.NEGRO;
            return;
        } 
        // Caso 2
        if (padre.color == Color.NEGRO) {
            return;
        }
        VerticeRojinegro abuelo = verticeRojinegro(actual.padre.padre);
        VerticeRojinegro tio = getTio(actual);
        // Caso 3
        if (getColor(tio) == Color.ROJO) {
            padre.color = Color.NEGRO;
            tio.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalanceoAgrega(abuelo);
            return;
        }
        // Caso 4
        if (esIzquierdo(actual) && !esIzquierdo(padre)) {
            super.giraDerecha(padre);
            padre = actual;
            actual = verticeRojinegro(padre.derecho);
        } else if (!esIzquierdo(actual) && esIzquierdo(padre)) {
            super.giraIzquierda(padre);
            padre = actual;
            actual = verticeRojinegro(padre.izquierdo);
        }
        // Caso 5
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if (esIzquierdo(padre))
            super.giraDerecha(padre.padre);
        else 
            super.giraIzquierda(padre.padre);
    }
    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro adios = verticeRojinegro(busca(raiz, elemento));
        if (adios == null)
            return;
        // Checamos si tiene dos hijos
        if (adios.izquierdo != null && adios.derecho != null) {
            VerticeRojinegro max;
            max = verticeRojinegro(maximoEnSubarbol(adios.izquierdo));
            intercambio(adios, max);
            adios = max;
        }
        // Verificamos si no tiene hijos, en caso de no tener agregamos un fantasma
        if (adios.izquierdo == null && adios.derecho == null) {
            VerticeRojinegro aux = verticeRojinegro(nuevoVertice(null));
            aux.color = Color.NEGRO;
            adios.izquierdo = aux;
            aux.padre = adios;
        }
        // Eliminamos el único hijo
        if (adios.derecho != null) {
            eliminaAux(getHijoD(adios));
        } else {
            eliminaAux(getHijoI(adios));
        }
        elementos--;
    }
    
    /*
     * Algoritmo recursivo que realancea el árbol roji-negro a través de un número 
     * constante de operaciones los cuales incluyen giros,
     * cambios de color, etc.
     * @param actual el vértice a partir del cual inciara el algortimo el 
     * rebalanceo
     */
    private void rebalanceoElimina(VerticeRojinegro actual) {
        // Caso 1
        if (actual.padre == null)
            return;
        // Caso 2
        VerticeRojinegro hermano = getHermano(actual);
        VerticeRojinegro padre = verticeRojinegro(actual.padre);
        if (!esNegro(hermano)) {
            hermano.color = Color.NEGRO;
            padre.color = Color.ROJO;
            if (esIzquierdo(actual))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
        }
        // Caso 3
        hermano = getHermano(actual);
        if (esNegro(padre) && esNegro(hermano) && sonSobrinoNegros(actual)) { 
            hermano.color = Color.ROJO;
            rebalanceoElimina(padre);
            return;
        }
        // Caso 4
        if (padre.color == Color.ROJO && esNegro(hermano) && sonSobrinoNegros(actual)) {
            padre.color = Color.NEGRO;
            hermano.color = Color.ROJO;
            return;
        }
        // Caso 5
        if (sonBicoloreadosCruzados(actual)) {
            VerticeRojinegro cruzado = getSobrinoCruzado(actual);
            VerticeRojinegro noCruzado = getSobrinoNoCruzado(actual);
            noCruzado.color = Color.NEGRO;
            hermano.color = Color.ROJO;
            if (esIzquierdo(actual))
                super.giraDerecha(hermano);
            else
                super.giraIzquierda(hermano);
            hermano = getHermano(actual);
        }
        // Caso 6
        padre = verticeRojinegro(actual.padre);
        hermano.color = padre.color;
        padre.color = Color.NEGRO;
        VerticeRojinegro cruzado = getSobrinoCruzado(actual);
        cruzado.color = Color.NEGRO;
        if (esIzquierdo(actual)) {
            super.giraIzquierda(padre);
        } else { 
            super.giraDerecha(padre);
        }
    }
    
    /*
     * Método auxilir que intercambia los elementos de dos vértices
     * @param a vértice a intercambiar elemento
     * @param b vértice a intercambiar elemento
     */
    private void intercambio(VerticeRojinegro a, VerticeRojinegro b) {
        T tmp = a.elemento;
        a.elemento = b.elemento;
        b.elemento = tmp;
    }
    
    /*
     * Método auxiliara para elimina(T elemento), se encarga de subir 
     * uno de los hijos del vértice a eliminar y llama al método para rebalancear
     * en caso de ser necesario
     * @param vertice El vértice cuyo padre va a ser eliminado
     */
    private void eliminaAux(VerticeRojinegro vertice) {
        VerticeRojinegro padre = verticeRojinegro(vertice.padre);
        vertice.padre = padre.padre;
        if (padre != raiz) {
            if (esIzquierdo(padre))
                padre.padre.izquierdo = vertice;
            else
                padre.padre.derecho = vertice;
        } else {
            raiz = vertice;
        }
        if (padre.color == Color.NEGRO && vertice.color == Color.ROJO) {
            vertice.color = Color.NEGRO;
        } else if (padre.color == Color.NEGRO && vertice.color == Color.NEGRO) {
            rebalanceoElimina(vertice);
        }
        eliminaFantasma(vertice);
    }
            
     /*
      * Método auxiliar elimina un vértice fantasma si existe
      * @param vertice El vértice que puede ser fantasma
      */
    private void eliminaFantasma(VerticeRojinegro vertice) {
        if (vertice.elemento != null)
            return;
        if (vertice == raiz) {
            raiz = null;
            return;
        }
        if (esIzquierdo(vertice))
            vertice.padre.izquierdo = null;
        else
            vertice.padre.derecho = null;
    }
    
     /**
     * Lanza la excepción {@link UnsupportedOperationException}: los
     * árboles rojinegros no pueden ser girados a la derecha por los usuarios
     * de la clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
    
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los
     * árboles rojinegros no pueden ser girados a la izquierda por los usuarios
     * de la clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la Izquierda " +
                                                "por el usuario.");
    }   
}               