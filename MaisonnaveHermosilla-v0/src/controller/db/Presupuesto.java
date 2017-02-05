package controller.db;

import java.util.List;
import java.util.Date;

public class Presupuesto {
	protected int Nro_Presupuesto;
	protected List<Concepto> conceptos;
	protected Cliente cliente;
	protected boolean efectivo;
	protected float alicuota;
	protected double monto_total;
	protected Date fecha;
	

}
