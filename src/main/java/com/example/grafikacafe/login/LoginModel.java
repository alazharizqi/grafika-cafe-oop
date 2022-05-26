package com.example.grafikacafe.login;
import java.io.IOException;
import java.sql.*;
import com.example.grafikacafe.connection.SqiliteConnection;

public class LoginModel {

    Connection connection;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public boolean isLogin(String username, String password) throws SQLException {
        connection = SqiliteConnection.Connector();
        String query = "select * from tb_user where username = ? and password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            return false;
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    public boolean role(String username, String role) throws  SQLException {
        connection = SqiliteConnection.Connector();
        String query = "select * from tb_user where username = ? and role = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
            return false;
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

}
