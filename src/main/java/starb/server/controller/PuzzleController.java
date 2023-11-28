package starb.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import starb.server.Puzzle;
import starb.server.repo.PuzzleRepository;
import starb.server.repo.PuzzleService;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping(path="starb")
public class PuzzleController {

    //private PuzzleService ps;
    @Autowired
    private PuzzleRepository repo;

    @GetMapping("/")
    public Puzzle levelOne() {
        Optional<Puzzle> rtr = Optional.of(repo.findPuzzleByLevel(1));
        if(rtr.isPresent()) {
            return rtr.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public List<Puzzle> findAllPuzzles(){
        Optional<List<Puzzle>> rtr = Optional.of(repo.findAll());
        if(rtr.isPresent()) {
            return rtr.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
//
//    @GetMapping
//    public Puzzle findPuzzleByLevel(int level){
//        return ps.getPuzzleBYLevel(level);
//    }

    @GetMapping("{level}")
    public Puzzle getOnePuzzleByLevel( @PathVariable("level") int level ) {
        Optional<Puzzle> rtr = Optional.of(repo.findPuzzleByLevel(level));
        if(rtr.isPresent()) {
            return rtr.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
