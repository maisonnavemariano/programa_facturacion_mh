package controller.reports;

import controller.db.DBEngine;

public class tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBEngine motor = new DBEngine();
		System.out.println("arranca reportes");
		ReportsEngine.generarReporte(motor.verPresupuesto(2175),"/home/maiso/tmp.pdf");
		System.out.println("reporte 1");
		ReportsEngine.generarReporte(motor.verPresupuesto(2775));
		System.out.println("reporte 2");
		//ReportsEngine.generarReporteBorrador(motor.verPresupuesto(2575));
		System.out.println("reporte 3");

	}

}
