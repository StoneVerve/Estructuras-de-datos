package mx.unam.ciencias.edd;

/**
 * Clase para fabricar picadillos.
 */
public class FabricaPicadillos {

    /**
     * Regresa una instancia de {@link Picadillo} para cadenas.
     * @param algoritmo el algoritmo de picadillo que se desea.
     * @return una instancia de {@link Picadillo} para cadenas.
     * @throws IllegalArgumentException si recibe un identificador no
     *         reconocido.
     */
    public static Picadillo<String> getInstancia(AlgoritmoPicadillo algoritmo) {
    	if (algoritmo == AlgoritmoPicadillo.BJ_STRING)
    		return bobJenkins();
    	else if (algoritmo == AlgoritmoPicadillo.GLIB_STRING)
    		return glib();
    	else if (algoritmo == AlgoritmoPicadillo.XOR_STRING)
    		return xor();
    	else 
    		throw new IllegalArgumentException("Hash no soportado");
    }
    
    /**
     * Realiza el picadillo Bob Jenkins con un string
     * @return una instancia del picadillo de Bob J
     */
    private static Picadillo<String> bobJenkins() {
    	return (String z) -> {
    		byte[] m = z.getBytes();
    		int l = m.length;
        	int a = 0x9e3779b9; /* "Golden ratio"; es arbitrario. */
        	int b = a; 
        	int c = 0xffffffff;      /* Valor inicial. */
			int k = 0;       /* Contador */
        	
        	/* Dividimos la llave en 3 enteros (grupos de 4 bytes), y
           	   los mezclamos. Hacemos esto mientras tengamos grupos de 3
           	   enteros disponibles en la llave. */
           	   
        	while (l >= 12) {
      	    	a += (m[k] + (m[k + 1] << 8) + (m[k + 2]  << 16) + (m[k + 3]  <<
		    	      24));
            	b += (m[k + 4] + (m[k + 5] << 8) + (m[k + 6]  << 16) + (m[k + 7]  <<
				      24));
            	c += (m[k + 8] + (m[k + 9] << 8) + (m[k + 10] << 16) + (m[k + 11] <<
            	      24));
                  
            	int [] elem = mezclaBJ(a, b, c);
            	a = elem[0];
            	b = elem[1];
            	c = elem[2];
            	k += 12;
            	l -= 12;
        	}	
        
         	/* El switch lidia con los últimos 11 bytes. */
        	c += m.length;
        	switch (l) {
            	    /* No hay breaks; se ejecutan los casos desde el
            	       primero encontrado hasta el último. */
        	case 11:
        		c += (m[k + 10] << 24);
        	case 10: 
        	    c += (m[k + 9]  << 16);
        	case  9:
        	    c += (m[k + 8]  <<  8);
        	        /* El primer byte de c se reserva para la
        	           longitud. */
       		 case  8:
       		     b += (m[k + 7]  << 24);
       		 case  7:
       		     b += (m[k + 6]  << 16);
       		 case  6:
            	b += (m[k + 5]  <<  8);
        	case  5:
            	b += m[k + 4];
        	case  4:
            	a += (m[k + 3]  << 24);
        	case  3:
            	a += (m[k + 2]  << 16);
        	case  2:
            	a += (m[k + 1]  <<  8);
        	case  1:
            	a += m[k];
            	    /* Caso 0: Nada que hacer, se acabaron los bytes. */
        	}

        	/* Mezclamos una última vez. */
        	return mezclaBJ(a, b, c)[2];
		};
	}

    /* Realiza la mezcla del señor Bob Jenkins */
    private static int[] mezclaBJ(int a, int b, int c) {
    	a -= b; a -= c; a ^= (c >>> 13);
		b -= c; b -= a; b ^= (a <<  8);
		c -= a; c -= b; c ^= (b >>> 13);
		
		a -= b; a -= c; a ^= (c >>> 12);
		b -= c; b -= a; b ^= (a <<  16);
		c -= a; c -= b; c ^= (b >>> 5);
		
		a -= b; a -= c; a ^= (c >>> 3);
		b -= c; b -= a; b ^= (a << 10);
		c -= a; c -= b; c ^= (b >>> 15);
		
		return new int[]{a, b, c};
	}
    
    
    /**
     * Realiza el picadillo glib con un string
     * @return una instancia del picadillo glib
     */
    private static Picadillo<String> glib() {
    	return (String z) -> {
    	    byte[] arreglo = z.getBytes();
    	    int n = 5381;
    	    for (int i = 0; i < arreglo.length; i++) {
    		    int c = arreglo[i] & 0xFF;
    		    n = n * 33 + c;
    	    }
    	    return n;
    	};
    }
    
    /**
     * Realiza el picadillo xor con un string
     * @return una instancia del picadillo xor
     */
    private static Picadillo<String> xor() {
    	return (String z) -> {
    	    byte[] arreglo = z.getBytes();
    	    int l = arreglo.length;
    	    int r = 0;
    	    int i = 0;
     	    while (l >= 4) {
    		    r ^= (arreglo[i] << 24) | (arreglo[i + 1] << 16) | (arreglo[i + 2] << 8) |
    			      arreglo[i + 3];
    		    i += 4;
    	     	l -= 4;
    	    }
    	    int t = 0;
    	    switch (l) {
    		    case 3:
    		        t |= arreglo[i+2] << 8;
                case 2:
                    t |= arreglo[i+1] << 16;
                case 1:
                    t |= arreglo[i] << 24;
            }
            r ^= t;
            return r;
        };
    }
}
