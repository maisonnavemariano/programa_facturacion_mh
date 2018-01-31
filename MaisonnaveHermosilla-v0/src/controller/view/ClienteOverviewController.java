package controller.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Optional;

import controller.Main;
import controller.db.Cliente;
import controller.db.DBEngine;

public class ClienteOverviewController {
    @FXML
    private TableView<Cliente> clienteTable;
    @FXML
    private TableColumn<Cliente, String> cuitColumn;
    @FXML
    private TableColumn<Cliente, String> denominacionColumn;

    @FXML
    private Label cuitLabel;
    @FXML
    private Label denominacionLabel;
    @FXML
    private Label direccionLabel;
    @FXML
    private Label localidadLabel;
    @FXML
    private Label telefonoLabel;
    @FXML
    private Label correoElectronicoLabel;
    @FXML
    private Label condicionIvaLabel;
    @FXML
    private Label habilitadoLabel;
    @FXML
    private RadioButton cuitRadioButton;
    @FXML
    private RadioButton denominacionRadioButton;
    @FXML
    private TextField busquedaTextField;
    
    //Variables booleanas de control
    private boolean cuitPresionado = true;
    private boolean denomPresionado = false;

    // Reference to the main application.
    private Main mainApp;
    
    /**
     * Motor de la base de datos.
     */
    private DBEngine DBMotor;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ClienteOverviewController() {
    	
    }
    
    /**
     * Fills all text fields (labels informativos) to show details about the cliente.
     * If the specified cliente is null, all text fields (labels) are cleared.
     * 
     * @param cliente the cliente or null
     */
    private void showClienteDetails(Cliente cliente) {
        if (cliente != null) {
            // Fill the labels with info from the cliente object.
            cuitLabel.setText(cliente.getCuit());
            denominacionLabel.setText(cliente.getDenominacion());
            direccionLabel.setText(cliente.getDireccion());
            localidadLabel.setText(cliente.getLocalidad());
            telefonoLabel.setText(cliente.getTelefono());
            correoElectronicoLabel.setText(cliente.getCorreoElectronico());
            
            String ci = cliente.getCondicionIva();
            if(ci.startsWith("RI")){
            	condicionIvaLabel.setText("Responsable Inscripto");
            }
            else if(ci.startsWith("EX")){
            	condicionIvaLabel.setText("Exento");
            }
            else if(ci.startsWith("MO")){
            	condicionIvaLabel.setText("Monotributista");
            }
            else if(ci.startsWith("NR")){
            	condicionIvaLabel.setText("No Responsable");
            }
            else if(ci.startsWith("CF")){
            	condicionIvaLabel.setText("Consumidor Final");
            }
            else{
            	condicionIvaLabel.setText("Sin información");
            }
            
            String hab = cliente.getHabilitado();
            if(hab.startsWith("S")){
            	habilitadoLabel.setText("SI");
            }
            else if(hab.startsWith("N")){
            	habilitadoLabel.setText("NO");
            }
            else{
            	habilitadoLabel.setText("Sin información");
            }
            

        } else {
            // Cliente is null, remove all the text.
            cuitLabel.setText("");
            denominacionLabel.setText("");
            direccionLabel.setText("");
            localidadLabel.setText("");
            telefonoLabel.setText("");
            correoElectronicoLabel.setText("");
            condicionIvaLabel.setText("");
            habilitadoLabel.setText("");
        }
    }
    
    /**
     * LLamado cuando el usuario hace click en el boton Nuevo. Abre un 
     * diálogo para crear un nuevo cliente.
     */
    @FXML
    private void handleNewCliente() {
        Cliente tempCliente = new Cliente();
        boolean okClicked = mainApp.showNuevoClienteOverview(tempCliente);
        if (okClicked) {
            mainApp.getClienteData().add(tempCliente);
        }
    }

