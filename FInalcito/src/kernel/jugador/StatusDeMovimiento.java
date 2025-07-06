package kernel.jugador;



public enum StatusDeMovimiento {
	
	DEJA_A_JUGADOR_EN_JAQUE {
		@Override
		public boolean estaHecho() {
			return false;
		}
	},
	MOVIMIENTO_ILEGAL {
		@Override
		public boolean estaHecho() {
			return false;
		}
	},
	HECHO {
		@Override
		public boolean estaHecho() {
			return true;
		}
	};
	public abstract boolean estaHecho();
}
