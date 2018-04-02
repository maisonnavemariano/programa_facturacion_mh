package controller.reports;

import java.util.ArrayList;
import java.util.List;

import controller.db.Cliente;
import controller.db.Transaccion;

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
 	
 	public static List<TransaccionBean> TransaccionToTransaccionBean(List<Transaccion> lista){
 		
 		List<TransaccionBean> l = new ArrayList<TransaccionBean>();
 		
 		
 		for (Transaccion t : lista) {
 			double debe        = 0;
 			double haber       = 0;
 			double saldo       = t.getEstadoCuentaCorriente();
 			if (t.getEvento()=='C') { //credito (pago)
 				debe = t.getMonto(); 
 			}
 			else if (t.getEvento()=='P') { //Presupuesto
 				haber = t.getMonto();
 			}
 			String descripcion = t.getObservacion();
 			String fecha       = t.getFecha();
 			l.add(new TransaccionBean(fecha,descripcion, debe,haber,saldo));
 		}
 		return l;
 		
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
