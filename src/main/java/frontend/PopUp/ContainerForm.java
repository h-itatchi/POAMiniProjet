package frontend.PopUp;

import backend.ContainersFactory;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import jade.wrapper.AgentContainer;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ContainerForm extends JFXDialog {
    private ContainersFactory factory;

    public TextField containerName;
    private JFXDialogLayout layout;
    private AgentContainer container;

    public ContainerForm(StackPane dialogContainer, ContainersFactory factory) {
        this.factory = factory;
        containerName = new TextField();
        containerName.setPromptText("Container Name");
        layout = new JFXDialogLayout();
        layout.setHeading(new Text("ADD Container"));
        layout.setBody(containerName);
        Button add = new Button("ADD");
        add.setOnAction(e -> {
            container = createContainer();
            this.close();
        });
        layout.setActions(add);
        this.setContent(layout);
        this.setDialogContainer(dialogContainer);
        this.setTransitionType(DialogTransition.CENTER);
    }

    private AgentContainer createContainer() {
        return factory.createContainer(containerName.getText());
    }

    public AgentContainer getContainer() {
        return container;
    }
}
