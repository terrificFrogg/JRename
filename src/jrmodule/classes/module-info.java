module jrmodule {
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;
    
    opens jr to javafx.fxml;
    
    exports jr;
}
