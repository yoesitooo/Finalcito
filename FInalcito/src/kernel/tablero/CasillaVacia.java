package kernel.tablero;

import kernel.piezas.*;

public final class CasillaVacia extends Casilla {
	
	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización
	
	//Constructor
	CasillaVacia(int coordenada){
		super(coordenada);
	}
	//Metodo boleano para saber si la casilla esta ocupada
	@Override
	public boolean estaCasillaOcupada() {
		return false;
	}
	@Override
	public String toString() {
		return "-";
	}
	//Getter de la pieza en la casilla (en este caso, ninguna)
	@Override
	public Pieza getPieza() {
		return null;
	}
}