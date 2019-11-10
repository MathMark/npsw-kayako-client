package kayakoAPI.parser.kayakoClient;

import com.ning.http.client.AsyncHttpClient;
import kayakoAPI.urls.URLS;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Data
public class KayakoClientImpl implements KayakoClient {
    private String user;
    private String password;

    public KayakoClientImpl(){}

    public KayakoClientImpl(String user, String password){
        this.user = user;
        this.password = password;
    }

    public String getJsonFromUrl(String url){
        String body = null;
        try(AsyncHttpClient asyncClient = new AsyncHttpClient()){
            final String encoded = Base64.getEncoder().encodeToString((user + ':' + password).getBytes(StandardCharsets.UTF_8));
            body = asyncClient
                    .prepareGet(url)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoded)
                    .execute()
                    .get()
                    .getResponseBody(StandardCharsets.UTF_8.name());
        }catch (Exception e){
            e.printStackTrace();
        }
        return body;
    }

    public String getJsonFromUrl(URLS urls){
        return getJsonFromUrl(urls.getUrl());
    }
}
