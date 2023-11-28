package starb.client.ui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;

/**
 * A GUI component demonstrating how to use JavaFX GraphicsContext to draw shapes and
 * images.
 */
public class ExampleDrawingPanel extends StackPane {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private static final File STAR_IMAGE_FILE = new File("image/star_gold.png");
    private Image starImage;
    private static final File SPOT_IMAGE_FILE = new File("image/spot_the_cow.png");
    private Image spotImage;

    private Canvas canvas;

    // Grid dimensions and location
    private double cellSize = 40.0;
    private int rows = 10;
    private int cols = 10;
    private Point2D gridUpperLeft = new Point2D(15,15);

    public ExampleDrawingPanel() {
        canvas = new Canvas(WIDTH, HEIGHT);

        // Load the image files
        try {
            starImage = new Image(STAR_IMAGE_FILE.toURI().toURL().toString());
            spotImage = new Image(SPOT_IMAGE_FILE.toURI().toURL().toString());
        } catch(Exception e) {
            String message = "Unable to load image: " + STAR_IMAGE_FILE;
            System.err.println(message);
            System.err.println(e.getMessage());
            throw new RuntimeException(message);
        }

        this.getChildren().add(canvas);
        canvas.setOnMouseClicked( e -> mouseClicked(e));
        draw();
    }

    public void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);

        // Example grid
        g.setLineWidth(1.0);
        g.beginPath();
        for( int i = 0; i < rows + 1; i++ ) {
            double x1 = gridUpperLeft.getX();
            double y1 = gridUpperLeft.getY() + i * cellSize;
            double x2 = gridUpperLeft.getX() + cellSize * cols;
            double y2 = y1;
            g.moveTo(x1, y1);
            g.lineTo(x2, y2);
        }
        for( int i = 0; i < cols + 1; i++ ) {
            double x1 = gridUpperLeft.getX() + i * cellSize;
            double y1 = gridUpperLeft.getY();
            double x2 = x1;
            double y2 = gridUpperLeft.getY() + cellSize * rows;
            g.moveTo( x1, y1 );
            g.lineTo( x2, y2 );
        }
        g.stroke();

        // Draw a thicker line, left side of cells in column
        g.setLineWidth(5.0);
        int column = 1, startCellY = 1, endCellY = 3;
        double x1 = column * cellSize + gridUpperLeft.getX();
        double y1 = startCellY * cellSize + gridUpperLeft.getY();
        double x2 = x1;
        double y2 = (endCellY + 1) * cellSize + gridUpperLeft.getY();
        g.strokeLine(x1, y1, x2, y2);

        // Draw stars in a few cells of the grid
        drawStar( 3, 4, g );
        drawStar( 1, 1, g );
        drawStar( 0, 2, g );

        // Draw the cow, just for fun.  This is demonstrating how you can draw any custom
        // image.  Remove this... :)
        float scale = 0.4f;
        g.drawImage( spotImage, gridUpperLeft.getX() + cellSize * cols + 5,
                gridUpperLeft.getY(),
                spotImage.getWidth() * scale, spotImage.getHeight() * scale);
    }

    private void drawStar( int row, int col, GraphicsContext g ) {
        g.drawImage(starImage,
                gridUpperLeft.getX() + row * cellSize,
                gridUpperLeft.getY() + col * cellSize,
                cellSize, cellSize
                );
    }

    private void mouseClicked(MouseEvent e) {
        int row = ((int)e.getX() - 15)/ 40;
        int col = ((int)e.getY() - 15)/ 40;
        //System.out.printf("Click: (%d, %d)%n", (int)e.getX(), (int)e.getY());
        drawStar(row, col, canvas.getGraphicsContext2D());
    }
}