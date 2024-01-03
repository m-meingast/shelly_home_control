package model

import config.ShellyConfig
import network.ShellyConnection

class ShellyAppManager {

    private val shellyConnection = ShellyConnection(ShellyConfig.HTTP_ENDPOINT)

    fun initialise(): Boolean {
        print("Enter user name: ")
//        val user = readlnOrNull() ?: ""
        val user = "user"
        print("Enter password: \n")
//        val password = readlnOrNull() ?: ""
        val password = "user"
        return shellyConnection.initConnection(user, password)


    }

    fun run() {
        if (!initialise()) return
        start()
    }

    private fun start() {
        do {
            println(
                """
                    
                Choose option:
                (1) - Toggle Light 1
                (2) - Toggle Light 2
                (3) - Party Mode
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
                else -> false
            }
            if (!retVal) println("Option $option did not succeed!")
        } while (continueLoop)
    }

    private fun isValidOption(option: String) =
        (option == "1") || (option == "2") || (option == "3")
}