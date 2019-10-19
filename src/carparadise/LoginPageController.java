
package carparadise;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class LoginPageController implements Initializable {


    private String userFullname;
    public String recentname;
    @FXML
    private TextField signInUsername;

    @FXML
    private PasswordField signInPassword;

    @FXML
    void forgotPasswordHandleButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/ForgotPasswordPage.fxml"));

            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signInHandleButton(ActionEvent event) {
        recentname = signInUsername.getText();
        try {
            String url = "jdbc:sqlite:DBs/usersInfo.db";
            Connection con = DriverManager.getConnection(url);
            String sql = "Select signInUserName,signInPassword,name from usersInfo where signInUserName=? and signInPassword = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, signInUsername.getText());
            pst.setString(2, signInPassword.getText());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                userFullname = rs.getString("name");
                
                if (userFullname.contains(" "))
                    userFullname = userFullname.substring(0, userFullname.indexOf(" "));

                File f1 = new File("in.txt");
                String location = f1.getPath();
                Path filePath = Paths.get(location);
                byte[] b = recentname.getBytes();
                Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING);
                Files.write(filePath, b, StandardOpenOption.WRITE);
                
                      
                    File userFullNameFile = new File("userFullNameFile.txt");
                    String fileLocation = userFullNameFile.getPath();
                    Path userFullNameFilePath = Paths.get(fileLocation);
                    byte[] fileByte = userFullname.getBytes();
                    Files.newBufferedWriter(userFullNameFilePath, StandardOpenOption.TRUNCATE_EXISTING);
                    Files.write(userFullNameFilePath, fileByte, StandardOpenOption.WRITE);

                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/homePage.fxml"));

                    Parent root = (Parent) loader.load();

                    HomePageController second = loader.getController();


                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                }

            } else {
                JOptionPane.showMessageDialog(null, "Username and password not Correct");
                signInUsername.setText("");
                signInPassword.setText("");
            }
             con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    String name() {
        return this.recentname;
    }

    @FXML
    void signUpHandleButton(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/signUpPage.fxml"));

            Parent root = (Parent) loader.load();

            SignUpPageController second = loader.getController();

            //Stage stage= new Stage();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }

}
