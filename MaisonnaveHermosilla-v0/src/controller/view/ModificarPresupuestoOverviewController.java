package controller.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import controller.db.Presupuesto;
import exception.InvalidBudgetException;
import controller.db.Cliente;
import controller.db.Concepto;
import controller.db.DBEngine;
import controller.db.DBSingleton;


/**
 * Diálogo para editar los detalles de un presupuesto especificado.
 * 
 * @author Maria Virginia Sabando
 */
public class ModificarPresupuestoOverviewController {

    @FXML
    private Label nroPresupuestoField;
    @FXML
    private Label cuitField;
    @FXML
    private Label denominacionField;
    @FXML
    private DatePicker	fechaDatePicker;
    @FXML
    private Label condicionIvaField;
    @FXML
    private ChoiceBox<String> alicuotaChoiceBox = new ChoiceBox<String>();
    @FXML
    private Label montoTotalField;
    @FXML
    private TableView<Concepto> conceptosTable;
    @FXML
    private TableColumn<Concepto,String> conceptoColumn;
    @FXML
    private TableColumn<Concepto,String> montoColumn;
    @FXML
    private Button nuevoButton;
    @FXML
    private Button editarButton;
    @FXML
    private Button borrarButton;
    
 
    private Stage dialogStage;
    private Presupuesto presupuesto;
    private boolean okClicked = false;
    
    private DBEngine DBMotor = DBSingleton.getInstance();

