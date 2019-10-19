/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carparadise;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Matcovic
 */
public class SignUpPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
//    
//    ObservableList<String> securityQuestionArray= FXCollections.observableList(
//    "What is your pet name?",
//            "Where is your hometown?",
//            "What is your CGPA?");
    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpUserName;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField signUpEmail;

    @FXML
    private ChoiceBox securityQuestion;

    @FXML
    private TextField securityQuestionAnswer;

    @FXML
    private PasswordField signUpConfirmPassword;

    @FXML
    void signUpCancleHandleButton(ActionEvent event) {
       

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/loginPage.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignUpPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

       

    }

    @FXML
    void signUpHandleButton(ActionEvent event) {
        try {

            String url = "jdbc:sqlite:DBs/usersInfo.db";
            Connection con = DriverManager.getConnection(url);
            Statement createTable = con.createStatement();
            String name = "usersInfo";
            createTable.execute("CREATE TABLE IF NOT EXISTS "
                    + "'"
                    + name
                    + "'"
                    + "(signInUserName text,"
                    + "name text,"
                    + "signInPassword text,"
                    + "email text,"
                    + "securityQuestion text,"
                    + "securityQuestionAnswer text)");

            if (!signUpUserName.getText().equals("") && !signUpName.getText().equals("") && !signUpEmail.getText().equals("") && !signUpConfirmPassword.getText().equals("") && !signUpPassword.getText().equals("") && !securityQuestionAnswer.getText().equals("")) {

                String idCheck = "Select signInUserName from usersInfo where signInUserName=? ";
                PreparedStatement pstQ = con.prepareStatement(idCheck);
                pstQ.setString(1, signUpUserName.getText());
                ResultSet rs = pstQ.executeQuery();
                if (rs.next()) {

                    JOptionPane.showMessageDialog(null, "Username already exists! Try another..");

                } else {
                    if (signUpConfirmPassword.getText().equals(signUpPassword.getText())) {

                        String query = "insert into usersInfo(signInUserName,name,signInPassword,email,securityQuestion,securityQuestionAnswer)values(?,?,?,?,?,?)";
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.setString(1, signUpUserName.getText());
                        pst.setString(2, signUpName.getText());
                        pst.setString(3, signUpPassword.getText());
                        pst.setString(4, signUpEmail.getText());
                        pst.setString(5, securityQuestion.getValue().toString());
                        pst.setString(6, securityQuestionAnswer.getText());
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Info saved successfully");
                    

                            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/loginPage.fxml"));
                            Parent root = (Parent) loader.load();
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                    } else {

                        JOptionPane.showMessageDialog(null, "Password doesn't match");
                    }
                }
            } else {

                JOptionPane.showMessageDialog(null, "Fill all the fields properly");
            }

            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        securityQuestion.setValue("security Question");
        securityQuestion.getItems().addAll("What is your pet name?",
                "Where is your hometown?",
                "What is your CGPA?");

    }

}
