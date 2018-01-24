package controller.db;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

//TODO: hacer con properties como esta hecho el cliente.
public class Presupuesto {
	protected final int PRESUPUESTO_INVALIDO = -1;
	protected final int NRO_TRANSACCION_INVALIDO = -1;
	
	protected int Nro_Presupuesto;
	protected List<Concepto> conceptos;
	protected Cliente cliente;
	protected boolean efectivo;
	protected float alicuota;
	protected double monto_total;
	protected String fecha;
	
	// :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: ::::::::::
	//puede o no tener asociado un nro transaccion
	// :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: ::::::::::
	protected int transaccion_asociado; // solo si presupuesto es efectivo
	
	
	
	public Presupuesto(List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double monto_total, Date fecha){

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Nro_Presupuesto = PRESUPUESTO_INVALIDO;
		this.conceptos = conceptos;
		this.cliente = cliente;
		this.efectivo = efectivo;
		this.alicuota = alicuota;
		this.monto_total = monto_total;
		this.fecha = format1.format(fecha);
		this.transaccion_asociado = this.NRO_TRANSACCION_INVALIDO;
	}
	public void actualizarNroTransaccion(int nt){
		this.transaccion_asociado = nt;
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
	public String getFecha(){
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
	public void setMontoTotal(double mt){
		monto_total = mt;
	}
	public void setEfectivo(boolean b){
		efectivo = b;
	}
}
