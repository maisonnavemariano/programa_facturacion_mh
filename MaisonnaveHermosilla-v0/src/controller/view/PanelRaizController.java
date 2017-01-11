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
    	
    }

    /**
     * Funcionalidad menú Nuevo cliente
     */
    @FXML
    private void handleNuevoCliente() {
       
    }

    /**
     * Funcionalidad menú Editar cliente
     */
    @FXML
    private void handleEditarCliente() {
        
    }

    /**
     * Funcionalidad menú Eliminar cliente
     */
    @FXML
    private void handleEliminarCliente() {
        
    }
    
    /**
     * Funcionalidad del menu Salir
     */
    @FXML
    private void handleSalir() {
        System.exit(0);
    }

    /*---------------------------MENU FACTURACION---------------------------*/
    
    /**
     * Funcionalidad menú Facturar todos los clientes
     */
    @FXML
    private void handleFacturarTodos() {
       
    }
    
    /**
     * Funcionalidad menú Facturar cliente único
     */
    @FXML
    private void handleFacturarUnico() {
       
    }

    /**
     * Funcionalidad menú Ver presupuestos
     */
    @FXML
    private void handleVerPresupuestos() {
       
    }
    
    /*-----------------------MENU CUENTAS CORRIENTES-----------------------*/
    
    /**
     * Funcionalidad menú Ver cuentas
     */
    @FXML
    private void handleVerCuentas() {
       
    }

    /**
     * Funcionalidad menú Resumen de cuenta
     */
    @FXML
    private void handleResumenCuenta() {
       
    }

    /*-----------------------------MENU AYUDA-----------------------------*/
    
    /**
     * Funcionalidad menú Manual de uso
     */
    @FXML
    private void handleManualUso() {
       
    }

    /**
     * Funcionalidad menú Acerca de...
     */
    @FXML
    private void handleAcercaDe() {
       
    }
   
}