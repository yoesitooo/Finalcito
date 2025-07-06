package kernel.piezas;

public enum TipoDePieza {
	
	PEON("P") {
		@Override
		public boolean esRey() {
			return false;
		}

		@Override
		public boolean esTorre() {
			return false;
		}
	},
	CABALLO("N") {
		@Override
		public boolean esRey() {
			return false;
		}

		@Override
		public boolean esTorre() {
			return false;
		}
	},
	ALFIL("B") {
		@Override
		public boolean esRey() {
			return false;
		}

		@Override
		public boolean esTorre() {
			return false;
		}
	},
	TORRE("R") {
		@Override
		public boolean esRey() {
			return false;
		}

		@Override
		public boolean esTorre() {
			return true;
		}
	},
	REINA("Q") {
		@Override
		public boolean esRey() {
			return false;
		}

		@Override
		public boolean esTorre() {
			return false;
		}
	},
	REY("K") {
		@Override
		public boolean esRey() {
			return true;
		}

		@Override
		public boolean esTorre() {
			return false;
		}
	};
	
	private String nombreDePieza;
	
	TipoDePieza(final String nombreDePieza){
		this.nombreDePieza=nombreDePieza;
	}
	@Override
	public String toString() {
		return this.nombreDePieza;
	}
	public abstract boolean esRey();
	public abstract boolean esTorre();
}
