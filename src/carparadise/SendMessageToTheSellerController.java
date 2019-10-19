package carparadise;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.MessagingException;

public class SendMessageToTheSellerController {

    @FXML
    private TextArea toSellerMessageTextArea;

    @FXML
    private TextField toSellerNameField;

    @FXML
    private TextField toSellerEmailField;

    @FXML
    private TextField toSellerContactNoField;

    @FXML
    private Label errorMessageLabel;

    private String senderUsername;

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    private String sellerName;
    private String sellerEmail;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    @FXML
    void toSellerSendHandleButton(ActionEvent event) throws MessagingException {
        if (!toSellerNameField.getText().equals("") && !toSellerEmailField.getText().equals("") && !toSellerContactNoField.getText().equals("") && !toSellerMessageTextArea.getText().equals("")) {

            String message = toSellerMessageTextArea.getText() + "\n\n"
                    + toSellerNameField.getText() + ",\n"
                    + "My Username: " + getSenderUsername() + "\n"
                    + "My Contact No: " + toSellerContactNoField.getText() + "\n\n"
                    + ">>>>>>>>>Click below to reply back<<<<<<<<<\n" + toSellerEmailField.getText();

            SendingEmail.Send("carparadise1804", "xxxxx", getSellerEmail(), "Message from " + getSenderUsername(), message);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } else {
            errorMessageLabel.setText("**Field all the fields properly**");
        }
    }

}
