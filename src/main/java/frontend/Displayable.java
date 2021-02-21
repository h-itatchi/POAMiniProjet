package frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Displayable extends AnchorPane {
    private String fxmlPath;
    private Stage stage;
    private boolean loaded = false;

    public Displayable(String fxmlPath) {
        this.fxmlPath = fxmlPath;
        LoadFXML();
    }

    public Displayable() {
    }

    public void display(boolean init) {
        if (!loaded) {
            LoadFXML();
        }
        if (init) {
            init();
        }
        display();
    }

    public void display(){
        stage = new Stage();
        Scene scene = new Scene(this);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setOnHidden((e) -> {
            System.exit(0);
        });
        stage.show();
    }

    protected void LoadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            loaded = true;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    abstract void init();

    public void setFxmlPath(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
}