    /**
     * Inicializa la clase controlador. Este método es llamado automáticamente
     * luego de que el archivo .fxml ha sido cargado.
     */
    @FXML
    private void initialize() {
    	
    	editarButton.disableProperty().bind(Bindings.isEmpty(conceptosTable.getSelectionModel().getSelectedItems()));
        borrarButton.disableProperty().bind(Bindings.isEmpty(conceptosTable.getSelectionModel().getSelectedItems()));
            	
    	conceptosTable.setPlaceholder(new Label("No hay conceptos para mostrar."));
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
     * Ubica en la vista los datos del presupuesto que esta siendo modificado.
     * 
     * @param presupuesto
     */
    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
        Cliente cliente = presupuesto.getCliente();

        nroPresupuestoField.setText(String.valueOf(presupuesto.getNroPresupuesto()));
        cuitField.setText(cliente.getCuit());
        denominacionField.setText(cliente.getDenominacion());
        fechaDatePicker.setValue(pasarLocalDate(presupuesto.getFecha_ARG()));
        String ci = cliente.getCondicionIva();
        if(ci!= null){
        	if(ci.startsWith("RI")){
            	this.condicionIvaField.setText("Responsable Inscripto");
            }
            else if(ci.startsWith("EX")){
            	this.condicionIvaField.setText("Exento");
            }
            else if(ci.startsWith("MO")){
            	this.condicionIvaField.setText("Monotributista");
            }
            else if(ci.startsWith("NR")){
            	this.condicionIvaField.setText("No Responsable");
            }
            else if(ci.startsWith("CF")){
            	this.condicionIvaField.setText("Consumidor Final");
            }
        }
        
        alicuotaChoiceBox.setItems(FXCollections.observableArrayList("0%","10,5%","21%"));
        
        Float ali = presupuesto.getAlicuota();
        if(ali!= null){
        	if(ali == 0.0){
            	alicuotaChoiceBox.setValue("0%");
            }
            else if(ali == 10.5){
            	alicuotaChoiceBox.setValue("10,5%");
            }
            else if(ali == 21.0){
            	alicuotaChoiceBox.setValue("21%");
            }
            else{
            	alicuotaChoiceBox.setValue("");
            }
        }
        
        montoTotalField.setText(String.valueOf(presupuesto.getMontoTotal()));
        
        conceptosTable.setItems(presupuesto.getConceptosObservables());
        conceptoColumn.setCellValueFactory(
			cellData -> cellData.getValue().getConceptoProperty());
        
		montoColumn.setCellValueFactory(
			cellData -> cellData.getValue().getMontoConceptoStringProperty());
 
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
     * Llamado cuando el usuario presiona el botón OK: Guarda los cambios realizados
     * sobre el presupuesto en la base de datos
     * TODO: editar conceptos en el objeto presupuesto
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	//TODO
        	Date date = Date.from(fechaDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        	presupuesto.setFecha(date);
        	
            String ali = alicuotaChoiceBox.getValue();
            if(ali!= null){
            	if(ali.startsWith("0%")){
            		presupuesto.setAlicuota(0);
                }
                else if(ali.startsWith("10,5%")){
                	presupuesto.setAlicuota((float)10.5);
                }
                else if(ali.startsWith("21%")){
                	presupuesto.setAlicuota((float)21.0);
                }
            }
            
            //TODO: editar conceptos en el objeto presupuesto
                        
            Alert alert = new Alert(AlertType.INFORMATION, 
		  			 "",
                    ButtonType.OK, 
                    ButtonType.CANCEL);
            alert.initOwner(dialogStage);
            alert.setTitle("Aceptar: Editar presupuesto");
            alert.setHeaderText("¿Desea guardar los cambios realizados?");
            alert.setContentText("Pulse Aceptar para guardar los cambios realizados.");


            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
            	 okClicked = true;
                 /*
                 //Actualizo el presupuesto en la base de datos
                 try {
					DBMotor.editarPresupuesto(presupuesto);
				} catch (InvalidBudgetException e) {
					e.printStackTrace();
					System.out.println("El presupuesto es inválido, y no lo puedo editar.");
				}*/
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
          alert.setTitle("Cancelar: Editar presupuesto");
          alert.setHeaderText("¿Desea descartar los cambios realizados?");
          alert.setContentText("Pulse Aceptar para descartar los cambios realizados.");
         
          Optional<ButtonType> result = alert.showAndWait();

          if (result.get() == ButtonType.OK) {
        	  dialogStage.close();
          }   
    }

    
    /**
     * Valida las entradas de datos en la pantalla de modificacion del presupuesto.
     * 
     * @return true si la entrada es válida
     * TODO
     */
    private boolean isInputValid() {
       /* String errorMessage = "";

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
        if(denominacionField.getText().length() > 60){
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
        }*/
    	return true;
    }
    
    private static final LocalDate pasarLocalDate (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }
    
    public void handleNuevoConcepto(){
    	
    	Concepto nuevo;
    	
    	Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Nuevo concepto");
    	dialog.setHeaderText(null);
    	dialog.setResizable(true);

    	Label label1 = new Label("Concepto: ");
    	Label label2 = new Label("Monto: $");
    	TextField text1 = new TextField();
    	TextField text2 = new TextField();
    	text1.setPromptText("Nuevo concepto");
    	text2.setPromptText("0.0");
    			
    	//Validacion de campos
    	 text1.textProperty().addListener((obs, oldText, newText) -> {
             if (oldText.length() < 150 && newText.length() >= 150) {
                      text2.requestFocus();
             }
          });
    	 
     	 
     	 text2.textProperty().addListener((obs, oldText, newText) -> {
     		 if (newText.matches("\\d+\\.|\\d*(\\.\\d+)?")) {
     			 text2.setText(newText);
     		 }
     		 else
     			 text2.setText(oldText);
     		 
     	 });
    	
    	GridPane grid = new GridPane();
    	grid.add(label1, 1, 1);
    	grid.add(text1, 2, 1);
    	grid.add(label2, 1, 2);
    	grid.add(text2, 2, 2);
    	dialog.getDialogPane().setContent(grid);
    			
    	ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    	
    	ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

    	Optional<ButtonType> result = dialog.showAndWait();
    	
    	if (result.get() == buttonTypeOk) {
    		String cs;
    		double m;
    		//Chequeo si las cajas de texto quedaron vacías:
    		//en ese caso creo un concepto por default
    		if (text1.getText()==null || text1.getText()==""){
    			cs="Concepto nuevo";
    			System.out.println("entre aqui cs "+ cs);
    		}
    		else{
    			cs = text1.getText();
    		}
    		
    		if (text2.getText()==null || text2.getText()==""){
    			m=0.0;
    			System.out.println("entre aqui monto " + m);
    		}
    		else{
    			m = Double.parseDouble(text2.getText());
    		}
    		
    		//Creo nuevo concepto con los datos ingresados
            nuevo = new Concepto(cs, m);
            //agrego el concepto nuevo a la lista de conceptos del presupuesto
            presupuesto.getConceptos().add(nuevo);
            //System.out.println(nuevo);
            //Actualizo contenido tabla en la vista
       	 	conceptosTable.setItems(presupuesto.getConceptosObservables());
        	
         }
         else{
        	 //No hago nada y cierro
         }
    	
    	
    }
    
    public void handleEditarConcepto(){
    	
    	Concepto concepto = conceptosTable.getSelectionModel().getSelectedItem();
    	if (concepto != null) {
    	Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Editar concepto");
    	dialog.setHeaderText(null);
    	dialog.setResizable(true);

    	Label label1 = new Label("Concepto: ");
    	Label label2 = new Label("Monto: $");
    	TextField text1 = new TextField(concepto.getConcepto());
    	TextField text2 = new TextField(String.valueOf(concepto.getMonto()));
    	
    	 text1.textProperty().addListener((obs, oldText, newText) -> {
            if (oldText.length() < 150 && newText.length() >= 150) {
                     text2.requestFocus();
            }
         });
    	 
    	 text2.textProperty().addListener((obs, oldText, newText) -> {
    		 if (!newText.matches("\\d+\\.|\\d+(\\.\\d+)?")) {
    			 text2.setText(newText.replaceAll("[^\\d]", ""));//("[^\\d]", ""));
    		 }
    	 });
    			
    	GridPane grid = new GridPane();
    	grid.add(label1, 1, 1);
    	grid.add(text1, 2, 1);
    	grid.add(label2, 1, 2);
    	grid.add(text2, 2, 2);
    	dialog.getDialogPane().setContent(grid);
    			
    	ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    	
    	ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

    	Optional<ButtonType> result = dialog.showAndWait();
    	
    	if (result.get() == buttonTypeOk) {
    		//borro el concepto viejo de la lista
    		presupuesto.getConceptos().remove(concepto);
    		
    		//cambio los parametros del concepto
    		double m = Double.parseDouble(text2.getText());
        	Concepto nuevo = new Concepto(text1.getText(),m);
        	
        	//agrego el concepto nuevo a la lista de nuevo
        	presupuesto.getConceptos().add(nuevo);
    		
            //Actualizo contenido tabla en la vista
       	 	conceptosTable.setItems(presupuesto.getConceptosObservables());
        	
         }
         else{
        	 //No hago nada y cierro
         }
    	}
    	else{
    		 // No se seleccionó ningún concepto
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(dialogStage);
            alert.setTitle("Seleccionar concepto");
            alert.setHeaderText("No se ha seleccionado un concepto");
            alert.setContentText("Por favor, seleccione un concepto en la tabla.");

            alert.showAndWait();
    	}
    }
    
    public void handleBorrarConcepto(){
    	
    	Concepto selectedConcepto = conceptosTable.getSelectionModel().getSelectedItem();
        if (selectedConcepto != null) {
        	 // Se procede a alertar al usuario
             Alert alert = new Alert(AlertType.WARNING, 
		  			 "",
                   ButtonType.YES, 
                   ButtonType.NO);
             alert.initOwner(dialogStage);
             alert.setTitle("Borrar concepto");
             alert.setHeaderText(null);
             alert.setContentText("¿Borrar concepto seleccionado?");


             Optional<ButtonType> result = alert.showAndWait();

             if (result.get() == ButtonType.YES) {
            	 //Elimino el concepto de la lista de conceptos del presupuesto
            	 presupuesto.getConceptos().remove(selectedConcepto);
            	 //Actualizo contenido tabla en la vista
            	 conceptosTable.setItems(presupuesto.getConceptosObservables());
                
             }
             else{
            	 //No hago nada y cierro
             }

        } 
        else {
            // No se seleccionó ningún concepto
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(dialogStage);
            alert.setTitle("Seleccionar concepto");
            alert.setHeaderText("No se ha seleccionado un concepto");
            alert.setContentText("Por favor, seleccione un concepto en la tabla.");

            alert.showAndWait();
        }
    }

   
}