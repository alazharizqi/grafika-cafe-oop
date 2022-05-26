package com.example.grafikacafe.pos;

import com.example.grafikacafe.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class Sales {

    @FXML
    public void logout (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("login/Login.fxml");
    }

    @FXML
    public void pos (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("pos/Pos.fxml");
    }

}
