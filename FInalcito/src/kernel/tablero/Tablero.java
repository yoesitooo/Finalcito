package kernel.tablero;

import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import kernel.jugador.*;
import kernel.piezas.*;
import java.io.Serializable;


public class Tablero implements Serializable {
	//Atributos (constantes en este caso)
	private static final long serialVersionUID = 1L;
	private final List<Casilla> tableroDeJuego;
	private final Collection<Pieza> piezasBlancas;
	private final Collection<Pieza> piezasNegras;
	private final JugadorBlanco jugadorBlanco;
	private final JugadorNegro jugadorNegro;
	private final Jugador jugadorActual;
	private final Peon peonCapturaAlPaso; // NUEVO: Peón que puede ser capturado al paso
	
	//Constructor dado un builder
	private Tablero(final Builder builder) {
		this.tableroDeJuego=crearTableroDeJuego(builder);
		this.piezasBlancas = calcularPiezasActivas(this.tableroDeJuego, Equipo.BLANCO);
		this.piezasNegras = calcularPiezasActivas(this.tableroDeJuego,Equipo.NEGRO);
		
		// NUEVO: Establecer el peón captura al paso
		this.peonCapturaAlPaso = builder.peonCapturaAlPaso;
		
		final Collection<Movimiento> movimientosStandarBlanco = calcularMovimientosLegales(this.piezasBlancas);
		final Collection<Movimiento> movimientosStandarNegro= calcularMovimientosLegales(this.piezasNegras);
		this.jugadorBlanco= new JugadorBlanco(this, movimientosStandarBlanco, movimientosStandarNegro);
		this.jugadorNegro = new JugadorNegro(this, movimientosStandarBlanco, movimientosStandarNegro);
		this.jugadorActual= builder.movSiguiente.elegirJugador(this.jugadorBlanco, this.jugadorNegro);
	}
	
	// NUEVO: Getter para peón captura al paso
	public Peon getPeonCapturaAlPaso() {
		return this.peonCapturaAlPaso;
	}
	
