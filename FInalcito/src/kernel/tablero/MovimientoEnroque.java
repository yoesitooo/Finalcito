package kernel.tablero;


import kernel.piezas.*;
import kernel.tablero.Tablero.Builder;

public class MovimientoEnroque extends Movimiento {
	private static final long serialVersionUID = 1L;
	protected final Torre torreEnroque;
	protected final int torreEnroqueInicio;
	protected final int torreEnroqueDestino;
	
	//Constructor
	public MovimientoEnroque(final Tablero tablero,final Pieza piezaMovida,final int coordDestino, final Torre torreEnroque, final int torreEnroqueInicio, final int torreEnroqueDestino) {
		super(tablero, piezaMovida, coordDestino);
		this.torreEnroque= torreEnroque;
		this.torreEnroqueInicio=torreEnroqueInicio;
		this.torreEnroqueDestino = torreEnroqueDestino;
	}
	
	
	public Torre getTorreEnroque() {
		return this.torreEnroque;
	}
	
	@Override
	public boolean esMovimientoEnroque() {
		return true;
	}
	@Override
	public Tablero ejecutar() {
		 final Builder builder = new Builder();
		 for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()) {
			 if(!this.piezaMovida.equals(pieza) && !this.torreEnroque.equals(pieza)) {
				 builder.setPieza(pieza);
			 }
		 }
		 for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()) {
			 builder.setPieza(pieza);
		 }
		 builder.setPieza(this.piezaMovida.muevePieza(this));
		 //TODO aun tengo que mirar los primeros movimientos en las piezas normaless
		 builder.setPieza(new Torre(this.torreEnroque.getEquipo(), this.torreEnroqueDestino));
		 builder.setMovimiento(this.tablero.jugadorActual().getOponente().getEquipo());
		 return builder.build();
	}
	
}