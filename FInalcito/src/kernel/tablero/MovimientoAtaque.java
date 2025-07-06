package kernel.tablero;


import kernel.piezas.*;
public class MovimientoAtaque extends Movimiento {
	private static final long serialVersionUID = 1L;
	//Atributo (en este caso, constante)
	final Pieza piezaAtacada;
	//Constructor
	public MovimientoAtaque(final Tablero tablero,final Pieza piezaMovida,final int coordDestino, final Pieza piezaAtacada) {
		super(tablero, piezaMovida, coordDestino);
		this.piezaAtacada=piezaAtacada;
	}
	@Override
	public int hashCode() {
		return this.piezaAtacada.hashCode()+super.hashCode();
	}
	@Override
	public boolean equals(final Object otro) {
		if(this==otro) {
			return true;
		}
		if(!(otro instanceof MovimientoAtaque)) {
			return false;
		}
		final MovimientoAtaque otroMovimientoAtaque = (MovimientoAtaque) otro;
		return super.equals(otroMovimientoAtaque) && getPiezaAtacada().equals(otroMovimientoAtaque.getPiezaAtacada());
	}
	
	@Override
	public Tablero ejecutar() {
		final Tablero.Builder builder = new Tablero.Builder();
	    
	    // 1. Copiar todas las piezas del jugador actual EXCEPTO la que se mueve
	    for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()) {
	        if(!this.piezaMovida.equals(pieza)) {
	            builder.setPieza(pieza);
	        }
	    }
	    
	    // 2. Copiar todas las piezas del oponente EXCEPTO la que será capturada
	    for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()) {
	        if(!pieza.equals(this.piezaAtacada)) {
	            builder.setPieza(pieza);
	        }
	    }
	    
	    // 3. Colocar la pieza atacante en su nueva posición
	    builder.setPieza(this.piezaMovida.muevePieza(this));
	    
	    // 4. Cambiar el turno al oponente
	    builder.setMovimiento(this.tablero.jugadorActual().getOponente().getEquipo());
	    
	    // 5. Construir y retornar el nuevo tablero
	    return builder.build();
	}
	
	@Override
	public boolean esAtaque() {
		return true;
	}
	@Override
	public Pieza getPiezaAtacada() {
		return this.piezaAtacada;
	}
}