	//Metodo para calcular los movimientos legales de todo un equipo
	private Collection<Movimiento> calcularMovimientosLegales(Collection<Pieza> piezas) {
		final List<Movimiento> movLegales = new ArrayList<>();
		for(final Pieza pieza : piezas) {
			movLegales.addAll(pieza.movimientosLegales(this));
		}
		return Collections.unmodifiableList(movLegales);
	}
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for(int i=0;i<TableroUtilidades.numCasillas;i++) {
			final String textoCasilla = this.tableroDeJuego.get(i).toString();
			builder.append(String.format("%3s", textoCasilla));
			if(((i+1)%TableroUtilidades.numCasillasPorColumna)==0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	public Jugador jugadorBlanco() {
		return this.jugadorBlanco;
	}
	
	public Jugador jugadorNegro() {
		return this.jugadorNegro;
	}
	
	public Jugador jugadorActual() {
		return this.jugadorActual;
	}
	
	//Metodo que devuelve una lista de las piezas activas en el tablero de determinado equipo
	private static Collection<Pieza> calcularPiezasActivas(final List<Casilla> tableroDeJuego, final Equipo equipo){
		final List<Pieza> piezasActivas = new ArrayList<>();
		for(final Casilla casilla : tableroDeJuego) {
			if(casilla.estaCasillaOcupada()) {
				final Pieza pieza = casilla.getPieza();
				if(pieza.getEquipo()==equipo) {
					piezasActivas.add(pieza);
				}
			}
		}
		return Collections.unmodifiableList(piezasActivas);
	}
	
	//Getter que devuelve la casilla en la coordenada dadá
	public Casilla getCasilla(final int coordenada) {
		return tableroDeJuego.get(coordenada);
	} 
	//Metodo para crear el tablero
	private static List<Casilla> crearTableroDeJuego(final Builder builder){
		final List<Casilla> casillas = new ArrayList<>(TableroUtilidades.numCasillas);
		for(int i = 0; i < TableroUtilidades.numCasillas; i++) {
			casillas.add(Casilla.crearCasilla(i, builder.configTablero.get(i)));
		}
		return Collections.unmodifiableList(casillas);
	}
	//Getter que devueleve las piezas negras
	public Collection<Pieza> getPiezasNegras(){
		return this.piezasNegras;
	}
	//Getter que devuelve las piezas Blancas
	public Collection<Pieza> getPiezasBlancas(){
		return this.piezasBlancas;
	}
	
	//Metodo que crea el tablero en su posicion inicial
	public static Tablero crearTableroStandar() {
		final Builder builder = new Builder();
		//Equipo Negro
		builder.setPieza(new Torre(Equipo.NEGRO,0));
		builder.setPieza(new Caballo(Equipo.NEGRO,1));
		builder.setPieza(new Alfil(Equipo.NEGRO,2));
		builder.setPieza(new Reina(Equipo.NEGRO,3));
		builder.setPieza(new Rey(Equipo.NEGRO,4));
		builder.setPieza(new Alfil(Equipo.NEGRO,5));
		builder.setPieza(new Caballo(Equipo.NEGRO,6));
		builder.setPieza(new Torre(Equipo.NEGRO,7));
		builder.setPieza(new Peon(Equipo.NEGRO,8));
		builder.setPieza(new Peon(Equipo.NEGRO,9));
		builder.setPieza(new Peon(Equipo.NEGRO,10));
		builder.setPieza(new Peon(Equipo.NEGRO,11));
		builder.setPieza(new Peon(Equipo.NEGRO,12));
		builder.setPieza(new Peon(Equipo.NEGRO,13));
		builder.setPieza(new Peon(Equipo.NEGRO,14));
		builder.setPieza(new Peon(Equipo.NEGRO,15));
		//Equipo Blanco
		builder.setPieza(new Peon(Equipo.BLANCO,48));
		builder.setPieza(new Peon(Equipo.BLANCO,49));
		builder.setPieza(new Peon(Equipo.BLANCO,50));
		builder.setPieza(new Peon(Equipo.BLANCO,51));
		builder.setPieza(new Peon(Equipo.BLANCO,52));
		builder.setPieza(new Peon(Equipo.BLANCO,53));
		builder.setPieza(new Peon(Equipo.BLANCO,54));
		builder.setPieza(new Peon(Equipo.BLANCO,55));
		builder.setPieza(new Torre(Equipo.BLANCO,56));
		builder.setPieza(new Caballo(Equipo.BLANCO,57));
		builder.setPieza(new Alfil(Equipo.BLANCO,58));
		builder.setPieza(new Reina(Equipo.BLANCO,59));
		builder.setPieza(new Rey(Equipo.BLANCO,60));
		builder.setPieza(new Alfil(Equipo.BLANCO,61));
		builder.setPieza(new Caballo(Equipo.BLANCO,62));
		builder.setPieza(new Torre(Equipo.BLANCO,63));
		//Mueve primero el Blanco
		builder.setMovimiento(Equipo.BLANCO);
		// NO establecer peonCapturaAlPaso (será null inicialmente)
		return builder.build();
	}
	//Getter de todos los movimientos legales
	public Iterable<Movimiento> getTodosMovimientosLegales() {
	    List<Movimiento> todas = new ArrayList<>();
	    for (Movimiento m : this.jugadorBlanco.getMovLegales()) {
	        todas.add(m);
	    }
	    for (Movimiento m : this.jugadorNegro.getMovLegales()) {
	        todas.add(m);
	    }
	    return Collections.unmodifiableList(todas);
	}

	
	//Clase interna Builder
	public static class Builder{
		//Atributos del Builder
		Map<Integer, Pieza> configTablero;
		Equipo movSiguiente;
		Peon peonCapturaAlPaso;
		public Builder() {
			this.configTablero=new HashMap<>();
		}
		//Setter para establecer una pieza
		public Builder setPieza(final Pieza pieza) {
			this.configTablero.put(pieza.getPosicion(), pieza);
			return this;
		}
		//Setter para estableces el movimiento
		public Builder setMovimiento(final Equipo movSiguiente) {
			this.movSiguiente=movSiguiente;
			return this;
		}
		//Metodo
		public Tablero build() {
			return new Tablero(this);
		}
		public void setCapturaAlPaso(Peon peonMovido) {
			this.peonCapturaAlPaso=peonMovido;
		}
	}
}