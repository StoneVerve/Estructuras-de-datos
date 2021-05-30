package mx.unam.ciencias.edd.proyecto3.diseñadores;

/**
 * Clase abstracta que busca ofrecer una variedad de métodos para iniciar y 
 * desarrollar un archivo html básico
 */
public abstract class GeneradorHTML {
    
    /**
     * Genera una cadena que representa la inicilización de un archivo html
     * estandar
     * @param titulo El titulo que recibira en el archivo en la etiqueta 
     * head
     * @param css El CSS que personaliza la visualización del archivo html
     * @return Una cadena con la inicialización de un archivo html
     */
    protected String inicializa(String titulo, String css) {
        String ini = "<!DOCTYPE html>" + '\n' + "<html lang=\"es\" >" + '\n' +
                     "<head>" + '\n' + "<title>" + titulo + "</title> " + '\n' +
                     "<meta charset=\"UTF-8\">" + '\n' + css + '\n' +
                     "</head>" + '\n' + "<body>";
        return ini;
    }
    
    /**
     * Genera una cadena que cierra las etiquetas body y html de un archivo
     * html
     * @return Una cadena con el cierre de una archivo estandar de html
     */
    protected String cierra() {
        String fin = "</body>" + '\n' + "</html>";
        return fin;
    }

    /**
     * Crea un parrafo visible de un archivo html
     * @param texto El texto que se busca tener en el párrafo
     * @param id EL id que le correspondera el párrafo
     * @return Una cadena con el texto formateado correctamente para un archivo
     * html
     */
    protected String creaParrafo(String texto, String id) {
        String tex = "<p" + " id=\"" + id + "\"> " + texto + " </p>";
        return tex;
    }
    
    /**
     * Genera una etiqueta <hr> que corrsponde a una línea horizontal en html
     * @return Un String con la etiqueta <hr>
     */
    protected String creaLineaH() {
        return "<hr>";
    }
    
    /**
     * Crea un título visible de un archivo html
     * @param num El número correspondiente al párrafo, ejemplo: h(num)
     * @param texto El título que se desea crear
     * @return Una cadena con el texto formateado correctamente para un título de 
     * un archivo estandar de html
     */ 
    protected String creaTitulo(String num, String texto) {
        String tex = "<h" + num + ">" + texto + "</h" + num + ">";
        return tex;
    }
    
    /**
     * Crea un link visible de un archivo html
     * @param direccion La ruta que se desea poner como link
     * @param texto El texto visible en el archivo html
     * @return Una cadena con el texto formateado correctamente para un link de 
     * un archivo estandar de html
     */ 
    protected String creaDireccion(String direccion, String texto) {
        String tex = "<a href=" + "\"" + direccion + "\" " + "target=\"_blank\">"
                      + texto + "</a>";
        return tex;
    }
}