package network

sealed interface IConnection {

    /**
     * Initialise the connection and perform user authentication
     *
     * @param userName The username for the authentication
     * @param password The password for the authentication
     * @return True if initialisation succeeded, false otherwise
     */
    fun initConnection(userName: String, password: String): Boolean

    /**
     * Toggle Shelly 2PM light on channel 1
     * @return True if operation succeeded, false otherwise
     */
    fun toggleLight1(): Boolean

    /**
     * Toggle Shelly 2PM light on channel 2
     * @return True if operation succeeded, false otherwise
     */
    fun toggleLight2(): Boolean

    /**
     * Enable party mode. When this method is called the lights on both channels of Shelly 2PM device start switching on
     * and off with respective cycle times, imitating a party location with strobe lights.
     * Use this method with care as your lights might not like the constant toggling with high frequencies
     * @return True if operation succeeded, false otherwise
     */
    fun enablePartyMode(): Boolean

    /**
     * Disables the toggling of both channels. The lights might still be on depending on when this method is called.
     */
    fun disablePartyMode(): Boolean
}