package network.response

import com.google.gson.annotations.SerializedName

data class ShellyAPIStatusResponse(
    @SerializedName("wifi_sta") val wifiStatus: WifiStatus
) {
    data class WifiStatus(
        @SerializedName("connected") val connected: Boolean,
        @SerializedName("ip") val ipAddr: String,
    ) {
    }
}