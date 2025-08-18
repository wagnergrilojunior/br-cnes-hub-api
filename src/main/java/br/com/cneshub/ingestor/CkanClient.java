package br.com.cneshub.ingestor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Client to interact with CKAN API.
 */
@Component
public class CkanClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CkanClient.class);

    private final String baseUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CkanClient(@Value("${cneshub.ingest.baseUrl}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Downloads the CSV resource to the given destination path.
     *
     * @param resourceId resource identifier on CKAN
     * @param destino    destination path for the downloaded file
     */
    public void downloadCsv(String resourceId, Path destino) {
        String url = String.format("%s/datastore/dump/%s?format=csv", baseUrl, resourceId);
        try (InputStream in = streamCsvFromUrl(url)) {
            Files.createDirectories(destino.getParent());
            Files.copy(in, destino);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao baixar recurso do CKAN", e);
        }
    }

    /**
     * Streams CSV content from a direct URL.
     *
     * @param url URL pointing to CSV
     * @return input stream of the CSV
     */
    public InputStream streamCsvFromUrl(String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return response.body();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Falha ao obter stream do CKAN", e);
        }
    }
}
