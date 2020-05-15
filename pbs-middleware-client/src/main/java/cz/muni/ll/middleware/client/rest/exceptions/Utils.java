package cz.muni.ll.middleware.client.rest.exceptions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import feign.Response;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

import static java.util.Optional.ofNullable;

public class Utils {

    public static String getResponseBody(Response response) {
        try {
            final String body = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8.name());
            return getMessage(body);
        } catch (Exception e) {
            return defaultErrorMessage(response.status());
        }
    }

    private static String getMessage(String responseBody) {
        try {
            JsonObject json = new JsonParser().parse(responseBody).getAsJsonObject();
            return ofNullable(json.get("message"))
                    .map(JsonElement::getAsString)
                    .orElse(
                            ofNullable(json.get("code"))
                                    .map(JsonElement::getAsString)
                                    .orElse(responseBody));
        } catch (Exception e) {
            return responseBody;
        }
    }

    private static String defaultErrorMessage(int status) {
        return switch (status) {
            case 400 -> "Unknown bad request error";
            case 401, 403 -> "Unknown authentication error";
            case 404 -> "Not found error";
            case 405 -> "Wrong bad request error";
            case 406 -> "Not acceptable request";
            case 409 -> "Unknown conflict error";
            case 500 -> "Internal server error";
            default -> "Unknown request error";
        };
    }

}
