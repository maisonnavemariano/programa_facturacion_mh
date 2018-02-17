package controller.db;
 
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.*;
//import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase modelo para la entidad Presupuesto
 *
 * @author Maria Virginia Sabando
 * @author Mariano Maisonnave
 */

public class Presupuesto {
	protected static final int PRESUPUESTO_INVALIDO = -1;
	protected static final int NRO_TRANSACCION_INVALIDO = -1;
	
	private IntegerProperty Nro_Presupuesto;
	private List<Concepto> conceptos;  			
	private Cliente cliente;					
	private BooleanProperty efectivo;
	private FloatProperty alicuota;
	private DoubleProperty subtotal;
	//private DoubleProperty monto;
	private StringProperty fecha;
	private StringProperty fecha_ARG;
	
	
	// :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: ::::::::::
	//puede o no tener asociado un nro transaccion
	// :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: :::::::::: ::::::::::
	private IntegerProperty transaccion_asociado; // solo si presupuesto es efectivo
	
	
	
	public Presupuesto(List<Concepto> conceptos, Cliente cliente, boolean efectivo, float alicuota, double sub_total, Date fecha){

		Nro_Presupuesto = new SimpleIntegerProperty(PRESUPUESTO_INVALIDO);
		this.conceptos = conceptos;
		this.cliente = cliente;
		this.efectivo = new SimpleBooleanProperty(efectivo);
		this.alicuota = new SimpleFloatProperty(alicuota);
		this.subtotal = new SimpleDoubleProperty(sub_total);
		//this.monto = new SimpleDoubleProperty(calcularMontoTotal());
		
		//Format1: lo que se usa en sql
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		this.fecha = new SimpleStringProperty(format1.format(fecha));
		
		//Format2: lo que se muestra por pantallas (formato argentino)
		//Solo para mostrarlo en la aplicacion cuando sea necesario
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
		this.fecha_ARG = new SimpleStringProperty(format2.format(fecha));
		
		this.transaccion_asociado =new SimpleIntegerProperty(NRO_TRANSACCION_INVALIDO);
	}
	
	public void actualizarNroTransaccion(int nt){
		this.transaccion_asociado = new SimpleIntegerProperty(nt);
	}
	
	
	public void actualizarNroPresupuesto(int Nro_Presupuesto){
		this.Nro_Presupuesto = new SimpleIntegerProperty(Nro_Presupuesto);
	}
	
	public boolean hasValidNumber(){
		return getNroPresupuesto() != PRESUPUESTO_INVALIDO;
	}
	
	
	
	/*-------------------------------------------------------------------------------------
	 * --------------------------------------ATRIBUTOS-------------------------------------
	 * ------------------------------------------------------------------------------------	 * 
	 */
	
	/**
     * Nro_presupuesto: tiene un getter que retorna int,
     * un getter que retorna un StringProperty
     * y un getter que retorna IntegerProperty
     * 
     * */
	
	public int getNroPresupuesto(){
		return Nro_Presupuesto.get();
	}
	
    public IntegerProperty NroPresupuestoProperty() {
        return Nro_Presupuesto;
    }
	
    public StringProperty NroPresupuestoStringProperty(){
    	 StringProperty aux = new SimpleStringProperty(String.valueOf(Nro_Presupuesto.get()));
    	 return aux;
    	
    }
    
	/**
     * cliente: tiene un getter que retorna un cliente
     * 
     * */
	
	public Cliente getCliente(){
		return cliente;
	}
	
	/**
     * efectivo: tiene un getter que retorna boolean,
     * un setter que pide un boolean
     * un getter que retorna un StringProperty adaptado 
     * y un getter que retorna un BooleanProperty
     * 
     * */
	
	public boolean getEfectivo(){
		return efectivo.get();
	}
	
	public BooleanProperty efectivoProperty(){
		return efectivo;
	}

	public void setEfectivo(boolean b){
		efectivo.set(b);
	}
	
	public StringProperty efectivoStringProperty(){
		StringProperty aux = new SimpleStringProperty();
		if(getEfectivo())
			aux.set("SI");
		else
			aux.set("NO");
		return aux;
	}
	
	/**
     * alicuota: tiene un getter que retorna float,
     * un getter que retorna un StringProperty
     * y un getter que retorna un FloatProperty
     * 
     * */
	public void setAlicuota(float a){
		this.alicuota.set(a);
	}
	
	public float getAlicuota(){
		return alicuota.get();
	}
	
	public FloatProperty alicuotaProperty(){
		return alicuota;
	}
	
	public StringProperty alicuotaStringProperty(){
		StringProperty aux = new SimpleStringProperty(String.valueOf(alicuota));
		return aux;
	}
	
	/**
     * subtotal: tiene un getter que retorna double,
     * un getter que retorna un StringProperty
     * un getter que retorna un DoubleProperty
     * y un setter que pide double
     * 
     * */
	
	public double getSubtotal(){
		return subtotal.get();
	}
	
	public DoubleProperty subtotalProperty(){
		return subtotal;
	}
	
	public StringProperty subtotalStringProperty(){
		StringProperty aux = new SimpleStringProperty(String.valueOf(subtotal.get()));
		return aux;
	}

	public void setSubtotal(double st){
		subtotal.set(st);
	}
	
	/**
     * conceptos: tiene un getter que retorna la lista de conceptos
     * 
     * */
	
	public List<Concepto> getConceptos(){
		return conceptos;
	}
	
	public ObservableList<Concepto> getConceptosObservables(){
		ObservableList<Concepto> listaObservableConceptos = FXCollections.observableArrayList(conceptos);
		return listaObservableConceptos;
	}
	
		
	/**
     * fecha y fecha_ARG: tiene un getter que retorna String,
     * y un getter que retorna un StringProperty
     * 
     * */
	
	public void setFecha(Date f){
		//Format1: lo que se usa en sql
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		this.fecha = new SimpleStringProperty(format1.format(f));
				
		//Format2: lo que se muestra por pantallas (formato argentino)
		//Solo para mostrarlo en la aplicacion cuando sea necesario
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		this.fecha_ARG = new SimpleStringProperty(format2.format(f));
	}
	
	public String getFecha(){
		return fecha.get();
	}
	
    public StringProperty fechaProperty() {
        return fecha;
    }
    
    public String getFecha_ARG(){
		return fecha_ARG.get();
	}
	
    public StringProperty fecha_ARGProperty() {
        return fecha_ARG;
    }
   
    
    /**
     * NroTransaccion: tiene un getter que retorna String,
     * y un getter que retorna un StringProperty
     * 
     * */
	
	public int getNroTransaccion(){
		return transaccion_asociado.get();
	}
	
    public IntegerProperty transaccionProperty() {
        return transaccion_asociado;
    }
    
    public StringProperty transaccionStringProperty() {
        StringProperty aux = new SimpleStringProperty(String.valueOf(getNroTransaccion()));
        return aux;
    }
    
    public double calcularMontoTotal(){

        double monto_calculado = 0;
        for(Concepto c : conceptos){
        	monto_calculado= monto_calculado + c.getMonto();
        }
        float ali = getAlicuota();
        monto_calculado = monto_calculado * (1.0 + ali/100);
        monto_calculado = Math.round(monto_calculado * 20.0) / 20.0;
        return monto_calculado;
    }
}
