package starb.server;

import java.util.List;

public class Region {
    public Cell[][] cells;
    private int stars;


    public Region(Cell[][] cells) {
        this.cells = cells;
        stars = 0;
    }

    public void incrementStars(){
        stars = stars + 1;
    }

    public int getStars() {
        return stars;
    }

//    public boolean containsCell(Cell c){
//        return cells.contains(c);
//    }
}
