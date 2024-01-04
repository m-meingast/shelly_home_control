package view

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.kordamp.bootstrapfx.BootstrapFX

class ShellyControlGUI : Application() {
    override fun start(primaryStage: Stage) {
        val loader = FXMLLoader(this::class.java.getResource("shellyControl.fxml"))
        val scene = Scene(loader.load())
        scene.stylesheets.add(BootstrapFX.bootstrapFXStylesheet())
        val viewController: ViewController = loader.getController()
        viewController.setPrimaryStage(primaryStage)

        primaryStage.title = "Shelly Control"
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(ShellyControlGUI::class.java)
}