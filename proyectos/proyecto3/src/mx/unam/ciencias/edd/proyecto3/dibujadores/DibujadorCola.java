package mx.unam.ciencias.edd.proyecto3.dibujadores;

import mx.unam.ciencias.edd.*;


/**
 * Clase pública que extiende la clase DibujadorLista
 * La clase nos permite dibujar una cola en SVG a partir de la 
 * cola dada, la cola tiene que contener solo números enteros.
 */
public class DibujadorCola extends DibujadorLista {
    
    private Cola<Integer> estructura;
    
    
    /**
     * Constructor público que forma un nuevo dibujador a partir de una cola dada
     * @param cola La cola que se quiere dibujar
     * @param largoElemento el largo de los rectángulos que contendran a los 
     * enteros
     * @param altoElemento el alto de los rectángulos que contendran a los 
     * enteros
     * @param largoFlecha el largo de las flechas que apuntan entre los rectángulos
     */
    public DibujadorCola(Cola<Integer> cola, double largoElemento, double altoElemento, double largoFlecha) {
        super(new Lista<Integer>(), largoElemento, altoElemento, largoFlecha);
        this.estructura = cola;
    }
    
    /**
     * Método público que genera un String que representa la cola contenidad en 
     * dibujador en SVG
     * @return un String que representa la cola en SVG
     */
    public String dibujaCola() {
        double coorX = 20;
        double coorY = 20;
        
        String salida = "<g>" + '\n' + flechaD + '\n';
        while (!estructura.esVacia()) {
            Integer e = estructura.saca();
            /* Dibujamos el rectángulo */
             salida = salida + dibujaElemento(e, coorX, coorY) + '\n'; 
            
            /*Modificamos valor x para el siguiente rectángulo*/
            coorX = coorX + largo + largoFlecha;
             
             
             /* Dibujamos la línea */
             if (!estructura.esVacia()) { 
             double incY = calculaRectaY(coorY, alto);
             salida = salida + dibujaRectaDireccion(coorX + 10, incY, (coorX - largoFlecha + 10),
                                           incY, "black", 3.0) + '\n';
             }
             elementos++; 
        }
        this.tamañoY = (40 + alto);
        this.tamañoX = (50 + largo) * elementos;
        salida = incializaArchivo(tamañoX, tamañoY) + '\n' + salida;
        salida = salida + "</g>" + '\n' + "</svg>";
        return salida;
    }
    
    
    @Override public String dibujaLista() {
        return "Operacion no soportada";
    }

}
