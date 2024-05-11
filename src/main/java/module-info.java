module org.example.oop2024_rt2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens HocusFocus to javafx.fxml;
    exports HocusFocus;
}