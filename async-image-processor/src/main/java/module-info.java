module com.image.imageprocessing {
    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    opens com.image.imageprocessing to javafx.graphics;
    exports com.image.imageprocessing;
}