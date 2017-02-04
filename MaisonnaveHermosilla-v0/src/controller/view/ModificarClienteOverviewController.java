package controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import controller.db.Cliente;
import controller.util.DateUtil;

/**
 * Diálogo para editar los detalles de un cliente especificado.
 * 
 * @author Maria Virginia Sabando
 */
public class ModificarClienteOverviewController {

    @FXML
    private TextField cuitField;
    @FXML
    private TextField denominacionField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField localidadField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField correoElectronicoField;
    @FXML
    private ChoiceBox<String> condicionIvaChoiceBox;
    @FXML
    private RadioButton	SI_habilitadoRadioButton;
    @FXML
    private RadioButton NO_habilitadoRadioButton;
    
    


    private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicked = false;

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

    /**
     * Setea el cliente a ser modificado en la vista.
     * 
     * @param cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        cuitField.setText(cliente.getCuit());
        denominacionField.setText(cliente.getDenominacion());
        direccionField.setText(cliente.getDireccion());
        localidadField.setText(cliente.getLocalidad());
        telefonoField.setText(cliente.getTelefono());
        correoElectronicoField.setText(cliente.getCorreoElectronico());
        
    }

    /**
     * Retorna true si el cliente presionó el botón OK, falso otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Llamado cuando el cliente presiona el botón Borrar Todo
     */
    @FXML
    private void handleBorrarTodo() {
        if (isInputValid()) {
            cliente.setCuit("");
            cliente.setDenominacion("");
            cliente.setDireccion("");
            cliente.setLocalidad("");
            cliente.setTelefono("");
            cliente.setCorreoElectronico("");
        }
    }

    /**
     * Llamado cuando el cliente presiona el botón OK
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            cliente.setCuit(cuitField.getText());
            cliente.setDenominacion(denominacionField.getText());
            cliente.setDireccion(direccionField.getText());
            cliente.setLocalidad(localidadField.getText());
            cliente.setTelefono(telefonoField.getText());
            cliente.setCorreoElectronico(correoElectronicoField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * LLamado cuando el cliente presiona el botón Cancelar
     */
    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    /**
     * Valida las entradas de datos en la pantalla de modificacion del cliente.
     * Por ahora solo pido que el cuit no sea nulo, y que la denominacion no sea nula.
     * 
     * @return true si la entrada es válida
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (cuitField.getText() == null || cuitField.getText().length() == 0) {
            errorMessage += "No valid first name!\n"; 
        }
        if (denominacionField.getText() == null || denominacionField.getText().length() == 0) {
            errorMessage += "No valid last name!\n"; 
        }
      

       

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, complete correctamente los campos.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}