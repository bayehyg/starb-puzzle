package starb.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import starb.server.Cell;
import starb.server.Puzzle;

//import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class StarbClientPanel extends VBox {
    public static Button b2;

    public StarbClientDrawing drawing;

    public StarbClientPanel(StarbClientDrawing drawing){
        this.drawing = drawing;
        this.setBorder(new Border(
                new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID,
                        new CornerRadii(5), BorderWidths.DEFAULT,
                        new javafx.geometry.Insets(10,10,10,10)) )
        );
        this.setPadding(new Insets(10,10,10,10));

        setSpacing(20);
        Button b1 = new Button("Hints");
        AtomicInteger value = new AtomicInteger(3);
        b1.setOnMouseClicked(event -> {
            value.set(value.intValue() - 1);
        });
        Label hint = new Label("Hints Remaining: " + value);
        hint.setFont(Font.font("Serif", FontWeight.BOLD, 14));
        b2 = new Button("Reset");
        b2.setStyle("-fx-background-color: #ef7070");
        Label comboLabel = new Label("Level");
        comboLabel.setFont(Font.font("Serif", FontWeight.BOLD, 14));
        ComboBox<String> b3 = new ComboBox<>();
        comboLabel.setLabelFor(b3);
        List<String> values = new ArrayList<>();
        for(int i = 1; i < 25; ++i){
            values.add(String.valueOf(i));
        }
        b3.setValue("1");
        b3.getItems().addAll(values);
        b3.setOnAction(e -> {

            try {
                String val = b3.getValue();
                String url = "http://127.0.0.1:3390/starb/" + val;
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                Puzzle puzz = mapper.readValue(response.body(), Puzzle.class);
                drawing.newLevel(puzz);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        });
        AtomicInteger lev = new AtomicInteger(0);
        Label level = new Label("Level's Completed: " + lev);
        level.setFont(Font.font("Serif", FontWeight.BOLD, 14));
        b2.setOnAction(e -> {
            System.out.println("RESET");
            drawing.handleClear();
        });
        getChildren().addAll(comboLabel,b3,level,b1,hint,b2);
    }

    private void levelSelection(Stage stage) throws Exception{
        Pane back = new Pane();
        StackPane col = new StackPane();
        Canvas cv = new Canvas();

        col.getChildren().add(cv);
        back.getChildren().add(back);

        col.setStyle("-fx-background-color: Orange");
        Scene sc = new Scene(back, 400, 400);
        stage.setScene(sc);
        stage.setTitle("Level Selection");
        stage.show();
    }

}
