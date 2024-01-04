package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
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
    private lateinit var enablePartyModeBtn: Button

    @FXML
    private lateinit var connectBtn: Button

    private val shellyManager = ShellyAppManager()

    private lateinit var primaryStage: Stage

    fun setPrimaryStage(stage: Stage) {
        primaryStage = stage
    }

    fun enableButtons() {
        toggleLight1Btn.isDisable = false
        toggleLight2Btn.isDisable = false
        enablePartyModeBtn.isDisable = false
    }

    @FXML
    private fun initialize() {
        connectBtn.setOnAction {
            createPopupWindow()
        }
        toggleLight1Btn.setOnAction { shellyManager.toggle1() }
        enablePartyModeBtn.setOnAction { shellyManager.party() }
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
}