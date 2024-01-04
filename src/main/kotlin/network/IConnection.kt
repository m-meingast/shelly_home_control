package network

sealed interface IConnection {

    fun initConnection(userName: String, password: String): Boolean

    fun toggleLight1(): Boolean

    fun toggleLight2(): Boolean

    fun enablePartyMode(): Boolean

    fun disablePartyMode(): Boolean
}