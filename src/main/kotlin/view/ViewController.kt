package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.stage.Modality
import javafx.stage.Stage
import model.ShellyAppManager
import org.kordamp.bootstrapfx.BootstrapFX

class ViewController {

    @FXML
    private lateinit var toggleLight1Btn: Button

    @FXML
    private lateinit var toggleLight2Btn: Button

    @FXML
    private lateinit var enablePartyModeBtn: ToggleButton

    @FXML
    private lateinit var connectBtn: Button

    @FXML
    private lateinit var statusLabel: Label

    private val shellyManager = ShellyAppManager()

    private lateinit var primaryStage: Stage

    fun setPrimaryStage(stage: Stage) {
        primaryStage = stage
    }

    fun onDeviceConnected() {
        toggleLight1Btn.isDisable = false
        toggleLight2Btn.isDisable = false
        enablePartyModeBtn.isDisable = false
        statusLabel.text = "Connected"
        connectBtn.isVisible = false
    }

    @FXML
    private fun initialize() {
        connectBtn.setOnAction {
            createPopupWindow()
        }
        toggleLight1Btn.setOnAction {
            statusLabel.text = if (shellyManager.toggleLight1()) "Light 1 toggled" else "Operation not successful"
        }
        toggleLight2Btn.setOnAction {
            statusLabel.text = if (shellyManager.toggleLight2()) "Light 2 toggled" else "Operation not successful"
        }
        enablePartyModeBtn.setOnAction {
            if (enablePartyModeBtn.isSelected) enablePartyMode() else disablePartyMode()
        }

    }

    private fun createPopupWindow() {
        val loader = FXMLLoader(this::class.java.getResource("connect.fxml"))
        val scene = Scene(loader.load())
        scene.stylesheets.add(BootstrapFX.bootstrapFXStylesheet())

        val connectController: ConnectController = loader.getController()
        connectController.init(shellyManager, this)

        val popupStage = Stage()
        popupStage.title = "Connect to Shelly Device"
        popupStage.scene = scene
        popupStage.initOwner(primaryStage)
        popupStage.initModality(Modality.APPLICATION_MODAL)
        popupStage.show()
    }

    private fun enablePartyMode() {
        enablePartyModeBtn.styleClass.remove("btn-default")
        enablePartyModeBtn.styleClass.add("btn-primary")
        statusLabel.text = if (shellyManager.enablePartyMode()) "Party Mode active" else "Operation not successful"
    }

    private fun disablePartyMode() {
        enablePartyModeBtn.styleClass.remove("btn-primary")
        enablePartyModeBtn.styleClass.add("btn-default")
        statusLabel.text =
            if (shellyManager.disablePartyMode()) "Party Mode deactivated" else "Operation not successful"
    }
}