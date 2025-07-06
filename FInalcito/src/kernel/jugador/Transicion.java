package kernel.jugador;

import kernel.tablero.*;
import java.io.Serializable; // NUEVO: Importar Serializable

public class Transicion implements Serializable { // NUEVO: Implementar Serializable
	
	private static final long serialVersionUID = 1L; // NUEVO: Version ID para serialización
	
	private final Tablero tableroTransicion;
	private final StatusDeMovimiento statusDeMovimiento;
	
	public Transicion(final Tablero tableroTransicion, final Movimiento movimiento, final StatusDeMovimiento statusDeMovimiento) {
		this.tableroTransicion=tableroTransicion;
		this.statusDeMovimiento=statusDeMovimiento;
	}
	
	public StatusDeMovimiento getStatusDeMovimiento() {
		return this.statusDeMovimiento;
	}
	
	public Tablero getTableroTransicion() {
	    return this.tableroTransicion;
	}
}