package kernel.jugador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kernel.piezas.*;
import kernel.tablero.*;


public class JugadorNegro extends Jugador {

	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización

	public JugadorNegro(final Tablero tablero, final Collection<Movimiento> movimientosStandarBlanco, final Collection<Movimiento> movimientosStandarNegro) {
		super(tablero, movimientosStandarNegro, movimientosStandarBlanco);
	}
	@Override
	public Collection<Pieza> getPiezasActivas(){
		return this.tablero.getPiezasNegras();
	}
	@Override
	public Equipo getEquipo() {
		return Equipo.NEGRO;
	}
	@Override
	public Jugador getOponente() {
		return this.tablero.jugadorBlanco();
	}
	@Override
	protected Collection<Movimiento> calcularEnroques(final Collection<Movimiento> legalesJugador,final Collection<Movimiento> legalesOponente) {
		final List<Movimiento> reyEnroques= new ArrayList<>();
		if(this.reyDelJugador.getEsPrimerMovimieto() && !this.estaEnJaque()) {
			//Enroque corto (del lado del rey)
			if(!this.tablero.getCasilla(5).estaCasillaOcupada() && !this.tablero.getCasilla(6).estaCasillaOcupada()) {
				final Casilla torreCasilla = this.tablero.getCasilla(7);
				if(torreCasilla.estaCasillaOcupada() && torreCasilla.getPieza().getEsPrimerMovimieto()) {
					if(Jugador.calcularAtaquesEnCasilla(5, legalesOponente).isEmpty() && Jugador.calcularAtaquesEnCasilla(6, legalesOponente).isEmpty() && torreCasilla.getPieza().getTipoDePieza().esTorre()) {
						reyEnroques.add(new MovimientoEnroqueCorto(this.tablero, this.reyDelJugador, 6, (Torre)torreCasilla.getPieza(), torreCasilla.getCoordCasilla(), 5));	
					}
					
				}
			}
			//Enroque largo (Del lado de la reina) - CORREGIDO
			if(!this.tablero.getCasilla(1).estaCasillaOcupada() && !this.tablero.getCasilla(2).estaCasillaOcupada() && !this.tablero.getCasilla(3).estaCasillaOcupada()) {
				final Casilla torreCasilla = this.tablero.getCasilla(0);
				if(torreCasilla.estaCasillaOcupada() && torreCasilla.getPieza().getEsPrimerMovimieto()) {
					// CORREGIDO: Verificar que TODAS las casillas por donde pasa el rey no están atacadas
					if(Jugador.calcularAtaquesEnCasilla(3, legalesOponente).isEmpty() && // d8
					   Jugador.calcularAtaquesEnCasilla(2, legalesOponente).isEmpty() && // c8 - ¡ESTA FALTABA!
					   torreCasilla.getPieza().getTipoDePieza().esTorre()) {
						
						reyEnroques.add(new MovimientoEnroqueLargo(this.tablero, this.reyDelJugador, 2, (Torre)torreCasilla.getPieza(), torreCasilla.getCoordCasilla(), 3));
					}
				}
			}
		}
		return Collections.unmodifiableList(reyEnroques);
	}
	

}