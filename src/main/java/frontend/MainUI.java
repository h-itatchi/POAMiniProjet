package frontend;

import backend.Agents.Buyer;
import backend.Agents.MyAgent;
import backend.Agents.Seller;
import backend.Book;
import backend.ContainersFactory;
import frontend.PopUp.*;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;

public class MainUI extends Displayable {
    // DATA
    private final ObservableList<AgentContainer> containers = FXCollections.observableArrayList();
    private final ObservableList<MyAgent> sellers = FXCollections.observableArrayList();
    private final ObservableList<MyAgent> buyers = FXCollections.observableArrayList();
    // Agents backend
    private final ContainersFactory factory;
    private  Logger logger;
    // UI CONTROLS

    public ListView<AgentContainer> containersList;
    public ListView<MyAgent> sellersList;
    public ListView<MyAgent> buyersList;
    public StackPane popPane;
    public TextFlow loggingPanel;
    public ScrollPane scroll;

    public MainUI() {
        this.setFxmlPath("/Main.fxml");
        factory = new ContainersFactory();
        containers.add(factory.getDefaultContainer());
    }

    public void init() {
        logger = new Logger(loggingPanel);
        scroll.vvalueProperty().addListener((observable, oldValue, newValue) -> scroll.setVvalue(1.0));
        containersList.setItems(containers);
        sellersList.setItems(sellers);
        buyersList.setItems(buyers);
        containersList.setCellFactory(param -> new ListCell<AgentContainer>() {
            @Override
            protected void updateItem(AgentContainer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                } else {
                    try {
                        setText("Container : " + item.getContainerName());
                    } catch (ControllerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sellersList.setCellFactory(param -> new ListCell<MyAgent>() {
            @Override
            protected void updateItem(MyAgent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                } else {
                    setText("Agent : " + item.getName());
                }
            }
        });
        buyersList.setCellFactory(param -> new ListCell<MyAgent>() {
            @Override
            protected void updateItem(MyAgent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                } else {
                    setText("Agent : " + item.getName());
                }
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem addbook = new MenuItem("Add Book");
        addbook.setOnAction(e -> {
            this.addBook((Seller) sellersList.getSelectionModel().getSelectedItem());
        });
        menu.getItems().add(addbook);
        sellersList.setContextMenu(menu);

        ContextMenu menu1 = new ContextMenu();
        MenuItem buybook = new MenuItem("Buy Book");
        buybook.setOnAction(e -> {
            this.buyBook((Buyer) buyersList.getSelectionModel().getSelectedItem());
        });
        menu1.getItems().add(buybook);
        buyersList.setContextMenu(menu1);

        sellersList.setOnMouseClicked(e->{
            if(e.getClickCount()==2){
                MyAgent agent = sellersList.getSelectionModel().getSelectedItem();
                BooksList booksList = new BooksList(popPane,(Seller)agent);
                booksList.show();
            }
        });
    }

    public void addContainer() {
        ContainerForm form = new ContainerForm(popPane, factory);
        form.show();
        form.setOnDialogClosed(event -> {
            AgentContainer container = form.getContainer();
            if (container != null) {
                containers.add(container);
                containersList.refresh();
                logger.println("Adding new Container");
            }
        });
    }

    public void addAgent() {
        AgentForm form = new AgentForm(popPane, factory);
        form.show();
        form.setOnDialogClosed(event -> {
            MyAgent agent = form.getAgent();
            if (agent != null) {
                if (form.getType().equals(factory.seller)) {
                    this.sellers.add(agent);
                } else {
                    this.buyers.add(agent);
                }
                logger.println("Adding new Agent");
                buyersList.refresh();
                sellersList.refresh();
            }
        });
    }

    public void addBook(Seller agent) {
        BookForm form = new BookForm(popPane);
        form.show();
        form.setOnDialogClosed(e -> {
            Book book = form.getBook();
            agent.addBook(book);
        });
    }

    public void buyBook(Buyer agent) {
        AskForBookName form = new AskForBookName(popPane);
        form.show();
        form.setOnDialogClosed(e -> {
            String name = form.getBook();
            agent.buyBook(name);
        });
    }

}
