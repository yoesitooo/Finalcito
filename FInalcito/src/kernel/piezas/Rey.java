package kernel.piezas;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import kernel.jugador.*;
import kernel.tablero.*;
public class Rey extends Pieza {
	private static final long serialVersionUID = 1L;
	//Atributo (en este caso, una constante)
	private final static int[] candidatosMovimientos = {-9,-8,-7,-1,1,7,8,9};
	//Constructor
	public Rey(final Equipo piezaEquipo, final int posicion) {
		super(TipoDePieza.REY, posicion, piezaEquipo);
	}
	//Metodo para calcular los movimientos legales del rey
	@Override
	public Collection<Movimiento> movimientosLegales(Tablero tablero) {
		final List<Movimiento> movLegales = new ArrayList<>();
		for(final int candidato : candidatosMovimientos) {
			final int coordCandidata = this.posicion+candidato;
			if(estaEnPrimeraColumna(this.posicion, candidato) || estaEnOctavaColumna(this.posicion, candidato)) {
				continue;
			}
			if(TableroUtilidades.coordCasillaEsValida(coordCandidata)) {
				final Casilla casillaCandidata = tablero.getCasilla(coordCandidata);
				if(!casillaCandidata.estaCasillaOcupada()) {
					movLegales.add(new MovimientoNormal(tablero,this,coordCandidata));
				}else {
					final Pieza piezaDestino = casillaCandidata.getPieza();
					final Equipo equipoPiezaDestino= piezaDestino.getEquipo();
					
					if(this.piezaEquipo!=equipoPiezaDestino) {
						movLegales.add(new  MovimientoAtaque(tablero,this,coordCandidata,piezaDestino));
					}
				}
			}
		}
		return movLegales;
	}
	@Override
	public Rey muevePieza(final Movimiento movimiento) {
		return new Rey(movimiento.getPiezaMovida().getEquipo(), movimiento.getCoordDestino());
	}
	@Override
	public String toString() {
		return TipoDePieza.REY.toString();
	}
	
	//Metodo para saber si el caballo se encuentra en la primera columna
	private static boolean estaEnPrimeraColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.primeraColumna[posicion] && (posicionCandidata==-9||posicionCandidata==-1||posicionCandidata==7);
	}
	//Metodo para saber si el caballo se encuentra en la segunda columna
	private static boolean estaEnOctavaColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.octavaColumna[posicion] && (posicionCandidata==-7 || posicionCandidata==1 || posicionCandidata==9);
	}
}
