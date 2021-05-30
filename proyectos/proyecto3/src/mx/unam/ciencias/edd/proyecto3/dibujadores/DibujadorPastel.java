package mx.unam.ciencias.edd.proyecto3.dibujadores;

import mx.unam.ciencias.edd.proyecto3.diseñadores.Palabra;
import mx.unam.ciencias.edd.*;
import java.text.DecimalFormat;
import java.lang.Math;

/**
 * Clase pública que extiende la clase DibujadorSVG
 * La clase nos permite dibujar una gráfica de pastel en SVG a partir de 
 * una lista de palabras y el total
 */
public class DibujadorPastel extends DibujadorSVG {

    private Lista<Palabra> lista;
    private int elementos;
    private String informacion;
    
    /**
     * Constructor que recibe una lista de palabras y el total a partir del cual se
     * generara la gráfica de pastel
     * @param lista La lsita de Palabras
     * @param elementos EL total de palabras contando repeticiones
     */
    public DibujadorPastel(Lista<Palabra> lista, int elementos) {
        this.lista = lista;
        this.elementos = elementos;
        informacion = "Palabras y porcentajes :" + "<br>" + '\n';
    }
    
    /**
     * Método público principal que dibuja la gráfica de pastel contenida en el dibujador a
     * partir de los datos ingresado en el constructor de la clase
     * @return Una cadena SVG que representa la gráfica de pastel que ha sido dibujado
     */
    public String dibujaGrafica() {
        String salida = incializaArchivo(700, 700) + '\n';
        salida = salida + "<g>" + '\n';
        salida = salida + dibujaCirculo(350.00, 350.00, 250.00, "black", 3.0, "lightblue") + '\n';
        double anguloAnt = 0.0;
        double anguloSig = 0.0;
        boolean otros = false;
        double por = 0.0;
        for (Palabra e : lista) {
            if (Double.compare(calculaPorcentaje(e), 1.5) > 0) {
                anguloSig = calculaAngulo(e, anguloSig);
                String texto = pasaTexto(calculaPorcentaje(e));
                informacion = informacion + e.getPalabra() + ":" +
                              " (" + texto +"),   ";
                salida = salida + creaTexto(anguloAnt, anguloSig, texto) + '\n';
                salida = salida + dibujaRecta(350.0, 350.0,
                                              calculaCoorX(250.0, anguloSig, 350.0), 
                                              calculaCoorY(250.0, anguloSig, 350.0),
                                              "black", 1.0) + '\n';
                anguloAnt = anguloSig;
            } else {
                anguloSig = calculaAngulo(e,anguloSig);
                por = por + calculaPorcentaje(e);
                otros = true;
            }
            
        }
        if (otros) {
            String texto = pasaTexto(por);
            informacion = informacion + "* Otros (" + texto + ")";
            salida = salida + creaTexto(anguloAnt, anguloSig, texto) + '\n'; 
            salida = salida + dibujaRecta(350.0, 350.0,
                                          calculaCoorX(250.0, anguloSig, 350.0), 
                                          calculaCoorY(250.0, anguloSig, 350.0),
                                          "black", 1.0) + '\n';
        }
        salida = salida + "</g>" + '\n' + "</svg>";
        return salida;
    
    }
    
    /**
     * Devuelve los procentajes correspondietes con las palabras de la gráfica 
     * de pastel
     * @return Una cadena con la palabras y los porcentajes correpondientes a la 
     * gráfica
     */
    public String getInformacion() {
        return informacion;
    }
    
    /*
     * Pasa a texto un valor double
     * @param j el valor a pasara a texto
     * @return Un string con el valor
     */
    private String pasaTexto(double j) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        String texto = formatter.format(j) + "%";
        return texto;
    }
    
    /*
     * Acomoda y formate los porcentajes correspondietes a cada rebanada de la gráfica
     * @param anguloAnt el ángulo anterior 
     * @param anguloSig el ángulo siguiente
     * @param texto El porcentaje a poner
     * @return Un string con el texto
     */
    private String creaTexto(double anguloAnt, double anguloSig, String texto) {
        double anguloTexto = (anguloSig + anguloAnt) / 2.0;
        double coorX = calculaCoorX(290.0, anguloTexto, 350.0);
        double coorY = calculaCoorY(290.0, anguloTexto, 350.0);
        return dibujaTexto("sans-serif", 9.00, coorX, coorY, "middle",
                           texto, "black");
    }
    
    /* 
     * Calcula el ángulo correspondiente a un punto de la palabra en el círculo
     * @param e La palabra correspondiente al punto
     * @param angulo el angulo del punto anterior
     * @return El valor del ángulo
     */
    private double calculaAngulo(Palabra e, double angulo) {
        double aux = (e.repeticiones() * (2.0 * Math.PI)) / (elementos * 1.0);
        return angulo + aux;
    }
    
    /*
     * Calcula el porcentaje de una palabra en base el número total de palabras
     * @param e la palabra a calcular el porcentaje
     * @return El valor del porcentaje
     */
    private double calculaPorcentaje(Palabra e) {
        double aux = (e.repeticiones() * 100.0) / (elementos * 1.0);
        return aux;
    }
    
    /*
     * Calcula la coordenada X de un punto en la circunferencia
     * @param radio el radio de la circunferencia
     * @param angulo el ángulo del punto
     * @param centro el centro de la circunferencia
     * @return El valor de la coordenada x
     */
    private double calculaCoorX(double radio, double angulo, double centro) {
        return (Math.cos(angulo) * radio) + centro;
    }
    
    /*
     * Calcula la coordenada Y de un punto en la circunferencia
     * @param radio el radio de la circunferencia
     * @param angulo el ángulo del punto
     * @param centro el centro de la circunferencia
     * @return El valor de la coordenada Y
     */
    private double calculaCoorY(double radio, double angulo, double centro) {
        return (Math.sin(angulo) * radio) + centro;
    }

}