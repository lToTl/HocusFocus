module org.example.oop2024_rt2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires JIconExtractReloaded;
    requires com.sun.jna;


    opens HocusFocus to javafx.fxml;
    exports HocusFocus;
}