package network.response

data class ShellyResponse<T>(
    val responseCode: Int,
    val responseData: T?
) {
    constructor() : this(-1, null) {
    }

    fun isEmpty() =
        responseCode == -1 && responseData == null
}