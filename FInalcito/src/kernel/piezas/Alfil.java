package kernel.piezas;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import kernel.jugador.*;
import kernel.tablero.*;

public class Alfil extends Pieza {
	private static final long serialVersionUID = 1L;
	//Atributo (en este caso, constante)
	private final static int[] candidatosMovimientos = {-9,-7,7,9};
	//Constructor
	public Alfil(final Equipo piezaEquipo,final int posicion) {
		super(TipoDePieza.ALFIL, posicion, piezaEquipo);
	}
	//Metodo para calcular los movimientos legales del alfil
	@Override
	public Collection<Movimiento> movimientosLegales(final Tablero tablero) {
		final List<Movimiento> movLegales = new ArrayList<>(); 
		for(final int candidato : candidatosMovimientos) {
			int coordCandidata = this.posicion;
			while(TableroUtilidades.coordCasillaEsValida(coordCandidata)) {
				
				if(estaEnPrimeraColumna(coordCandidata,candidato) || estaEnOctovaColumna(coordCandidata, candidato)) {
					break;
				}
				coordCandidata += candidato;
				if(TableroUtilidades.coordCasillaEsValida(coordCandidata)) {
					final Casilla casillaCandidata = tablero.getCasilla(coordCandidata);
					if(!casillaCandidata.estaCasillaOcupada()) {
						movLegales.add(new MovimientoNormal(tablero, this, coordCandidata));
					}else {
						final Pieza piezaDestino = casillaCandidata.getPieza();
						final Equipo equipoPiezaDestino = piezaDestino.getEquipo();
						if(this.piezaEquipo!=equipoPiezaDestino) {
							movLegales.add(new MovimientoAtaque(tablero, this, coordCandidata, piezaDestino	));
						}
						break;
					}
				}
			}
		}
		return movLegales;
	}
	@Override
	public Alfil muevePieza(final Movimiento movimiento) {
		return new Alfil(movimiento.getPiezaMovida().getEquipo(), movimiento.getCoordDestino());
	}
	
	@Override
	public String toString() {
		return TipoDePieza.ALFIL.toString();
	}
	
	//Metodo para saber si el alfil se encuentra en la primera columna
	private static boolean estaEnPrimeraColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.primeraColumna[posicion] && (posicionCandidata == -9 || posicionCandidata== 7);
	}
	//Metodo para saber si el alfil se encuentra en la octava columna
	private static boolean estaEnOctovaColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.octavaColumna[posicion] && (posicionCandidata == -7 || posicionCandidata == 9);
	}
}
