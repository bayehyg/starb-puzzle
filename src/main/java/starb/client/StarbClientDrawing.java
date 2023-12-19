package starb.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import starb.server.Cell;
import starb.server.Puzzle;
import starb.server.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.geometry.Insets;
//import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StarbClientDrawing extends StackPane {
    private Canvas canvas;
    private int width = 500;
    private int length = 500;
    private double cellSize = 40.0;

    private final int NUM_SIDES = 10;
    private Image starImage;
    private static final File STAR_IMAGE_FILE = new File("image/star_gold.png");
    private static final File DOT_IMAGE_FILE = new File("image/moon_136693.png");

    public Puzzle puzzle;

    private Point2D gridUpperLeft = new Point2D(15,15);
    private int[] rows = new int[NUM_SIDES];
    private int[] cols = new int[NUM_SIDES];
    private boolean[][] isOccupied;
    public List<Cell> starred = new ArrayList<>();
    private Text solved;
    private List<Cell> dotted;
    private VBox v1;
    public Button b1 = new Button("Next Level");
    public StarbClientDrawing(Puzzle puzzle){
        dotted = new ArrayList<>();
        this.puzzle = puzzle;
        canvas = new Canvas(width, length);
        isOccupied = new boolean[NUM_SIDES][NUM_SIDES];
        try {
            starImage = new Image(STAR_IMAGE_FILE.toURI().toURL().toString());
        } catch (Exception e) {
            String message = "Unable to load image: " + STAR_IMAGE_FILE;
            System.err.println(message);
            System.err.println(e.getMessage());
            throw new RuntimeException(message);
        }
        solved = new Text("CONGRATULATIONS! You have solved this level");
        v1 = new VBox();
        v1.getChildren().addAll(solved, b1);
        b1.setOnAction(e -> {
            try {
                int val = this.puzzle.getLevel() + 1;
                System.out.println("level " + this.puzzle.getLevel());
                String url = "http://127.0.0.1:3390/starb/" + val;
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                Puzzle puzz = mapper.readValue(response.body(), Puzzle.class);
                newLevel(puzz);
                v1.setVisible(false);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox.setMargin(b1, new Insets(10, 0, 0, 0));
        v1.setAlignment(Pos.CENTER);
        v1.setVisible(false);
        this.getChildren().addAll(canvas, v1);
        solved.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        canvas.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY){
                rightClick(e);
            }else {
                mouseClicked(e);
            }
        });
        solved.setFill(Color.GOLD);
        draw();
    }



    public void draw() {
        GraphicsContext graph = canvas.getGraphicsContext2D();
        graph.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graph.setFill(Color.BLACK);


        // Draw grid
        graph.setLineWidth(1.0);
        graph.beginPath();
        for (int i = 0; i < NUM_SIDES + 1; i++) {
            double x1 = gridUpperLeft.getX();
            double y1 = gridUpperLeft.getY() + i * cellSize;
            double x2 = gridUpperLeft.getX() + cellSize * NUM_SIDES;
            double y2 = y1;
            graph.moveTo(x1, y1);
            graph.lineTo(x2, y2);

        }
        for( int i = 0; i < NUM_SIDES + 1; i++ ) {
            double x1 = gridUpperLeft.getX() + i * cellSize;
            double y1 = gridUpperLeft.getY();
            double x2 = x1;
            double y2 = gridUpperLeft.getY() + cellSize * NUM_SIDES;
            graph.moveTo( x1, y1 );
            graph.lineTo( x2, y2 );
        }
        graph.stroke();
        graph.setFill(Color.WHITE);



        graph.beginPath();
        graph.setLineWidth(3.0);

        graph.moveTo(gridUpperLeft.getX(), gridUpperLeft.getY());
        graph.lineTo(gridUpperLeft.getX() + 10 * cellSize, gridUpperLeft.getY());

        graph.moveTo(gridUpperLeft.getX(), gridUpperLeft.getY());
        graph.lineTo(gridUpperLeft.getX(), gridUpperLeft.getY() + 10 * cellSize);

        graph.moveTo(gridUpperLeft.getX(), gridUpperLeft.getY() + 10 * cellSize);
        graph.lineTo(gridUpperLeft.getX() + 10 * cellSize, gridUpperLeft.getY() + 10 * cellSize);

        graph.moveTo(gridUpperLeft.getX() + 10 * cellSize, gridUpperLeft.getY());
        graph.lineTo(gridUpperLeft.getX() + 10 * cellSize, gridUpperLeft.getY() + 10 * cellSize);

        ;
        List<List<Cell>> regions = puzzle.getRegions();
;
        List<Double> xPoints = new ArrayList<Double>();
        List<Double> yPoints = new ArrayList<Double>();
        int[][] adj = {{0,1}, {0,-1}, {1,0}, {-1,0}};
        for(List<Cell> region: regions){
            Cell[][] cells = toTwoDimension(region);
            drawRegion(cells, graph);
        }
        graph.stroke();
        graph.setFill(Color.WHITE);


    }
    public Cell[][] toTwoDimension(List<Cell> cells){
        Cell[][] result = new Cell[10][10];
        for(Cell c: cells){
            result[c.getRow()][c.getCol()] = c;
        }
        return result;
    }

    public void drawRegion(Cell[][] cells, GraphicsContext g) {
        int[][] adj = {{0,1}, {0,-1}, {1,0}, {-1,0}};
        for(int i = 0; i < 10; ++i){
            for(int j = 0; j < 10; ++j){
                if(cells[i][j] == null) continue;
                if(j != 9 && cells[i][j + 1] == null) {
                    double x1 = gridUpperLeft.getX() + ((j + 1) * cellSize);
                    double y1 = gridUpperLeft.getY() + (i * cellSize);
                    double y2 = gridUpperLeft.getY() + ((i + 1) * cellSize);
                    g.moveTo( x1, y1 );
                    g.lineTo( x1, y2 );
                }
                if(j != 0 && cells[i][j - 1] == null) {
                    double x1 = gridUpperLeft.getX() + (j * cellSize);
                    double y1 = gridUpperLeft.getY() + (i * cellSize);
                    double y2 = gridUpperLeft.getY() + ((i + 1) * cellSize);
                    g.moveTo( x1, y1 );
                    g.lineTo( x1, y2 );
                }
                if(i != 9 && cells[i + 1][j] == null) {
                    double x1 = gridUpperLeft.getX() + (j * cellSize);
                    double x2 = gridUpperLeft.getX() + ((j + 1) * cellSize);
                    double y1 = gridUpperLeft.getY() + ((i + 1) * cellSize);
                    g.moveTo( x1, y1 );
                    g.lineTo( x2, y1 );
                }
                if(i != 0 && cells[i - 1][j] == null) {
                    double x1 = gridUpperLeft.getX() + (j * cellSize);
                    double x2 = gridUpperLeft.getX() + ((j + 1) * cellSize);
                    double y1 = gridUpperLeft.getY() + (i * cellSize);
                    g.moveTo( x1, y1 );
                    g.lineTo( x2, y1 );
                }
            }
        }
    }
    private void drawStar( int row, int col, GraphicsContext g ) {
        g.drawImage(starImage,
                gridUpperLeft.getX() + col * cellSize + 1,
                gridUpperLeft.getY() + row * cellSize + 1,
                cellSize - 3, cellSize - 3
        );
        starred.add(new Cell(row, col));
        isOccupied[row][col] = true;
        cols[col] += 1;
        rows[row] += 1;
    }

    private void invalidCell( int row, int col, GraphicsContext g ) {
        double x = gridUpperLeft.getX() + col * cellSize;
        double y = gridUpperLeft.getY() + row * cellSize;

        g.setFill(Color.RED);
        g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        Duration delay = Duration.millis(500);
        KeyFrame keyFrame = new KeyFrame(delay, e -> {
            g.clearRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    private void removeStar( int row, int col, GraphicsContext g ) {

        double x = gridUpperLeft.getX() + col * cellSize;
        System.out.println("X: " + x);
        double y = gridUpperLeft.getY() + row * cellSize;
        g.clearRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        if (starred.contains(new Cell(row, col))) starred.remove(new Cell(row, col));
        puzzle.updateStarredRegion(new Cell(row, col), -1);
        isOccupied[row][col] = false;
        cols[col] -= 1;
        rows[row] -= 1;
    }

    private boolean noNeighbor(int row, int col) {
        int[][] adj = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        for (int[] pair : adj) {
            if (starred.contains(new Cell(row + pair[0], col + pair[1]))) return false;
        }
        return true;
    }




    private void mouseClicked(MouseEvent e) {


        int col = ((int) e.getX() - 15) / 40;
        int row = ((int) e.getY() - 15) / 40;

        if(col < 0 || row < 0|| row > 9 || col > 9) return;

        if (isOccupied[row][col]) {
            removeStar(row, col, canvas.getGraphicsContext2D());
            System.out.println("No of Stars in COl: " + col + " is " + cols[col]);
            System.out.println("No of Stars in Row: " + row + " is " + rows[row]);
        } else {
            if (col < NUM_SIDES && row < NUM_SIDES) {
                if (rows[row] < 2 && cols[col] < 2 && noNeighbor(row, col) && puzzle.updateStarredRegion(new Cell(row,col), +1)) { //&& validRegion(col,row)
                    drawStar(row, col, canvas.getGraphicsContext2D());
                    while(!dotted.isEmpty()){
                        removeDot(dotted.get(0).getRow(), dotted.get(0).getCol(), canvas.getGraphicsContext2D());
                    }
                    if(starred.size() == 20) solved();
                } else {
                    System.out.println("Unavailable Cell");
                    invalidCell(row, col, canvas.getGraphicsContext2D());
                }
            }
        }
    }

    // mouse right click event
    public void rightClick(MouseEvent e){
        GraphicsContext g = canvas.getGraphicsContext2D();
        int mouseX = (int) e.getX();
        int mouseY = (int) e.getY();
        int col = ((int)  mouseX - 15) / 40;
        int row = ((int) mouseY - 15) / 40;
        if (dotted.contains(new Cell(row, col))) {
            removeDot(row, col, g);
            return;
        }
        drawDot(row, col, g);
    }

    public void newLevel(Puzzle newP) {
        this.puzzle = newP;
        handleClear();
        draw();
    }
    public void drawDot(int row, int col, GraphicsContext g){
        if((row < 0) || (row > 9) || (col < 0) || (col > 9)) return;
        if(starred.contains(new Cell(row, col))) return;
        Image dotImage;
        try {
            dotImage = new Image(DOT_IMAGE_FILE.toURI().toURL().toString());
        } catch (Exception e) {
            String message = "Unable to load image: " + DOT_IMAGE_FILE;
            System.err.println(message);
            System.err.println(e.getMessage());
            throw new RuntimeException(message);
        }
        double dotSize = 15;
        double dotX = gridUpperLeft.getX() + col * cellSize + (cellSize - dotSize) / 2;
        double dotY = gridUpperLeft.getY() + row * cellSize + (cellSize - dotSize) / 2;

        g.drawImage(dotImage,
                dotX,
                dotY,
                dotSize,
                dotSize
        );
        dotted.add(new Cell(row, col));
        System.out.println("added dot at (" + row + ", " + col + ")");
    }

    public void removeDot(int row, int col, GraphicsContext g){
        if((row < 0) || (row > 9) || (col < 0) || (col > 9)) return;
        if(starred.contains(new Cell(row, col))) return;
        double x = gridUpperLeft.getX() + col * cellSize;
        double y = gridUpperLeft.getY() + row * cellSize;
        g.clearRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        dotted.remove(new Cell(row, col));
        System.out.println("REMOVED dot AT: row: " + row + " col: " + col);
    }

    public void hint(){
        while(!dotted.isEmpty()){
            removeDot(dotted.get(0).getRow(), dotted.get(0).getCol(), canvas.getGraphicsContext2D());
        }
        for(int row = 0; row < 10; ++row){
            for (int col = 0; col < 10; col++) {
                if(!noNeighbor(row, col) || rows[row] >= 2 || cols[col] >= 2 || !puzzle.available(new Cell(row,col))){
                    drawDot(row, col, canvas.getGraphicsContext2D());
                }
            }
        }
    }

    public void solved(){
        v1.setVisible(true);
        try {
            String url = "http://127.0.0.1:3390/starb/client/" + puzzle.getId();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).PUT(HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Client client  = mapper.readValue(response.body(), Client.class);

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void handleClear(){
        while(!starred.isEmpty()){
            Cell c = starred.get(0);
            removeStar(c.getRow(), c.getCol(), canvas.getGraphicsContext2D());
        }
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public Button getB1() {
        return b1;
    }


}
