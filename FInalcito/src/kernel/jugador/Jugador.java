package kernel.jugador;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable; // NUEVO: Importar Serializable

import kernel.piezas.*;
import kernel.tablero.*;

public abstract class Jugador implements Serializable { // NUEVO: Implementar Serializable
	
	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización
	
	protected final Tablero tablero;
	protected final Rey reyDelJugador;
	protected final Collection<Movimiento> movLegales;
	private final boolean estaEnJaque;
	
	Jugador(final Tablero tablero, final Collection<Movimiento> movLegales, final Collection<Movimiento> movimientosOponente ){
		this.tablero=tablero;
		this.reyDelJugador=establecerRey();
		List<Movimiento> todosLosMovimientos = new ArrayList<>();
		for (Movimiento m : movLegales) {
		    todosLosMovimientos.add(m);
		}
		for (Movimiento m : calcularEnroques(movLegales, movimientosOponente)) {
		    todosLosMovimientos.add(m);
		}
		this.movLegales = Collections.unmodifiableList(todosLosMovimientos);

		this.estaEnJaque=!Jugador.calcularAtaquesEnCasilla(this.reyDelJugador.getPosicion(), movimientosOponente).isEmpty();
	}
	
	protected static Collection<Movimiento> calcularAtaquesEnCasilla(final int posicion, final Collection<Movimiento> movimientosOponente){
		final List<Movimiento> movimientosDeAtaque = new ArrayList<>();
		for (final Movimiento movimiento : movimientosOponente) {
			if(posicion == movimiento.getCoordDestino()) {
				movimientosDeAtaque.add(movimiento);
			}
		}
		return Collections.unmodifiableList(movimientosDeAtaque);
	}
	
	private Rey establecerRey() {
		for(final Pieza pieza : getPiezasActivas()) {
			if(pieza.getTipoDePieza().esRey()) {
				return (Rey)pieza;
			}
		}
		throw new RuntimeException("No deberia llegar aqui! No es un tablero valido");
	}
	
	public boolean esMovimientoLegal(Movimiento movimiento) {
		return this.movLegales.contains(movimiento);
	}
	
	public boolean estaEnJaque() {
		return this.estaEnJaque;
	}
	public boolean estaEnMate() {
		return this.estaEnJaque && !tieneMovimientosDeEscape();
	}
	public boolean estaAhogado() {
		return !this.estaEnJaque && !tieneMovimientosDeEscape();
	}
	//TODO aun me falta ver la lógica de estos tres métodos
	public boolean estaEnrocado() {
		return false;
	}
	public Transicion hacerMovimiento(final Movimiento movimiento) {
		if(!esMovimientoLegal(movimiento)) {
			return new Transicion(this.tablero, movimiento, StatusDeMovimiento.MOVIMIENTO_ILEGAL);
		}
		
		final Tablero tableroTransicion = movimiento.ejecutar();
		final Collection<Movimiento>  ataquesAlRey= Jugador.calcularAtaquesEnCasilla(tableroTransicion.jugadorActual().getOponente().getReyDelJugador().getPosicion(), tableroTransicion.jugadorActual().getMovLegales());
		if(!ataquesAlRey.isEmpty()) {
			return new Transicion(this.tablero, movimiento, StatusDeMovimiento.DEJA_A_JUGADOR_EN_JAQUE);
		}
		
		return new Transicion(tableroTransicion, movimiento, StatusDeMovimiento.HECHO);
	}
	protected boolean tieneMovimientosDeEscape() {
		for(final Movimiento movimiento : this.movLegales) {
			final Transicion transicion= hacerMovimiento(movimiento);
			if(transicion.getStatusDeMovimiento().estaHecho()) {
				return true;
			}
		}
		return false;
	}
	
	public Rey getReyDelJugador() {
		return this.reyDelJugador;
	}
	
	public Collection<Movimiento> getMovLegales(){
		return this.movLegales;
	}
	
	public abstract Collection<Pieza> getPiezasActivas();
	public abstract Equipo getEquipo();
	public abstract Jugador getOponente();
	protected abstract Collection<Movimiento> calcularEnroques(Collection<Movimiento> legalesJugador, Collection<Movimiento> legalesOponente);
}