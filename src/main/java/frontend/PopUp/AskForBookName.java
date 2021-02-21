package frontend.PopUp;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class AskForBookName extends JFXDialog {
    private TextField bookName;
    private JFXDialogLayout layout;
    private String book;

    public AskForBookName(StackPane dialogContainer){
        bookName = new TextField();
        bookName.setPromptText("Book Name");
        layout = new JFXDialogLayout();
        layout.setHeading(new Text("Buy Book"));
        layout.setBody(bookName);
        Button add = new Button("Buy");
        add.setOnAction(e -> {
            book = ""+bookName.getText();
            this.close();
        });
        layout.setActions(add);
        this.setContent(layout);
        this.setDialogContainer(dialogContainer);
        this.setTransitionType(DialogTransition.CENTER);
    }

    public String getBook() {
        return book;
    }
}
