module it.unipi.dii.reviook_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires jaxb.api;

    opens it.unipi.dii.reviook_app to javafx.fxml;
    exports it.unipi.dii.reviook_app;
    exports it.unipi.dii.reviook_app.Controllers;
    opens it.unipi.dii.reviook_app.Controllers to javafx.fxml;
}