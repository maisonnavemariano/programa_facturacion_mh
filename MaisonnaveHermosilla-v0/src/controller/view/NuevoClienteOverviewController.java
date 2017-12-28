package controller.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

import controller.db.Cliente;
import controller.db.DBEngine;
import controller.db.DBSingleton;


/**
 * Diálogo para editar los detalles de un nuevo cliente.
 * 
 * @author Maria Virginia Sabando
 */
public class NuevoClienteOverviewController {

    @FXML
    private TextField cuitField;
    @FXML
    private TextField denominacionField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField localidadField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField correoElectronicoField;
    @FXML
    private ChoiceBox<String> condicionIvaChoiceBox = new ChoiceBox<String>();
    @FXML
    private RadioButton	SI_habilitadoRadioButton;
    @FXML
    private RadioButton NO_habilitadoRadioButton;
    
//    private String[] posiblesCondicionesIva = {"Responsable Inscripto","Monotributista","Exento","No Responsable","Consumidor Final"};


    private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicked = false;
    
    private DBEngine DBMotor = DBSingleton.getInstance();

    /**
     * Inicializa la clase controlador. Este método es llamado automáticamente
     * luego de que el archivo .fxml ha sido cargado.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Setea el stage para este diálogo
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Ubica en la vista los datos del cliente que esta siendo modificado.
     * 
     * @param cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        cuitField.setText(cliente.getCuit());
        denominacionField.setText(cliente.getDenominacion());
        direccionField.setText(cliente.getDireccion());
        localidadField.setText(cliente.getLocalidad());
        telefonoField.setText(cliente.getTelefono());
        correoElectronicoField.setText(cliente.getCorreoElectronico());
        
        String hab = cliente.getHabilitado();
        if (hab.startsWith("N")){
       	 	SI_habilitadoRadioButton.setSelected(false);
       	 	NO_habilitadoRadioButton.setSelected(true);
        }
        else{ //valor por defecto: habilitado SI
       	 	SI_habilitadoRadioButton.setSelected(true);
       	 	NO_habilitadoRadioButton.setSelected(false);
        }
        	
        condicionIvaChoiceBox.setItems(FXCollections.observableArrayList("Responsable Inscripto","Monotributista","Exento","No Responsable","Consumidor Final"));
       
        String ci = cliente.getCondicionIva();
        if(ci!= null){
        	if(ci.startsWith("RI")){
            	condicionIvaChoiceBox.setValue("Responsable Inscripto");
            }
            else if(ci.startsWith("EX")){
            	condicionIvaChoiceBox.setValue("Exento");
            }
            else if(ci.startsWith("MO")){
            	condicionIvaChoiceBox.setValue("Monotributista");
            }
            else if(ci.startsWith("NR")){
            	condicionIvaChoiceBox.setValue("No Responsable");
            }
            else if(ci.startsWith("CF")){
            	condicionIvaChoiceBox.setValue("Consumidor Final");
            }
            else{
            	condicionIvaChoiceBox.setValue("");
            }
        }
        
    }

    /**
     * Retorna true si el cliente presionó el botón OK, falso otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Llamado cuando el usuario presiona el botón OK: Guarda al cliente en la base de datos
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            String numeroCuit = cuitField.getText();
            numeroCuit = numeroCuit.replace("-","");
            numeroCuit = numeroCuit.replace(".","");
            cliente.setCuit(numeroCuit);
            cliente.setDenominacion(denominacionField.getText());
            cliente.setDireccion(direccionField.getText());
            cliente.setLocalidad(localidadField.getText());
            cliente.setTelefono(telefonoField.getText());
            cliente.setCorreoElectronico(correoElectronicoField.getText());

            String ci = condicionIvaChoiceBox.getValue();
            if(ci!= null){
            	if(ci.startsWith("Responsable Inscripto")){
                	cliente.setCondicionIva("RI");
                }
                else if(ci.startsWith("Exento")){
                	cliente.setCondicionIva("EX");
                }
                else if(ci.startsWith("Monotributista")){
                	cliente.setCondicionIva("MO");
                }
                else if(ci.startsWith("No Responsable")){
                	cliente.setCondicionIva("NR");
                }
                else if(ci.startsWith("Consumidor Final")){
                	cliente.setCondicionIva("CF");
                }
            }
            
            if(SI_habilitadoRadioButton.isSelected()){
            	cliente.setHabilitado("S");
            }
            else if(NO_habilitadoRadioButton.isSelected()){
            	cliente.setHabilitado("N");
            }
            
            
            Alert alert = new Alert(AlertType.INFORMATION, 
		  			 "",
                    ButtonType.OK, 
                    ButtonType.CANCEL);
            alert.initOwner(dialogStage);
            alert.setTitle("Aceptar: Nuevo cliente");
            alert.setHeaderText("¿Desea guardar los cambios realizados?");
            alert.setContentText("Pulse Aceptar para guardar los cambios realizados.");


            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
            	 okClicked = true;
                 System.out.println(cliente.getCondicionIva());
                 //Actualizo el cliente en la base de datos
                 DBMotor.agregarCliente(cliente);
                 dialogStage.close();
            }
        }
    }

    /**
     * LLamado cuando el usuario presiona el botón Cancelar
     */
    @FXML
    private void handleCancelar() {
    	
    	  Alert alert = new Alert(AlertType.INFORMATION, 
    			  			 "",
    	                     ButtonType.OK, 
    	                     ButtonType.CANCEL);
          alert.initOwner(dialogStage);
          alert.setTitle("Cancelar: Nuevo cliente");
          alert.setHeaderText("¿Desea descartar los cambios realizados?");
          alert.setContentText("Pulse Aceptar para descartar los cambios realizados.");
         
          Optional<ButtonType> result = alert.showAndWait();

          if (result.get() == ButtonType.OK) {
        	  dialogStage.close();
          }   
    }

