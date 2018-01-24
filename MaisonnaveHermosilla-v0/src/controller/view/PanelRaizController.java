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
    private void handleAdministrarClientes() {
    	
    	mainApp.showClienteVista();
    	
    }
    
   

    /*---------------------------MENU PRESUPUESTOS---------------------------*/
    
    /**
     * Funcionalidad menú Generar Presupuestos mensuales
     * TODO: ver si le cambio el nombre a este menu y a su controllers
     * TODO: IMPLEMENTAR, ESTE VA A SER UN QUILOMBO.
     */
    @FXML
    private void handleGenerarPresupuestosMensuales() {    	
    }
    
    /**
     * Funcionalidad menú Area de trabajo
     * TODO: ver si le cambio el nombre a este menu y sus controllers
     */
    @FXML
    private void handleAreaDeTrabajo() {
    	
    	mainApp.showAreaDeTrabajoVista();
    	
    }
    
    /**
     * Funcionalidad menú Buscar Presupuestos
     * TODO: ver si le cambio el nombre a este menu y sus controllers
     */
    @FXML
    private void handleBuscarPresupuestos() {
    	
    	mainApp.showBuscarPresupuestosVista();
    	
    }
    
    /*-----------------------MENU CUENTAS CORRIENTES-----------------------*/
    
    

    /*-----------------------------MENU AYUDA-----------------------------*/
    
    
   
    
    
    
}