package controller.reports;

import controller.db.Presupuesto;

public class ReportsEngine {
	
	public static String generarReporte(Presupuesto p) {
		return "reportes/Original_y_copia.pdf";
	}
	public static String generarReporteBorrador(Presupuesto p) {
		return "reporters/Borrador.pdf";
	}
	
	public static void generarReporteBorrador(Presupuesto p, String url) {
		
	}

}
