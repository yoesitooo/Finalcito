package kernel.tablero;

import kernel.piezas.*;
import kernel.tablero.Tablero.Builder;
import java.io.Serializable; // NUEVO: Importar Serializable

public abstract class Movimiento implements Serializable { // NUEVO: Implementar Serializable
	
	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización
	
	//Atributos (en este caso, constantes)
	final Tablero tablero;
	final Pieza piezaMovida;
	final int coordDestino;
	
	public static final Movimiento MOVIMIENTO_NULO = new MovimientoNulo();	
	//Constructor
	protected Movimiento(final Tablero tablero, final Pieza piezaMovida, final int coordDestino){
		this.tablero=tablero;
		this.piezaMovida=piezaMovida;
		this.coordDestino=coordDestino;
	}
	
	@Override
	public int hashCode() {
		final int principal = 31;
		int resultado = 1;
		resultado = principal * resultado + this.coordDestino;
		resultado = principal * resultado + this.piezaMovida.hashCode();
		return resultado;
	}
	@Override
	public boolean equals(final Object otro) {
		if(this==otro) {
			return true;
		}
		if(!(otro instanceof Movimiento)) {
			return false;
		}
		final Movimiento otroMovimiento = (Movimiento) otro;
		return getCoordDestino() == otroMovimiento.getCoordDestino() && getPiezaMovida().equals(otroMovimiento.getPiezaMovida());
	}
	
	public int getCoordActual() {
		return this.getPiezaMovida().getPosicion();
	}
	
	public int getCoordDestino() {
		return this.coordDestino;
	}
	
	public Pieza getPiezaMovida() {
		return this.piezaMovida;
	}
	
	public boolean esAtaque() {
		return false;
	}
	
	public boolean esEnroque() {
		return false;
	}
	
	public Pieza getPiezaAtacada() {
		return null;
	}
	

	public Tablero ejecutar() {
		final Builder builder = new Builder();
		for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()) {
			//TODO AUN ME FALTA REVISAR EL CODIGO HASH
			if(!this.piezaMovida.equals(pieza)) {
				builder.setPieza(pieza);
			}
		}
		for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()) {
			builder.setPieza(pieza);
		}
		//mueve la pieza movida
		builder.setPieza(this.piezaMovida.muevePieza(this));
		builder.setMovimiento(this.tablero.jugadorActual().getOponente().getEquipo());
		
		return builder.build();
	}

	public boolean esMovimientoEnroque() {
		// TODO Auto-generated method stub
		return false;
	}
}