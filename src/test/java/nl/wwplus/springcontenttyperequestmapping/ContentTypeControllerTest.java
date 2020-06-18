package nl.wwplus.springcontenttyperequestmapping;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ContentTypeControllerTest {
    @LocalServerPort
    private int port;

    @Test
    void testGet() throws IOException {
        // Arrange
        HttpUriRequest request = new HttpGet(String.format("http://localhost:%s/api/contentType", port));

        // Act
        var httpResponse = HttpClientBuilder.create().build().execute(request);

        // Assert
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(new String(httpResponse.getEntity().getContent().readAllBytes())).isEqualTo("API is online!");
    }

    @Test
    void testAddInventoryItem() throws IOException, URISyntaxException {
        // Arrange
        final CloseableHttpClient httpClient = HttpClients.createDefault();

        var inventoryItem = "Nintendo Switch";

        var request = new HttpPost(
                new URIBuilder("http://localhost")
                        .setPort(port)
                        .setPath("api/contentType")
                        .setParameter("inventoryItem", inventoryItem)
                        .build());
        request.setHeader("Content-Type", "text/plain;domain-model=AddInventoryItemCommand");

        // Act
        var httpResponse = httpClient.execute(request);

        // Assert
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(new String(httpResponse.getEntity().getContent().readAllBytes())).isEqualTo(
                "Inventory item 'Nintendo Switch' added.");
        httpClient.close();
    }

    @Test
    void testRemoveInventoryItem() throws IOException, URISyntaxException {
        // Arrange
        final CloseableHttpClient httpClient = HttpClients.createDefault();

        var inventoryItem = "UbiquiTi AmpliFi";

        var request = new HttpPost(
                new URIBuilder("http://localhost")
                .setPort(port)
                .setPath("api/contentType")
                .setParameter("inventoryItem", inventoryItem)
                .build());
        request.setHeader("Content-Type", "text/plain;domain-model=RemoveInventoryItemCommand");

        // Act
        var httpResponse = httpClient.execute(request);

        // Assert
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(new String(httpResponse.getEntity().getContent().readAllBytes())).isEqualTo(
                "Inventory item 'UbiquiTi AmpliFi' removed.");
        httpClient.close();
    }
}