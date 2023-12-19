package starb.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.scene.Scene;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import starb.server.Puzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Document("clients")
public class Client {

    @Id
    private String id;
    private List<String> solved;

    private String ip;


    @JsonCreator
    public Client(
            @JsonProperty("id") String id,
            @JsonProperty("ip") String ip,
            @JsonProperty("solved") List<String> solved) {
        this.id = id;
        this.solved = solved;
        this.ip = ip;
    }
    public Client(){

    }
    public Client(String IP, List<String> solved) {
        this.solved = solved;
        this.ip = IP;
    }

    public boolean addSolved(String puzId){
        return solved.add(puzId);
    }

    public String getId() {
        return id;
    }

    public List<String> getSolved() {
        return solved;
    }
    public String getIP() {return ip;}

    public void setIP(String IP) {this.ip = IP;}

    @JsonIgnore
    public Scene getScene()throws Exception{
        StarbClientScene sys = new StarbClientScene();
        sys.setStyle("-fx-background-color: #6e6a6a;");
        return new Scene(sys);
    }

}
