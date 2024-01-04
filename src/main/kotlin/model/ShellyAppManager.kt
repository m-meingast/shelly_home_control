package model

import network.IConnection
import network.MockConnection
import network.ShellyConnection
import network.config.ShellyConfig

/**
 * The ShellyAppManager supports the usage of the Shelly 2PM Device. The Shelly device and the device, from which this
 * program is executed, need to be in the same network. Therefore this application is not testable without the
 * Shelly device. For this reason this class also has a testing mode where a mock connection is established and the
 * features can be tested. It is triggered by using the credentials 'name', for the user name, and '1234',
 * for the password.
 *
 */
class ShellyAppManager {

    private lateinit var shellyConnection: IConnection

    fun run() {
        println("Enter user name and password (use 'name' and '1234' for testing mode)")
        print("user name: ")
        var user = readlnOrNull() ?: ""
        print("password: ")
        var password = readlnOrNull() ?: ""
        while (!initialise(user, password)) {
            println("Wrong credentials. Please try again!")
            print("user name: ")
            user = readlnOrNull() ?: ""
            print("password: ")
            password = readlnOrNull() ?: ""
        }
        println("Connected and authorised successfully")
        start()
    }

    fun initialise(user: String, password: String): Boolean {
        shellyConnection =
            if (checkTestingMode(user, password)) MockConnection() else ShellyConnection(ShellyConfig.HTTP_ENDPOINT)
        return shellyConnection.initConnection(user, password)
    }

    fun toggleLight1(): Boolean {
        return shellyConnection.toggleLight1()
    }

    fun toggleLight2(): Boolean {
        return shellyConnection.toggleLight2()
    }

    fun enablePartyMode(): Boolean {
        return shellyConnection.enablePartyMode()
    }

    fun disablePartyMode(): Boolean {
        return shellyConnection.disablePartyMode()
    }

    private fun start() {
        do {
            println(
                """
                    
                Choose option:
                (1) - Toggle Light 1
                (2) - Toggle Light 2
                (3) - Activate Party Mode
                (4) - Deactivate Party Mode
                (*) - Any Input: Exit 
            """.trimIndent()
            )
            val option = readlnOrNull() ?: ""
            val continueLoop = isValidOption(option)
            if (!continueLoop) break
            val retVal: Boolean = when (option) {
                "1" -> shellyConnection.toggleLight1()
                "2" -> shellyConnection.toggleLight2()
                "3" -> shellyConnection.enablePartyMode()
                "4" -> shellyConnection.disablePartyMode()
                else -> false
            }
            println(if (retVal) "Option $option successful" else "Option $option did not succeed!")
        } while (continueLoop)
    }

    private fun isValidOption(option: String) =
        (option == "1") || (option == "2") || (option == "3") || (option == "4")

    private fun checkTestingMode(user: String, password: String) =
        user == ShellyConfig.TESTING_MODE_USERNAME && password == ShellyConfig.TESTING_MODE_PASSWORD
}