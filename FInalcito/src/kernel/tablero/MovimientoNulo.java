package kernel.tablero;


public class MovimientoNulo extends Movimiento{
	private static final long serialVersionUID = 1L;

	public MovimientoNulo() {
		super(null,null,-1);
	}
	
	@Override
	public Tablero ejecutar() {
		throw new RuntimeException("No se puede ejecutar un movimiento nulo");
	}
}

