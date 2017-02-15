package controller.db;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaccion {
	protected final int INVALID_NRO_TRANSACCION = -1 ;
	protected int Nro_Transaccion;
	protected Cliente cliente;
	protected String Fecha;
	protected char Evento;
	protected double Monto;
	protected String concepto_observacion;
	protected double Estado_cuenta_corriente;
	
	public Transaccion(Cliente cliente, Date fecha, char evento, double monto, String obs, double Estado_cuenta_corriente){
		this.cliente = cliente;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		this.Fecha = format1.format(fecha);
		this.Evento = evento;
		this.Monto = monto;
		this.concepto_observacion = obs;
		this.Estado_cuenta_corriente = Estado_cuenta_corriente;
	}
	
	public void actualizarNroTransaccion(int nroT){
		Nro_Transaccion = nroT;
	}
	
	public boolean isValidTransaction(){
		return (Nro_Transaccion != this.INVALID_NRO_TRANSACCION);
	}
	
	public String getFecha(){
		return Fecha;
	}
	
	public String toString(){
		return "("+this.Nro_Transaccion+") ["+this.Fecha+"]-- Cliente: ["+this.cliente.toString()+"] -- Monto: "+this.Monto+" -- Estado cuenta corriente: "+this.Estado_cuenta_corriente+".";
	}

}
