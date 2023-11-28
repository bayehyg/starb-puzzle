package starb.server;

import java.util.List;

public class User {


    private String userId;
    public List<Puzzle> solvedPuzzles;
    private List<String> titles;


    public User(String userId, List<Puzzle> solvedPuzzles, List<String> titles) {
        this.userId = userId;
        this.solvedPuzzles = solvedPuzzles;
        this.titles = titles;
    }

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
