package controller.view;
 
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import controller.Main;
import controller.db.Cliente;
import controller.db.Concepto;
import controller.db.DBEngine;
import controller.db.DBSingleton;
import controller.db.Presupuesto;
import exception.InvalidBudgetException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class VerPresupuestosOverviewController {
	
	//Elementos propios de todas las clases controller
	private Stage dialogStage;
	private Main mainApp;
	private DBEngine DBMotor = DBSingleton.getInstance();
	private final LocalDate EPOCH = LocalDate.of(2003, 1, 1);
	private final LocalDate NOW = LocalDate.now();
	private ObservableList<Presupuesto> ListaPresupuestos;
	
	//Elementos graficos de esta vista
	
	//BUSQUEDA
	 @FXML
	 private TextField cuitTextField;
	 @FXML
	 private TextField denominacionTextField;
	 @FXML
	 private RadioButton desdeHastaRadioButton;
	 @FXML
	 private RadioButton exactaRadioButton;
	 @FXML
	 private DatePicker desdeDatePicker;
	 @FXML
	 private DatePicker hastaDatePicker;
	 @FXML
	 private DatePicker exactaDatePicker;
	 @FXML
	 private Button buscarButton;
	 @FXML
	 private Button borrarCriteriosButton;
	 @FXML
	 private Button PDFButton;
	 
	 //TABLA PRESUPUESTOS
	 @FXML
	 private TableView<Presupuesto> presupuestosTable;
	 @FXML
	 private TableColumn<Presupuesto, Number> Nro_Column;
	 @FXML
	 private TableColumn<Presupuesto, String> cuit_Column;
	 @FXML
	 private TableColumn<Presupuesto, String> denominacion_Column;
	 
	 //TABLA CONCEPTOS
	 @FXML
	 private TableView<Concepto> conceptosTable;
	 @FXML
	 private TableColumn<Concepto, String> descripcion_Column;
	 @FXML
	 private TableColumn<Concepto, String> importe_Column;
	 @FXML
	 private Label nroLabel;
	 @FXML
	 private Label cuitLabel;
	 @FXML
	 private Label denominacionLabel;
	 @FXML
	 private Label ivaLabel;
	 @FXML
	 private Label alicuotaLabel;
	 @FXML
	 private Label fechaLabel;
	 @FXML
	 private Label subtotalLabel;
	 @FXML
	 private Label montoTotalLabel;
	 @FXML
	 private Label resultadosEncontradosLabel;
	 
	 //BOTONES GENERALES
	 @FXML
	 private Button vistaPreviaButton;
	 @FXML
	 private Button imprimirButton;
	 @FXML
	 private Button imprimirTodosButton;
	 
	 //BOTONES PARTICULARES
	 @FXML
	 private Button detallesButton;
	 @FXML
	 private Button anularButton;
	
	 //BOOLEANOS PARA LA BUSQUEDA POR FECHA
	 private boolean desdeHastaPresionado = true;
	 private boolean exactaPresionado = false;
	 
	 private DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 private DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 
	public VerPresupuestosOverviewController(){
	}
	
	/**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp, DBEngine motor) {
        this.mainApp = mainApp;
        this.DBMotor = motor;

    }
    
    /**
     * Inicializa la clase controlador. Este método es llamado automáticamente
     * luego de que el archivo .fxml ha sido cargado.
     */
    @FXML
    private void initialize() {
    	
    	// Inicializa la tabla de presupuestos con los valores de las 3 columnas.
    	Nro_Column.setCellValueFactory(
    			cellData -> cellData.getValue().NroPresupuestoProperty());
        cuit_Column.setCellValueFactory(
                cellData -> cellData.getValue().getCliente().cuitProperty());
        denominacion_Column.setCellValueFactory(
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
        
        desdeDatePicker.setPromptText(this.EPOCH.format(formatter_2));
        hastaDatePicker.setPromptText(this.NOW.format(formatter_2));
        exactaDatePicker.setPromptText(this.NOW.format(formatter_2));
                
        cuitTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (oldText.length() < 11 && newText.length() >= 11) {
            	cuitTextField.getParent().requestFocus();
            }
         });
        
        denominacionTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (oldText.length() < 60 && newText.length() >= 60) {
            	denominacionTextField.getParent().requestFocus();
            }
         });
        
        cuitTextField.textProperty().addListener((obs, oldText, newText) -> {
	   		if (newText.matches("\\d*")) {
   			 	cuitTextField.setText(newText);
   		 	}
   		 	else
   			 	cuitTextField.setText(oldText); 
   	 	}); 
        
        
        final Callback<DatePicker, DateCell> HCellFactory = 
    	        new Callback<DatePicker, DateCell>() {
    	            @Override
    	            public DateCell call(final DatePicker datePicker) {
    	                return new DateCell() {
    	                    @Override
    	                    public void updateItem(LocalDate item, boolean empty) {
    	                        super.updateItem(item, empty);
    	                        LocalDate piso;
    	                        if(desdeDatePicker.getValue()==null){
    	                        	piso = LocalDate.of(2003, 1, 1);
    	                        }
    	                        else{
    	                        	piso = desdeDatePicker.getValue().plusDays(1);
    	                        }
    	                        if (item.isBefore(piso)){
    	                                setDisable(true);
    	                                setStyle("-fx-background-color: #d3d3d3;");
    	                        }
    	                        if (item.isAfter(
    	                                LocalDate.now()) 
    	                        		){
    	                                setDisable(true);
    	                                setStyle("-fx-background-color: #d3d3d3;");
    	                        }
    	                        if (item.isBefore(
    	                        		LocalDate.of(2003, 1, 1)) 
    	                        		){
    	                                setDisable(true);
    	                                setStyle("-fx-background-color: #d3d3d3;");
    	                        }
    	                    }
    	                };
    	            }
        		};
    	    
    	final Callback<DatePicker, DateCell> DECellFactory = 
        	        new Callback<DatePicker, DateCell>() {
        	            @Override
        	            public DateCell call(final DatePicker datePicker) {
        	                return new DateCell() {
        	                    @Override
        	                    public void updateItem(LocalDate item, boolean empty) {
        	                        super.updateItem(item, empty);
        	                        if (item.isAfter(
        	                                LocalDate.now()) 
        	                        		){
        	                                setDisable(true);
        	                                setStyle("-fx-background-color: #d3d3d3;");
        	                        }
        	                        if (item.isBefore(
        	                        		LocalDate.of(2003, 1, 1)) 
        	                        		){
        	                                setDisable(true);
        	                                setStyle("-fx-background-color: #d3d3d3;");
        	                        }
        	                       
        	                }
        	            };
        	        }
        	    };
        
    	hastaDatePicker.setDayCellFactory(HCellFactory);
    	desdeDatePicker.setDayCellFactory(DECellFactory);
    	exactaDatePicker.setDayCellFactory(DECellFactory);
    	    
    	hastaDatePicker.setEditable(false);
    	desdeDatePicker.setEditable(false);
    	exactaDatePicker.setEditable(false);
    	exactaDatePicker.setDisable(true);
    	
    	resultadosEncontradosLabel.setText("");
        
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
     * Completa los labels informativos para mostrar detalles del presupuesto seleccionado.
     * Si no se ha seleccionado ningun presupuesto (presupuesto null), todos los labels son clareados.
     * 
     * @param presupuesto o null
     * 
     */
    private void showPresupuestoDetails(Presupuesto presupuesto) {
        if (presupuesto != null) {
        	
        	Cliente cliente= presupuesto.getCliente();
        	
            // Completa los labels con info de la instancia presupuesto.

            nroLabel.setText(String.valueOf(presupuesto.getNroPresupuesto()));
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
            
            String fechaMostrar = presupuesto.getFecha_ARG().replaceAll("-", "/");
            fechaLabel.setText(fechaMostrar);
            
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
            montoTotalLabel.setText(String.valueOf(presupuesto.calcularMontoTotal()));
          
            conceptosTable.setItems(presupuesto.getConceptosObservables());
            descripcion_Column.setCellValueFactory(
    			cellData -> cellData.getValue().getConceptoProperty());
            
    		importe_Column.setCellValueFactory(
    			cellData -> cellData.getValue().getMontoConceptoStringProperty());
            

        } else {
        	
            // Presupuesto nulo, clarear todas las etiquetas.
        	nroLabel.setText("");
            cuitLabel.setText("");
            denominacionLabel.setText("");
            ivaLabel.setText("");
            alicuotaLabel.setText("");
            subtotalLabel.setText("");
            montoTotalLabel.setText("");
            conceptosTable.setItems(null);
            fechaLabel.setText("");
            
        }
        
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
     * LLamado cuando el usuario tipea en la caja de texto de busqueda.
     * Actualiza la lista de presupuestos segun se haya seleccionado cuit o denominacion
     * en los radio buttons.
     * 
     */
    @FXML
    private void handleBusqueda() {
    	String errorMessage = "";
    	//Primero recupero todos los valores que potencialmente utilizaria en la busqueda
		String cuit="", denominacion="", fecha_desde="", fecha_hasta="", fecha_exacta="";
		
		if(cuitTextField.getText()!=null)
			cuit = cuitTextField.getText();
			
		if(denominacionTextField.getText()!=null)
			denominacion=denominacionTextField.getText();
		
		//Luego, establezco cuales son los criterios de búsqueda y busco
		
		if(desdeHastaPresionado){
			
			if(desdeDatePicker.getValue()!=null)
				fecha_desde = desdeDatePicker.getValue().format(formatter_1);
			else{
				fecha_desde = this.EPOCH.format(formatter_1);
				desdeDatePicker.setValue(this.EPOCH);
			}
			
			if(hastaDatePicker.getValue()!=null)
				fecha_hasta = hastaDatePicker.getValue().format(formatter_1);
			else{
				fecha_hasta = this.NOW.format(formatter_1);
				hastaDatePicker.setValue(this.NOW);
			}
			
			//Chequeo errores en las fechas
			boolean error = false;
			if(desdeDatePicker.getValue().isAfter(NOW) || hastaDatePicker.getValue().isAfter(NOW)){
				errorMessage = errorMessage.concat("Las fechas inicial y final no pueden ser posteriores a la fecha de hoy.\n\n");
				error=true;
			}
			if(hastaDatePicker.getValue().isBefore(desdeDatePicker.getValue())){
				errorMessage = errorMessage.concat("La fecha final no puede ser anterior a la fecha inicial.\n\n");
				error=true;
			}
			
			if(error){
				desdeDatePicker.setValue(null);
				hastaDatePicker.setValue(null);
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(dialogStage);
				alert.setTitle("Criterios de búsqueda incorrectos.");
				alert.setHeaderText(null);
				alert.setContentText(errorMessage);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
			}
			else{//Si no hay error procedo a la busqueda en la DB
				this.ListaPresupuestos = FXCollections.observableArrayList(DBMotor.BuscarDesdeHasta(denominacion, cuit, fecha_desde, fecha_hasta));
			}
			
		}
		if(exactaPresionado){

			if(exactaDatePicker.getValue()!=null)
				fecha_exacta = exactaDatePicker.getValue().format(formatter_1);
			else{
				fecha_exacta = this.NOW.format(formatter_1);
				exactaDatePicker.setValue(this.NOW);
			}
			
			//Chequeo errores en la fecha
			boolean error = false;
			if(exactaDatePicker.getValue().isAfter(NOW)){
				errorMessage = errorMessage.concat("La fecha ingresada no puede ser posterior a la fecha de hoy.\n\n");
				error=true;
			}
						
			if(error){
				exactaDatePicker.setValue(null);
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(dialogStage);
				alert.setTitle("Criterios de búsqueda incorrectos.");
				alert.setHeaderText(null);
				alert.setContentText(errorMessage);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
			}
			else{//Si no hay error procedo a la busqueda en la DB
				this.ListaPresupuestos = FXCollections.observableArrayList(DBMotor.BuscarFechaExacta(denominacion, cuit, fecha_exacta));
			}
		}
		int c = ListaPresupuestos.size();
		this.resultadosEncontradosLabel.setText(c + " resultados.");
		presupuestosTable.setItems(this.ListaPresupuestos);

		
    }
    
    /**
     * LLamado cuando el usuario selecciona el radiobutton cuit
     */
     @FXML
     private void handleDesdeHastaRadioButton() {
 	   desdeHastaPresionado = true;
 	   exactaPresionado = false;
 	   exactaDatePicker.setDisable(true);
 	   desdeDatePicker.setDisable(false);
 	   hastaDatePicker.setDisable(false);
    }
    
    /**
     * LLamado cuando el usuario selecciona el radiobutton cuit
     */
     @FXML
     private void handleExactaRadioButton() {
 	   desdeHastaPresionado = false;
 	   exactaPresionado = true;
 	   desdeDatePicker.setDisable(true);
 	   hastaDatePicker.setDisable(true);
 	   exactaDatePicker.setDisable(false);
 	   
    }
     
    public void handleBorrarCriterios(){
    	cuitTextField.setText("");
    	denominacionTextField.setText("");
    	desdeDatePicker.setValue(null);
    	hastaDatePicker.setValue(null);
    	exactaDatePicker.setValue(null);
    }
    
    public void handleVistaPrevia(){}
    
    public void handleImprimir(){}
    
    public void handleImprimirTodos(){}
    
    public void handleAnular(){
    	Presupuesto seleccionado = presupuestosTable.getSelectionModel().getSelectedItem();
    	if(seleccionado == null){
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(dialogStage);
    		alert.setTitle("Seleccionar presupuesto");
    		alert.setHeaderText(null);
    		alert.setContentText("No ha seleccionado ningún presupuesto de la lista.");
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		alert.showAndWait();
    	}
    	else{
    		Alert alert = new Alert(AlertType.CONFIRMATION,"",ButtonType.OK, ButtonType.CANCEL);
    		alert.initOwner(dialogStage);
    		alert.setTitle("Anular presupuesto efectivo");
    		alert.setHeaderText("Presupuesto Nº "+ seleccionado.getNroPresupuesto()+" - "+ seleccionado.getCliente().getDenominacion());
    		alert.setContentText("¿Desea anular el presupuesto seleccionado?\n\nEsta acción modificará el estado de cuenta corriente del cliente.");
    		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    		Optional<ButtonType> resultado = alert.showAndWait();
    		
    		if (resultado.get().equals(ButtonType.OK)){
    			//Primero anulo el presupuesto
    			try {
					DBMotor.desefectivizar(seleccionado);
				} catch (InvalidBudgetException e) {
					System.out.println("fallo el desefectivizar presupuesto");
					e.printStackTrace();
				}
    			
    			//Actualizo la vista
    			this.ListaPresupuestos.remove(this.ListaPresupuestos.indexOf(seleccionado));
    			this.presupuestosTable.setItems(ListaPresupuestos);
    			
    			//Luego informo al usuario que salio todo bien
    			alert = new Alert(AlertType.INFORMATION);
        		alert.initOwner(dialogStage);
        		alert.setTitle("Anular presupuesto efectivo");
        		alert.setHeaderText(null);
        		alert.setContentText("El presupuesto Nº "+ seleccionado.getNroPresupuesto() + " fue anulado.\n\nPuede editarlo o eliminarlo definitivamente en el menú Area de Trabajo.");
        		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        		alert.showAndWait();
    			
    		}
    		else{
    			//No hago nada y cierro el alerta ppal.
    		}
    	}
    }
    
    public void handleVerDetalle(){
    	if(presupuestosTable.getSelectionModel().getSelectedItem() != null)
		mainApp.showDetallePresupuestoVista(presupuestosTable.getSelectionModel().getSelectedItem());
	else{
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(dialogStage);
		alert.setTitle("Seleccionar presupuesto");
		alert.setHeaderText(null);
		alert.setContentText("No ha seleccionado ningún presupuesto de la lista.");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
	}}
    
    @FXML
    private void handleGuardarPDF(){
    	FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Archivos PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(dialogStage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".pdf")) {
                file = new File(file.getPath() + ".pdf");
            }
            //mainApp.savePersonDataToFile(file);
        }
    }
}
