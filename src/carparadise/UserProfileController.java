/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carparadise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sj alim
 */
public class UserProfileController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ObservableList<productListTableDatahandle> oblist = FXCollections.observableArrayList();

    @FXML
    private Label emailLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label useNamelabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private TableView<productListTableDatahandle> historyTable;

    @FXML
    private TableColumn<productListTableDatahandle, String> idCol;

    @FXML
    private TableColumn<productListTableDatahandle, ImageView> pictureCol;

    @FXML
    private TableColumn<productListTableDatahandle, String> brandCol;

    @FXML
    private TableColumn<productListTableDatahandle, String> ModelCol;

    @FXML
    private TableColumn<productListTableDatahandle, String> priceCol;

    @FXML
    private TextField selectBuyerTextField;

    @FXML
    private TextField changePasswordTextField;

    @FXML
    private Label userPageMessageLabel;


    @FXML
    void setPasswordHandleButton(ActionEvent event) {
        if (!changePasswordTextField.getText().equals("")) {
            String currentLoggedUser = "";
            try {
                File f1 = new File("in.txt");
                try (InputStream in = new FileInputStream(f1)) {
                    int c;
                    while ((c = in.read()) != -1) {
                        currentLoggedUser += (char) c;
                    }
                }
                try (Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/usersInfo.db")) {
                    String update = "UPDATE usersInfo SET signInPassword = ? WHERE signInUserName = ?";
                    PreparedStatement ps = con.prepareStatement(update);
                    ps.setString(1, changePasswordTextField.getText());
                    ps.setString(2, currentLoggedUser);
                    ps.executeUpdate();
                    con.close();
                    System.out.println("Password updated");
                }     
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void setUsernameHandleButton(ActionEvent event) {
        if (historyTable.getSelectionModel().getSelectedItem() != null) {
            String productId = historyTable.getSelectionModel().getSelectedItem().productId;
            if (!selectBuyerTextField.getText().equals("")) {
                try {
                    Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/usersInfo.db");
                    String insert = "UPDATE productList SET buyerUsername = ? WHERE productId = ?";
                    PreparedStatement ps = con.prepareStatement(insert);
                    ps.setString(1, selectBuyerTextField.getText());
                    ps.setString(2, productId);
                    ps.executeUpdate();
                    con.close();
                    System.out.println("Username updated");
                } catch (SQLException ex) {
                    Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                userPageMessageLabel.setText("**Enter Your Buyer's Username**");
            }
        } else {
            userPageMessageLabel.setText("**Select Your Car From Table First**");
        }

    }

    @FXML
    void closeHandleButton(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/homePage.fxml"));

            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        InputStream in = null;
        String recentName = "";
        try {

            File f2 = new File("in.txt");
            in = new FileInputStream(f2);
            int c;
            while ((c = in.read()) != -1) {
                recentName += (char) c;

            }
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

//                ResultSet rs = con.createStatement().executeQuery("select table if EXISTS * from "+recentName);
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet rs1 = dbm.getTables(null, null, recentName, null);
                if (rs1.next()) {
                    System.out.println("Table exists");

                    ResultSet rs = con.createStatement().executeQuery("select * from " + recentName);
                    while (rs.next()) {

                        try {
                            InputStream is = rs.getBinaryStream("image1");

                            OutputStream os = new FileOutputStream(new File("photo.jpg"));

                            byte[] content = new byte[1024];
                            int size = 0;
                            while ((size = is.read(content)) != -1) {
                                os.write(content, 0, size);
                            }

                            os.close();
                            is.close();
                            File tempPhotoFile = new File("photo.jpg");
                            String photo = tempPhotoFile.toURI().toURL().toString();

                            Image image = new Image(photo, 240, 120, false, false);

                            ImageView imgview = new ImageView(image);

                            oblist.add(new productListTableDatahandle(rs.getString("productId"), rs.getString("vehicleMakeField"), rs.getString("vehicleModelField"), rs.getString("sellerPriceField"), imgview));

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Table does not exist");
                    errorMessageLabel.setText("*You haven't posted yet");

                }

                String insertQuery = "Select * from usersInfo where signInUserName=" + "'" + recentName + "'";
                PreparedStatement pst = con.prepareStatement(insertQuery);
                ResultSet rs3 = pst.executeQuery();

                if (rs3.next()) {
                    useNamelabel.setText(recentName);
                    nameLabel.setText(rs3.getString("name"));
                    emailLabel.setText(rs3.getString("email"));
//                    phoneLabel.setText("phone");

                }
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            historyTable.setItems(oblist);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(UserProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
