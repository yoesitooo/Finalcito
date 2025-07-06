package kernel.tablero;


import kernel.piezas.*;
public class MovimientoEnroqueLargo extends MovimientoEnroque {
	private static final long serialVersionUID = 1L;

	public MovimientoEnroqueLargo(final Tablero tablero,final Pieza piezaMovida,final int coordDestino, final Torre torreEnroque, final int torreEnroqueInicio, final int torreEnroqueDestino) {
		super(tablero,piezaMovida,coordDestino,torreEnroque,torreEnroqueInicio,torreEnroqueDestino);
	}
}

