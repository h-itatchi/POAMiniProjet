package frontend.PopUp;

import backend.Agents.MyAgent;
import backend.ContainersFactory;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class AgentForm extends JFXDialog {
    private ContainersFactory factory;
    private MyAgent agent;
    private TextField agentName;
    private ComboBox<String> type;
    private ComboBox<AgentContainer> containers;
    private TextField book;
    private JFXDialogLayout layout;

    public AgentForm(ContainersFactory factory) {
        this.factory = factory;
        initFields();
    }

    private void initFields() {
        agentName = new TextField();
        type = new ComboBox<>();
        containers = new ComboBox<>();
        book = new TextField();
        //
        agentName.setPromptText("Agent Name");
        containers.getItems().addAll(factory.getContainers());
        containers.setCellFactory(param -> new ListCell<AgentContainer>() {
            @Override
            protected void updateItem(AgentContainer item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty || item != null) {
                    try {
                        setText(item.getContainerName());
                    } catch (ControllerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        type.getItems().addAll(factory.seller, factory.buyer);
        book.setDisable(true);
    }

    public AgentForm(StackPane dialogContainer, ContainersFactory factory) {
        this(factory);
        layout = new JFXDialogLayout();
        layout.setHeading(new Text("Add new Agent"));
        FlowPane flow = new FlowPane();
        flow.setHgap(15.0);
        flow.setVgap(15.0);
        flow.getChildren().addAll(agentName, type, containers, book);
        layout.setBody(flow);
        Button add = new Button("ADD");
        add.setOnAction(e -> {
            this.agent = createAgent();
            this.close();
        });
        layout.setActions(add);
        this.setDialogContainer(dialogContainer);
        this.setContent(layout);
        this.setTransitionType(DialogTransition.CENTER);
    }

    private MyAgent createAgent() {
        AgentContainer container = containers.getValue();
        String type = this.type.getValue();
        String name = agentName.getText();
        String book = "XML" + this.book.getText();
        if (type.equals(factory.seller)) {
            return factory.createSeller(container, name, null);
        } else {
            return factory.createBuyer(container, name, book);
        }
    }

    public String getType() {
        return type.getValue();
    }

    public MyAgent getAgent() {
        return agent;
    }
}
