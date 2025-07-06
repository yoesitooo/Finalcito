package kernel.tablero;

public class FabricarMovimiento {
	private FabricarMovimiento() {
		throw new RuntimeException("No es instansiable");
	}
	
	public static Movimiento crearMovimiento(final Tablero tablero, final int coordActual, final int coordDestino) {
		for(final Movimiento movimiento : tablero.getTodosMovimientosLegales()) {
			if(movimiento.getCoordActual() == coordActual && movimiento.getCoordDestino() == coordDestino) {
				return movimiento;
			}
		}
		return Movimiento.MOVIMIENTO_NULO;
	}
}
