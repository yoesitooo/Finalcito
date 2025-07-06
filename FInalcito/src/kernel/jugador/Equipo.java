package kernel.jugador;

public enum Equipo {
	BLANCO{
		@Override
		public int getDireccion() {
			return -1;
		}
		@Override
		public boolean esBlanca(){
			return true;
		}
		@Override
		public boolean esNegra(){
			return false;
		}
		@Override
		public Jugador elegirJugador(final JugadorBlanco jugadorBlanco, final JugadorNegro jugadorNegro) {
			return jugadorBlanco;
		}
	},
	NEGRO{
		@Override
		public int getDireccion() {
			return 1;
		}
		@Override
		public boolean esBlanca() {
			return false;
		}
		@Override
		public boolean esNegra() {
			return true;
		}
		@Override
		public Jugador elegirJugador(final JugadorBlanco jugadorBlanco, final JugadorNegro jugadorNegro) {
			return jugadorNegro;
		}
	};
	
	
	public abstract int getDireccion();
	public abstract boolean esBlanca();
	public abstract boolean esNegra();
	public abstract Jugador elegirJugador(JugadorBlanco jugadorBlanco, JugadorNegro jugadorNegro);
}
