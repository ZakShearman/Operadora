package pink.zak.minestom.operadora.utils.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.http.HttpResponse;

public class OperadoraBodyHandler {
    private static final HttpResponse.BodyHandler<JsonElement> JSON_BODY_HANDLER = responseInfo -> HttpResponse.BodySubscribers.mapping(
            HttpResponse.BodySubscribers.ofInputStream(),
            inputStream -> JsonParser.parseReader(new InputStreamReader(inputStream))
    );

    public static HttpResponse.BodyHandler<JsonElement> ofJson() {
        return JSON_BODY_HANDLER;
    }
}
