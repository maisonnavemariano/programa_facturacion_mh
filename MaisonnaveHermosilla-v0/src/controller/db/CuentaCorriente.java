package controller.db;
 
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CuentaCorriente {
	private Cliente cliente;
	private DoubleProperty estadoCuentaCorriente;
	
	public Cliente getCliente() {
		return cliente;
	}


	public double getEstadoCuentaCorriente() {
		return estadoCuentaCorriente.get();
	}
	
	public CuentaCorriente(Cliente cliente, double estadoCuentaCorriente) {
		this.cliente = cliente;
		this.estadoCuentaCorriente = new SimpleDoubleProperty(estadoCuentaCorriente);
	}
	
	public DoubleProperty montoProperty(){
		return this.estadoCuentaCorriente;
	}
}
