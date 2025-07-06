package kernel.tablero;

import kernel.piezas.*;
import kernel.tablero.Tablero.Builder;

public class MovimientoPeonPromocion extends Movimiento {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TipoDePieza piezaPromocion;
    
    public MovimientoPeonPromocion(final Tablero tablero, final Pieza piezaMovida, final int coordDestino, final TipoDePieza piezaPromocion) {
        super(tablero, piezaMovida, coordDestino);
        this.piezaPromocion = piezaPromocion;
    }
    
    @Override
    public Tablero ejecutar() {
        final Builder builder = new Builder();
        
        // Copiar todas las piezas excepto el peón que se mueve
        for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()) {
            if(!this.piezaMovida.equals(pieza)) {
                builder.setPieza(pieza);
            }
        }
        for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()) {
            builder.setPieza(pieza);
        }
        
        // Agregar la pieza promocionada
        builder.setPieza(crearPiezaPromocionada());
        builder.setMovimiento(this.tablero.jugadorActual().getOponente().getEquipo());
        
        return builder.build();
    }
    
    private Pieza crearPiezaPromocionada() {
        switch(this.piezaPromocion) {
            case REINA: return new Reina(this.piezaMovida.getEquipo(), this.coordDestino);
            case TORRE: return new Torre(this.piezaMovida.getEquipo(), this.coordDestino);
            case ALFIL: return new Alfil(this.piezaMovida.getEquipo(), this.coordDestino);
            case CABALLO: return new Caballo(this.piezaMovida.getEquipo(), this.coordDestino);
            default: return new Reina(this.piezaMovida.getEquipo(), this.coordDestino);
        }
    }
    
    public TipoDePieza getPiezaPromocion() {
        return this.piezaPromocion;
    }
}
