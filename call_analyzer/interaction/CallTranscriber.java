package call_analyzer.interaction;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;

public class CallTranscriber {
    private final Path clientFilePath;
    private final Path agentFilePath;
    private String apiUrl;

    public CallTranscriber(String clientFile, String agentFile) {
        this.clientFilePath = Path.of(clientFile);
        this.agentFilePath = Path.of(agentFile);
        this.apiUrl = loadApiUrl();
    }

    private String loadApiUrl() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            prop.load(fis);
            String url = prop.getProperty("TRANSCRIBE_API_URL");
            if (url != null && !url.isEmpty()) {
                return url + "/transcribe";
            }
        } catch (Exception e) {
            // Log error
        }
        // Try looking in call_analyzer/.env if running from root
        try (FileInputStream fis = new FileInputStream("call_analyzer/.env")) {
            prop.load(fis);
            String url = prop.getProperty("TRANSCRIBE_API_URL");
            if (url != null && !url.isEmpty()) {
                return url + "/transcribe";
            }
        } catch (Exception e) {
            // Log error
        }
        return "http://localhost:8000/transcribe";
    }

    public String transcribeClient() throws IOException, InterruptedException {
        return sendTranscriptionRequest(clientFilePath, "whisper");
    }

    public String transcribeAgent() throws IOException, InterruptedException {
        return sendTranscriptionRequest(agentFilePath, "whisper");
    }

    private String sendTranscriptionRequest(Path filePath, String model_name) throws IOException, InterruptedException {
        String boundary = "JavaHttpClientBoundary" + UUID.randomUUID().toString();
        HttpClient client = HttpClient.newHttpClient();

        byte[] fileBytes = Files.readAllBytes(filePath);
        String fileName = filePath.getFileName().toString();

        // Build Multipart Body
        StringBuilder bodyBuilder = new StringBuilder();
        // Parameter: model
        bodyBuilder.append("--").append(boundary).append("\r\n");
        bodyBuilder.append("Content-Disposition: form-data; name=\"model_name\"\r\n\r\n");
        bodyBuilder.append(model_name).append("\r\n");

        // Parameter: file
        bodyBuilder.append("--").append(boundary).append("\r\n");
        bodyBuilder.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName)
                .append("\"\r\n");
        bodyBuilder.append("Content-Type: audio/mpeg\r\n\r\n");

        byte[] part1 = bodyBuilder.toString().getBytes();
        byte[] part2 = "\r\n--".concat(boundary).concat("--\r\n").getBytes();

        byte[] combined = new byte[part1.length + fileBytes.length + part2.length];
        System.arraycopy(part1, 0, combined, 0, part1.length);
        System.arraycopy(fileBytes, 0, combined, part1.length, fileBytes.length);
        System.arraycopy(part2, 0, combined, part1.length + fileBytes.length, part2.length);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(combined))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            return "Error: " + response.statusCode() + " - " + response.body();
        }
    }
}
