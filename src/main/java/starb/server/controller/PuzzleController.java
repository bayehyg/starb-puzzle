package starb.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import starb.client.Client;
import starb.server.Puzzle;
import starb.server.repo.ClientRepository;
import starb.server.repo.PuzzleRepository;

import java.util.ArrayList;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping(path="starb")
public class PuzzleController {

    @Autowired
    private PuzzleRepository puzRepo;
    @Autowired
    private ClientRepository cliRepo;


    @PutMapping("/client/{puzzId}")
    public ResponseEntity<Client> addToSolved(@PathVariable String puzzId, HttpServletRequest request){

        try{
            Optional<Puzzle> opt = puzRepo.findById(puzzId);
            if(opt.isPresent()){
                Client cli = cliRepo.ip(request.getRemoteAddr());
                cli.addSolved(opt.get().getId());
                cliRepo.save(cli);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(cli);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/client")
    public ResponseEntity<Client> findOrCreate(HttpServletRequest request){
        try {
            Optional<Client> optionalClient = Optional.ofNullable(cliRepo.ip(request.getRemoteAddr()));

            if (optionalClient.isPresent()) {
                Client cli = optionalClient.get();
                cliRepo.save(cli);
                return ResponseEntity.status(HttpStatus.OK).body(cli);
            } else {
                List<String> solved = new ArrayList<>();
                Client newClient = new Client(request.getRemoteAddr(), solved);
                cliRepo.save(newClient);
                return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public List<Puzzle> findAllPuzzles(){
        Optional<List<Puzzle>> rtr = Optional.of(puzRepo.findAll());
        if(rtr.isPresent()) {
            return rtr.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{level}")
    public Puzzle getOnePuzzleByLevel( @PathVariable("level") int level ) {
        Optional<Puzzle> rtr = Optional.of(puzRepo.findPuzzleByLevel(level));
        if(rtr.isPresent()) {
            return rtr.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
