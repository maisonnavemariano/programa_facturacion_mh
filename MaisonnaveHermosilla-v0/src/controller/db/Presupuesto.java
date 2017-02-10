package controller.db;

import java.util.List;
import java.util.Date;

public class Presupuesto {
	protected final int PRESUPUESTO_INVALIDO = -1;
	
	protected int Nro_Presupuesto;
	protected List<Concepto> conceptos;
	protected Cliente cliente;
	protected boolean efectivo;
	protected float alicuota;
	protected double monto_total;
	protected Date fecha;
	
	
	public Presupuesto(List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double monto_total, Date fecha){
		Nro_Presupuesto = PRESUPUESTO_INVALIDO;
		this.conceptos = conceptos;
		this.cliente = cliente;
		this.efectivo = efectivo;
		this.alicuota = alicuota;
		this.monto_total = monto_total;
		this.fecha = fecha;
	}
	
	public List<Concepto> getConceptos(){
		return conceptos;
	}
	
	public void actualizarNroPresupuesto(int Nro_Presupuesto){
		this.Nro_Presupuesto = Nro_Presupuesto;
	}
	
	public boolean hasValidNumber(){
		return Nro_Presupuesto != PRESUPUESTO_INVALIDO;
	}
	
	public String toString(){
		return "Nro_Prespuesto: "+Nro_Presupuesto+" cliente: "+cliente.toString()+" Monto total: "+monto_total+ " fecha: "+fecha.toString()+" conceptos: "+conceptos.toString();
	}
	
	public int getNroPresupuesto(){
		return Nro_Presupuesto;
	}
	
	public Cliente getCliente(){
		return cliente;
	}
	public Date getFecha(){
		return fecha;
	}
	public boolean efectivo(){
		return efectivo;
	}
	public float getAlicuota(){
		return alicuota;
	}
	public double getMontoTotal(){
		return monto_total;
	}
}
