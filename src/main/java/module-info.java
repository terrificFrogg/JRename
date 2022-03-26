module com.sinaye.jrename {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires java.logging;


    opens com.sinaye.jrename to javafx.fxml;
    exports com.sinaye.jrename;
}