    /**
     * LLamado cuando el usuario hace click en el boton Editar. Abre un 
     * diálogo para editar el cliente seleccionado.
     */
    @FXML
    private void handleEditCliente() {
        Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
        if (selectedCliente != null) {
            boolean okClicked = mainApp.showModificarClienteOverview(selectedCliente);
            if (okClicked) {
                showClienteDetails(selectedCliente);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Seleccionar cliente");
            alert.setHeaderText("No se ha seleccionado un cliente");
            alert.setContentText("Por favor, seleccione un cliente en la tabla.");

            alert.showAndWait();
        }
    }
    
    /**
     * LLamado cuando el usuario hace click en el boton Eliminar Cliente. Abre un 
     * diálogo para confirmar la eliminacion del cliente seleccionado.
     */
    @FXML
    private void handleEliminarCliente() {
        Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
        if (selectedCliente != null) {
        	 // Se procede a alertar al usuario
             Alert alert = new Alert(AlertType.WARNING, 
		  			 "",
                   ButtonType.YES, 
                   ButtonType.NO);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Eliminar cliente");
             alert.setHeaderText("Cliente seleccionado: "+ selectedCliente.getDenominacion());
             alert.setContentText("¿Desea eliminar al cliente de la base de datos?");


             Optional<ButtonType> result = alert.showAndWait();

             if (result.get() == ButtonType.YES) {
            	 //Elimino al cliente de la base de datos
            	 selectedCliente = DBMotor.eliminarCliente(selectedCliente.getCodigoCliente());
            	 //Actualizo contenido tabla en la vista
            	 handleSearch();
            	// Se informa al usuario que termino el proceso
                 alert = new Alert(AlertType.INFORMATION, 
    		  			 "",
                       ButtonType.OK);
                 alert.initOwner(mainApp.getPrimaryStage());
                 alert.setTitle("Eliminar cliente");
                 alert.setHeaderText("Cliente: "+ selectedCliente.getDenominacion());
                 alert.setContentText("Se ha eliminado al cliente de la base de datos.");
                 alert.showAndWait();
             }
             else{
            	 //No hago nada y cierro
             }

        } 
        else {
            // No se seleccionó ningún cliente
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Seleccionar cliente");
            alert.setHeaderText("No se ha seleccionado un cliente");
            alert.setContentText("Por favor, seleccione un cliente en la tabla.");

            alert.showAndWait();
        }
    }
    
     /**
     * Inicializa la clase controller. Este metodo es automaticamente llamado
     * luego de que fue cargado el archivo fxml. Usa expresiones lambda.
     */
    @FXML
    private void initialize() {
        //Inicializa la tabla de clientes con los valores correspondientes a las columnas
        cuitColumn.setCellValueFactory(
                cellData -> cellData.getValue().cuitProperty());
        denominacionColumn.setCellValueFactory(
                cellData -> cellData.getValue().denominacionProperty());

        // Clear cliente details.
        showClienteDetails(null);

        // Listen for selection changes and show the cliente details when changed.
        clienteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showClienteDetails(newValue));
        
        clienteTable.setPlaceholder(new Label("No hay clientes para mostrar."));
    }
    
   
    /**
     * LLamado cuando el usuario tipea en la caja de texto de busqueda.
     * Actualiza la listad e clientes segun se haya seleccionado cuit o denominacion
     * en los radio buttons.
     */
    @FXML
    private void handleSearch() {

		String busqueda = busquedaTextField.getText();
    	if(cuitPresionado){
    		ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(DBMotor.buscarCUIT(busqueda));
    		mainApp.setClienteData(listaClientes);    		
    	}
    	if(denomPresionado){
    		ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(DBMotor.buscarCliente(busqueda));
    		mainApp.setClienteData(listaClientes);
    	}
    	clienteTable.setItems(mainApp.getClienteData());
    }
    
    /**
    * LLamado cuando el usuario selecciona el radiobutton cuit
    */
   @FXML
   private void handleCuitRadioButton() {
	   cuitPresionado = true;
	   denomPresionado = false;
   }
   
   /**
    * LLamado cuando el usuario selecciona el radiobutton cuit
    */
   @FXML
   private void handleDenomRadioButton() {
	   cuitPresionado = false;
	   denomPresionado = true;
   }
    

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp, DBEngine motor) {
        this.mainApp = mainApp;
        this.DBMotor = motor;

        // Add observable list data to the table
        clienteTable.setItems(mainApp.getClienteData());
    }
    
    
}