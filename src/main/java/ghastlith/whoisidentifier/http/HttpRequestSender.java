package ghastlith.whoisidentifier.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import ghastlith.whoisidentifier.http.exception.HttpErrorResponseException;
import ghastlith.whoisidentifier.http.exception.InvalidURLException;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Utility class for HTTP actions.
 */
@Component
@RequiredArgsConstructor
public class HttpRequestSender {

    private final HttpClient httpClient;

    private static final String WHOIS_URL = "http://ipwho.is/";

    /**
     * Sends an HTTP GET Request to an specified url and returns the response body.
     *
     * @param ip the desired url to retrieve information
     * @return The request response body.
     */
    public String doGetRequest(final String ip) {
        val uri = buildUri(ip);

        val request = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build();
        val response = getResponse(httpClient, request);

        if (!HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
            throw new HttpErrorResponseException(response.statusCode());
        }

        return response.body();
    }

    private URI buildUri(final String ip) {
        val url = WHOIS_URL + ip;

        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new InvalidURLException(url);
        }
    }

    private HttpResponse<String> getResponse(final HttpClient httpClient, final HttpRequest request) {
        try {
            return httpClient.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpErrorResponseException(500);
        }
    }

}
