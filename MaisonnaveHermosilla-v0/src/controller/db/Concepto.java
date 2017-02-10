package controller.db;

public class Concepto {
	protected String concepto;
	protected double monto;
	
	
	public Concepto(String c, double monto){
		this.monto = monto;
		this.concepto = c;
	}
	
	public String toString(){
		return "[concepto: "+concepto+", "+monto+" ]";
	}
	
	public String getConcepto(){
		return concepto;
	}
	public Double getMonto(){
		return monto;
	}

}
