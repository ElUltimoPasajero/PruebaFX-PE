module com.example.pruebasjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;


    opens com.example.pruebasjavafx to javafx.fxml;
    exports com.example.pruebasjavafx;
    exports com.example.pruebasjavafx.models;
    opens com.example.pruebasjavafx.models to javafx.fxml;
    exports com.example.pruebasjavafx.controllers;
    opens com.example.pruebasjavafx.controllers to javafx.fxml;
}