package view

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.stage.Stage
import model.ShellyAppManager

class ConnectController {

    @FXML
    private lateinit var connectBtn: Button

    @FXML
    private lateinit var validInputLabel: Label

    @FXML
    private lateinit var userNameField: TextField

    @FXML
    private lateinit var passwordField: PasswordField

    fun init(shellyManager: ShellyAppManager, rootController: ViewController) {
        connectBtn.setOnAction {
            if (!shellyManager.initialise(userNameField.text, passwordField.text)) {
                validInputLabel.text = "Info: Wrong credentials. Please try again!"
                validInputLabel.textFill = Color.web("red")
                return@setOnAction
            }
            validInputLabel.text = "Info: Connected and authorised successfully!"
            rootController.onDeviceConnected()
            (connectBtn.scene.window as Stage).close()
        }
    }
}