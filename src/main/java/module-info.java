module com.sinaye.jrename {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sinaye.jrename to javafx.fxml;
    exports com.sinaye.jrename;
}