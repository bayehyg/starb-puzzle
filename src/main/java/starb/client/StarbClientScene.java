package starb.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.layout.HBox;
import starb.server.Puzzle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StarbClientScene extends HBox {
    private StarbClientDrawing drawing;

    private StarbClientPanel panel;

    public StarbClientScene() throws Exception {

        try {
            String url = "http://127.0.0.1:3390/starb/1";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Puzzle puzz = mapper.readValue(response.body(), Puzzle.class);
            drawing = new StarbClientDrawing(puzz);
            panel = new StarbClientPanel(drawing);
            this.getChildren().addAll(drawing,panel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
