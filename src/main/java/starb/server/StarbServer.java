package starb.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import starb.server.repo.PuzzleRepository;

import java.util.List;

@SpringBootApplication
public class StarbServer {

    private List<User> users;
    private List<Puzzle> puzzles;
    public static void main( String[] args ) {
        SpringApplication.run(StarbServer.class, args);
    }


}
