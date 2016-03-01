package nl.pvanassen.geckoboard.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import nl.pvanassen.geckoboard.api.gson.SoSGsonFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class SoSGeckoboard {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int RECOMMENDED_TIME_OUT = 70000;

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_TYPE_VALUE = "application/json";

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private String baseUrl = "https://push.geckoboard.com/v1/send/";

    private static void bestEffortToReleaseHttpWithoutExceptions(final OutputStream httpOutputStream,
            final InputStream httpInputStream, final HttpURLConnection connection) {
        IOUtils.closeQuietly(httpOutputStream);
        IOUtils.closeQuietly(httpInputStream);
        if (connection != null) {
            connection.disconnect();
        }
    }

    private final String apiKey;

    public SoSGeckoboard(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Api key cannot be null or empty");
        }
        this.apiKey = apiKey;
    }

    private HttpURLConnection createAndOpenConnection(String widgetKey) throws IOException {
        final URL url = new URL(baseUrl.concat(widgetKey));
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty(SoSGeckoboard.CONTENT_TYPE, SoSGeckoboard.CONTENT_TYPE_VALUE);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(SoSGeckoboard.RECOMMENDED_TIME_OUT);
        connection.setReadTimeout(SoSGeckoboard.RECOMMENDED_TIME_OUT);
        return connection;
    }

    public void push(Push push) throws IOException {
        if (push == null) {
            throw new IllegalArgumentException("Push cannot be empty");
        }
        WidgetWrapper wrapper = new WidgetWrapper(push, apiKey);
        Gson gson = SoSGsonFactory.getGson();
        String json = gson.toJson(wrapper);
        HttpURLConnection connection = null;
        OutputStream httpOutputStream = null;
        InputStream httpInputStream = null;
        try {
            connection = createAndOpenConnection(push.getWidgetKey());
            httpOutputStream = connection.getOutputStream();
            logger.info("Sending: " + json);
            IOUtils.write(json.getBytes(SoSGeckoboard.DEFAULT_CHARSET), httpOutputStream);
            if (connection.getResponseCode() >= 400) {
                logger.error("Error sending json, error: "
                        + new String(IOUtils.toByteArray(connection.getErrorStream()), SoSGeckoboard.DEFAULT_CHARSET)
                        + ", json send: " + json);
                return;
            }
            httpInputStream = connection.getInputStream();
            final byte[] responseXml = IOUtils.toByteArray(httpInputStream);
            logger.info(new String(responseXml, SoSGeckoboard.DEFAULT_CHARSET));
        }
        finally {
        	SoSGeckoboard.bestEffortToReleaseHttpWithoutExceptions(httpOutputStream, httpInputStream, connection);
        }
    }

    /**
     * Overriding of the base url. The default is 'https://push.geckoboard.com/v1/send/'
     *
     * @param baseUrl New base url to use
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
