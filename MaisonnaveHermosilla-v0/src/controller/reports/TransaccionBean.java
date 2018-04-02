package controller.reports;

public class TransaccionBean{
	private double debe;
	private double haber;
	private double saldo;
	private String descripcion;
	private String fecha;
	public double getDebe() {
		return debe;
	}

	public void setDebe(double debe) {
		this.debe = debe;
	}

	public double getHaber() {
		return haber;
	}

	public void setHaber(double haber) {
		this.haber = haber;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	
	public TransaccionBean(String f, String descr, double d, double h, double s ){
		this.debe        = d;
		this.haber       = h;
		this.saldo       = s;
		this.descripcion = descr;
		this.fecha       = f;
	}
	
	
	
}