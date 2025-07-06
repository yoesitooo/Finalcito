package kernel.tablero;

import kernel.piezas.*;

public final class CasillaOcupada extends Casilla {
	
	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización
	
	//Atributo
	private final Pieza piezaEnCasilla;
	//Constructor
	CasillaOcupada(int coordenada, Pieza pieza){
		super(coordenada);
		this.piezaEnCasilla=pieza;
	}
	//Metodo que nos dice que la casilla está ocupada
	@Override
	public boolean estaCasillaOcupada() {
		return true;
	}
	@Override
	public String toString() {
		return getPieza().getEquipo().esNegra() ? getPieza().toString().toLowerCase() : getPieza().toString();
	}
	//Getter de la pieza que está en la casilla
	@Override
	public Pieza getPieza() {
		return this.piezaEnCasilla;
	}
	
}
