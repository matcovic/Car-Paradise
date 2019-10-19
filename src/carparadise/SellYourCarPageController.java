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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import carparadise.LoginPageController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Matcovic
 */
public class SellYourCarPageController implements Initializable {

    private String imagePath[];
    private URI uri[] = new URI[4];
    private int productId = 0;
    @FXML
    private TextField vehicleMakeField;

    @FXML
    private TextField vehicleModelField;

    @FXML
    private TextField modelYearField;

    @FXML
    private TextField vehicleColorField;

    @FXML
    private TextField vehicleBodyField;

    @FXML
    private TextField vehicleRegField;

    @FXML
    private TextField vehicleMilField;

    @FXML
    private TextField vehicleTansField;

    @FXML
    private TextField vehicleEngineField;

    @FXML
    private TextField vehicleFuelField;

    @FXML
    private TextField vehicleCylinderField;

    @FXML
    private TextField vehicleChasisField;

    @FXML
    private ImageView carImage1;

    @FXML
    private ImageView carImage3;

    @FXML
    private ImageView carImage2;

    @FXML
    private ImageView carImage4;

    @FXML
    private Button chooseImage;

    @FXML
    private TextField sellerNameField;

    @FXML
    private TextField sellerContactField;

    @FXML
    private TextField sellerPriceField;
    @FXML
    private CheckBox airConditionCheckbox;

    @FXML
    private CheckBox allWheelDriveCheckbox;

    @FXML
    private CheckBox powerLockDoorsCheckbox;

    @FXML
    private CheckBox aloyWheelCheckbox;

    @FXML
    private CheckBox remoteKeylessEntryCheckbox;

    @FXML
    private CheckBox navigationSytemCheckbox;

    @FXML
    private CheckBox fourWheelDriveCheckbox;

    @FXML
    private CheckBox tintedWindowsCheckbox;

    @FXML
    private CheckBox cdPlayerCheckbox;
    
    
    
     @FXML
    void backButton(ActionEvent event) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/homePage.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SellYourCarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public SellYourCarPageController() {
        this.imagePath = new String[4];
    }

    @FXML
    void chooseImageHandleButton(ActionEvent event) throws MalformedURLException {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png files", "*.png", "*.jpg"));

        List<File> seletedFiles = fc.showOpenMultipleDialog(null);

        if (seletedFiles.size() == 4) {
            int i;
            for (i = 0; i < 4; i++) {
                this.imagePath[i] = seletedFiles.get(i).toURI().toURL().toString();
                uri[i] = seletedFiles.get(i).toURI();

            }
            
            carImage1.setImage(new Image(this.imagePath[0],320,185,false,false));
            carImage2.setImage(new Image(this.imagePath[1],320,185,false,false));
            carImage3.setImage(new Image(this.imagePath[2],320,185,false,false));
            carImage4.setImage(new Image(this.imagePath[3],320,185,false,false));

        } else {
            JOptionPane.showMessageDialog(null, "You didn't choose the Pictures properly follow the instraction>>>>");
        }

    }

