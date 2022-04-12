module com.example.ex1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    requires  org.apache.pdfbox;

    exports com.example.ex1;
    exports socialnetwork.domain;
    opens socialnetwork.domain to javafx.fxml;
    opens com.example.ex1 to javafx.fxml;


}