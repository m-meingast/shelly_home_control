package network

import network.config.ShellyConfig

class MockConnection : IConnection {

    private var isInitialised: Boolean = false
    private var partyModeActive: Boolean = false

    override fun initConnection(userName: String, password: String): Boolean {
        isInitialised = userName == ShellyConfig.TESTING_MODE_USERNAME && password == ShellyConfig.TESTING_MODE_PASSWORD
        return isInitialised
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
        println("Party Mode activated")
        return true
    }

    override fun disablePartyMode(): Boolean {
        if(!isInitialised) return false
        partyModeActive = false
        println("Party Mode deactivated")
        return true
    }

    private fun toggleLight(channel: Int): Boolean {
        if (!isInitialised) return false
        println("Toggle Light ${channel+1}")
        return true
    }
}