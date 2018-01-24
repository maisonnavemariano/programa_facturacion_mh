package controller.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import controller.Main;
import controller.db.Cliente;
import controller.db.Concepto;
import controller.db.DBEngine;
import controller.db.Presupuesto;

public class AreaDeTrabajoOverviewController  {
	
	//Lista de presupuestos
    @FXML
    private TableView<Presupuesto> presupuestosTable;
    @FXML
    private TableColumn<Presupuesto, String> numeroColumn;
    //TODO: ver si la fecha es un DateUtil 
    @FXML
    private TableColumn<Presupuesto, String> fechaColumn;
    @FXML
    private TableColumn<Presupuesto, String> cuitColumn;
    @FXML
    private TableColumn<Presupuesto, String> denominacionColumn;


    @FXML
    private Label numeroLabel;
    @FXML
    private Label cuitLabel;
    @FXML
    private Label denominacionLabel;
    @FXML
    private Label fechaLabel;
    @FXML
    private Label ivaLabel;
    @FXML
    private Label alicuotaLabel;
    @FXML
    private Label montoLabel;
    @FXML
    private RadioButton cuitRadioButton;
    @FXML
    private RadioButton denominacionRadioButton;
    @FXML
    private ListView<Concepto> conceptosListView;
    
    //Variables booleanas de control
    private boolean cuitPresionado = true;
    private boolean denomPresionado = false;
    
    @FXML
    private TextField busquedaTextField;

    // Reference to the main application.
    private Main mainApp;
    
    /**
     * Motor de la base de datos.
     */
    private DBEngine DBMotor;

    /**
     * Constructor llamado antes del metodo initialize().
     */
    public AreaDeTrabajoOverviewController() {
    	
    }
    
    /**
     * Completa los labels informativos) para mostrar detalles del presupuesto seleccionado.
     * Si no se ha seleccionado ningun presupuesto (presupuesto null), todos los labels son clareados.
     * 
     * @param presupuesto o null
     * TODO: Arreglar metodos aca y/o en presupuesto
     * TODO: faltan actualizar la lista de conceptos
     */
    private void showPresupuestoDetails(Presupuesto presupuesto) {
        if (presupuesto != null) {
        	
        	Cliente cliente= presupuesto.getCliente();
        	
            // Completa los labels con info de la instancia presupuesto.

            numeroLabel.setText(presupuesto.getNroPresupuesto());
            cuitLabel.setText(cliente.getCuit());
            denominacionLabel.setText(cliente.getDenominacion());
            fechaLabel.setText(presupuesto.getFecha());
                       
            String ci = cliente.getCondicionIva();
            if(ci.startsWith("RI")){
            	ivaLabel.setText("Responsable Inscripto");
            }
            else if(ci.startsWith("EX")){
            	ivaLabel.setText("Exento");
            }
            else if(ci.startsWith("MO")){
            	ivaLabel.setText("Monotributista");
            }
            else if(ci.startsWith("NR")){
            	ivaLabel.setText("No Responsable");
            }
            else if(ci.startsWith("CF")){
            	ivaLabel.setText("Consumidor Final");
            }
            else{
            	ivaLabel.setText("Sin información");
            }
            
            alicuotaLabel.setText(presupuesto.getAlicuota());
            montoLabel.setText(presupuesto.getMontoTotal());
          
            //Falta la lista de conceptos

        } else {
        	
            // Presupuesto nulo, clarear todas las etiquetas.
        	numeroLabel.setText("");
            cuitLabel.setText("");
            denominacionLabel.setText("");
            fechaLabel.setText("");
            ivaLabel.setText("");
            alicuotaLabel.setText("");
            montoLabel.setText("");
            
            //Falta los conceptos
        }
    }
    
   /**
     * LLamado cuando el usuario hace click en el boton Nuevo. Abre un 
     * diálogo para crear un nuevo cliente.
     * TODO: Ver si se saca del todo o se debe reemplazar con otra cosa este método. Aca no tiene sentido.
     */
   /* @FXML
    private void handleNewCliente() {
        Cliente tempCliente = new Cliente();
        boolean okClicked = mainApp.showNuevoClienteOverview(tempCliente);
        if (okClicked) {
            mainApp.getClienteData().add(tempCliente);
        }
    }*/

    /**
     * LLamado cuando el usuario hace click en el boton Editar. Abre un 
     * diálogo para editar el presupuesto seleccionado.
     * TODO: terminar metodo: ver que pasa si aprieto ok despues de editar presupuesto.
     * (tal vez debo solo mostrar los resultados, al igual que en la vista cliente. en ese caso descomentar)
     */
    @FXML
    private void handleEditPresupuesto() {
        Presupuesto selectedPresupuesto = presupuestosTable.getSelectionModel().getSelectedItem();
        if (selectedPresupuesto != null) {
            boolean okClicked = mainApp.showModificarPresupuestoOverview(selectedPresupuesto);
            if (okClicked) {
                //showClienteDetails(selectedPresupuesto);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Seleccionar presupuesto");
            alert.setHeaderText("No se ha seleccionado un presupuesto");
            alert.setContentText("Por favor, seleccione un presupuesto en la tabla.");

            alert.showAndWait();
        }
    }

    /**
     * Inicializa la clase controller. Este metodo es automaticamente llamado
     * luego de que fue cargado el archivo fxml. Usa expresiones lambda.
     * TODO: hay que hacer que los elementos del presupuesto sean properties, tal
     * y como se hizo para la clase cliente. Ir a clase Presupuesto y acomodar eso.
     */
    @FXML
    private void initialize() {
        // Inicializa la tabla de presupuestos con los valores de las 4 columnas.
    	numeroColumn.setCellValueFactory(
    			cellData -> cellData.getValue().numeroProperty());
    	fechaColumn.setCellValueFactory(
    			cellData -> cellData.getValue().fechaProperty());
        cuitColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCliente().cuitProperty());
        denominacionColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCliente().denominacionProperty());

        //Borrar detalles de Presupuesto
        showPresupuestoDetails(null);

        // Se queda escuchando por cambios en seleccion de presupuesto en la tabla
        // y muestra los detalles del presupuesto seleccionado.
        presupuestosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPresupuestoDetails(newValue));
    }
    
   
    /**
     * LLamado cuando el usuario tipea en la caja de texto de busqueda.
     * Actualiza la lista de presupuestos segun se haya seleccionado cuit o denominacion
     * en los radio buttons.
     * TODO: como hago para crear la lista de presupuestos a partir de la lista de clientes?
     */
    @FXML
    private void handleSearch() {

		String busqueda = busquedaTextField.getText();
    	if(cuitPresionado){
    		ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(DBMotor.buscarCUIT(busqueda));
    	}
    	if(denomPresionado){
    		ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(DBMotor.buscarCliente(busqueda));
    	}
    	//Esto no! tengo que hacer una lista de presupuestos con esta info
    	mainApp.setClienteData(listaClientes);
		presupuestosTable.setItems(mainApp.getClienteData());
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
        //TODO: ver si es necesario esto
       presupuestosTable.setItems(mainApp.getClienteData());
    }
    
    
}