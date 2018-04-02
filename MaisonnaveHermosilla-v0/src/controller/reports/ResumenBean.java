package controller.reports;

import java.util.List;

import controller.db.Cliente;

public class ResumenBean {
	protected String desde;
	protected String hasta;
	protected Cliente cliente;
	protected List<TransaccionBean> transacciones;
	
 	public ResumenBean(Cliente cliente, String desde, String hasta, List<TransaccionBean> transacciones) {
 		this.cliente       = cliente;
 		this.desde         = desde;
 		this.hasta         = hasta;
 		this.transacciones = transacciones;
 	}
 	
 	public String getDesde() {
 		return this.desde;
 	}
	public String getHasta() {
		return this.hasta;
	}
	public String getDenominacion() {
		return this.cliente.getDenominacion();
	}
	public String getCUIT() {
		return this.cliente.getCuit();
	}
	
	public List<TransaccionBean> getTransacciones(){
		return this.transacciones;
	}

}
