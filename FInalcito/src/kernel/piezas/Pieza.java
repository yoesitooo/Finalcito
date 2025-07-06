package kernel.piezas;

import java.util.Collection;
import java.io.Serializable;

import kernel.jugador.*;
import kernel.tablero.*;

public abstract class Pieza implements Serializable {
	//Atributos
	private static final long serialVersionUID = 1L;
	protected final int posicion;
	protected final Equipo piezaEquipo;
	protected final boolean esPrimerMovimiento;
	protected final TipoDePieza tipoDePieza;
	private final int cacheHashCode;
	
	//Constructor principal
	public Pieza(final TipoDePieza tipoDePieza, final int posicion, final Equipo piezaEquipo){
		this.piezaEquipo=piezaEquipo;
		this.posicion=posicion;
		this.esPrimerMovimiento=true; // Por defecto es primer movimiento
		this.tipoDePieza=tipoDePieza;
		this.cacheHashCode = calcularHashCode();
	}
	
	//Constructor con esPrimerMovimiento específico
	public Pieza(final TipoDePieza tipoDePieza, final int posicion, final Equipo piezaEquipo, final boolean esPrimerMovimiento){
		this.piezaEquipo=piezaEquipo;
		this.posicion=posicion;
		this.esPrimerMovimiento=esPrimerMovimiento;
		this.tipoDePieza=tipoDePieza;
		this.cacheHashCode = calcularHashCode();
	}
	
	private int calcularHashCode() {
		int resultado = tipoDePieza.hashCode();
		resultado = 31 * resultado + piezaEquipo.hashCode();
		resultado = 31 * resultado + posicion;
		resultado = 31 * resultado + (esPrimerMovimiento ? 1:0);
		return resultado;
	}
	@Override
	public boolean equals(final Object otro) {
		if(this == otro) {
			return true;
		}
		if(!(otro instanceof Pieza)) {
			return false;
		}
		final Pieza otraPieza= (Pieza) otro;
		return posicion == otraPieza.getPosicion() && tipoDePieza== otraPieza.getTipoDePieza() && piezaEquipo == otraPieza.getEquipo() && esPrimerMovimiento == otraPieza.getEsPrimerMovimieto();
	}
	
	@Override
	public int hashCode() {
		return this.cacheHashCode;
	}
	
	
	//Metodo abstracto que dará los movimientos legales para cada pieza
	public abstract Collection<Movimiento> movimientosLegales(final Tablero tablero);
	//
	public abstract Pieza muevePieza(Movimiento movimiento);
	//Getter de la posicion de la pieza
	public int getPosicion() {
		return this.posicion;
	}
	//Getter del equipo de la pieza
	public Equipo getEquipo() {
		return this.piezaEquipo;
	}
	//Getter que retorna el booleano que indicia si la pieza se mueve por primera vez
	public boolean getEsPrimerMovimieto() {
		return esPrimerMovimiento;
	}
	//Getter que retornara el tipo de pieza
	public TipoDePieza getTipoDePieza() {
		return this.tipoDePieza;
	}
}
