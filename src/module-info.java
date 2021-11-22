module ooad.assignment {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens com.chinyangatl.github;
    opens com.chinyangatl.github.dataSource;
    opens com.chinyangatl.github.controller;
}