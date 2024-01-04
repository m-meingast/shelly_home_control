module shelly.home.control {
    requires com.google.gson;
    requires kotlinx.coroutines.core.jvm;
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.kordamp.bootstrapfx.core;

    opens network.response to com.google.gson;
    opens view to javafx.fxml;
    exports view;
}