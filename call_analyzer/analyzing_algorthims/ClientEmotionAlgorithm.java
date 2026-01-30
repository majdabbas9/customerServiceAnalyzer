package call_analyzer.analyzing_algorthims;

import call_analyzer.interaction.Interaction;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.util.Properties;

public class ClientEmotionAlgorithm implements Algorithm {
    private String apiUrl;

    public ClientEmotionAlgorithm() {
        this.apiUrl = loadApiUrl();
    }

    private String loadApiUrl() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            prop.load(fis);
            String url = prop.getProperty("EMOTION_API_URL");
            if (url != null && !url.isEmpty()) {
                return url + "/analyzeClient";
            }
        } catch (Exception e) {
            // Log error or handle missing file
        }
        return "http://localhost:8001/analyzeClient"; // This is now only a last resort
    }

    @Override
    public String apply(Interaction interaction) {
        String textToAnalyze = interaction.getClientSideCall();
        if (textToAnalyze == null || textToAnalyze.isEmpty()) {
            return "Error: No client side call text found.";
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            String encodedText = URLEncoder.encode(textToAnalyze, StandardCharsets.UTF_8);
            String urlWithParams = apiUrl + "?text=" + encodedText;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithParams))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Error: Received status code " + response.statusCode();
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
