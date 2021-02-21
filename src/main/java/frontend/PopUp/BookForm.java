package frontend.PopUp;

import backend.Book;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class BookForm extends JFXDialog {

    private Book book;

    public TextField name;
    public TextField price;
    public TextField quantity;
    private JFXDialogLayout layout;

    public BookForm(StackPane dialogContainer){
        super();
        name = new TextField();
        price = new TextField();
        quantity = new TextField();
        layout = new JFXDialogLayout();
        name.setPromptText("Name (Unique)");
        price.setPromptText("Price (Double)");
        quantity.setPromptText("Quantity (Integer)");
        FlowPane flow = new FlowPane();
        flow.setHgap(15.0);
        flow.setVgap(15.0);
        flow.getChildren().addAll(name,price,quantity);
        Button add = new Button("ADD");
        add.setOnAction(e -> {
            this.book = createBook();
            this.close();
        });
        layout.setHeading(new Text("Add new Book"));
        layout.setBody(flow);
        layout.setActions(add);
        this.setDialogContainer(dialogContainer);
        this.setContent(layout);
        this.setTransitionType(DialogTransition.CENTER);
    }

    public Book createBook(){
        String name = this.name.getText();
        Double price = Double.parseDouble(this.price.getText());
        int quantity = Integer.parseInt(this.quantity.getText());
        return new Book(name,price,quantity);
    }

    public Book getBook() {
        return book;
    }
}
