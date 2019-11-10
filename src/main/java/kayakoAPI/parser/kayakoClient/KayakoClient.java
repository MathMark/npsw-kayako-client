package kayakoAPI.parser.kayakoClient;

public interface KayakoClient {
    String getJsonFromUrl(String url);
    void setUser(String login);
    void setPassword(String password);
}
