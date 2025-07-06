package kernel.tablero;

import kernel.piezas.*;
import kernel.tablero.Tablero.Builder;

public class MovimientoPeonSalto extends Movimiento {
	private static final long serialVersionUID = 1L;

	public MovimientoPeonSalto(final Tablero tablero,final Pieza piezaMovida,final int coordDestino) {
		super(tablero,piezaMovida,coordDestino);
	}
	
	@Override
	public Tablero ejecutar() {
		final Builder builder = new Builder();
		for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()) {
			if(!(this.piezaMovida.equals(pieza))) {
				builder.setPieza(pieza);
			}
		}
		for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()) {
			builder.setPieza(pieza);
		}
		
		final Peon peonMovido = (Peon)this.piezaMovida.muevePieza(this);
		builder.setPieza(peonMovido);
		
		// CRUCIAL: Establecer este peón como disponible para captura al paso
		builder.setCapturaAlPaso(peonMovido);
		
		builder.setMovimiento(this.tablero.jugadorActual().getOponente().getEquipo());
		return builder.build();
	}
}
