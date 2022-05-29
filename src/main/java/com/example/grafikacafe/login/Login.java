package com.example.grafikacafe.login;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import com.example.grafikacafe.session.Session;
import com.example.grafikacafe.session.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import com.example.grafikacafe.login.LoginModel;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login {

    LoginModel loginModel = new LoginModel();

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

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

    public void setLog() {
        Connection connection = SqiliteConnection.Connector();
        var session = Session.getSession();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String format = localDateTime.format(dateTimeFormatter);
        String query = "insert into tb_log (employee, activity, date) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, session.name);
            preparedStatement.setString(2, "Login");
            preparedStatement.setString(3, format);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public User session(String username) {
        connection = SqiliteConnection.Connector();
        String query = "select * from tb_user where username = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var user = resultSet.getString("username");
                var role = resultSet.getString("role");
                preparedStatement.close();
                resultSet.close();
                connection.close();
                return new User(user, role);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void Userlogin(ActionEvent event) throws IOException {
        Main change = new Main();
        try {
            if(loginModel.isLogin(username.getText(), password.getText())) {
                var session = this.session(username.getText());
                if(session.role.equals("Admin")) {
                    User data = new User(session.name, session.role);
                    Session.setSession(data);
                    setLog();
                    change.changeScene("admin/AdminUser.fxml");
                } else if (session.role.equals("Manager")) {
                    User data = new User(session.name, session.role);
                    Session.setSession(data);
                    setLog();
                    change.changeScene("menu/Menu.fxml");
                } else {
                    User data = new User(session.name, session.role);
                    Session.setSession(data);
                    setLog();
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
