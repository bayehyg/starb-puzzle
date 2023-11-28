package starb.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.tokens.Token;
import starb.client.StarbClient;
import starb.client.StarbClientDrawing;
import starb.client.StarbClientScene;
import starb.server.Puzzle;
import starb.server.StarbServer;
import starb.server.bootstrap.PuzzleBootstrap;
import starb.server.repo.PuzzleRepository;
import starb.server.repo.PuzzleService;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PuzzleControllerTest {

    private  Puzzle item1;

    @Autowired
    private PuzzleRepository repo;

    @Autowired
    private PuzzleController controller;
    @BeforeEach
    void setUp() throws IOException {
        repo.deleteAll();
        ObjectMapper om = new ObjectMapper();
        item1 = om.readValue(new File("puzzles/testPuzzle.json"), Puzzle.class);
        repo.save(item1);
    }

    @Test
    public void getAll() {
        Puzzle allItems = controller.getOnePuzzleByLevel(item1.getLevel());
        assertNotEquals(allItems, item1);
    }

    @Test
    void getOnePuzzleByLevel() {
        Puzzle p = controller.getOnePuzzleByLevel(1);
        assertEquals(p.getRegions(), item1.getRegions());
        assertEquals(p.getLevel(), item1.getLevel());
        assertNotEquals(p.getLevel(), item1.getGridSize());
        assertEquals(p.getSolution(), item1.getSolution());
        assertEquals(1, p.getLevel());
    }


}