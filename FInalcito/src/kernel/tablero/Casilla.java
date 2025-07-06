package kernel.tablero; 

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable; // NUEVO: Importar Serializable
import kernel.piezas.*;

public abstract class Casilla implements Serializable { // NUEVO: Implementar Serializable
	
	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización
	
	//Atributos
	protected final int coordenada;
	private static final Map<Integer, CasillaVacia> casillasVacias	=crearPosiblesCasillaVacia();

	//Constructor
	protected Casilla(final int coordenada){
		this.coordenada=coordenada;
	}
	//Metodo para crear las casillas vacias
	private static Map<Integer, CasillaVacia> crearPosiblesCasillaVacia() {
		final Map<Integer, CasillaVacia> mapaCasillasVacias = new HashMap<>();
		for(int i=0;i<TableroUtilidades.numCasillas;i++) {
			mapaCasillasVacias.put(i, new CasillaVacia(i));
		}
		return mapaCasillasVacias;
	}
	public static Casilla crearCasilla(final int coordCasilla, final Pieza pieza) {
		return pieza!=null ? new CasillaOcupada(coordCasilla, pieza) : casillasVacias.get(coordCasilla);
	}
	
	//Metodo abstracto que dirá si la casilla está ocupada
	public abstract boolean estaCasillaOcupada();
	//Getter abstracto que optendrá la pieza en la casilla
	public abstract Pieza getPieza();
	//Getter para la coordenada de la casilla
	public int getCoordCasilla() {
		return this.coordenada;
	}
	
}