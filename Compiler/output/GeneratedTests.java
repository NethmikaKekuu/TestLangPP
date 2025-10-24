import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.http.*;
import java.net.*;
import java.time.Duration;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GeneratedTests {
    static String BASE = "http://localhost:8080";
    static Map<String,String> DEFAULT_HEADERS = new HashMap<>();
    static HttpClient client;

    @BeforeAll
    static void setup() {
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        DEFAULT_HEADERS.put("Content-Type", "application/json");
    }

    @Test
    void test_LoginMultiline() throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(BASE + "/api/login"))
            .timeout(Duration.ofSeconds(10))
            .POST(HttpRequest.BodyPublishers.ofString("\r\n    {\r\n      \"username\": \"admin\",\r\n      \"password\": \"1234\",\r\n      \"remember\": true,\r\n      \"deviceInfo\": {\r\n        \"browser\": \"Chrome\",\r\n        \"os\": \"Windows\"\r\n      }\r\n    }\r\n    "));
        for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());
        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("token"));
    }

    @Test
    void test_SuccessRange() throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(BASE + "/api/users/42"))
            .timeout(Duration.ofSeconds(10))
            .GET();
        for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());
        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertTrue(resp.statusCode() >= 200 && resp.statusCode() <= 299, "Status " + resp.statusCode() + " not in range 200..299");
        assertTrue(resp.body().contains("user"));
    }

    @Test
    void test_AnySuccess() throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(BASE + "/api/health"))
            .timeout(Duration.ofSeconds(10))
            .GET();
        for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());
        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertTrue(resp.statusCode() >= 200 && resp.statusCode() <= 299, "Status " + resp.statusCode() + " not in range 200..299");
        assertTrue(resp.body().contains("status"));
    }

    @Test
    void test_UpdateWithMultilineBody() throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(BASE + "/api/users/42"))
            .timeout(Duration.ofSeconds(10))
            .PUT(HttpRequest.BodyPublishers.ofString("\r\n    {\r\n      \"role\": \"ADMIN\",\r\n      \"permissions\": [\r\n        \"read\",\r\n        \"write\",\r\n        \"delete\"\r\n      ],\r\n      \"active\": true\r\n    }\r\n    "));
        for (var e : DEFAULT_HEADERS.entrySet()) b.header(e.getKey(), e.getValue());
        HttpResponse<String> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertTrue(resp.statusCode() >= 200 && resp.statusCode() <= 299, "Status " + resp.statusCode() + " not in range 200..299");
        assertTrue(resp.body().contains("updated"));
    }

}
