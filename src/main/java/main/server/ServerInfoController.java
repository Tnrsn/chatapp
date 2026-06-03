package main.server; // Change this if your folder is named differently!

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ServerInfoController {

    // This links directly to the VBox in your FXML
    @FXML
    private VBox serverMembersList;

    // This method runs automatically the exact moment the screen loads
    @FXML
    public void initialize() {
        System.out.println("Server Info Screen Loaded!");
    }
    
    // You can add a button click method here later for the "Join Server" button
}