package controller.db;

public class CuentaCorriente {
	private Cliente cliente;
	private double  estadoCuentaCorriente;
	
	public Cliente getCliente() {
		return cliente;
	}


	public double getEstadoCuentaCorriente() {
		return estadoCuentaCorriente;
	}
	
	public CuentaCorriente(Cliente cliente, double estadoCuentaCorriente) {
		this.cliente = cliente;
		this.estadoCuentaCorriente = estadoCuentaCorriente;
	}
}
