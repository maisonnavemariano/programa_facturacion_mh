package controller;

import controller.db.Cliente;
//import controller.model.*;
import controller.view.*;
import controller.db.*;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * lista observable de clientes.
     */
    private ObservableList<Cliente> clienteData;
    
    /**
     * lista observable de presupuestos no efectivos para el area de trabajo.
     */
    private ObservableList<Presupuesto> presupuestosNoEfectivosData;
    
 
    /**
     * Motor de la base de datos.
     */
    private DBEngine DBMotor;
   
    /**
     * Constructor de la clase principal. Inicializa las listas de clientes y 
     * presupuestos que se cargan en las tablas en cada vista.
     */
    public Main() {
    	
    	DBMotor = DBSingleton.getInstance();
    	clienteData = FXCollections.observableArrayList(DBMotor.buscarCliente(""));
    	//DBMotor.facturarTodos(); //Solo con propositos de prueba.
    	presupuestosNoEfectivosData = FXCollections.observableArrayList(DBMotor.obtenerPresupuestosNoEfectivos());
    	
    }
    
    /**
     * Retorna el stage principal
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Retorna la informacion de clientes como una lista observable de Clientes. 
     * @return
     */
    public ObservableList<Cliente> getClienteData() {
        return clienteData;
    }
    
    /**
     * Setea lista observable de clientes 
     * @return
     */
    public void setClienteData(ObservableList<Cliente> listaClientes) {
       clienteData = listaClientes;
    }
    
    
    /**
     * Retorna la informacion de presupuestos no efectivos como una lista observable de Clientes. 
     * @return
     */
    public ObservableList<Presupuesto> getPresupuestosNoEfectivosData() {
        return presupuestosNoEfectivosData;
    }
    
    /**
     * Setea lista observable de presupuestos no efectivos 
     * @return
     */
    public void setPresupuestosNoEfectivosData(ObservableList<Presupuesto> listaPresupuestos) {
       presupuestosNoEfectivosData = listaPresupuestos;
    }
    
    /**
     * Setea lista observable de presupuestos no efectivos desde la DB
     * @return
     */
    public void setPresupuestosNoEfectivosData_DB() {
    	presupuestosNoEfectivosData = FXCollections.observableArrayList(DBMotor.obtenerPresupuestosNoEfectivos());
    	
    }
    
     /**
     * Disparador de la aplicación para el ejecutable, propio de JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicia la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Maisonnave Hermosilla - Estudio Contable ");
        this.primaryStage.setMaximized(true);
        //this.primaryStage.getIcons().add(new Image("file:Resources/Images/piggybank.png"));
      
        initRootLayout();
        
    }

    /**
     * Inicializa el Panel Raiz
     */
    public void initRootLayout() {
        try {
            // Carga el Panel Raiz desde el archivo .fxml correspondiente
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PanelRaiz.fxml"));
            rootLayout = (BorderPane) loader.load();
        
            // Brinda acceso a la apilcaciòn principal al controlador particular de la vista
            PanelRaizController controller = loader.getController();
            controller.setMainApp(this);
            
            // Muestra la scene conteniendo el Panel Raiz
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //---------------------CLIENTES-------------------------------------------

    /**
     * Muestra la vista ClienteVista dentro del Panel Raiz
     */
    public void showClienteVista() {
        try {
            // Carga la vista ClienteVista desde el archivo .fxml correspondiente
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ClienteVista.fxml"));
            AnchorPane clienteVista = (AnchorPane) loader.load();

            // Setea la vista en el centro del Panel Raiz
            rootLayout.setCenter(clienteVista);

            // Brinda acceso a la apilcaciòn principal al controlador particular de la vista
            ClienteOverviewController controller = loader.getController();
            controller.setMainApp(this, DBMotor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra la vista ModificarClienteOverview dentro del Panel Raiz
     */
    public boolean showModificarClienteOverview(Cliente cliente) {
        try {
            // Carga el archivo .fxml y crea un nuevo stage para el diálogo pop-up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ModificarClienteOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea el Stage para el diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar cliente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Setear los parametros del controlador       
            ModificarClienteOverviewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCliente(cliente);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Muestra la vista NuevoClienteOverview dentro del Panel Raiz
     */
    public boolean showNuevoClienteOverview(Cliente cliente) {
        try {
            // Carga el archivo .fxml y crea un nuevo stage para el diálogo pop-up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/NuevoClienteOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea el Stage para el diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nuevo cliente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Setear los parametros del controlador       
            NuevoClienteOverviewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCliente(cliente);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //---------------------PRESUPUESTOS-------------------------------------------------
    
    /**
     * Muestra la vista AreaDeTrabajoVista dentro del Panel Raiz
     * TODO: programar contenido e este metodo, similar a showClienteVista
     * TODO: ver si le cambio el nombre a este menu y a sus controllers
     */
    
    public void showAreaDeTrabajoVista(){
    	try {
            // Carga la vista AreaDeTrabajoVista desde el archivo .fxml correspondiente
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AreaDeTrabajoVista.fxml"));
            AnchorPane areaTrabajoVista = (AnchorPane) loader.load();

            // Setea la vista en el centro del Panel Raiz
            rootLayout.setCenter(areaTrabajoVista);

            // Brinda acceso a la apilcaciòn principal al controlador particular de la vista
            //TODO: Asignar controlador a la vista, y programarlo. por ahora muestra una ventana boba.
            AreaDeTrabajoOverviewController controller = loader.getController();
            controller.setMainApp(this, DBMotor);

        } catch (IOException e) {
            e.printStackTrace();
        }    	
    	
    }
    
    
    /**
     * Muestra la vista BuscarPresupuestosVista dentro del Panel Raiz
     * TODO: programar contenido e este metodo, similar a showClienteVista
     * TODO: ver si le cambio el nombre a este menu y a sus controllers
     */
    public void showBuscarPresupuestosVista(){
    	try {
            // Carga la vista BuscarPresupuestosVista desde el archivo .fxml correspondiente
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BuscarPresupuestosVista.fxml"));
            AnchorPane buscarPrespuestosVista = (AnchorPane) loader.load();

            // Setea la vista en el centro del Panel Raiz
            rootLayout.setCenter(buscarPrespuestosVista);

            // Brinda acceso a la apilcaciòn principal al controlador particular de la vista
            //TODO: Asignar controlador a la vista, y programarlo. por ahora muestra una ventana boba.
            //ClienteOverviewController controller = loader.getController();
            //controller.setMainApp(this, DBMotor);

        } catch (IOException e) {
            e.printStackTrace();
        }    	
    }
    
    /** 
     * TODO
     * 
     */
    public boolean showModificarPresupuestoOverview(Presupuesto presupuesto){
    	try {
            // Carga el archivo .fxml y crea un nuevo stage para el diálogo pop-up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ModificarPresupuestoOverview.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea el Stage para el diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar presupuesto");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Setear los parametros del controlador       
            ModificarPresupuestoOverviewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPresupuesto(presupuesto);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void handleNuevoPresupuestoVista(){
    	
    	//Primero instancio al cliente cuyo presupuesto se fabricará
    	Cliente cliente;
    	
    	//Creo un diálogo con una lista de clientes ordenados por denominación
    	//para seleccionar el cliente
    	Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Nuevo presupuesto");
    	dialog.setHeaderText(null);
    	dialog.setResizable(true); //ver por que no me entran los botones

    	Label label1 = new Label("Seleccione un cliente de la lista:");
    	
    	//Creo la lista de clientes
    	ChoiceBox<String> clientesChoiceBox = new ChoiceBox<String>();
    	ObservableList<String> listaDenominaciones = FXCollections.observableArrayList();
    	for(Cliente c : this.getClienteData()){
    		listaDenominaciones.add(c.getDenominacion());
    	}
    	clientesChoiceBox.setItems(listaDenominaciones);
    	
    	//Agrego listener para que se cargue el cliente seleccionado
    	clientesChoiceBox.setValue(listaDenominaciones.get(0));
    	//clientesChoiceBox jhbhbhvgvjgvcj TODO
    	cliente = this.getClienteData().get(0);
        
    	//Creo la grilla de componentes para el dialogo    	
    	GridPane grid = new GridPane();
    	grid.add(label1, 1, 1);
    	grid.add(clientesChoiceBox, 1, 2);
    	dialog.getDialogPane().setContent(grid);
    			
    	//genero los dos botones : generar presupuesto (OK) y cancelar.
    	ButtonType buttonTypeOk = new ButtonType("Generar presupuesto", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
    	
    	ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
    	dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

    	Optional<ButtonType> result = dialog.showAndWait();
    	
    	//Si el usuario elige Generar Presupuesto
    	if (result.get() == buttonTypeOk) {
    		
    		//Chequeo que el cliente no sea nulo
    		if (cliente != null){
    			//genero un presupuesto nuevo para ese cliente por medio del motor de DB
    			//TODO
    			//Presupuesto presupuesto = DBMotor.facturarBorrador();
    			//con ese presupuesto voy a la vista de modificar presupuesto
    			//this.showModificarPresupuestoOverview(presupuesto);
    			System.out.println("Hasta ahora todo ok");
    			
    		}
    		//Si el cliente es nulo, le aviso que no puede ser nulo
    		else{
    			//TODO: dialog apropiado
    			//por defecto ya tiene un cliente cargado al arranque, 
    			//entonces no deberia jamas poder ser nulo 
    		}
    	}
    	else{
    		//apretó cancelar, no hago nada y cierro
    	}
    }
    
}