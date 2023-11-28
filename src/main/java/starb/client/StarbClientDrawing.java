package starb.client;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import starb.server.Cell;
import starb.server.Puzzle;
import starb.server.Region;


//import java.awt.*;
import java.io.File;
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

    private Puzzle puzzle;
    private Point2D gridUpperLeft = new Point2D(15,15);
    private int[] rows = new int[NUM_SIDES];
    private int[] cols = new int[NUM_SIDES];
    public List<Cell> starred = new ArrayList<>();
    private Text solved;
    public StarbClientDrawing(Puzzle puzzle){
        this.puzzle = puzzle;
        canvas = new Canvas(width,length);
        try {
            starImage = new Image(STAR_IMAGE_FILE.toURI().toURL().toString());
        } catch(Exception e) {
            String message = "Unable to load image: " + STAR_IMAGE_FILE;
            System.err.println(message);
            System.err.println(e.getMessage());
            throw new RuntimeException(message);
        }
        solved = new Text("CONGRATULATIONS! You have solved this level");
        solved.setVisible(false);
        this.getChildren().addAll(canvas,solved);
        solved.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        canvas.setOnMouseClicked( e -> mouseClicked(e));
        solved.setFill(Color.GOLD);
        draw();
    }

    public  void draw(){
        GraphicsContext graph = canvas.getGraphicsContext2D();
        graph.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graph.setFill(Color.BLACK);


        // Draw grid
        graph.setLineWidth(1.0);
        graph.beginPath();
        for( int i = 0; i < NUM_SIDES + 1; i++ ) {
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
                gridUpperLeft.getX() + row * cellSize + 1,
                gridUpperLeft.getY() + col * cellSize + 1,
                cellSize - 3, cellSize - 3
        );
    }

    private void removeStar( int row, int col, GraphicsContext g ) {

        double x = gridUpperLeft.getX() + row * cellSize;
        System.out.println("remove: "+ x);
        double y = gridUpperLeft.getY() + col * cellSize;
        g.clearRect(x + 2, y + 2,cellSize - 4,cellSize - 4);
        //starred.remove(new Cell(row,col));
        puzzle.updateStarredRegion(new Cell(row,col), -1);
    }

    private boolean noNeighbor(int row, int col){
        int[][] adj = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {-1,-1}, {-1,1}, {1,-1}};
        for (int[] pair: adj) {
            if(starred.contains(new Cell(row + pair[0], col + pair[1]))) return false;
        }
        return true;
    }


//    public boolean validRegion(int col, int row) {
//        for(List<Cell> rg: puzzle.getRegions()){
//            Cell temp = new Cell(col, row);
//            if(rg.contains(temp)) {
//                if(rg.getStars() >= 2) return false;
//                else rg.incrementStars();
//            }
//        }
//        return true;
//    }



    private void mouseClicked(MouseEvent e) {

        int col = ((int)e.getX() - 15)/ 40;
        int row = ((int)e.getY() - 15)/ 40;
        System.out.println("Stars in " +col+ " COl: " + cols[col]);
        System.out.println("Stars in " +row+ " Row: " + rows[row] );
        if(rows[row] > 0 && cols[col] > 0){
            removeStar(col, row, canvas.getGraphicsContext2D());
            starred.remove(new Cell(row,col));
            cols[col]-=1;
            rows[row]-=1;
        } else {
            if(col < NUM_SIDES && row < NUM_SIDES){
                if(rows[row] < 2 && cols[col] < 2  && noNeighbor(row,col) && puzzle.updateStarredRegion(new Cell(row,col), 1)){ //&& validRegion(col,row)
                    if(starred.size() >= 19) solved();
                    drawStar(col, row, canvas.getGraphicsContext2D());
                    cols[col]+=1;
                    rows[row]+=1;
                    starred.add(new Cell(row,col));
                } else {
                    System.out.println("Invalid Placement");
                }
            }
        }
    }

    public void newLevel(Puzzle newP) {
        this.puzzle = newP;
        handleClear();
        draw();
    }

    public void solved(){
        solved.setVisible(true);
    }


    public void handleClear(){
        for(Cell c: starred){
            removeStar(c.getCol(), c.getRow(), canvas.getGraphicsContext2D());
        }
        starred.clear();
    }

}
