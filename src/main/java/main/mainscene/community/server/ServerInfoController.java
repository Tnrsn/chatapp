package main.mainscene.community.server;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ServerInfoController {

    @FXML
    private VBox serverMembersList;
    @FXML
    public void initialize() {
        System.out.println("Server Info Screen Loaded!");
    }
}