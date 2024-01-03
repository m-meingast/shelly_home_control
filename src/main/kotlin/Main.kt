import config.ShellyConfig
import kotlinx.coroutines.*
import model.ShellyAppManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val manager = ShellyAppManager()
    manager.run()
}

