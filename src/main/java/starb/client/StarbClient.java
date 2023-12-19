package starb.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import starb.server.Puzzle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Creates a single window as an example of a Java GUI with a component
 * for drawing.
 */
public class StarbClient extends Application {
    public static void main( String[] args ) throws IOException {
        // Start the GUI
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "http://127.0.0.1:3390/starb/client";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        Client client  = mapper.readValue(response.body(), Client.class);
        primaryStage.setScene(client.getScene());
        primaryStage.setTitle("STAR BATTLE");
        primaryStage.show();
    }
}
