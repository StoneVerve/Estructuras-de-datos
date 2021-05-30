import java.util.Random;
import mx.unam.ciencias.edd.*;

public class Prueba {

	public static void main(String[] args) {
	
		Random gen = new Random();
		ArbolRojinegro<Integer> arbol = new ArbolRojinegro<>();
		Integer[] ar = new Integer[10];
		ar[0] = 2;
		ar[1] = 3;
		ar[2] = 4;
		ar[3] = 1;
		ar[4] = 0;
		ar[5] = 6;
        for (int i = 0; i < 6; i++) {
            int n = ar[i];
            arbol.agrega(n);
        }
        arbol.agrega(ar[0]);
        //System.out.println(arbol.toString());
        arbol.elimina(2);
        //System.out.println(arbol.toString());
        
        ArbolRojinegro<Integer> arbol1 = new ArbolRojinegro<>();
        int a = 0;
        for (int k = 0; k < 10; k++) {
        	int n = gen.nextInt(100);
        	ar[k] = n;
        	//System.out.println(n);
        	arbol1.agrega(n);
        	if (k == 5)
        		a = n;
        	//System.out.println(arbol1.toString());
        }
        System.out.println(arbol1.toString());
        arbol1.elimina(a);
        System.out.println(arbol1.toString());
        
        ArbolBinarioOrdenado<Integer> arbol3 = new ArbolBinarioOrdenado<>();
        for (int j = 0; j < 3; j++) {
        	int s = ar[j];
        	arbol3.agrega(s);
        }
        //System.out.println(arbol3.toString());
        arbol3.giraIzquierda(arbol3.raiz());
        //System.out.println(arbol3.toString());


        
        ArbolRojinegro<Integer> arbol2 = new ArbolRojinegro<>();
        for (int k = 0; k < 5; k++) {
        	arbol2.agrega(5);
        }
        //System.out.println(arbol2.toString());
    }
}
        