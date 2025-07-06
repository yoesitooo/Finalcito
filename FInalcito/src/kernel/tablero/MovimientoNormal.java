package kernel.tablero;


import kernel.piezas.*;

public final class MovimientoNormal extends Movimiento {
	private static final long serialVersionUID = 1L;

	//Constructor
	public MovimientoNormal(final Tablero tablero,final Pieza piezaMovida,final int coordDestino) {
		super(tablero, piezaMovida, coordDestino);
	}
	
	@Override
	public Tablero ejecutar() {
		final Tablero.Builder builder = new Tablero.Builder();
		
		for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()) {
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
		
		// IMPORTANTE: Los movimientos normales limpian el peón captura al paso
		// (No llamar setCapturaAlPaso - será null)
		
		return builder.build();
	}
}