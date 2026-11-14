package client;

/**
 * Thrown by ServerFacade when a request fails, either because the server
 * returned an error status or because the server could not be reached.
 */
public class ResponseException extends Exception {
    private final int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /** HTTP status code from the server, or 0 if the server could not be reached. */
    public int statusCode() {
        return statusCode;
    }
}
