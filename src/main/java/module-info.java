module com.example.pingfront {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;


    opens com.app.ping to javafx.fxml;
    exports com.app.ping;
    exports com.app.ping.controller;
    opens com.app.ping.controller to javafx.fxml;
}