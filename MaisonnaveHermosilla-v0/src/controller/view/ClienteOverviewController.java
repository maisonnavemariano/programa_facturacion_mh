package controller.view;

import controller.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import controller.Main;
import controller.model.Cliente;

public class ClienteOverviewController {
    @FXML
    private TableView<Cliente> clienteTable;
    @FXML
    private TableColumn<Cliente, String> cuitColumn;
    @FXML
    private TableColumn<Cliente, String> denominacionColumn;

    @FXML
    private Label cuitLabel;
    @FXML
    private Label denominacionLabel;
    @FXML
    private Label direccionLabel;
    @FXML
    private Label localidadLabel;
    @FXML
    private Label telefonoLabel;
    @FXML
    private Label correoElectronicoLabel;
    @FXML
    private Label condicionIvaLabel;
    @FXML
    private Label habilitadoLabel;

    // Reference to the main application.
    private Main mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ClienteOverviewController() {
    }
    
    /**
     * Fills all text fields (labels informativos) to show details about the cliente.
     * If the specified cliente is null, all text fields (labels) are cleared.
     * 
     * @param cliente the cliente or null
     */
    private void showClienteDetails(Cliente cliente) {
        if (cliente != null) {
            // Fill the labels with info from the cliente object.
            cuitLabel.setText(cliente.getCuit());
            denominacionLabel.setText(cliente.getDenominacion());
            direccionLabel.setText(cliente.getDireccion());
            localidadLabel.setText(cliente.getLocalidad());
            telefonoLabel.setText(cliente.getTelefono());
            correoElectronicoLabel.setText(cliente.getCorreoElectronico());
            condicionIvaLabel.setText(cliente.getTelefono());
            habilitadoLabel.setText(cliente.getCorreoElectronico());

        } else {
            // Cliente is null, remove all the text.
            cuitLabel.setText("");
            denominacionLabel.setText("");
            direccionLabel.setText("");
            localidadLabel.setText("");
            telefonoLabel.setText("");
            correoElectronicoLabel.setText("");
        }
    }
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new cliente.
     */
    @FXML
    private void handleNewCliente() {
        Cliente tempCliente = new Cliente();
        boolean okClicked = mainApp.showModificarClienteOverview(tempCliente);
        if (okClicked) {
            mainApp.getClienteData().add(tempCliente);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected cliente.
     */
    @FXML
    private void handleEditCliente() {
        Cliente selectedCliente = clienteTable.getSelectionModel().getSelectedItem();
        if (selectedCliente != null) {
            boolean okClicked = mainApp.showModificarClienteOverview(selectedCliente);
            if (okClicked) {
                showClienteDetails(selectedCliente);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Cliente Selected");
            alert.setContentText("Please select a cliente in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. USA EXPRESIONES LAMBDA.
     */
    @FXML
    private void initialize() {
        // Initialize the cliente table with the two columns.
        cuitColumn.setCellValueFactory(
                cellData -> cellData.getValue().cuitProperty());
        denominacionColumn.setCellValueFactory(
                cellData -> cellData.getValue().denominacionProperty());

        // Clear cliente details.
        showClienteDetails(null);

        // Listen for selection changes and show the cliente details when changed.
        clienteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showClienteDetails(newValue));
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteCliente() {
        int selectedIndex = clienteTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            clienteTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Cliente Selected");
            alert.setContentText("Please select a cliente in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        clienteTable.setItems(mainApp.getClienteData());
    }
}