module com.example.grafikacafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.prefs;
    requires jasperreports;


    opens com.example.grafikacafe to javafx.fxml;
    exports com.example.grafikacafe;

    opens com.example.grafikacafe.admin to javafx.fxml;
    exports com.example.grafikacafe.admin;

    opens com.example.grafikacafe.connection to javafx.fxml;
    exports com.example.grafikacafe.connection;

    opens com.example.grafikacafe.menu to javafx.fxml;
    exports com.example.grafikacafe.menu;

    opens com.example.grafikacafe.pos to javafx.fxml;
    exports com.example.grafikacafe.pos;

    opens com.example.grafikacafe.login to javafx.fxml;
    exports com.example.grafikacafe.login;
}