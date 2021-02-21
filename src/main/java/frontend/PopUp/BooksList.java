package frontend.PopUp;

import backend.Agents.MyAgent;
import backend.Agents.Seller;
import backend.Book;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class BooksList extends JFXDialog {
    private JFXDialogLayout layout;
    private Seller agent;
    private StackPane dialogContainer;
    private ListView<Book> booksList;

    public BooksList(StackPane dialogContainer, Seller agent) {
        this.dialogContainer = dialogContainer;
        this.agent = agent;
        layout = new JFXDialogLayout();
        layout.setHeading(new Text("Books : " + agent.getLocalName()));
        booksList = getList();
        layout.setBody(getList());
        Button close = new Button("CLOSE");
        close.setOnAction(e -> {
            this.close();
        });
        layout.setActions(close);
        this.setContent(layout);
        this.setDialogContainer(dialogContainer);
        this.setTransitionType(DialogTransition.CENTER);
    }

    private ListView<Book> getList() {
        ListView<Book> list = new ListView<>();
        list.setItems(agent.getBooks());
        list.setContextMenu(getContextMenu());
        list.setCellFactory(param -> new ListCell<Book>(){
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                } else {
                    setText(item.getName()+" -- "+item.getPrice()+" DA -- (Q):"+item.getQuantity());
                }
            }
        });
        list.getSelectionModel().select(0);
        return list;
    }

    private ContextMenu getContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem add = new MenuItem("ADD Book");
        MenuItem delete = new MenuItem("Delete Book");
        add.setOnAction(e -> {
            addBook();
        });
        delete.setOnAction(e -> {
            int book = booksList.getSelectionModel().getSelectedIndex();
            System.out.println("Index = "+book);
            //this.agent.getBooks().remove(book);
        });
        menu.getItems().addAll(add,delete);
        return menu;
    }

    private void addBook() {
        BookForm form = new BookForm(dialogContainer);
        form.show();
        form.setOnDialogClosed(event -> {
            Book book = form.getBook();
            this.agent.getBooks().add(book);
        });
    }
}
