package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Wraps every HTTP endpoint of the chess server in a plain Java method,
 * so the rest of the client never deals with URLs, JSON, or status codes.
 */
public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this("http://localhost:" + port);
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws ResponseException {
        makeRequest("DELETE", "/db", null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object body, String authToken,
                              Class<T> responseClass) throws ResponseException {
        HttpRequest.BodyPublisher publisher = (body == null)
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(gson.toJson(body));

        var builder = HttpRequest.newBuilder(URI.create(serverUrl + path)).method(method, publisher);
        if (body != null) {
            builder.header("Content-Type", "application/json");
        }
        if (authToken != null) {
            builder.header("authorization", authToken);
        }

        HttpResponse<String> response = send(builder.build());

        int status = response.statusCode();
        if (status / 100 != 2) {
            throw new ResponseException(status, readErrorMessage(response.body()));
        }
        return (responseClass == null) ? null : gson.fromJson(response.body(), responseClass);
    }

    private HttpResponse<String> send(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ResponseException(0, "Unable to reach the server");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseException(0, "Request was interrupted");
        }
    }

    private String readErrorMessage(String body) {
        try {
            JsonObject json = gson.fromJson(body, JsonObject.class);
            if (json != null && json.has("message")) {
                return json.get("message").getAsString();
            }
        } catch (JsonParseException ignored) {
            // fall through to a generic message
        }
        return "Error: request failed";
    }
}
