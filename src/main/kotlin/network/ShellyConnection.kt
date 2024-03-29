package network

import com.google.gson.Gson
import kotlinx.coroutines.*
import network.config.ShellyConfig
import network.response.ShellyAPIRelayResponse
import network.response.ShellyAPIResponse
import network.response.ShellyAPIStatusResponse
import network.response.ShellyResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Authenticator
import java.net.HttpURLConnection
import java.net.PasswordAuthentication
import java.net.URL
import kotlin.reflect.KClass

class ShellyConnection(private val endpoint: String) : IConnection {

    private var authentication: Authenticator? = null
    private var isInitialised: Boolean = false
    private var partyModeActive: Boolean = false

    override fun initConnection(userName: String, password: String): Boolean {
        var retVal = false
        authentication = getAuthenticator(userName, password)
        isInitialised = true

        runBlocking {
            val response: ShellyResponse<ShellyAPIStatusResponse> = asyncGetHttpRequest(
                String.format(ShellyConfig.HTTP_SHELLY_STATUS, endpoint),
                ShellyAPIStatusResponse::class
            ).await()
            if (!response.isEmpty() && checkConnectionStatus(response))
                retVal = true
        }
        return retVal
    }

    override fun toggleLight1(): Boolean {
        return toggleLight(ShellyConfig.HTTP_SHELLY_CHANNEL_ONE)
    }

    override fun toggleLight2(): Boolean {
        return toggleLight(ShellyConfig.HTTP_SHELLY_CHANNEL_TWO)
    }

    override fun enablePartyMode(): Boolean {
        if (!isInitialised) return false
        partyModeActive = true
        runBlocking {
            asyncPartyModeRequest(
                String.format(
                    ShellyConfig.HTTP_SHELLY_LIGHT_TOGGLE,
                    endpoint,
                    ShellyConfig.HTTP_SHELLY_CHANNEL_ONE
                ),
                ShellyConfig.SHELLY_PARTYMODE_DELAY_LIGHT_ONE
            )
            asyncPartyModeRequest(
                String.format(
                    ShellyConfig.HTTP_SHELLY_LIGHT_TOGGLE,
                    endpoint,
                    ShellyConfig.HTTP_SHELLY_CHANNEL_TWO
                ),
                ShellyConfig.SHELLY_PARTYMODE_DELAY_LIGHT_TWO
            )
        }
        return true
    }

    override fun disablePartyMode(): Boolean {
        if(!isInitialised) return false
        partyModeActive = false
        return true
    }

    /**
     * Http call to the endpoint(URL of the Shelly device)
     * @param endpoint URL destination of the call
     * @param responseClass Class of the API Response. It is needed because the API response is dependent on the
     *  used endpoint
     * @return The response of the Http call
     */
    private fun <T : ShellyAPIResponse> asyncGetHttpRequest(
        endpoint: String,
        responseClass: KClass<T>
    ): Deferred<ShellyResponse<T>> {
        return CoroutineScope(Dispatchers.IO).async {
            val connection: HttpURLConnection = configureConnection(endpoint)
            var shellyResponse: ShellyResponse<T>? = null
            try {
                shellyResponse = getResponseFromConnection(connection, responseClass)
            } catch (e: IOException) {
                println(Exception("HTTP GET Request failed with response code: ${shellyResponse?.responseCode ?: "Unknown"}, message: ${e.message}"))
            } catch (e: Exception) {
                println("ERROR occurred at Http Request: ${e.message}")
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
            return@async shellyResponse ?: ShellyResponse()
        }
    }

    /**
     * Http call to the endpoint(URL of the Shelly device) for the party mode. The method repeats the calls to the
     * endpoint until the party
     * @param endpoint URL destination of the call
     * @param responseClass Class of the API Response. It is needed because the API response is dependent on the
     *  used endpoint
     * @return The response of the Http call
     */
    private suspend fun asyncPartyModeRequest(
        endpoint: String,
        delay: Long
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var shellyResponse: ShellyResponse<ShellyAPIRelayResponse>? = null
            while (partyModeActive) {
                var connection: HttpURLConnection?
                connection = configureConnection(endpoint)
                try {
                    shellyResponse = getResponseFromConnection(connection, ShellyAPIRelayResponse::class)
                } catch (e: IOException) {
                    println(Exception("HTTP GET Request failed with response code: ${shellyResponse?.responseCode ?: "Unknown"}, message: ${e.message}"))
                    break
                } catch (e: Exception) {
                    println("ERROR occurred at Http Request: ${e.message}")
                    e.printStackTrace()
                    break
                } finally {
                    connection.disconnect()
                    delay(delay)
                }
            }
        }
    }

    /**
     * Set the endpoint for toggling the respective channel
     * @param channel The channel of the Shelly device
     * @return True if operation succeeded, false otherwise
     */
    private fun toggleLight(channel: Int): Boolean {
        if (!isInitialised) return false
        var retVal = false
        runBlocking {
            val response: ShellyResponse<ShellyAPIRelayResponse> = asyncGetHttpRequest(
                String.format(ShellyConfig.HTTP_SHELLY_LIGHT_TOGGLE, endpoint, channel),
                ShellyAPIRelayResponse::class
            ).await()
            if (!response.isEmpty() && response.responseCode == ShellyConfig.HTML_RESPONSE_GOOD)
                retVal = true
        }
        return retVal
    }

    private fun configureConnection(endpoint: String): HttpURLConnection {
        val url = URL(endpoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.setAuthenticator(authentication)
        connection.requestMethod = ShellyConfig.HTTP_REQUEST_METHOD_GET
        connection.connectTimeout = ShellyConfig.HTTP_TIMEOUT
        return connection
    }

    /**
     * Get the Http response and parse the respective Shelly API response.
     * @param connection The Http connection
     * @param responseClass The class of the API response
     * @return The parsed response
     */
    private fun <T : ShellyAPIResponse> getResponseFromConnection(
        connection: HttpURLConnection,
        responseClass: KClass<T>
    ): ShellyResponse<T> {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        val shellyResponse = ShellyResponse(
            connection.responseCode,
            parseJSON(response, responseClass)
        )
        reader.close()
        return shellyResponse
    }

    private fun checkConnectionStatus(response: ShellyResponse<ShellyAPIStatusResponse>): Boolean {
        if (response.responseCode != ShellyConfig.HTML_RESPONSE_GOOD) return false
        val wifiStatus = response.responseData?.wifiStatus ?: return false
        return (wifiStatus.connected) && ("${ShellyConfig.HTTP_PREFIX}${wifiStatus.ipAddr}" == endpoint)
    }

    private fun getAuthenticator(userName: String, password: String): Authenticator {
        return object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(userName, password.toCharArray())
            }
        }
    }

    private fun <T : ShellyAPIResponse> parseJSON(text: String, responseClass: KClass<T>): T =
        Gson().fromJson(text, responseClass.java)
}