package controller.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;

import java.util.Optional;

import javafx.concurrent.Task;

import java.lang.Thread;

import controller.Main;
import controller.db.Cliente;
import controller.db.Concepto;
import controller.db.DBEngine;
import controller.db.Presupuesto;
import exception.InvalidBudgetException;

public class AreaDeTrabajoOverviewController  {

	//Lista de presupuestos
    @FXML
    private TableView<Presupuesto> presupuestosTable;
    @FXML
    private TableColumn<Presupuesto, String> numeroColumn;
    @FXML
    private TableColumn<Presupuesto, String> fechaColumn;
    @FXML
    private TableColumn<Presupuesto, String> cuitColumn;
    @FXML
    private TableColumn<Presupuesto, String> denominacionColumn;

    //Lista de Conceptos
    @FXML
    private TableView<Concepto> conceptosTable;
    @FXML
    private TableColumn<Concepto, String> conceptoColumn;
    @FXML
    private TableColumn<Concepto, String> montoConceptoColumn;

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
    private Button editar;
    @FXML
    private Button efectivizarUno;
    @FXML
    private Button efectivizarTodos;
    @FXML
    private Button vistaPrevia;
    
    //Usados solamente en el metodo: efectivizar todos
    @FXML
    private ProgressBar barraProgreso;
    
    
    //Variables booleanas de control
    private boolean cuitPresionado = true;
    private boolean denomPresionado = false;
    
    @FXML
    private TextField busquedaTextField;

    //Referencia a la aplicación principal.
    private Main mainApp;
    
    //Motor de la base de datos.
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
     * 
     */
    private void showPresupuestoDetails(Presupuesto presupuesto) {
        if (presupuesto != null) {
        	
        	Cliente cliente= presupuesto.getCliente();
        	
            // Completa los labels con info de la instancia presupuesto.

            numeroLabel.setText(String.valueOf(presupuesto.getNroPresupuesto()));
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
            
            alicuotaLabel.setText(String.valueOf(presupuesto.getAlicuota()));
            montoLabel.setText(String.valueOf(presupuesto.getMontoTotal()));
          
            //conceptosListView.setItems(presupuesto.getConceptosObservables());
            conceptosTable.setItems(presupuesto.getConceptosObservables());
            conceptoColumn.setCellValueFactory(
    			cellData -> cellData.getValue().getConceptoProperty());
    		montoConceptoColumn.setCellValueFactory(
    			cellData -> cellData.getValue().getMontoConceptoStringProperty());
            

        } else {
        	
            // Presupuesto nulo, clarear todas las etiquetas.
        	numeroLabel.setText("");
            cuitLabel.setText("");
            denominacionLabel.setText("");
            fechaLabel.setText("");
            ivaLabel.setText("");
            alicuotaLabel.setText("");
            montoLabel.setText("");
            conceptosTable.setItems(null);
            
        }
        
    }
  
    /**
     * Inicializa la clase controller. Este metodo es automaticamente llamado
     * luego de que fue cargado el archivo fxml. Usa expresiones lambda.
     */
    @FXML
    private void initialize() {
        // Inicializa la tabla de presupuestos con los valores de las 4 columnas.
    	numeroColumn.setCellValueFactory(
    			cellData -> cellData.getValue().NroPresupuestoStringProperty());
    	fechaColumn.setCellValueFactory(
    			cellData -> cellData.getValue().fecha_ARGProperty());
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
     * Llamado por la aplicacion principal para dar una autorreferencia
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp, DBEngine motor) {
        this.mainApp = mainApp;
        this.DBMotor = motor;

        //Agrega el contenido de la lista observable a la tabla
        presupuestosTable.setItems(mainApp.getPresupuestosNoEfectivosData());
    }
    
    //-------------------------------BOTONES----------------------------------
    
