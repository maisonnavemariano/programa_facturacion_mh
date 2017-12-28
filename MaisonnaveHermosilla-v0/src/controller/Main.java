package controller;

import controller.db.Cliente;
//import controller.model.*;
import controller.view.*;
import controller.db.*;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

//comentario que hay que borrar luego.
public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Clientes.
     */
    private ObservableList<Cliente> clienteData;
 
    /**
     * Motor de la base de datos.
     */
    private DBEngine DBMotor;
   
    /**
     * Constructor vacío
     */
    public Main() {
    	
    	DBMotor = DBSingleton.getInstance();
    	clienteData = FXCollections.observableArrayList(DBMotor.buscarCliente(""));
    	
    }
    
    /**
     * Retorna el stage principal
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Returns the data as an observable list of Clientes. 
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
     * Muestra la vista PresupuestoVista dentro del Panel Raiz
     */
    //TODO: programar contenido e este metodo, similar a showClienteVista
    public void showPresupuestoVista(){}
    
    
    /**
     * Muestra la vista FacturacionVista dentro del Panel Raiz
     */
    //TODO: programar contenido e este metodo, similar a showClienteVista
    public void showFacturacionVista(){}
    
    
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

    
    
}