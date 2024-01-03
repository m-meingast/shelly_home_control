package network.response

import com.google.gson.annotations.SerializedName

data class ShellyAPIRelayResponse(
    @SerializedName("ison") val isOn:Boolean,
    @SerializedName("has_timer") val hasTimer:Boolean,
    @SerializedName("overpower") val overPower:Boolean,
    @SerializedName("overtemperature") val overTemperature:Boolean,
    @SerializedName("is_valid") val isValid:Boolean,
    @SerializedName("source") val source:String
) {
}