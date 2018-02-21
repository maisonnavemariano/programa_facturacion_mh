package controller.reports;

import controller.db.DBEngine;

public class tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBEngine motor = new DBEngine();
		ReportsEngine.generarReporte(motor.verPresupuesto(2575));
		ReportsEngine.generarReporteBorrador(motor.verPresupuesto(2575));

	}

}
