package geektime.hw.week2;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class TestHttpClient {

    public static void main(String[] args) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(new URI("http://localhost:8801/"));
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();

                System.out.printf("Response Code:%s %s%n", statusLine.getStatusCode(), statusLine.getReasonPhrase());
                HttpEntity entity = response.getEntity();
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
