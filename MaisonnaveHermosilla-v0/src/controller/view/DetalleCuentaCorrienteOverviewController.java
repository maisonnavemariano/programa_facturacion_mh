package controller.view;
 

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

import controller.Main;
import controller.db.Cliente;
import controller.db.CuentaCorriente;


/**
 * Diálogo para editar los detalles de un cliente especificado.
 * 
 * @author Maria Virginia Sabando
 */
public class DetalleCuentaCorrienteOverviewController {
   
    @FXML
    private TableView<CuentaCorriente> cuentasTable;
    @FXML
    private TableColumn<CuentaCorriente, String> cuitColumn;
    @FXML
    private TableColumn<CuentaCorriente, String> denominacionColumn;
    @FXML
    private TableColumn<CuentaCorriente,String> montoColumn;
   
 
    private Stage dialogStage;
    private Main mainApp;

    /**
     * Inicializa la clase controlador. Este método es llamado automáticamente
     * luego de que el archivo .fxml ha sido cargado.
     */
    @FXML
    private void initialize() {
    	cuentasTable.setPlaceholder(new Label("No hay cuentas corrientes para mostrar."));
    	  	  	
 		cuentasTable.getColumns().forEach(this::addTooltipToColumnCells_CuentaCorriente);
 		
 		montoColumn.setStyle( "-fx-alignment: CENTER-RIGHT;");
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
    	this.mainApp=m;
    	
    	mainApp.setCuentasCorrientesData_DB();
    }
    
    public void arranque(){
    	//Actualizo contenido tabla en la vista
	 	cuentasTable.setItems(mainApp.getCuentasCorrientesData());
	 	cuitColumn.setCellValueFactory(
	 				cellData -> cellData.getValue().getCliente().cuitProperty());
	 		
	 	denominacionColumn.setCellValueFactory(
	 				cellData -> cellData.getValue().getCliente().denominacionProperty());
			
	 	montoColumn.setCellValueFactory(
 				cellData -> cellData.getValue().montoStringProperty());
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
    private void handleUltimosMovimientos(){
    	
    	if(cuentasTable.getSelectionModel().getSelectedItem() != null){
    	try {
            // Carga el archivo .fxml y crea un nuevo stage para el diálogo pop-up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UltimosMovimientosOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea el Stage para el diálogo
            Stage nuevoStage = new Stage();
            nuevoStage.setTitle("Últimos Movimientos");
            nuevoStage.initModality(Modality.WINDOW_MODAL);
            nuevoStage.setMaximized(false);
            nuevoStage.initOwner(this.dialogStage);
            Scene scene = new Scene(page);
            nuevoStage.setScene(scene);

            // Setear los parametros del controlador       
            UltimosMovimientosOverviewController controller = loader.getController();
            controller.setDialogStage(nuevoStage);
            controller.setCuentaCorriente(cuentasTable.getSelectionModel().getSelectedItem());

            // Show the dialog and wait until the user closes it
            nuevoStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }}
    	else{
    		 Alert alert = new Alert(AlertType.WARNING, 
 		  			 "",
                    ButtonType.OK);
              alert.initOwner(mainApp.getPrimaryStage());
              alert.setTitle("Seleccionar Cuenta Corriente");
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
    private void handleAceptar() {
    	dialogStage.close();
    }
    
   
}