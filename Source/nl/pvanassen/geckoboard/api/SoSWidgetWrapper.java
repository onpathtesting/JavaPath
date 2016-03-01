//package Framework.Core.Geckoboard;
package nl.pvanassen.geckoboard.api;

import com.google.gson.annotations.SerializedName;

/**
 * Wrapper to get correct json through gson
 *
 * @author Paul van Assen
 */
class SoSWidgetWrapper {

    @SerializedName("data")
    private final Push push;

    @SerializedName("api_key")
    private final String apiKey;

    SoSWidgetWrapper(Push push, String apiKey) {
        this.push = push;
        this.apiKey = apiKey;
    }

    String getApiKey() {
        return apiKey;
    }

    Push getPush() {
        return push;
    }

}
