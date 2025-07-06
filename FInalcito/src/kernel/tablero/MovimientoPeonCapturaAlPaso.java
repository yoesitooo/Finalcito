package kernel.tablero;

import kernel.piezas.*;

public class MovimientoPeonCapturaAlPaso extends MovimientoPeonAtaque {
	private static final long serialVersionUID = 1L;

	public MovimientoPeonCapturaAlPaso(final Tablero tablero, final Pieza piezaMovida, final int coordDestino, final Pieza piezaAtacada) {
		super(tablero, piezaMovida, coordDestino, piezaAtacada);
	}
}