    @FXML
    void sellCarConfirmHandleButton(ActionEvent event) throws IOException {
        String recentName = "";
        String productIdString = "";

        try {
            File f2 = new File("in.txt");
            File f3 = new File("productCurrentId.txt");

            InputStream currentProductIdStreamIn = new FileInputStream(f3);
            InputStream in = new FileInputStream(f2);
            int c;

            while ((c = in.read()) != -1) {
                recentName += (char) c;

            }
            System.out.println(""+recentName);
                int c1;
               System.out.println("1st");
            while ((c1 = currentProductIdStreamIn.read()) != -1) {
                System.out.println("hel"+productIdString+"hel");
                productIdString += (char) c1;
            }
            
            productId = Integer.parseInt(productIdString);
            System.out.println(""+productId);
            productId++;

            String url = "jdbc:sqlite:DBs/usersInfo.db";
            Connection con = DriverManager.getConnection(url);
            Statement createTable = con.createStatement();
            String query;
            query = "CREATE TABLE IF NOT EXISTS "
                    + "'"
                    + recentName
                    + "'"
                    + "(productId text,sellerPriceField text,"
                    + "sellerContactField text,"
                    + "sellerNameField text,"
                    + "vehicleMakeField text,"
                    + "vehicleModelField text,"
                    + "modelYearField text,"
                    + "vehicleColorField text,"
                    + "vehicleBodyField text,"
                    + "vehicleRegField text,"
                    + "vehicleMilField text,"
                    + "vehicleTansField text,"
                    + "vehicleEngineField text,"
                    + "vehicleFuelField text,"
                    + "vehicleCylinderField text,"
                    + "vehicleChasisField text,"
                    + "allWheelDriveCheckbox text,"
                    + "airConditionCheckbox text,"
                    + "powerLockDoorsCheckbox text,"
                    + "aloyWheelCheckbox text,"
                    + "remoteKeylessEntryCheckbox text,"
                    + "navigationSytemCheckbox text,"
                    + "fourWheelDriveCheckbox text,"
                    + "tintedWindowsCheckbox text,"
                    + "cdPlayerCheckbox text,"
                    + "image1 blob,"
                    + "image2 blob,"
                    + " image3 blob,"
                    + " image4 blob)";

            createTable.execute(query);

            if (!vehicleMakeField.getText().equals("") && !vehicleBodyField.getText().equals("") && !vehicleChasisField.getText().equals("") && !vehicleColorField.getText().equals("") && !vehicleCylinderField.getText().equals("") && !vehicleEngineField.getText().equals("") && !vehicleFuelField.getText().equals("") && !vehicleMilField.getText().equals("") && !vehicleModelField.getText().equals("") && !vehicleRegField.getText().equals("") && !vehicleTansField.getText().equals("")) {

                String insertQuery = "insert into "
                        + recentName
                        + "(productId,sellerPriceField,"
                        + "sellerContactField,"
                        + "sellerNameField,"
                        + "vehicleMakeField,"
                        + "vehicleModelField,"
                        + "modelYearField,"
                        + "vehicleColorField,"
                        + "vehicleBodyField,"
                        + "vehicleRegField,"
                        + "vehicleMilField,"
                        + "vehicleTansField,"
                        + "vehicleEngineField,"
                        + "vehicleFuelField,"
                        + "vehicleCylinderField,"
                        + "vehicleChasisField,"
                        + "allWheelDriveCheckbox,"
                        + "airConditionCheckbox,"
                        + "powerLockDoorsCheckbox,"
                        + "aloyWheelCheckbox,"
                        + "remoteKeylessEntryCheckbox,"
                        + "navigationSytemCheckbox,"
                        + "fourWheelDriveCheckbox,"
                        + "tintedWindowsCheckbox,"
                        + "cdPlayerCheckbox,"
                        + "image1,"
                        + "image2,"
                        + "image3,"
                        + "image4)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement pst = con.prepareStatement(insertQuery);

                pst.setString(1, Integer.toString(productId));
                pst.setString(2, sellerPriceField.getText());
                pst.setString(3, sellerContactField.getText());
                pst.setString(4, sellerNameField.getText());
                pst.setString(5, vehicleMakeField.getText());
                pst.setString(6, vehicleModelField.getText());
                pst.setString(7, modelYearField.getText());
                pst.setString(8, vehicleColorField.getText());
                pst.setString(9, vehicleBodyField.getText());
                pst.setString(10, vehicleRegField.getText());
                pst.setString(11, vehicleMilField.getText());
                pst.setString(12, vehicleTansField.getText());
                pst.setString(13, vehicleEngineField.getText());
                pst.setString(14, vehicleFuelField.getText());
                pst.setString(15, vehicleCylinderField.getText());
                pst.setString(16, vehicleChasisField.getText());

                if (allWheelDriveCheckbox.isSelected()) {
                    pst.setString(17, "YES");
                } else {
                    pst.setString(17, "NO");
                }
                if (airConditionCheckbox.isSelected()) {
                    pst.setString(18, "YES");
                } else {
                    pst.setString(18, "NO");
                }
                if (powerLockDoorsCheckbox.isSelected()) {
                    pst.setString(19, "YES");
                } else {
                    pst.setString(19, "NO");
                }
                if (aloyWheelCheckbox.isSelected()) {
                    pst.setString(20, "YES");
                } else {
                    pst.setString(20, "NO");
                }
                if (remoteKeylessEntryCheckbox.isSelected()) {
                    pst.setString(21, "YES");
                } else {
                    pst.setString(21, "NO");
                }
                if (navigationSytemCheckbox.isSelected()) {
                    pst.setString(22, "YES");
                } else {
                    pst.setString(22, "NO");
                }
                if (fourWheelDriveCheckbox.isSelected()) {
                    pst.setString(23, "YES");
                } else {
                    pst.setString(23, "NO");
                }
                if (tintedWindowsCheckbox.isSelected()) {
                    pst.setString(24, "YES");
                } else {
                    pst.setString(24, "NO");
                }
                if (cdPlayerCheckbox.isSelected()) {
                    pst.setString(25, "YES");
                } else {
                    pst.setString(25, "NO");
                }
                for (int i = 0; i < 4; i++) {
                    File file = new File(uri[i]);
                    InputStream fis = new FileInputStream(file);
                    pst.setBinaryStream(26 + i, fis, (int) file.length());
                }
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Info saved successfully");

            } else {
                JOptionPane.showMessageDialog(null, "Fill all the fields properly");
            }

            String productListTableCreateQuery ="CREATE TABLE IF NOT EXISTS productList"
              
                    + "(productId text,"
                    + "sellerPriceField text,"
                    + "sellerContactField text,"
                    + "sellerNameField text,"
                    + "vehicleMakeField text,"
                    + "vehicleModelField text,"
                    + "modelYearField text,"
                    + "vehicleColorField text,"
                    + "vehicleBodyField text,"
                    + "vehicleRegField text,"
                    + "vehicleMilField text,"
                    + "vehicleTansField text,"
                    + "vehicleEngineField text,"
                    + "vehicleFuelField text,"
                    + "vehicleCylinderField text,"
                    + "vehicleChasisField text,"
                    + "allWheelDriveCheckbox text,"
                    + "airConditionCheckbox text,"
                    + "powerLockDoorsCheckbox text,"
                    + "aloyWheelCheckbox text,"
                    + "remoteKeylessEntryCheckbox text,"
                    + "navigationSytemCheckbox text,"
                    + "fourWheelDriveCheckbox text,"
                    + "tintedWindowsCheckbox text,"
                    + "cdPlayerCheckbox text,"
                    + "image1 blob,"
                    + "image2 blob,"
                    + " image3 blob,"
                    + " image4 blob,"
                    + "sellerUsername text,"
                    + "buyerUsername text)";
            createTable.execute(productListTableCreateQuery);

            String insertProductList = "insert into productList"
                     
                        + "(productId,sellerPriceField,"
                        + "sellerContactField,"
                        + "sellerNameField,"
                        + "vehicleMakeField,"
                        + "vehicleModelField,"
                        + "modelYearField,"
                        + "vehicleColorField,"
                        + "vehicleBodyField,"
                        + "vehicleRegField,"
                        + "vehicleMilField,"
                        + "vehicleTansField,"
                        + "vehicleEngineField,"
                        + "vehicleFuelField,"
                        + "vehicleCylinderField,"
                        + "vehicleChasisField,"
                        + "allWheelDriveCheckbox,"
                        + "airConditionCheckbox,"
                        + "powerLockDoorsCheckbox,"
                        + "aloyWheelCheckbox,"
                        + "remoteKeylessEntryCheckbox,"
                        + "navigationSytemCheckbox,"
                        + "fourWheelDriveCheckbox,"
                        + "tintedWindowsCheckbox,"
                        + "cdPlayerCheckbox,"
                        + "image1,"
                        + "image2,"
                        + "image3,"
                        + "image4,"
                        + "sellerUsername)"               
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                    "insert into productList(productId,vehicleMakeField,vehicleModelField,sellerPriceField,picture)values(?,?,?,?,?)";

            PreparedStatement pst1 = con.prepareStatement(insertProductList);
            
                pst1.setString(1, Integer.toString(productId));
                pst1.setString(2, sellerPriceField.getText());
                pst1.setString(3, sellerContactField.getText());
                pst1.setString(4, sellerNameField.getText());
                pst1.setString(5, vehicleMakeField.getText());
                pst1.setString(6, vehicleModelField.getText());
                pst1.setString(7, modelYearField.getText());
                pst1.setString(8, vehicleColorField.getText());
                pst1.setString(9, vehicleBodyField.getText());
                pst1.setString(10, vehicleRegField.getText());
                pst1.setString(11, vehicleMilField.getText());
                pst1.setString(12, vehicleTansField.getText());
                pst1.setString(13, vehicleEngineField.getText());
                pst1.setString(14, vehicleFuelField.getText());
                pst1.setString(15, vehicleCylinderField.getText());
                pst1.setString(16, vehicleChasisField.getText());

                if (allWheelDriveCheckbox.isSelected()) {
                    pst1.setString(17, "YES");
                } else {
                    pst1.setString(17, "NO");
                }
                if (airConditionCheckbox.isSelected()) {
                    pst1.setString(18, "YES");
                } else {
                    pst1.setString(18, "NO");
                }
                if (powerLockDoorsCheckbox.isSelected()) {
                    pst1.setString(19, "YES");
                } else {
                    pst1.setString(19, "NO");
                }
                if (aloyWheelCheckbox.isSelected()) {
                    pst1.setString(20, "YES");
                } else {
                    pst1.setString(20, "NO");
                }
                if (remoteKeylessEntryCheckbox.isSelected()) {
                    pst1.setString(21, "YES");
                } else {
                    pst1.setString(21, "NO");
                }
                if (navigationSytemCheckbox.isSelected()) {
                    pst1.setString(22, "YES");
                } else {
                    pst1.setString(22, "NO");
                }
                if (fourWheelDriveCheckbox.isSelected()) {
                    pst1.setString(23, "YES");
                } else {
                    pst1.setString(23, "NO");
                }
                if (tintedWindowsCheckbox.isSelected()) {
                    pst1.setString(24, "YES");
                } else {
                    pst1.setString(24, "NO");
                }
                if (cdPlayerCheckbox.isSelected()) {
                    pst1.setString(25, "YES");
                } else {
                    pst1.setString(25, "NO");
                }
                for (int i = 0; i < 4; i++) {
                    File file = new File(uri[i]);
                    InputStream fis = new FileInputStream(file);
                    pst1.setBinaryStream(26 + i, fis, (int) file.length());
                }
                pst1.setString(30,recentName);
//            pst1.setString(1, Integer.toString(productId));
//            pst1.setString(2, vehicleMakeField.getText());
//            pst1.setString(3, vehicleModelField.getText());
//            pst1.setString(4, sellerPriceField.getText());
//            File file = new File(uri[0]);
//            InputStream fis = new FileInputStream(file);
//            pst1.setBinaryStream(5, fis, (int) file.length());
            pst1.executeUpdate();
            
            String location = f3.getPath();
            Path filePath = Paths.get(location);
            String temp = Integer.toString(productId);
            byte[] b = temp.getBytes();
            Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(filePath, b, StandardOpenOption.WRITE);
            

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/homePage.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SellYourCarPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void initialize(URL url, ResourceBundle rb) {
        productId = 0;
    }

}