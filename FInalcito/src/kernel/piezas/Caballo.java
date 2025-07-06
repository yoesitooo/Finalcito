package kernel.piezas;

import java.util.List;

import kernel.jugador.*;
import kernel.tablero.*;

import java.util.ArrayList;
import java.util.Collection;

public class Caballo extends Pieza{
	private static final long serialVersionUID = 1L;
	//Atributo(en este caso, constante)
	private final static int[] candidatosMovimientos= {-17,-15,-10,-6,6,10,15,17};
	//Constructor
	public Caballo(final Equipo piezaEquipo,final int posicion){
		super(TipoDePieza.CABALLO, posicion, piezaEquipo);
	}
	//Método para calcular los movimientos legales del caballo
	@Override
	public Collection<Movimiento> movimientosLegales(Tablero tablero) {
		List<Movimiento> movLegales = new ArrayList<>();
		
		for(final int candidato : candidatosMovimientos) {
			final int coordCandidata;
			coordCandidata= this.posicion+candidato;
			if(TableroUtilidades.coordCasillaEsValida(coordCandidata)) {
				if(estaEnPrimeraColumna(this.posicion, candidato) || estaEnSegundaColumna(this.posicion, candidato) || estaEnSeptimaColumna(this.posicion, candidato) || estaEnOcatavaColumna(this.posicion, candidato)) {
					continue;
				}
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
	public Caballo muevePieza(final Movimiento movimiento) {
		return new Caballo(movimiento.getPiezaMovida().getEquipo(), movimiento.getCoordDestino());
	}
	@Override
	public String toString() {
		return TipoDePieza.CABALLO.toString();
	}
	
	//Metodo para saber si el caballo se encuentra en la primera columna
	private static boolean estaEnPrimeraColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.primeraColumna[posicion] && (posicionCandidata==-17||posicionCandidata==-10||posicionCandidata==6||posicionCandidata==15);
	}
	//Metodo para saber si el caballo se encuentra en la segunda columna
	private static boolean estaEnSegundaColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.segundaColumna[posicion] && (posicionCandidata==-10 || posicionCandidata==6);
	}
	//Metodo para saber si el caballo se encuentra en la septima columna
	private static boolean estaEnSeptimaColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.septimaColumna[posicion] && (posicionCandidata==-6 || posicionCandidata==10);
	}
	//Metodo para saber si el caballo se encuentra en la octava columna
	private static boolean estaEnOcatavaColumna(final int posicion, final int posicionCandidata) {
		return TableroUtilidades.octavaColumna[posicion] && (posicionCandidata==-15 || posicionCandidata==-6 || posicionCandidata==10 || posicionCandidata==17);
	}
}
