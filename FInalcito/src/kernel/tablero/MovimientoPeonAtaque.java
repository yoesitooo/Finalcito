package kernel.tablero;


import kernel.piezas.*;
public class MovimientoPeonAtaque extends MovimientoAtaque {
	private static final long serialVersionUID = 1L;

	public MovimientoPeonAtaque(final Tablero tablero,final Pieza piezaMovida,final int coordDestino, final Pieza piezaAtacada) {
		super(tablero,piezaMovida,coordDestino,piezaAtacada);
	}
}
