package starb.server.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import starb.server.Puzzle;

public interface PuzzleRepository extends MongoRepository<Puzzle,String> {
    Puzzle findPuzzleByLevel( int lev );
}
