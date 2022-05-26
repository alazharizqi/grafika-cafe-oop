package com.example.grafikacafe.login;

import com.example.grafikacafe.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import com.example.grafikacafe.login.LoginModel;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class Login {

    LoginModel loginModel = new LoginModel();

    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    @FXML
    public Label status = new Label();

    @FXML
    public void login (ActionEvent event) throws IOException{
    Main change = new Main();
    change.changeScene("admin/AdminUser.fxml");
    }

    public void Userlogin(ActionEvent event) throws IOException {
        Main change = new Main();
        try {
            if(loginModel.isLogin(username.getText(), password.getText())) {
                if(loginModel.role(username.getText(), "Admin")) {
                    change.changeScene("admin/AdminUser.fxml");
                } else if (loginModel.role(username.getText(), "Manager")) {
                    change.changeScene("menu/Menu.fxml");
                } else {
                    change.changeScene("pos/Pos.fxml");
                }
            } else {
                status.setText("Wrong username or password");
            }
        } catch (SQLException e) {
            status.setText("error");
            System.out.println(e.getCause());
        }
    }

}