    /**
      * LLamado cuando el usuario hace click en el boton Nuevo. Abre un 
      * diálogo para crear un nuevo cliente.
      * 
      */
     @FXML
     private void handleEfectivizarUno() {
    	 Presupuesto selectedPresupuesto = presupuestosTable.getSelectionModel().getSelectedItem();
         if (selectedPresupuesto != null) {
         	  // Se procede a alertar al usuario
              Alert alert = new Alert(AlertType.WARNING, 
 		  			 "",
                    ButtonType.YES, 
                    ButtonType.NO);
              alert.initOwner(mainApp.getPrimaryStage());
              alert.setTitle("Efectivizar presupuesto");
              alert.setHeaderText("Presupuesto seleccionado: "+ selectedPresupuesto.getNroPresupuesto()+ " - "
            		  			  + selectedPresupuesto.getCliente().getDenominacion());
              alert.setContentText("¿Desea efectivizar el presupuesto seleccionado?");
             
              Optional<ButtonType> result = alert.showAndWait();

              if (result.get() == ButtonType.YES) {
             	 //Efectivizo el presupuesto en la base de datos
             	 try{
             		 DBMotor.efectivizarPresupuesto(selectedPresupuesto);
                     
             	 }
             	 catch(InvalidBudgetException e){
             		e.printStackTrace();
             		System.out.println("La efectivización del presupuesto "+ selectedPresupuesto.getNroPresupuesto() + " tiro error.");
             	 }
             	 //Actualizo contenido tabla en la vista
             	 handleSearch();
             	 //Se informa al usuario que termino el proceso
                  alert = new Alert(AlertType.INFORMATION, 
     		  			 "",
                        ButtonType.OK);
                  alert.initOwner(mainApp.getPrimaryStage());
                  alert.setTitle("Efectivizar presupuesto");
                  alert.setHeaderText("Se ha efectivizado el presupuesto nº "+ selectedPresupuesto.NroPresupuestoStringProperty().get());
                  alert.setContentText("Transacción nº "+ selectedPresupuesto.transaccionStringProperty().get());
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
             alert.setTitle("Seleccionar presupuesto");
             alert.setHeaderText("No se ha seleccionado un presupuesto");
             alert.setContentText("Por favor, seleccione un presupuesto en la tabla.");

             alert.showAndWait();
         }
     }

     /**
      * LLamado cuando el usuario hace click en el boton Efectivizar Todos.
      * Abre un alerta donde dice cuantos presupuestos se harán efectivos, y pide confirmación.
      * TODO: agregar un cartel de "espere" o "cargando"
      */
     @FXML
     private void handleEfectivizarTodos() {

        
    	 //Primero recupero la lista de presupuestos no efectivos desde la DB
    	 mainApp.setPresupuestosNoEfectivosData_DB();
    	 ObservableList<Presupuesto> lista = mainApp.getPresupuestosNoEfectivosData();
    	 
    	 //Luego chequeo que haya algo en la lista
    	 if (!lista.isEmpty()){
    		 //Hay algo en la lista, luego hay que efectivizarlo
         	 // Se procede a alertar al usuario
             Alert alert = new Alert(AlertType.WARNING, 
 		  			 "",
                    ButtonType.YES, 
                    ButtonType.NO);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Efectivizar todo");
             alert.setHeaderText("Cantidad total de presupuestos: " + lista.size());
             alert.setContentText("¿Desea efectivizar TODOS los presupuestos no efectivos?");
             
             Optional<ButtonType> result = alert.showAndWait();

             
             if (result.get() == ButtonType.YES) {
            	
            	 //Efectivizo TODOS los presupuestos en la base de datos.
            	 
              	 Task<Void> task = new Task<Void>() {
            		    @Override
            		    protected Void call() throws Exception {
            		    	        	
            		    	for (int i=0; i<= lista.size(); i++){
                		    	updateProgress(i, lista.size());
                   			 	Thread.sleep(115);
            		    	}
            		    	return null;
            		    };
            	 };
            	 
            	 Task<Void> task_efectivizar = new Task<Void>() {
         		    @Override
         		    protected Void call() throws Exception {
         		    	
         		    	//Efectivizo cada item de la lista
                        for (Presupuesto p : lista){
        		    		try{
        		    			
                   			 	DBMotor.efectivizarPresupuesto(p);
                   			 	
        		    		}
                        	catch(InvalidBudgetException e){
                        		e.printStackTrace();
                        		System.out.println("La efectivización del presupuesto "+ p.getNroPresupuesto() + " tiro error.");
                        	}
        		    	}
                        
                        //Vuelvo a habilitar componentes de la ventana.
                        deshabilitarComponentes(false);
                                                
                     	//Actualizo contenido tabla en la vista: deberia no retornar nada
                        handleSearch();
                        
                        //Muestro alerta de finalizacion
         		    	 Platform.runLater(() -> {
         		    		
         		    		//Se informa al usuario que termino el proceso
         		           Alert alert = new Alert(AlertType.INFORMATION, 
         		  	  			 "",
         		                 ButtonType.OK);
         		           alert.initOwner(mainApp.getPrimaryStage());
         		           alert.setTitle("Efectivizar todo");
         		           alert.setHeaderText("Se han efectivizado "+ lista.size() + " presupuestos.");
         		           alert.setContentText("Puede consultar los presupuestos efectivos en el menú Buscar Presupuestos.");
         		           alert.showAndWait();
         		           barraProgreso.progressProperty().unbind();
         		           barraProgreso.progressProperty().set(0);
         		           
                         });
         		    	return null;
         		    };
         	 };
            		
            	//Bindea la barra de progreso con la property de presupuestos efectivizados so far
                barraProgreso.progressProperty().unbind();
                barraProgreso.progressProperty().bind(task.progressProperty());
              
                //Primero pongo como disabled los elementos graficos de la 
 		    	//ventana: botones, lista, etc.
 		    	deshabilitarComponentes(true);
                
 		    	//Arranco la efectivizacion de todos los presupuestos
 		    	new Thread(task).start();;
                new Thread(task_efectivizar).start();
               
             }
             else{
            	 //No hago nada: el usuario eligió el botón NO.
              }
    	 } 
         else {
        	 //No hay presupuesto no efectivos: la lista del area de trabajo debería estar vacía,
        	 //Se debe informar al usuario que no hay nada para efectivizar.
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Efectivizar todo");
             alert.setHeaderText("No existen presupuestos no efectivos.");
             alert.setContentText("");

             alert.showAndWait();
         }
     }
     
     
     /**
      * LLamado cuando el usuario hace click en el boton Editar. Abre un 
      * diálogo para editar el presupuesto seleccionado.
      * TODO: terminar metodo: ver que pasa si aprieto ok despues de editar presupuesto.
      * (tal vez debo solo mostrar los resultados, al igual que en la vista cliente. en ese caso descomentar)
      */
     @FXML
     private void handleEditarPresupuesto() {
         Presupuesto selectedPresupuesto = presupuestosTable.getSelectionModel().getSelectedItem();
         if (selectedPresupuesto != null) {
             boolean okClicked = mainApp.showModificarPresupuestoOverview(selectedPresupuesto);
             if (okClicked) {
                 //showClienteDetails(selectedPresupuesto);
             }
         } 
         else{
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
      * LLamado cuando el usuario hace click en el boton Vista Previa. Abre un 
      * diálogo para mostrar la vista previa imprimible del presupuesto.
      * TODO
      */
    @FXML
      private void handleVistaPrevia() {
    	Presupuesto selectedPresupuesto = presupuestosTable.getSelectionModel().getSelectedItem();
        if (selectedPresupuesto != null) {
    	 System.out.println(selectedPresupuesto.getEfectivo());}
     }

     /**
      * LLamado cuando el usuario tipea en la caja de texto de busqueda.
      * Actualiza la lista de presupuestos segun se haya seleccionado cuit o denominacion
      * en los radio buttons.
      * 
      */
     @FXML
     private void handleSearch() {

 		String busqueda = busquedaTextField.getText();
 		
     	if(cuitPresionado){
     		ObservableList<Presupuesto> listaPresupuestos = FXCollections.observableArrayList(DBMotor.verPresupuestosNoEfectivosPorCuit(busqueda));
     		mainApp.setPresupuestosNoEfectivosData(listaPresupuestos);
     	}
     	if(denomPresionado){
     		ObservableList<Presupuesto> listaPresupuestos = FXCollections.observableArrayList(DBMotor.verPresupuestosNoEfectivosPorDenominacion(busqueda));
     		mainApp.setPresupuestosNoEfectivosData(listaPresupuestos);
     	}
     	presupuestosTable.setItems(mainApp.getPresupuestosNoEfectivosData());
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
     
    private void deshabilitarComponentes(boolean value){
    	//Tablas
    	this.conceptosTable.setDisable(value);
    	this.presupuestosTable.setDisable(value);
    	
    	//Botones
    	this.editar.setDisable(value);
    	this.vistaPrevia.setDisable(value);
    	this.efectivizarTodos.setDisable(value);
    	this.efectivizarUno.setDisable(value);
    	
    	//Radiobutton
    	this.cuitRadioButton.setDisable(value);
    	this.denominacionRadioButton.setDisable(value);
    	
    	//Textfield
    	this.busquedaTextField.setDisable(value);
    	    	
    	//Label
    	this.alicuotaLabel.setDisable(value);
    	this.cuitLabel.setDisable(value);
    	this.denominacionLabel.setDisable(value);
    	this.fechaLabel.setDisable(value);
    	this.ivaLabel.setDisable(value);
    	this.montoLabel.setDisable(value);
    	this.numeroLabel.setDisable(value);
    	
    	
    }
     
    
    
}