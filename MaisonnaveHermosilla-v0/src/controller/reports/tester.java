package controller.reports;

import java.util.List;

import controller.db.Cliente;
import controller.db.DBEngine;

public class tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBEngine motor = new DBEngine("localhost");
		String desde = "2001-01-01";
		String hasta = "2018-04-01";
		//Cliente cliente, String desde, String hasta, List<TransaccionBean> transacciones
		ReportsEngine.generarResumen(new ResumenBean(motor.getCliente(8),desde,hasta, ResumenBean.TransaccionToTransaccionBean(motor.ultimosMovimientosDesdeHasta(motor.getCliente(8), desde, hasta)) ));
		/**
		System.out.println("arranca reportes");
		ReportsEngine.generarReporte(motor.verPresupuesto(2175),"/home/maiso/tmp.pdf");
		System.out.println("reporte 1");
		ReportsEngine.generarReporte(motor.verPresupuesto(2775));
		System.out.println("reporte 2");
		ReportsEngine.generarReporteBorrador(motor.verPresupuesto(2575));
		System.out.println("reporte 3");
*/
			System.out.println("Terminó.");
	}

}
