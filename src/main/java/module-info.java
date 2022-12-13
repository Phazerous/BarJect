module com.barteam.barject {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.desktop;


    opens com.barteam.barject to javafx.fxml;
    exports com.barteam.barject;
}