    /**
     * Valida las entradas de datos en la pantalla de nuevo cliente.
     * 
     * @return true si la entrada es válida
     */
    private boolean isInputValid() {
        String errorMessage = "";

        //Chequeo formato del numero de CUIT ingresado
        String numeroCuit = cuitField.getText();
        numeroCuit = numeroCuit.replace("-","");
        numeroCuit = numeroCuit.replace(".","");
        boolean cuitIsNumeric = numeroCuit.chars().allMatch( Character::isDigit );
        
        if (numeroCuit == null || numeroCuit.length() <= 0) {
            errorMessage += "El número de CUIT es un campo obligatorio.\n"; 
        }
        if(numeroCuit.length() > 11){
        	errorMessage += "El número de CUIT debe estar formado por 11 dígitos como máximo.\n"; 
        }
        if(!cuitIsNumeric){
        	errorMessage += "Se han ingresado caracteres inválidos en el campo CUIT.\n"; 
        }
        if (denominacionField.getText() == null || denominacionField.getText().length() <= 0) {
            errorMessage += "La denominación de cliente es un campo obligatorio.\n"; 
        }
        if (denominacionField.getText().length() > 60){
        	errorMessage += "La denominación no puede contener más de 60 caracteres.\n";
        }
        if (condicionIvaChoiceBox.getValue() == null || condicionIvaChoiceBox.getValue().length() <= 0) {
            errorMessage += "La condición de IVA del cliente es un campo obligatorio.\n"; 
        }
        if (direccionField.getText() != null && direccionField.getText().length() > 100){
        	errorMessage += "La dirección no puede contener más de 100 caracteres.\n"; 
        }
        if (localidadField.getText() != null && localidadField.getText().length() > 45){
        	errorMessage += "La localidad no puede contener más de 45 caracteres.\n"; 
        }
        if (telefonoField.getText() != null && telefonoField.getText().length() > 25){
        	errorMessage += "El número de teléfono no puede contener más de 25 caracteres.\n"; 
        }
        if (correoElectronicoField.getText() != null && correoElectronicoField.getText().length() > 60){
        	errorMessage += "El correo electrónico no puede contener más de 60 caracteres.\n"; 
        }
        

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, complete correctamente los campos.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}