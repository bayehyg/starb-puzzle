package starb.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

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

        StarbClientScene sys = new StarbClientScene();
        sys.setStyle("-fx-background-color: #6e6a6a;");
        Scene client = new Scene(sys);
        primaryStage.setScene(client);
        primaryStage.setTitle("STAR BATTLE");
        primaryStage.show();
    }
}
