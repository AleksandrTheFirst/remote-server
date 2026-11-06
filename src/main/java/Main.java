import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class Main {

    public static final String API_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            CloseableHttpClient client = httpClient();
            HttpGet request = new HttpGet(API_URL);
            request.setHeader("Accept", "application/json");
            CloseableHttpResponse response = client.execute(request);
            try {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                System.out.println("Ответ сервера: " + jsonResponse);

                List<Cat> cats = mapper.readValue(jsonResponse, new TypeReference<List<Cat>>() {});

                System.out.println("Список, где upvotes не равно null и больше нуля:");
                cats.stream()
                        .filter(value -> value.getUpvotes() > 0)
                        .forEach(System.out::println);
            } finally {
                response.close();
                client.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
    }
}
