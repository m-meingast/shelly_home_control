package network.response

import com.google.gson.annotations.SerializedName

/**
 * ShellyAPIRelayResponse is the response returned from the API when a relay function is called, i.e., switching on/off
 * lights
 */
data class ShellyAPIRelayResponse(
    @SerializedName("ison") val isOn:Boolean,
    @SerializedName("has_timer") val hasTimer:Boolean,
    @SerializedName("overpower") val overPower:Boolean,
    @SerializedName("overtemperature") val overTemperature:Boolean,
    @SerializedName("is_valid") val isValid:Boolean,
    @SerializedName("source") val source:String
): ShellyAPIResponse {
}