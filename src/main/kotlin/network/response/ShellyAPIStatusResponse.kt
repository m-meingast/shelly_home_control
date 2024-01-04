package network.response

import com.google.gson.annotations.SerializedName

/**
 * ShellyAPIRelayResponse is the response returned from the API when the status function is called. This is used for the
 * initial authentication.
 */
data class ShellyAPIStatusResponse(
    @SerializedName("wifi_sta") val wifiStatus: WifiStatus
): ShellyAPIResponse {
    data class WifiStatus(
        @SerializedName("connected") val connected: Boolean,
        @SerializedName("ip") val ipAddr: String,
    ) {
    }
}