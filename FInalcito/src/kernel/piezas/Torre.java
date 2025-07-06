package kernel.piezas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kernel.jugador.*;
import kernel.tablero.*;

public class Torre extends Pieza {
	private static final long serialVersionUID = 1L;
	//Atributo (en este caso, constante)
	private static final int[] candidatosMovimientos = {-8,-1,1,8};
	//Constructor
	public Torre(final Equipo piezaEquipo,final int posicion) {
		super(TipoDePieza.TORRE, posicion, piezaEquipo);
	}
	
	//Metodo para calcular los movimietntos legales de la torre
	@Override
	public Collection<Movimiento> movimientosLegales(Tablero tablero) {
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
	public Torre muevePieza(final Movimiento movimiento) {
		return new Torre(movimiento.getPiezaMovida().getEquipo(), movimiento.getCoordDestino());
	}
	@Override
	public String toString() {
		return TipoDePieza.TORRE.toString();
	}
	//Metodo para saber si el alfil se encuentra en la primera columna
	private static boolean estaEnPrimeraColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.primeraColumna[posicion] && (posicionCandidata == -1);
	}
	//Metodo para saber si el alfil se encuentra en la octava columna
	private static boolean estaEnOctovaColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.octavaColumna[posicion] && (posicionCandidata == 1);
	}
}
