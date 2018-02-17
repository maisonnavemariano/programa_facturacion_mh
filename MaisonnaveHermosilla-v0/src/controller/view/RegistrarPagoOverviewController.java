package controller.view;


import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.input.KeyEvent;
import javafx.event.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import controller.Main;
import controller.db.CuentaCorriente;
import controller.db.DBEngine;
import controller.db.DBSingleton;
import controller.db.Transaccion;
import exception.InvalidClientException;


/**
 * Diálogo para editar los detalles de un cliente especificado.
 * 
 * @author Maria Virginia Sabando
 */
public class RegistrarPagoOverviewController {
   
    @FXML
    private TableView<CuentaCorriente> cuentasTable;
    @FXML
    private TableColumn<CuentaCorriente, String> cuitColumn;
    @FXML
    private TableColumn<CuentaCorriente, String> denominacionColumn;
    @FXML
    private TableColumn<CuentaCorriente,Number> montoColumn;
   
    @FXML
    private Label fechaLabel;
    @FXML 
    private Label montoViejoLabel;
    @FXML
    private Label montoNuevoLabel;
    
    @FXML
    private TextField importeTextField;
    @FXML
    private TextArea observacionesTextArea;
 
    private Stage dialogStage;
    private Main mainApp;
    private DBEngine DBMotor = DBSingleton.getInstance();

    /**
     * Inicializa la clase controlador. Este método es llamado automáticamente
     * luego de que el archivo .fxml ha sido cargado.
     */
    @FXML
    private void initialize() {
    	cuentasTable.setPlaceholder(new Label("No hay cuentas corrientes para mostrar."));
    	  	  	
 		cuentasTable.getColumns().forEach(this::addTooltipToColumnCells_CuentaCorriente);
 		
 		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 		String formattedString = LocalDate.now().format(formatter);
 		fechaLabel.setText(formattedString);
 		
 		montoViejoLabel.setText("");
 		montoNuevoLabel.setText("");
 		
 		importeTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
        public void handle(KeyEvent event) {
        	if(event.getCode() == KeyCode.TAB){
        		calcularNuevoMonto();
        	}
        }
    });
 		
    }

    /**
     * Setea el stage para este diálogo
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setMainApp(Main m){
    	this.mainApp = m;
    }

    
    public void arranque(){
    	//Actualizo contenido tabla en la vista
	 	cuentasTable.setItems(mainApp.getCuentasCorrientesData());
	 	cuitColumn.setCellValueFactory(
	 				cellData -> cellData.getValue().getCliente().cuitProperty());
	 		
	 	denominacionColumn.setCellValueFactory(
	 				cellData -> cellData.getValue().getCliente().denominacionProperty());
			
	 	montoColumn.setCellValueFactory(
 				cellData -> cellData.getValue().montoProperty());
	 	
	 	cuentasTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> {
	                	
	                	montoViejoLabel.setText(String.valueOf(newValue.getEstadoCuentaCorriente()));
	                	
	                });
	 	
	 	//Validacion de campos
	 	observacionesTextArea.textProperty().addListener((obs, oldText, newText) -> {
            if (oldText.length() < 100 && newText.length() >= 100) {
            	importeTextField.requestFocus();
            }
         });
   	 
    	 
	 	importeTextField.textProperty().addListener((obs, oldText, newText) -> {
    		 if (newText.matches("\\d+\\.|\\d*(\\.\\d+)?")) {
    			 importeTextField.setText(newText);
    		 }
    		 else
    			 importeTextField.setText(oldText);
    	 });
	 	
    }
    

    @FXML
    private void calcularNuevoMonto(){
    	CuentaCorriente c = cuentasTable.getSelectionModel().getSelectedItem();
    	double viejo = 0.0;
    	if(c!=null){
    		viejo = c.getEstadoCuentaCorriente();
    		
    		double pagado = Double.parseDouble(importeTextField.getText());
    		
    		viejo = viejo + pagado;
    		
    	}
    	else
    		this.montoViejoLabel.setText("0.0");
    	
    	this.montoNuevoLabel.setText(String.valueOf(viejo));
    	this.observacionesTextArea.requestFocus();
    }
    
    
    private <T> void addTooltipToColumnCells_CuentaCorriente(TableColumn<CuentaCorriente,T> column) {

	    Callback<TableColumn<CuentaCorriente, T>, TableCell<CuentaCorriente,T>> existingCellFactory 
	        = column.getCellFactory();

	    column.setCellFactory(c -> {
	        TableCell<CuentaCorriente, T> cell = existingCellFactory.call(c);

	        Tooltip tooltip = new Tooltip();
	        // can use arbitrary binding here to make text depend on cell
	        // in any way you need:
	        tooltip.textProperty().bind(cell.itemProperty().asString());

	        cell.setTooltip(tooltip);
	        return cell;
	    });
	}
    
    @FXML
    private void handleRegistrar(){
    	
    	CuentaCorriente c = cuentasTable.getSelectionModel().getSelectedItem();
    	if(c!=null){

    		String v = importeTextField.getText();
    		if(v==null || v.length()==0){
    			Alert alert = new Alert(AlertType.ERROR, 
    		  			 "",
                       ButtonType.OK);
    			alert.initOwner(dialogStage);
    			alert.setTitle("Error - Campos obligatorios");
    			alert.setHeaderText(null);
    			alert.setContentText("Ingrese un importe de pago.");
    			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    			alert.showAndWait();
    		}
    		else{
    			Alert alert = new Alert(AlertType.CONFIRMATION, 
    		  			 "",
                       ButtonType.OK,
                       ButtonType.CANCEL);
    			alert.initOwner(dialogStage);
    			alert.setTitle("Registrar pago");
    			alert.setHeaderText(null);
    			alert.setContentText("¿Desea registrar el pago en la cuenta corriente de "+c.getCliente().getDenominacion() + " ?");
    			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    			Optional<ButtonType> resu = alert.showAndWait();
    			
    			if(resu.get().equals(ButtonType.OK)){
    				//Recupero el importe pagado
    				double pagado = Double.parseDouble(importeTextField.getText());
    				//Recupero observaciones
    				String obs = observacionesTextArea.getText();
    				//Primero registro el pago en la DB
    				Transaccion T=null;
    				try {
						T = DBMotor.efectuarPago(c.getCliente(), pagado, obs);
					} catch (InvalidClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				//Luego aviso
    				alert = new Alert(AlertType.INFORMATION, 
        		  			 "",
                           ButtonType.OK);
    				alert.initOwner(dialogStage);
    				alert.setTitle("Registrar pago");
    				alert.setHeaderText(null);
    				alert.setContentText("Se ha registrado exitosamente el pago en la cuenta corriente. \n\nTransacción Nº "+ T.getNroTransaccion() );
    				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    				alert.showAndWait();
    			}
    		}
        	
    		
    	}
    	else{

        	Alert alert = new Alert(AlertType.WARNING, 
     		  			 "",
                        ButtonType.OK);
            alert.initOwner(dialogStage);
            alert.setTitle("Seleccionar cuenta corriente");
            alert.setHeaderText(null);
            alert.setContentText("No se ha seleccionado una cuenta corriente de la lista.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
    	}
    }
    
    /**
     * Llamado cuando el usuario presiona el botón OK: Guarda al cliente en la base de datos
     */
    @FXML
    private void handleCancelar() {
    	dialogStage.close();
    }
    
    
    
    
    
}