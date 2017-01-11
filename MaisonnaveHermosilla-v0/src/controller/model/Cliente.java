package controller.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//comentario

/**
 * Clase modelo para la entidad Cliente
 *
 * @author Maria Virginia Sabando
 */
public class Cliente {
	
	private final IntegerProperty codigoCliente;
    private final StringProperty cuit;
    private final StringProperty denominacion;
    private final StringProperty direccion;
    private final StringProperty localidad;
    private final StringProperty telefono;
    private final StringProperty correoElectronico;
    private final StringProperty condicionIva;
    private final StringProperty habilitado;
   // private final ObjectProperty<LocalDate> birthday;

    /**
     * Default constructor. LLama al otro constructor con nombre y apellido nulos.
     */
    public Cliente() {
        this(null, null, null,0);
    }

    /**
     * Constructor de la clase
     * Requiere de todos los datos que no pueden ser nulos, los demás
     * son seteados a su valor por defecto, que puede ser nulo.
     */
    public Cliente(String cu, String de, String ci, int cc) {
        this.cuit = new SimpleStringProperty(cu);
        this.denominacion = new SimpleStringProperty(de);
        this.condicionIva = new SimpleStringProperty(ci);
        
        // hay que ver como se inicializa el codigoCliente.
        // por ahora es pasado por parametro.
        this.codigoCliente = new SimpleIntegerProperty(cc);
        
        // el resto de los campos se completa con información por defecto.
        this.direccion = new SimpleStringProperty("Calle Falsa 123");
        this.localidad = new SimpleStringProperty("Coronel Suarez");
        this.telefono = new SimpleStringProperty("02926435738");
        this.correoElectronico = new SimpleStringProperty("ejemplo@gmail.com");
        
     
        
        //habilitado=SI por defecto
        this.habilitado = new SimpleStringProperty("S"); 
        
        //this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }
    
    //SETTERS Y GETTERS DE LA CLASE Cliente.

    /**
     * codigoCliente: tiene un getter que retorna int,
     * un setter que pide un int,
     * y un getter que retorna un IntegerProperty
     * */

    public int getCodigoCliente() {
        return codigoCliente.get();
    }

    public void setCodigoCliente(int cc) {
        this.codigoCliente.set(cc);
    }

    public IntegerProperty codigoClienteProperty() {
        return codigoCliente;
    }
    
    
    /**
     * cuit: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */

    public String getCuit() {
        return cuit.get();
    }

    public void setCuit(String cu) {
        this.cuit.set(cu);
    }

    public StringProperty cuitProperty() {
        return cuit;
    }
    
    /**
     * denominacion: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */

    public String getDenominacion() {
        return denominacion.get();
    }

    public void setDenominacion(String de) {
        this.denominacion.set(de);
    }

    public StringProperty denominacionProperty() {
        return denominacion;
    }
    
    /**
     * direccion: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */

    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String di) {
        this.direccion.set(di);
    }

    public StringProperty direccionProperty() {
        return direccion;
    }
    
    /**
     * localidad: tiene un getter que retorna string,
     * un setter que pide un string,
     * y un getter que retorna un StringProperty
     * */

    public String getLocalidad() {
        return localidad.get();
    }

    public void setLocalidad(String lo) {
        this.localidad.set(lo);
    }

    public StringProperty localidadProperty() {
        return localidad;
    }

    /**
     * telefono: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */
    
    public String getTelefono() {
        return telefono.get();
    }

    public void setTelefono(String te) {
        this.telefono.set(te);
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    /**
     * correoElectronico: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */
    
    public String getCorreoElectronico() {
        return correoElectronico.get();
    }

    public void setCorreoElectronico(String ce) {
        this.correoElectronico.set(ce);
    }

    public StringProperty correoElectronicoProperty() {
        return correoElectronico;
    }
    
    /**
     * condicionIva: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */
    
    public String getCondicionIva() {
        return condicionIva.get();
    }

    public void setCondicionIva(String ci) {
        this.condicionIva.set(ci);
    }

    public StringProperty condicionIvaProperty() {
        return condicionIva;
    }
    
    /**
     * habilitado: tiene un getter que retorna String,
     * un setter que pide un String,
     * y un getter que retorna un StringProperty
     * */
    
    public String getHabilitado() {
        return habilitado.get();
    }

    public void setHabilitado(String h) {
        this.habilitado.set(h);
    }

    public StringProperty habilitadoProperty() {
        return habilitado;
    }
    /* 
    /**
     * Birthday: tiene un getter que retorna LocalDate,
     * un setter que pide un LocalDate,
     * y un getter que retorna un ObjectProperty<LocalDate> 
     */
    
    /*
    
    public LocalDate getBirthday() {
        return birthday.get();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }
    
    */
}