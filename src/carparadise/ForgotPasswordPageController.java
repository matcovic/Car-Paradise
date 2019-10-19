
package carparadise;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.MessagingException;

public class ForgotPasswordPageController implements Initializable {

    @FXML
    private TextField forgotPasswordField;
    @FXML
    private Label forgotPasswordErrorMessage;

    @FXML
    void forgotPasswordCancelHandleButton(ActionEvent event) {
        Stage stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void getPasswordHandleButton(ActionEvent event) throws IOException, MessagingException {

        String[] query = new String[2];
        try {
            String url = "jdbc:sqlite:DBs/usersInfo.db";
            Connection con = DriverManager.getConnection(url);
            String check = "SELECT signInUserName, email, signInPassword FROM usersInfo WHERE signInUserName=?";
            PreparedStatement ps = con.prepareStatement(check);

            ps.setString(1, forgotPasswordField.getText());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                query[0] = rs.getString("email");
                query[1] = rs.getString("signInPassword");

                SendingEmail.Send("carparadise1804", "xxxxx", query[0], "Password Recovery", "Your password: " + query[1]);

                forgotPasswordErrorMessage.setText("An email has been sent to " + query[0]);
            } else {

                forgotPasswordErrorMessage.setText("Username not found");

            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ForgotPasswordPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

}
