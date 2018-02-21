package controller.reports;

import controller.db.Presupuesto;

public class ReportsEngine {
	
	public String generarReporte(Presupuesto p) {
		return "reportes/Original_y_copia.pdf";
	}
	public String generarReporteBorrador(Presupuesto p) {
		return "reporters/Borrador.pdf";
	}
	
	public void generarReporteBorrador(Presupuesto p, String url) {
		
	}

}
