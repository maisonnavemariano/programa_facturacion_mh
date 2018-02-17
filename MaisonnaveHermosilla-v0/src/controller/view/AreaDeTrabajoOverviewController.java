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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.util.Callback;
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
    private TableColumn<Presupuesto, Number> numeroColumn;
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
    private Label ivaLabel;
    @FXML
    private Label alicuotaLabel;
    @FXML
    private Label montoLabel;
    @FXML
    private Label subtotalLabel;
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
    @FXML
    private Button descartarTodos;
    @FXML
    private Button descartarUno;
    @FXML
    private Button generarPresupuestosMensuales;
    
    
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
            
            
            Float ali = presupuesto.getAlicuota();
            if(ali!= null){
            	if(ali == 0.0){
            		alicuotaLabel.setText("0%");
                }
                else if(ali == 10.5){
                	alicuotaLabel.setText("10,5%");
                }
                else if(ali == 21.0){
                	alicuotaLabel.setText("21%");
                }
                else{
                	alicuotaLabel.setText("");
                }
            }
            
            subtotalLabel.setText(String.valueOf(presupuesto.getSubtotal()));
            montoLabel.setText(String.valueOf(presupuesto.calcularMontoTotal()));
          
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
            ivaLabel.setText("");
            alicuotaLabel.setText("");
            subtotalLabel.setText("");
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
        // Inicializa la tabla de presupuestos con los valores de las 3 columnas.
    	numeroColumn.setCellValueFactory(
    			cellData -> cellData.getValue().NroPresupuestoProperty());
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
        presupuestosTable.setPlaceholder(new Label("No hay presupuestos para mostrar."));
        presupuestosTable.getColumns().forEach(this::addTooltipToColumnCells_Presupuesto);
        
        conceptosTable.getColumns().forEach(this::addTooltipToColumnCells_Concepto);
        conceptosTable.setPlaceholder(new Label("No hay conceptos."));
    	
        //generarPresupuestosMensuales.disableProperty().bind(Bindings.size(presupuestosTable.getItems()).isEqualTo(0));
        
    
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
              Alert alert = new Alert(AlertType.CONFIRMATION, 
 		  			 "",
                    ButtonType.YES, 
                    ButtonType.NO);
              alert.initOwner(mainApp.getPrimaryStage());
              alert.setTitle("Efectivizar presupuesto");
              alert.setHeaderText("Presupuesto seleccionado: "+ selectedPresupuesto.getNroPresupuesto()+ " - "
            		  			  + selectedPresupuesto.getCliente().getDenominacion());
              alert.setContentText("¿Desea efectivizar el presupuesto seleccionado?");
              alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
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
                  alert.setHeaderText(null);
                  alert.setContentText("Se ha efectivizado el presupuesto nº "+ selectedPresupuesto.NroPresupuestoStringProperty().get());
                  alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                  alert.showAndWait();
              }
              else{
             	 //No hago nada y cierro
              }
    	 } 
         else {
             // No se seleccionó ningún cliente
             Alert alert = new Alert(AlertType.WARNING);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Seleccionar presupuesto");
             alert.setHeaderText(null);
             alert.setContentText("No se ha seleccionado un presupuesto de la lista.");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             alert.showAndWait();
         }
     }

     /**
      * LLamado cuando el usuario hace click en el boton Efectivizar Todos.
      * Abre un alerta donde dice cuantos presupuestos se harán efectivos, y pide confirmación.
      * 
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
             Alert alert = new Alert(AlertType.CONFIRMATION, 
 		  			 "",
                    ButtonType.YES, 
                    ButtonType.NO);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Efectivizar todo");
             alert.setHeaderText("Cantidad total de presupuestos: " + lista.size());
             alert.setContentText("¿Desea efectivizar TODOS los presupuestos no efectivos?");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
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
                        try{           
                        	
                        	//Actualizo tabla y muestro alerta de finalizacion
                        	Platform.runLater(() -> {
         		    		
                        		//Actualizo contenido tabla en la vista: deberia no retornar nada
                            	clarearTablaYLista();
                            	
         		    		//Se informa al usuario que termino el proceso
         		           Alert alert = new Alert(AlertType.INFORMATION, 
         		  	  			 "",
         		                 ButtonType.OK);
         		           alert.initOwner(mainApp.getPrimaryStage());
         		           alert.setTitle("Efectivizar todo");
         		           alert.setHeaderText(null);
         		           alert.setContentText("Se han efectivizado "+ lista.size() + " presupuestos.\n\nPuede consultar los presupuestos efectivos en el menú Buscar Presupuestos.");
         		           alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
         		           alert.showAndWait();
         		           barraProgreso.progressProperty().unbind();
         		           barraProgreso.progressProperty().set(0);
         		           
                         });
         		    	 }
                         catch(IllegalStateException e){
                        	System.out.println("El problema esta en el Efectivizar todos: illegal state exception");
                         }
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
             alert.setHeaderText(null);
             alert.setContentText("No existen presupuestos no efectivos.");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             alert.showAndWait();
         }
     }
     
     
     /**
      * LLamado cuando el usuario hace click en el boton Editar. Abre un 
      * diálogo para editar el presupuesto seleccionado.
      * 
      * (tal vez debo solo mostrar los resultados, al igual que en la vista cliente. en ese caso descomentar)
      */
     @FXML
     private void handleEditarPresupuesto() {
         Presupuesto selectedPresupuesto = presupuestosTable.getSelectionModel().getSelectedItem();
         if (selectedPresupuesto != null) {
             boolean okClicked = mainApp.showModificarPresupuestoOverview(selectedPresupuesto, this);
             if (okClicked) {
                 showPresupuestoDetails(selectedPresupuesto);
             }
         } 
         else{
             // Nothing selected.
             Alert alert = new Alert(AlertType.WARNING);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Seleccionar presupuesto");
             alert.setHeaderText(null);
             alert.setContentText("No se ha seleccionado un presupuesto de la lista.");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
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
    
    //------------------------BUSQUEDA-----------------------------------

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
     
     private void clarearTablaYLista(){
    	 ObservableList<Presupuesto> listaPresupuestos = FXCollections.observableArrayList(DBMotor.obtenerPresupuestosNoEfectivos());
    	 mainApp.setPresupuestosNoEfectivosData(listaPresupuestos);
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
     
     //-----------------------------OTROS----------------------------------
   
     private void deshabilitarComponentes(boolean value){
    	//Tablas
    	this.conceptosTable.setDisable(value);
    	this.presupuestosTable.setDisable(value);
    	
    	//Botones
    	this.editar.setDisable(value);
    	this.vistaPrevia.setDisable(value);
    	this.efectivizarTodos.setDisable(value);
    	this.efectivizarUno.setDisable(value);
    	this.descartarTodos.setDisable(value);
    	this.descartarUno.setDisable(value);
    	this.generarPresupuestosMensuales.setDisable(value); //TODO: este se configura aparte
    	    	
    	//Radiobutton
    	this.cuitRadioButton.setDisable(value);
    	this.denominacionRadioButton.setDisable(value);
    	
    	//Textfield
    	this.busquedaTextField.setDisable(value);
    	    	
    	//Label
    	this.alicuotaLabel.setDisable(value);
    	this.cuitLabel.setDisable(value);
    	this.denominacionLabel.setDisable(value);
    	this.ivaLabel.setDisable(value);
    	this.subtotalLabel.setDisable(value);
    	this.montoLabel.setDisable(value);
    	this.numeroLabel.setDisable(value);
    	
    	
    }
     
     private <T> void addTooltipToColumnCells_Concepto(TableColumn<Concepto,T> column) {

    	    Callback<TableColumn<Concepto, T>, TableCell<Concepto,T>> existingCellFactory 
    	        = column.getCellFactory();

    	    column.setCellFactory(c -> {
    	        TableCell<Concepto, T> cell = existingCellFactory.call(c);

    	        Tooltip tooltip = new Tooltip();
    	        // can use arbitrary binding here to make text depend on cell
    	        // in any way you need:
    	        tooltip.textProperty().bind(cell.itemProperty().asString());

    	        cell.setTooltip(tooltip);
    	        return cell;
    	    });
    	}
     

     private <T> void addTooltipToColumnCells_Presupuesto(TableColumn<Presupuesto,T> column) {

 	    Callback<TableColumn<Presupuesto, T>, TableCell<Presupuesto,T>> existingCellFactory 
 	        = column.getCellFactory();

 	    column.setCellFactory(c -> {
 	        TableCell<Presupuesto, T> cell = existingCellFactory.call(c);

 	        Tooltip tooltip = new Tooltip();
 	        // can use arbitrary binding here to make text depend on cell
 	        // in any way you need:
 	        tooltip.textProperty().bind(cell.itemProperty().asString());
 	       
 	        cell.setTooltip(tooltip);
 	        return cell;
 	    });
 	}
     
     /**
      * Permite al usuario realizar la facturación en borrador para todo los clientes
      * habilitados, si y solo si no existe actualmente ningún presupuesto no efectivo 
      * en la DB.
      * 
      */
     public void handleGenerarPresupuestosMensuales(){
     	
     	//Primero, pregunto si ya hay no efectivos en la DB
     	boolean noHay = (DBMotor.obtenerPresupuestosNoEfectivos().size() == 0);
     	//Si no hay no efectivos, procedo con el método.
     	if (noHay){
     		 //Primero averiguo cuantos presupuestos debo generar
    		int cantidad = DBMotor.cantidadClientesHabilitados();
    		
     		//Creo una alerta para pedir confirmacion
     		Alert alert = new Alert(AlertType.CONFIRMATION, 
 		  			 "",
                    ButtonType.OK,
                    ButtonType.CANCEL);
              alert.initOwner(mainApp.getPrimaryStage());
              alert.setTitle("Generar presupuestos mensuales");
              alert.setHeaderText(null);
              alert.setContentText("¿Desea generar "+cantidad+" nuevos presupuestos para los clientes habilitados?");
              alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
              Optional<ButtonType> result = alert.showAndWait();
              
           
              	//Si el usuario elige Generar Presupuestos mensuales = OK
          	 	if (result.get() == ButtonType.OK) {
          		 
          	 	//Deshabilito componentes
            	deshabilitarComponentes(true);
            		
          		//Generar alert con la barra de progreso 
          		//Creo la barra de progreso
           		
           		barraProgreso.progressProperty().unbind();
           		barraProgreso.progressProperty().set(0);
           		
           		
           		//Creo el dialogo que la va a mostrar
           		Alert dialog = new Alert(AlertType.INFORMATION);
              	dialog.setTitle("Generando "+cantidad+" presupuestos mensuales...");
              	dialog.setHeaderText(null);
              	dialog.setContentText("Operación en proceso. Por favor, aguarde unos instantes.");
              	dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
              	dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
              	dialog.show();
              	
          		//Creo el task para actualizar la barra de progreso
              	Task<Void> task_barra = new Task<Void>() {
          		    @Override
          		    protected Void call() throws Exception {
          		    	       		    	
          		    	
          		    	for (int i=0; i<= cantidad; i++){
              		    	updateProgress(i, cantidad);
                 			 	Thread.sleep(115);
          		    	}
          		    
                    	Platform.runLater(() -> {
                    		deshabilitarComponentes(false);
                    		dialog.close();
                    		barraProgreso.progressProperty().unbind();
                    		barraProgreso.progressProperty().set(0);
     		           
                    	});
          		    	return null;
          		    };
              	}; //Fin task barra
              	
              	//Creo el task para generar los borradores
              	Task<Void> task_borradores = new Task<Void>() {
         		    @Override
         		    protected Void call() throws Exception {
         		    	
         		    	//Primero hago lo que debo
         		    	DBMotor.facturarTodos();
         		    	
         		    	//Despues actualizo la vista
         		    	handleSearch();
         		    	
         		    	
         		    	return null;
         		    };
              	};//Fin task generar borradores
              	
              	//Bindea la barra de progreso 
                 barraProgreso.progressProperty().unbind();
                 barraProgreso.progressProperty().bind(task_barra.progressProperty());
               
                 //Arranco la efectivizacion de todos los presupuestos
  		    	 new Thread(task_barra).start();;
                 new Thread(task_borradores).start();
          		
          	 }
          	 //Si no, si el usuario elige Generar Presupuestos mensuales = CANCEL
          	 else{
          		 //No hacer nada.
          	 }
          	 
     	}
     	//sino, es decir, si SI hay no efectivos, aviso al usuario que no se puede proceder.
     	else{
     		Alert alert = new Alert(AlertType.WARNING, 
 		  			 "",
                   ButtonType.OK);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Generar presupuestos mensuales");
             alert.setHeaderText("No es posible generar nuevos presupuestos mensuales.");
             alert.setContentText("Aún existen presupuestos no efectivos en la base de datos. \n\nPuede ver y editar dichos presupuestos en el menú Área de Trabajo.");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             alert.showAndWait();
     	}
     }
     
     public void handleDescartarUno(){
    	 Presupuesto selectedPresupuesto = presupuestosTable.getSelectionModel().getSelectedItem();
         
    	 if (selectedPresupuesto != null) {
    		 Alert alert = new Alert(AlertType.CONFIRMATION,
    				 "",
    				 ButtonType.OK,
    				 ButtonType.CANCEL);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Descartar presupuesto");
             alert.setHeaderText(null);
             alert.setContentText("¿Desea descartar el presupuesto seleccionado?");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             Optional<ButtonType> result = alert.showAndWait();
             
             if(result.get().equals(ButtonType.OK)){
            	 //Eliminar el presupuesto de todos lados:
            	 //1: de la DB
            	 try {
					DBMotor.eliminarNoEfectivo(selectedPresupuesto);
				} catch (InvalidBudgetException e) {
					System.out.println("Error al descartar uno");
					e.printStackTrace();
				}
            	 //2: de la vista
            	 handleSearch();
            	 //3: de la lista en mainapp
            	 mainApp.setPresupuestosNoEfectivosData_DB();
             }
         } 
         else{
        	 // Nothing selected.
             Alert alert = new Alert(AlertType.WARNING);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Seleccionar presupuesto");
             alert.setHeaderText(null);
             alert.setContentText("No se ha seleccionado un presupuesto de la lista.");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             alert.showAndWait();
         }
     }
     
     public void handleDescartarTodos(){
    	 if (presupuestosTable.getItems().size()>0) {
    		 Alert alert = new Alert(AlertType.CONFIRMATION,
    				 "",
    				 ButtonType.OK,
    				 ButtonType.CANCEL);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Descartar todos");
             alert.setHeaderText(null);
             alert.setContentText("¿Desea descartar todos los presupuestos de la lista?");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             Optional<ButtonType> result = alert.showAndWait();
             
             if(result.get().equals(ButtonType.OK)){
            	 //Eliminar el presupuesto de todos lados:
            	 
            	 //1: de la DB
            	 DBMotor.eliminarPresupuestosNoEfectivos();
				
            	 //2: de la vista
            	 handleSearch();
            	 
            	 //3: de la lista en mainapp
            	 mainApp.setPresupuestosNoEfectivosData_DB();
             }
         } 
         else{
        	 // Nothing selected.
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Descartar todos");
             alert.setHeaderText(null);
             alert.setContentText("No existen presupuestos para descartar.");
             alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
             alert.showAndWait();
         }
     }
     
     
     
     
     
     
     
     
    
}