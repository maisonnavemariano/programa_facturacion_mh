package controller.view;
 

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import org.joda.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controller.Main;
import controller.db.Cliente;
import controller.db.CuentaCorriente;
import controller.db.DBEngine;
import controller.db.DBSingleton;
import controller.db.Presupuesto;
import controller.db.Transaccion;


/**
 * Diálogo para editar los detalles de un cliente especificado.
 * 
 * @author Maria Virginia Sabando
 */
public class UltimosMovimientosOverviewController {
   
    @FXML
    private TableView<Transaccion> transaccionesTable;
    @FXML
    private TableColumn<Transaccion, Object> fechaColumn;
    @FXML
    private TableColumn<Transaccion, String> eventoColumn;
    @FXML
    private TableColumn<Transaccion, String> importeColumn;
    @FXML
    private TableColumn<Transaccion, String> observacionColumn;
    @FXML
    private TableColumn<Transaccion, String> estadoColumn;
    
    @FXML
    private Label denominacionLabel;
    @FXML
    private Label cuitLabel;
    @FXML 
    private Label montoLabel;
    @FXML
    private Label fechaUltimoLabel;
   
 
    private Stage dialogStage;
    private DBEngine DBMotor=  DBSingleton.getInstance();
    private CuentaCorriente cuenta;
        

    /**
     * Inicializa la clase controlador. Este método es llamado automáticamente
     * luego de que el archivo .fxml ha sido cargado.
     */
    @FXML
    private void initialize() {
    	transaccionesTable.setPlaceholder(new Label("No hay transacciones para mostrar."));
    	  	  	
 		transaccionesTable.getColumns().forEach(this::addTooltipToColumnCells_Transaccion);
 		 		
 		importeColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
 		
 		estadoColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
 		
 		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 		
 		/*fechaColumn.setCellFactory(tc -> new TableCell<Transaccion, LocalDate>() {
 		    @Override
 		    protected void updateItem(LocalDate date, boolean empty) {
 		        super.updateItem(date, empty);
 		        if (empty) {
 		            setText(null);
 		        } else {
 		            setText(formatter.format(date));
 		        }
 		    }
 		}); */
    }

    /**
     * Setea el stage para este diálogo
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    
    public void setCuentaCorriente(CuentaCorriente c){
    	this.cuenta=c;
    	if(cuenta!= null){
    		this.cuitLabel.setText(cuenta.getCliente().getCuit());
    		this.denominacionLabel.setText(cuenta.getCliente().getDenominacion());
    		
    		//Viene asi: yyyy-MM-dd
    		String s = DBMotor.fechaUltimoPago(cuenta.getCliente());
    		//Lo quiero asi: dd/MM/yyyy
    		String ss = s.substring(8,10)+"/"+s.substring(5,7)+"/"+s.substring(0,4);
    		this.fechaUltimoLabel.setText(ss); 
    		
    		this.montoLabel.setText(String.valueOf(cuenta.getEstadoCuentaCorriente()));
    		
    		ObservableList<Transaccion> listaTransacciones = FXCollections.observableArrayList(DBMotor.ultimosMovimientos(this.cuenta.getCliente()));
    	 	transaccionesTable.setItems(listaTransacciones);
    	 	
    	 	fechaColumn.setCellValueFactory(
    	 				cellData -> cellData.getValue().fechaObjectProperty());
    	 	
    	 	    	 	
    	 	eventoColumn.setCellValueFactory(
    	 				cellData -> cellData.getValue().eventoProperty());
    			
    	 	importeColumn.setCellValueFactory(
     				cellData -> cellData.getValue().montoStringProperty());
    	 	observacionColumn.setCellValueFactory(
     				cellData -> cellData.getValue().observacionProperty());
    		
    	 	estadoColumn.setCellValueFactory(
    				cellData -> cellData.getValue().estadoStringProperty());
    	}
    	else{
    		this.cuitLabel.setText("");
    		this.denominacionLabel.setText("");
    		this.fechaUltimoLabel.setText("");
    		this.montoLabel.setText("");
    		transaccionesTable.setItems(null);
    	}
    }
    

    private <T> void addTooltipToColumnCells_Transaccion(TableColumn<Transaccion,T> column) {

	    Callback<TableColumn<Transaccion, T>, TableCell<Transaccion,T>> existingCellFactory 
	        = column.getCellFactory();

	    column.setCellFactory(c -> {
	        TableCell<Transaccion, T> cell = existingCellFactory.call(c);

	        Tooltip tooltip = new Tooltip();
	        // can use arbitrary binding here to make text depend on cell
	        // in any way you need:
	        tooltip.textProperty().bind(cell.itemProperty().asString());

	        cell.setTooltip(tooltip);
	        return cell;
	    });
	}
       
    /**
     * Llamado cuando el usuario presiona el botón OK: Guarda al cliente en la base de datos
     */
    @FXML
    private void handleAceptar() {
    	dialogStage.close();
    }
    
    @FXML
    private void handleVerPresupuestoAsociado(){
    	
    	if(transaccionesTable.getSelectionModel().getSelectedItem()!=null){
    		Transaccion T = transaccionesTable.getSelectionModel().getSelectedItem();
    		if(T.eventoProperty().get().equals("Presupuesto")){
    			Presupuesto p = DBMotor.getPresupuestoAsociado(transaccionesTable.getSelectionModel().getSelectedItem());
    	    	try {
    	            // Carga el archivo .fxml y crea un nuevo stage para el diálogo pop-up
    	            FXMLLoader loader = new FXMLLoader();
    	            loader.setLocation(Main.class.getResource("view/DetallePresupuestoOverview.fxml"));
    	            AnchorPane page = (AnchorPane) loader.load();

    	            // Crea el Stage para el diálogo
    	            Stage dialogStage = new Stage();
    	            dialogStage.setTitle("Detalle de Presupuesto");
    	            dialogStage.initModality(Modality.WINDOW_MODAL);
    	            dialogStage.initOwner(this.dialogStage);
    	            Scene scene = new Scene(page);
    	            dialogStage.setScene(scene);

    	            // Setear los parametros del controlador       
    	            DetallePresupuestoOverviewController controller = loader.getController();
    	            controller.setDialogStage(dialogStage);
    	            controller.setPresupuesto(p);

    	            // Show the dialog and wait until the user closes it
    	            dialogStage.showAndWait();

    	            //return controller.isOkClicked();
    	        	} 
    	    		catch (IOException e) {
    	    			e.printStackTrace();
    	    			//return false;
    	    		}
    		}
    		else{
    			Alert alert = new Alert(AlertType.INFORMATION, 
    		  			 "",
                       ButtonType.OK);
                 alert.initOwner(dialogStage);
                 alert.setTitle("Registro de pago");
                 alert.setHeaderText(null);
                 //TODO: cancelaciones
                 alert.setContentText("La transacción seleccionada corresponde a un pago o a una cancelación, y por ende no tiene asociada un presupuesto efectivo.");
                 alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                 alert.showAndWait();
    		}
    	
    	}
    	else{
    		 Alert alert = new Alert(AlertType.WARNING, 
 		  			 "",
                    ButtonType.OK);
              alert.initOwner(dialogStage);
              alert.setTitle("Seleccionar transacción");
              alert.setHeaderText(null);
              alert.setContentText("No se ha seleccionado una transaccion de la lista.");
              alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
              alert.showAndWait();
    	}
    }
}
