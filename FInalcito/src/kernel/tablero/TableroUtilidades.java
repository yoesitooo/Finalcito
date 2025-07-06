package kernel.tablero;


public class TableroUtilidades {
	//Atributos(en este caso, constantes)
	public static final boolean[] primeraColumna = inicioColumna(0);
	public static final boolean[] segundaColumna = inicioColumna(1);
	public static final boolean[] septimaColumna = inicioColumna(6);
	public static final boolean[] octavaColumna = inicioColumna(7);
	
	// TODAS LAS FILAS NECESARIAS PARA AJEDREZ
	public static final boolean[] primeraFila = inicioFila(0);   // Fila 8 (promoción negros)
	public static final boolean[] segundaFila = inicioFila(8);   // Fila 7 (peones negros inicial)
	public static final boolean[] terceraFila = inicioFila(16);  // Fila 6
	public static final boolean[] cuartaFila = inicioFila(24);   // Fila 5 (en passant blancos)
	public static final boolean[] quintaFila = inicioFila(32);   // Fila 4 (en passant negros)
	public static final boolean[] sextaFila = inicioFila(40);    // Fila 3
	public static final boolean[] septimaFila = inicioFila(48);  // Fila 2 (peones blancos inicial)
	public static final boolean[] octavaFila = inicioFila(56);   // Fila 1 (promoción blancos)
	
	public static final int numCasillas= 64;
	public static final int numCasillasPorColumna = 8;
	
	//Constructor
	TableroUtilidades(){
		throw new RuntimeException("No puedes instanciarme");
	}
	//Metodo para llenar un arreglo boleano con true en la columna indicada
	public static boolean[] inicioColumna(int numColumna) {
		final boolean[] columna = new boolean[numCasillas];
		do {
			columna[numColumna] = true;
			numColumna +=numCasillasPorColumna;
		}while(numColumna<numCasillas);
		return columna;
	}
	//Metodo para llenar un arreglo booleano con true en la fila indicada
	public static boolean[] inicioFila(int numFila) {
		final boolean[] fila = new boolean[numCasillas];
		do {
			fila[numFila]=true;
			numFila++;
		}while(numFila%numCasillasPorColumna!=0);
		return fila;
	}
	//Metodo que nos dice si una coordenada dada es una casilla valida
	public static boolean coordCasillaEsValida(final int coordenada) {
		return coordenada>=0 && coordenada<numCasillas;
	}
}