module it.unipi.dii.reviook_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.unipi.dii.reviook_app to javafx.fxml;
    exports it.unipi.dii.reviook_app;
    exports it.unipi.dii.reviook_app.Controllers;
    opens it.unipi.dii.reviook_app.Controllers to javafx.fxml;
}