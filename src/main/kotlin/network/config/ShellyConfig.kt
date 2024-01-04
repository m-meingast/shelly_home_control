package network.config

class ShellyConfig {
    companion object {

        /* HTTP URL Configuration */
        const val HTTP_PREFIX = "http://"

        const val HTTP_ENDPOINT = "${HTTP_PREFIX}10.0.0.175"

        const val HTTP_SHELLY_STATUS = "%s/status"

        const val HTTP_SHELLY_LIGHT_ON = "%s/relay/%d?turn=on"

        const val HTTP_SHELLY_LIGHT_OFF = "%s/relay/%d?turn=off"

        const val HTTP_SHELLY_LIGHT_TOGGLE = "%s/relay/%d?turn=toggle"

        const val HTTP_SHELLY_CHANNEL_ONE = 0

        const val HTTP_SHELLY_CHANNEL_TWO = 1

        /* Request Configuration */
        const val HTTP_REQUEST_METHOD_GET = "GET"

        const val HTTP_TIMEOUT = 5000

        /* Party Mode Configuration */

        const val SHELLY_PARTYMODE_DELAY_LIGHT_ONE = 500L

        const val SHELLY_PARTYMODE_DELAY_LIGHT_TWO = 250L

        /* Response Code */
        const val HTML_RESPONSE_GOOD = 200

        /* Mock Connection (Testing Mode) Configuration */
        const val TESTING_MODE_USERNAME = "name"
        const val TESTING_MODE_PASSWORD = "1234"
    }
}