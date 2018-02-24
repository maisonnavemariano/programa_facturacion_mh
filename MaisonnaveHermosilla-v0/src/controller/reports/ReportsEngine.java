package controller.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.db.Concepto;
import controller.db.Presupuesto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportsEngine {
	
	public static String generarReporte(Presupuesto p) {
	boolean salida_ok=false;
	String sourceFile = "reports_templates/Original_y_copia.jasper";
	String salida = "reportes/"+p.getCliente().getDenominacion()+"-"+p.getNroPresupuesto()+".pdf";
	List<ConceptosBean> dataList = new ArrayList<ConceptosBean>();
	
	ConceptosBean cb;
	for (Concepto c: p.getConceptos()) {
		cb = new ConceptosBean();
		cb.setDescripcion(c.getConcepto());
		cb.setMonto(c.getMonto());
		dataList.add(cb);
	}
	
	String printFile = null;
	JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
	
	Map<String, Object> parameters = new HashMap<String,Object>();

	parameters.put("denominacion",p.getCliente().getDenominacion());
	String direccion = p.getCliente().getDireccion()!=null? p.getCliente().getDireccion():"";
	direccion +=      p.getCliente().getLocalidad()!=null? p.getCliente().getLocalidad():"";
	parameters.put("domicilio",direccion);
	parameters.put("iva",p.getCliente().getFormattedCondicionIva());
	parameters.put("Alicuota",new Double(p.getAlicuota()));
	parameters.put("fecha",p.getFormattedFecha());
	parameters.put("cuit",p.getCliente().getFormattedCuit());
	parameters.put("Nro_Presu",new Integer(p.getNroPresupuesto()));
	parameters.put("Subtotal",new Double(p.calcularSubtotal()));
	parameters.put("Iva_monto",new Double(p.calcularIva()));
	
	try {
		printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfFile(printFile, salida);
		salida_ok = true;
         
	}catch(JRException e) {
		e.printStackTrace();
	}
		return salida_ok?salida:null;
	}
	
	
	
	
	public static String generarReporteBorrador(Presupuesto p) {
		boolean salida_ok=false;
		String sourceFile = "reports_templates/Presupuesto_borrador.jasper";
		String salida = "borradores/"+p.getCliente().getDenominacion()+"-"+p.getNroPresupuesto()+"-BORRADOR.pdf";
		List<ConceptosBean> dataList = new ArrayList<ConceptosBean>();
		
		ConceptosBean cb;
		for (Concepto c: p.getConceptos()) {
			cb = new ConceptosBean();
			cb.setDescripcion(c.getConcepto());
			cb.setMonto(c.getMonto());
			dataList.add(cb);
		}
		
		String printFile = null;
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		
		Map<String, Object> parameters = new HashMap<String,Object>();

		parameters.put("denominacion",p.getCliente().getDenominacion());
		String direccion = p.getCliente().getDireccion()!=null? p.getCliente().getDireccion():"";
		direccion +=      p.getCliente().getLocalidad()!=null? p.getCliente().getLocalidad():"";
		parameters.put("domicilio",direccion);
		parameters.put("iva",p.getCliente().getFormattedCondicionIva());
		parameters.put("Alicuota",new Double(p.getAlicuota()));
		parameters.put("fecha",p.getFormattedFecha());
		parameters.put("cuit",p.getCliente().getFormattedCuit());
		parameters.put("Nro_Presu",new Integer(p.getNroPresupuesto()));
		parameters.put("Subtotal",new Double(p.calcularSubtotal()));
		parameters.put("Iva_monto",new Double(p.calcularIva()));
		
		try {
			printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
			JasperExportManager.exportReportToPdfFile(printFile, salida);
			salida_ok = true;
	         
		}catch(JRException e) {
			e.printStackTrace();
		}
			return salida_ok?salida:null;
	}
	
	public static void generarReporte(Presupuesto p, String url) {
		String sourceFile = "reports_templates/Original_y_copia.jasper";
		List<ConceptosBean> dataList = new ArrayList<ConceptosBean>();
		
		ConceptosBean cb;
		for (Concepto c: p.getConceptos()) {
			cb = new ConceptosBean();
			cb.setDescripcion(c.getConcepto());
			cb.setMonto(c.getMonto());
			dataList.add(cb);
		}
		
		String printFile = null;
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		
		Map<String, Object> parameters = new HashMap<String,Object>();

		parameters.put("denominacion",p.getCliente().getDenominacion());
		String direccion = p.getCliente().getDireccion()!=null? p.getCliente().getDireccion():"";
		direccion +=      p.getCliente().getLocalidad()!=null? p.getCliente().getLocalidad():"";
		parameters.put("domicilio",direccion);
		parameters.put("iva",p.getCliente().getFormattedCondicionIva());
		parameters.put("Alicuota",new Double(p.getAlicuota()));
		parameters.put("fecha",p.getFormattedFecha());
		parameters.put("cuit",p.getCliente().getFormattedCuit());
		parameters.put("Nro_Presu",new Integer(p.getNroPresupuesto()));
		parameters.put("Subtotal",new Double(p.calcularSubtotal()));
		parameters.put("Iva_monto",new Double(p.calcularIva()));
		
		try {
			printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
			JasperExportManager.exportReportToPdfFile(printFile, url);
	         
		}catch(JRException e) {
			e.printStackTrace();
		}
		
	}

}
