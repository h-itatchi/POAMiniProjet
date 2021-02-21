package frontend;

import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Logger {
    private static TextFlow loggingPanel;
    private static boolean initialized = false;

    public Logger(TextFlow loggingPanel) {
        Logger.loggingPanel = loggingPanel;
        loggingPanel.setLineSpacing(10.0);
        initialized = true;
    }

    public Logger() {

    }

    public void println(String text) {
        Platform.runLater(() -> {
            loggingPanel.getChildren().add(new Text(text + "\n"));
        });
    }

    public void print(String text) {
        Platform.runLater(() -> {
            loggingPanel.getChildren().add(new Text(text));
        });
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
