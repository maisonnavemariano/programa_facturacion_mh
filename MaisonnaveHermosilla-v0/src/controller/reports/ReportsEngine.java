package controller.reports;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.db.Cliente;
import controller.db.Concepto;
import controller.db.DBEngine;
import controller.db.Presupuesto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class ReportsEngine {
	public static String DefaultName(Presupuesto p) {
		return p.getCliente().getDenominacion()+"-"+p.getNroPresupuesto()+".pdf";
	}

	private static void generarResumen(ResumenBean resumen, String url) {

		String sourceFile = "reports_templates/Resumen_cuenta.jasper";
		
		List<TransaccionBean> dataList = resumen.getTransacciones();

		
		String printFile = null;
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		Map<String, Object> parameters = new HashMap<String,Object>();

		parameters.put("denominacion",resumen.getDenominacion());
		parameters.put("CUIT",resumen.getCUIT());

		
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		parameters.put("desde", formatter.format(resumen.getDesde()).toString());
		parameters.put("hasta", formatter.format(resumen.getHasta()).toString());
	    Date date = new Date();  

		parameters.put("fecha_emision", formatter.format(date));
		
		try {
			printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
			JasperExportManager.exportReportToPdfFile(printFile, url);
	         
		}catch(JRException e) {
			e.printStackTrace();
		}

				
	}
	
	public static void generarResumen(Cliente c, String desde, String hasta,String file) {
		DBEngine motor = controller.db.DBSingleton.getInstance();
		generarResumen(new ResumenBean(c,
									desde,
									hasta, 
									ResumenBean.TransaccionToTransaccionBean(motor.ultimosMovimientosDesdeHasta(c, desde, hasta)) 
									)
				,file);

	}
	
	public static void generarResumen(Cliente c, String desde, String hasta) {
		DBEngine motor = controller.db.DBSingleton.getInstance();
		generarResumen(new ResumenBean(c,desde,hasta, ResumenBean.TransaccionToTransaccionBean(motor.ultimosMovimientosDesdeHasta(c, desde, hasta)) ));

	}
	private static String generarResumen(ResumenBean resumen) {
		boolean salida_ok=false;

		String sourceFile = "reports_templates/Resumen_cuenta.jasper";
		String salida = "reportes/tmp/"+resumen.getDenominacion()+"_"+resumen.getDesde()+"_"+resumen.getHasta()+".pdf";
		
		List<TransaccionBean> dataList = resumen.getTransacciones();

		
		String printFile = null;
		List<MainReportBean> l = new ArrayList<MainReportBean>();
		l.add(new MainReportBean(dataList));
		
				
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(l);

		Map<String, Object> parameters = new HashMap<String,Object>();

		parameters.put("denominacion",resumen.getDenominacion());
		parameters.put("CUIT",resumen.getCUIT());

		Date date;
		parameters.put("saldo_inicial",resumen.getSaldoInicial());
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    try {
	    date = new SimpleDateFormat("yyyy-MM-dd").parse(resumen.getDesde());
	    parameters.put("desde", formatter.format(date));

		date = new SimpleDateFormat("yyyy-MM-dd").parse(resumen.getHasta());
		parameters.put("hasta", formatter.format(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    date = new Date();  

		parameters.put("fecha_emision", formatter.format(date));
		
		try {
			printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
			JasperExportManager.exportReportToPdfFile(printFile, salida);
			salida_ok = true;
	         
		}catch(JRException e) {
			e.printStackTrace();
		}
		return salida_ok?salida:null;
				
	}
	
	
	public static String generarReporte(Presupuesto p) {
	boolean salida_ok=false;
	String sourceFile = "reports_templates/Original_y_copia.jasper";
	String salida = "reportes/tmp/"+p.getCliente().getDenominacion()+"-"+p.getNroPresupuesto()+".pdf";
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
	parameters.put("Mes", p.getMesFormateado());
	
	
	
	
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
		String salida = "borradores/tmp/"+p.getCliente().getDenominacion()+"-"+p.getNroPresupuesto()+"-BORRADOR.pdf";
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
		parameters.put("Mes", p.getMesFormateado());
		
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
		parameters.put("Mes",p.getMesFormateado());
		
		try {
			printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
			JasperExportManager.exportReportToPdfFile(printFile, url);
	         
		}catch(JRException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static String generarJRprintReport(Presupuesto p) {
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
		parameters.put("Mes", p.getMesFormateado());
		
		try {
			printFile = JasperFillManager.fillReportToFile(sourceFile, parameters, beanColDataSource);
			return printFile;
	         
		}catch(JRException e) {
			e.printStackTrace();
		}
			return null;
		}

	public static String generar_todos_los_presupuestos(List<Presupuesto> lista) throws JRException{
	  long start = System.currentTimeMillis();

	  List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
	  
	  List<String> archivosJRprint = new ArrayList<String>();
	  String file;
	  for (Presupuesto p : lista) {
		  file = ReportsEngine.generarJRprintReport(p);
		  archivosJRprint.add(file);
		  jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile(file));
	  }
	  


	  JRPdfExporter exporter = new JRPdfExporter();

	  exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
	  exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("borradores/tmp/imprimir_todos.pdf"));
	  SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
	  configuration.setCreatingBatchModeBookmarks(true);
	  exporter.setConfiguration(configuration);

	  exporter.exportReport();

	  //for (String f : archivosJRprint)
		//  (new File(f)).delete();
	  
	  
	  System.err.println("PDF creation time : " + (System.currentTimeMillis() - start)+" millis");
	  
	  return "borradores/tmp/imprimir_todos.pdf";
	
	}
}
