package controller.view;

import controller.Main;
import controller.db.DBEngine;
import controller.db.DBSingleton;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VerPresupuestosOverviewController {
	
	  private Stage dialogStage;
	private Main mainApp;
	private DBEngine DBMotor = DBSingleton.getInstance();

	
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
    	
    }
    
    /**
     * Setea el stage para este diálogo
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
       
    }

	
}
