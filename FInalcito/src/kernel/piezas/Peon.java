package kernel.piezas;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import kernel.jugador.*;
import kernel.tablero.*;

public class Peon extends Pieza {
	private static final long serialVersionUID = 1L;
	//Atributo (en este caso, constante)
	private final static int[] candidatosMovimientos= {7, 8, 9, 16};
	//Constructor
	public Peon(final Equipo piezaEquipo,final int posicion) {
		super(TipoDePieza.PEON, posicion, piezaEquipo);
	}
	//Constructor con primer movimiento (para copias/promociones)
	public Peon(final Equipo piezaEquipo, final int posicion, final boolean esPrimerMovimiento) {
		super(TipoDePieza.PEON, posicion, piezaEquipo, esPrimerMovimiento);
	}
	
	//Metodo para calcular los movimientos legales del peon
	@Override
	public Collection<Movimiento> movimientosLegales(final Tablero tablero) {
		final List<Movimiento> movLegales = new ArrayList<>();
		
		for(final int candidato : candidatosMovimientos) {
			final int coordCandidata = this.posicion + (this.piezaEquipo.getDireccion() * candidato);
			
			if(!TableroUtilidades.coordCasillaEsValida(coordCandidata)) {
				continue;
			}
			
			// MOVIMIENTO NORMAL HACIA ADELANTE (1 casilla)
			if(candidato == 8 && !tablero.getCasilla(coordCandidata).estaCasillaOcupada()) {
				// Verificar si es promoción
				if(esPromocion(coordCandidata)) {
					// TODO: Agregar movimientos de promoción (Reina, Torre, Alfil, Caballo)
					movLegales.add(new MovimientoPeonPromocion(tablero, this, coordCandidata, TipoDePieza.REINA));
				} else {
					movLegales.add(new MovimientoPeon(tablero, this, coordCandidata));
				}
			}
			// MOVIMIENTO INICIAL DE 2 CASILLAS
			else if(candidato == 16 && this.getEsPrimerMovimieto() && 
				   ((TableroUtilidades.segundaFila[this.posicion] && this.piezaEquipo.esNegra()) || 
				    (TableroUtilidades.septimaFila[this.posicion] && this.piezaEquipo.esBlanca()))) {
				
				final int detrasCoordCandidata = this.posicion + (this.getEquipo().getDireccion() * 8);
				if(!tablero.getCasilla(detrasCoordCandidata).estaCasillaOcupada() && 
				   !tablero.getCasilla(coordCandidata).estaCasillaOcupada()) {
					movLegales.add(new MovimientoPeonSalto(tablero, this, coordCandidata));
				}
			}
			// CAPTURA DIAGONAL IZQUIERDA
			else if(candidato == 7 && 
				   !((TableroUtilidades.octavaColumna[this.posicion] && this.piezaEquipo.esBlanca()) ||
				     (TableroUtilidades.primeraColumna[this.posicion] && this.piezaEquipo.esNegra()))) {
				
				if(tablero.getCasilla(coordCandidata).estaCasillaOcupada()) {
					final Pieza piezaEnCandidata = tablero.getCasilla(coordCandidata).getPieza();
					if(this.piezaEquipo != piezaEnCandidata.getEquipo()) {
						// Verificar si es promoción
						if(esPromocion(coordCandidata)) {
							movLegales.add(new MovimientoPeonPromocianAtaque(tablero, this, coordCandidata, piezaEnCandidata, TipoDePieza.REINA));
						} else {
							movLegales.add(new MovimientoPeonAtaque(tablero, this, coordCandidata, piezaEnCandidata));
						}
					}
				} else {
					// Verificar captura al paso - DIRECCIÓN DEPENDE DEL COLOR
					int direccion = this.piezaEquipo.esBlanca() ? +1 : -1;
					final Movimiento movimientoCapturaAlPaso = verificarCapturaAlPaso(tablero, coordCandidata, direccion);
					if(movimientoCapturaAlPaso != null) {
						movLegales.add(movimientoCapturaAlPaso);
					}
				}
			}
			// CAPTURA DIAGONAL DERECHA  
			else if(candidato == 9 && 
				   !((TableroUtilidades.primeraColumna[this.posicion] && this.piezaEquipo.esBlanca()) ||
				     (TableroUtilidades.octavaColumna[this.posicion] && this.piezaEquipo.esNegra()))) {
				
				if(tablero.getCasilla(coordCandidata).estaCasillaOcupada()) {
					final Pieza piezaEnCandidata = tablero.getCasilla(coordCandidata).getPieza();
					if(this.piezaEquipo != piezaEnCandidata.getEquipo()) {
						// Verificar si es promoción
						if(esPromocion(coordCandidata)) {
							movLegales.add(new MovimientoPeonPromocianAtaque(tablero, this, coordCandidata, piezaEnCandidata, TipoDePieza.REINA));
						} else {
							movLegales.add(new MovimientoPeonAtaque(tablero, this, coordCandidata, piezaEnCandidata));
						}
					}
				} else {
					// Verificar captura al paso - DIRECCIÓN DEPENDE DEL COLOR
					int direccion = this.piezaEquipo.esBlanca() ? -1 : +1;
					final Movimiento movimientoCapturaAlPaso = verificarCapturaAlPaso(tablero, coordCandidata, direccion);
					if(movimientoCapturaAlPaso != null) {
						movLegales.add(movimientoCapturaAlPaso);
					}
				}
			}
		}
		return movLegales;
	}
	
	/**
	 * Verifica si el peón puede hacer captura al paso
	 */
	private Movimiento verificarCapturaAlPaso(final Tablero tablero, final int coordDestino, final int direccion) {
		// Verificar si estamos en la fila correcta para captura al paso
		if((this.piezaEquipo.esBlanca() && TableroUtilidades.cuartaFila != null && TableroUtilidades.cuartaFila[this.posicion]) ||
		   (this.piezaEquipo.esNegra() && TableroUtilidades.quintaFila != null && TableroUtilidades.quintaFila[this.posicion])) {
			
			// Posición del peón enemigo que podría haber saltado
			final int posicionPeonEnemigo = this.posicion + direccion;
			
			if(TableroUtilidades.coordCasillaEsValida(posicionPeonEnemigo)) {
				final Casilla casillaPeonEnemigo = tablero.getCasilla(posicionPeonEnemigo);
				
				if(casillaPeonEnemigo.estaCasillaOcupada()) {
					final Pieza piezaAdyacente = casillaPeonEnemigo.getPieza();
					
					// Verificar que sea un peón enemigo que acaba de saltar 2 casillas
					if(piezaAdyacente.getTipoDePieza() == TipoDePieza.PEON && 
					   piezaAdyacente.getEquipo() != this.piezaEquipo) {
						
						// Verificar si este peón puede ser capturado al paso
						// (esto requeriría información del último movimiento del tablero)
						// Por ahora, implementación básica
						return new MovimientoPeonCapturaAlPaso(tablero, this, coordDestino, piezaAdyacente);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Verifica si el movimiento resulta en promoción
	 */
	private boolean esPromocion(final int coordDestino) {
		return (this.piezaEquipo.esBlanca() && TableroUtilidades.primeraFila[coordDestino]) ||
			   (this.piezaEquipo.esNegra() && TableroUtilidades.octavaFila[coordDestino]);
	}
	
	@Override
	public Peon muevePieza(final Movimiento movimiento) {
		return new Peon(movimiento.getPiezaMovida().getEquipo(), movimiento.getCoordDestino(), false);
	}
	
	@Override
	public String toString() {
		return TipoDePieza.PEON.toString();
	}
}