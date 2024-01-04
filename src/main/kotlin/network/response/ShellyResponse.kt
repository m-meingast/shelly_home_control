package network.response

/**
 * ShellyResponse wraps the API Response together with the HTTP Response Code
 */
data class ShellyResponse<T: ShellyAPIResponse>(
    val responseCode: Int,
    val responseData: T?
) {
    constructor() : this(-1, null) {
    }

    fun isEmpty() =
        responseCode == -1 && responseData == null
}