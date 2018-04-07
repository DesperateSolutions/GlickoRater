package solutions.desperate.glicko.infrastructure;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class AccessEvent {
    private final String queryString;
    private final String requestURI;
    private final String protocol;
    private final String remoteAddr;
    private final String method;
    private final int statusCode;
    private final String timestamp;

    public AccessEvent(String queryString, String requestURI, String protocol, String remoteAddr, String method, int statusCode, String timestamp) {
        this.queryString = queryString;
        this.requestURI = requestURI;
        this.protocol = protocol;
        this.remoteAddr = remoteAddr;
        this.method = method;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }
}
