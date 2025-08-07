package blarknes.whoisidentifier.identify;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import blarknes.whoisidentifier.http.HttpRequestSender;
import blarknes.whoisidentifier.identify.exception.JsonFieldNotFoundException;
import blarknes.whoisidentifier.identify.exception.JsonParsingException;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Handles all actions from the ip identifying process.
 */
@Component
@RequiredArgsConstructor
public class IdentifyIpWhois {

    private final HttpRequestSender httpRequestSender;
    private final ObjectMapper objectMapper;

    /**
     * Sends an HTTP GET request to the WHOIS api and builds an user readable
     * message based on the response from the request.
     *
     * @param ip the ip to be identified
     * @return Response based on the request response body.
     */
    public String getIPDetailedData(final String ip) {
        val responseBody = httpRequestSender.doGetRequest(ip);

        val mappedBody = parseContent(responseBody);

        if (!getValueFromJson(mappedBody, "success").asBoolean()) {
            return "The provided IP (" + ip + ") is invalid";
        }

        return buildResponse(mappedBody);
    }

    private JsonNode parseContent(final String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException();
        }
    }

    private JsonNode getValueFromJson(final JsonNode mappedBody, final String path) {
        val value = mappedBody.at("/" + path);

        if (value.isMissingNode()) {
            throw new JsonFieldNotFoundException(path);
        }

        return value;
    }

    private String buildResponse(final JsonNode mappedBody) {
        val ip = getValueFromJson(mappedBody,"ip").asText();
        val type = getValueFromJson(mappedBody,"type").asText();
        val country = getValueFromJson(mappedBody,"country").asText();
        val isp = getValueFromJson(mappedBody,"connection/isp").asText();

        return String.format(
            "%s %s is located on %s and belongs to %s",
            type,
            ip,
            country,
            isp
        );
    }

}
