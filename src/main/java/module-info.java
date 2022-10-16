module gui.classroomgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens gui to javafx.fxml;
    exports gui;
    exports Outdated;
    opens Outdated to javafx.fxml;
}