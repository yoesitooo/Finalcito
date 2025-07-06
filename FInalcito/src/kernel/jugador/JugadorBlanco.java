package kernel.jugador;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import kernel.piezas.*;
import kernel.tablero.*;


public class JugadorBlanco extends Jugador {

	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización

	public JugadorBlanco(final Tablero tablero, final Collection<Movimiento> movimientosStandarBlanco, final Collection<Movimiento> movimientosStandarNegro) {
		super(tablero, movimientosStandarBlanco, movimientosStandarNegro);
	}
	@Override
	public Collection<Pieza> getPiezasActivas() {
		return this.tablero.getPiezasBlancas();
	}
	@Override
	public Equipo getEquipo() {
		return Equipo.BLANCO;
	}
	@Override
	public Jugador getOponente() {
		return this.tablero.jugadorNegro();
	}
	@Override
	protected Collection<Movimiento> calcularEnroques(final Collection<Movimiento> legalesJugador,final Collection<Movimiento> legalesOponente) {
		final List<Movimiento> reyEnroques= new ArrayList<>();
		if(this.reyDelJugador.getEsPrimerMovimieto() && !this.estaEnJaque()) {
			//Enroque corto (del lado del rey)
			if(!this.tablero.getCasilla(61).estaCasillaOcupada() && !this.tablero.getCasilla(62).estaCasillaOcupada()) {
				final Casilla torreCasilla = this.tablero.getCasilla(63);
				if(torreCasilla.estaCasillaOcupada() && torreCasilla.getPieza().getEsPrimerMovimieto()) {
					if(Jugador.calcularAtaquesEnCasilla(61, legalesOponente).isEmpty() && Jugador.calcularAtaquesEnCasilla(62, legalesOponente).isEmpty() && torreCasilla.getPieza().getTipoDePieza().esTorre()) {
						reyEnroques.add(new MovimientoEnroqueCorto(this.tablero, this.reyDelJugador, 62, (Torre)torreCasilla.getPieza(), torreCasilla.getCoordCasilla(), 61));	
					}
					
				}
			}
			//Enroque largo (del lado de la reina) - CORREGIDO
			if(!this.tablero.getCasilla(59).estaCasillaOcupada() && !this.tablero.getCasilla(58).estaCasillaOcupada() && !this.tablero.getCasilla(57).estaCasillaOcupada()) {
				final Casilla torreCasilla = this.tablero.getCasilla(56);
				if(torreCasilla.estaCasillaOcupada() && torreCasilla.getPieza().getEsPrimerMovimieto()) {
					// CORREGIDO: Verificar que TODAS las casillas por donde pasa el rey no están atacadas
					if(Jugador.calcularAtaquesEnCasilla(59, legalesOponente).isEmpty() && // d1
					   Jugador.calcularAtaquesEnCasilla(58, legalesOponente).isEmpty() && // c1 - ¡ESTA FALTABA!
					   torreCasilla.getPieza().getTipoDePieza().esTorre()) {
						
						reyEnroques.add(new MovimientoEnroqueLargo(this.tablero, this.reyDelJugador, 58, (Torre)torreCasilla.getPieza(), torreCasilla.getCoordCasilla(), 59));
					}
				}
			}
		}
		return Collections.unmodifiableList(reyEnroques);
	}

}