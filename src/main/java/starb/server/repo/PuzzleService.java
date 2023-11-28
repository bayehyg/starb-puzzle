package starb.server.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starb.server.Puzzle;

import java.util.List;

@Service
public class PuzzleService {
   // private final List<Puzzle> puzzles;
    private PuzzleRepository repo;

//    public PuzzleService() {
//        try{
//        puzzles = repo.findAll();
//        } catch (NullPointerException n){
//            n.printStackTrace();
//        }
//        //this.repo = repo;
//    }

    public List<Puzzle> getAllPuzzles() {
        return repo.findAll();
    }

    public Puzzle getPuzzleBYLevel(int level){
        return repo.findPuzzleByLevel(level);
    }
}
