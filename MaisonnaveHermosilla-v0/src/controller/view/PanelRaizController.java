package controller.view;



import javafx.fxml.FXML;
import controller.Main;

/**
 * Controlador para el Panel Raiz. El Panel Raiz proporciona
 * el layout basico de la aplicacion, conteniendo una barra de menues
 * y espacio para los Panes de JavaFX.
 * 
 * @author Maria Virginia Sabando
 */
public class PanelRaizController {

    // Referencia al controlador principal
    private Main mainApp;

    /**
     * Es llamado por la aplicación principal para dar una referencia a sí mismo.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    /*---------------------------MENU CLIENTES---------------------------*/

    /**
     * Funcionalidad menú Ver clientes
     */
    @FXML
    private void handleVerClientes() {
    	
    	mainApp.showClienteVista();
    	
    }
    
   

    /*---------------------------MENU FACTURACION---------------------------*/
    
    /**
     * Funcionalidad menú Nuevo presupuesto
     */
    @FXML
    private void handleNuevoPresupuesto() {
    	
    	mainApp.showPresupuestoVista();
    	
    }
    
    /**
     * Funcionalidad menú Facturar todos
     */
    @FXML
    private void handleFacturarTodos() {
    	
    	mainApp.showFacturacionVista();
    	
    }
    
    /*-----------------------MENU CUENTAS CORRIENTES-----------------------*/
    
    

    /*-----------------------------MENU AYUDA-----------------------------*/
    
    
   
    
    
    
}