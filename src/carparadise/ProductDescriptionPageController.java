/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carparadise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author Matcovic
 */
public class ProductDescriptionPageController implements Initializable {
    
  

   Image yesPic ,noPic;
    @FXML
    private ImageView airConditionCheckImage;

    @FXML
    private ImageView powerLockDoorsCheckImage;

    @FXML
    private ImageView allWheelDriveCheckImage;

    @FXML
    private ImageView fourWheelDriveCheckImage;

    @FXML
    private ImageView cdCheckImage;

    @FXML
    private ImageView tintedWindowsCheckImage;

    @FXML
    private ImageView aloyWheelCheckImage;

    @FXML
    private ImageView navigationSystemCheckImage;

    @FXML
    private ImageView remoteKeylessCheckImage;


    @FXML
    private ImageView carDescriptionImage1;

    @FXML
    private ImageView carDescriptionImage2;

    @FXML
    private ImageView carDescriptionImage3;

    @FXML
    private ImageView carDescriptionImage4;

    @FXML
    private Label vehiclePriceLabel;

    @FXML
    private Label vehicleMakeLabel;

    @FXML
    private Label vehicleModelLabel;

    @FXML
    private Label vehicleModelYearLabel;

    @FXML
    private Label vehicleColorLabel;

    @FXML
    private Label vehicleBodyLabel;

    @FXML
    private Label vehicleRegLabel;

    @FXML
    private Label vehicleEngineLabel;

    @FXML
    private Label vehicleChasisLabel;

    @FXML
    private Label vehicleMilLabel;

    @FXML
    private Label vehicleTransLabel;

    @FXML
    private Label vehicleFuelLabel;

    @FXML
    private Label vehicleCylinderLabel;

    @FXML
    private Label sellerNameLabel;

    @FXML
    private Label sellContactNumLabel;

    @FXML
    private TextField offerPriceField;

    @FXML
    private TextField offerEmailField;

    @FXML
    private TextField offerContactNoField;

    @FXML
    private Label offerErrorLabel;

    @FXML
    private Label contactSellerErrorLabel;

    @FXML
    private TextArea commentBoxArea;

    @FXML
    private TextArea commentWritingArea;

    public String tempId;



    @FXML
    void confirmPurchaseHandleButton(ActionEvent event) throws SQLException {
       
        String currentLoggedUser = "", buyerUsername = "", sellerUsername = "";
            
            try {
                File f2 = new File("in.txt");
                InputStream in = new FileInputStream(f2);
                int c;
                while ((c = in.read()) != -1) {
                    currentLoggedUser += (char) c;       
                }
            try (Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/usersInfo.db")) {
                String check = "SELECT * FROM productList WHERE productID=?";
                PreparedStatement ps = con.prepareStatement(check);
                ps.setString(1, tempId);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    buyerUsername = rs.getString("buyerUsername");
                    sellerUsername = rs.getString("sellerUsername");
                }
                if(currentLoggedUser.equals(buyerUsername)){
                    
                    String deleteFromProductList = "DELETE FROM productList WHERE productID=?";
                    String deleteFromUserTable =  "DELETE FROM "+sellerUsername+" WHERE productID=?";
                    PreparedStatement psd = con.prepareStatement(deleteFromProductList);
                    psd.setString(1, tempId);
                    psd.executeUpdate();
                    PreparedStatement psd1 = con.prepareStatement(deleteFromUserTable);
                    psd1.setString(1, tempId);
                    psd1.executeUpdate();
                    contactSellerErrorLabel.setText("*Congratulation! You've purchased this Car*");            
                }
                else if(currentLoggedUser != buyerUsername || buyerUsername == null){
                    
                    contactSellerErrorLabel.setText("**Contact the seller first**");
                }
                con.close();
            }
            
                
            }catch(IOException ex){
                ex.printStackTrace();
            }

    }

    @FXML
    void offerSendHandleButton(ActionEvent event) throws IOException, SQLException, MessagingException {
        if (!offerPriceField.getText().equals("") && !offerEmailField.getText().equals("") && !offerContactNoField.getText().equals("")) {

            String currentLoggedUser = "";
            String sellerUsername = "", sellerEmail = "", sellerName = "";
            try {
                File f2 = new File("in.txt");
                InputStream in = new FileInputStream(f2);
                int c;
                while ((c = in.read()) != -1) {
                    currentLoggedUser += (char) c;
                }

                Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/usersInfo.db");
                String check = "SELECT * FROM productList WHERE productID=?";
                PreparedStatement ps = con.prepareStatement(check);
                ps.setString(1, tempId);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    sellerUsername = rs.getString("sellerUsername");
                }
                String query = "SELECT * FROM usersInfo WHERE signInUserName=?";
                PreparedStatement ps1 = con.prepareStatement(query);
                ps1.setString(1, sellerUsername);

                ResultSet rs1 = ps1.executeQuery();

                if (rs1.next()) {
                    sellerEmail = rs1.getString("email");
                    sellerName = rs1.getString("name");
                }

                String message = "Your Car's Information--->\n"
                        + "Brand: " + vehicleMakeLabel.getText() + "\nModel: " + vehicleModelLabel.getText() + "\nReg no: " + vehicleRegLabel.getText() + "\nChasis no: " + vehicleChasisLabel.getText() + "\n"
                        + "-----------------------------------------\n"
                        + "Buyer's Information--->\n"
                        + "Hi, " + sellerName + "\n"
                        + "I would like to bid your Car.\n"
                        + "My Bidded Price: BDT " + offerPriceField.getText() + "\n\n"
                        + "Buyer's Username: " + currentLoggedUser + "\n"
                        + "Buyer's Contact No: " + offerContactNoField.getText() + "\n"
                        + ">>>>>>>>>Click below to reply Buyer<<<<<<<<<\n" + offerEmailField.getText();

                SendingEmail.Send("carparadise1804", "xxxxx", sellerEmail, "Bidding", message);
                offerErrorLabel.setText("**Your bid has been placed!**");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            offerErrorLabel.setText("**Fill all the fields properly**");
        }
    }

    @FXML
    void sendMessageClickedButton(MouseEvent event) throws SQLException {

        //System.out.println("carparadise.ProductDescriptionPageController.sendMessageClickedButton()");
        String currentLoggedUser = "";
        String sellerUsername = "", sellerEmail = "", sellerName = "";
        try {
            File f2 = new File("in.txt");
            InputStream in = new FileInputStream(f2);
            int c;
            while ((c = in.read()) != -1) {
                currentLoggedUser += (char) c;
            }

            Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/usersInfo.db");
            String check = "SELECT * FROM productList WHERE productID=?";
            PreparedStatement ps = con.prepareStatement(check);
            ps.setString(1, tempId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sellerUsername = rs.getString("sellerUsername");
            }
            String query = "SELECT * FROM usersInfo WHERE signInUserName=?";
            PreparedStatement ps1 = con.prepareStatement(query);
            ps1.setString(1, sellerUsername);

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                sellerEmail = rs1.getString("email");
                sellerName = rs1.getString("name");
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/sendMessageToTheSeller.fxml"));
                Parent root = (Parent) loader.load();
                SendMessageToTheSellerController controller = loader.getController();
                controller.setSenderUsername(currentLoggedUser);
                controller.setSellerName(sellerName);
                controller.setSellerEmail(sellerEmail);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    void enterCommentHandleButton(ActionEvent event) throws FileNotFoundException, IOException, SQLException {

        String currentLoggedUser = "";
        File f2 = new File("in.txt");
        InputStream in = new FileInputStream(f2);
        int c;
        while ((c = in.read()) != -1) {
            currentLoggedUser += (char) c;
        }
        Connection connectToComment = DriverManager.getConnection("jdbc:sqlite:DBs/usersComment.db");
        String insertComment = "insert into "
                + "'" + tempId + "'"
                + "(Username,"
                + " Comment)"
                + "values(?,?)";
        PreparedStatement pst = connectToComment.prepareStatement(insertComment);
        pst.setString(1, currentLoggedUser);
        pst.setString(2, commentWritingArea.getText());
        pst.executeUpdate();

        File commentFile = new File("comment.txt");
        FileWriter fileWriter = new FileWriter(commentFile, true);
        try (PrintWriter pw = new PrintWriter(fileWriter)) {
            pw.print(currentLoggedUser + ": " + commentWritingArea.getText() + "\n\n");
            pw.close();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(commentFile))) {
            String line, text = "";
            while ((line = br.readLine()) != null) {
                text = text + line + "\n";
            }
            setCommentBoxArea(text);
            br.close();
        }
    }

    public void setContactNumberLabel(String contactNumber) {
        sellContactNumLabel.setText(contactNumber);
    }

    public void setSellerNameLabel(String sellerName) {
        sellerNameLabel.setText(sellerName);
    }

    public void setVehiclePriceLabel(String vehiclePrice) {
        vehiclePriceLabel.setText("BDT "+vehiclePrice);
    }

    public void setVehicleCylinderLabel(String vehicleCylinder) {
        vehicleCylinderLabel.setText(vehicleCylinder);
    }

    public void setVehicleFuelLabel(String vehicleFuel) {
        vehicleFuelLabel.setText(vehicleFuel);
    }

    public void setVehicleTransLabel(String vehicleTrans) {
        vehicleTransLabel.setText(vehicleTrans);
    }

    public void setVehicleMilLabel(String vehicleMil) {
        vehicleMilLabel.setText(vehicleMil);
    }

    public void setVehicleChasisLabel(String vehicleChasis) {
        vehicleChasisLabel.setText(vehicleChasis);
    }

    public void setVehicleEngineLabel(String vehicleEngine) {
        vehicleEngineLabel.setText(vehicleEngine);
    }

    public void setVehicleRegLabel(String vehicleReg) {
        vehicleRegLabel.setText(vehicleReg);
    }

    public void setVehicleBodyLabel(String vehicleBody) {
        vehicleBodyLabel.setText(vehicleBody);
    }

    public void setVehicleColorLabel(String vehicleColor) {
        vehicleColorLabel.setText(vehicleColor);
    }

    public void setVehicleModelYearLabel(String vehicleModelYear) {
        vehicleModelYearLabel.setText(vehicleModelYear);
    }

    public void setVehicleModelLabel(String vehicleModel) {
        vehicleModelLabel.setText(vehicleModel);
    }

    public void setVehicleMakeLabel(String vehicleMake) {
        vehicleMakeLabel.setText(vehicleMake);
    }

    public void setCarDescriptionImage1(Image image) {
        carDescriptionImage1.setImage(image);
    }

    public void setCarDescriptionImage2(Image image) {
        carDescriptionImage2.setImage(image);
    }

    public void setCarDescriptionImage3(Image image) {
        carDescriptionImage3.setImage(image);
    }

    public void setCarDescriptionImage4(Image image) {
        carDescriptionImage4.setImage(image);
    }

    public void setCommentBoxArea(String text) {
        commentBoxArea.setText(text);
    }

    public void setAirConditionCheckImage(String airConditionCheckImage) {
        if(airConditionCheckImage.equals("YES"))
        {
            
            this.airConditionCheckImage.setImage(yesPic);
        }
        else
        {
               this.airConditionCheckImage.setImage(noPic);
        }
        
    }

    public void setPowerLockDoorsCheckImage(String powerLockDoorsCheckImage) {
          if(powerLockDoorsCheckImage.equals("YES"))
        {
            
            this.powerLockDoorsCheckImage.setImage(yesPic);
        }
        else
        {
               this.powerLockDoorsCheckImage.setImage(noPic);
        }
        
    }

    public void setAllWheelDriveCheckImage(String allWheelDriveCheckImage) {
         if(allWheelDriveCheckImage.equals("YES"))
        {
            
            this.allWheelDriveCheckImage.setImage(yesPic);
        }
        else
        {
               this.allWheelDriveCheckImage.setImage(noPic);
        }
        
    }

    public void setFourWheelDriveCheckImage(String fourWheelDriveCheckImage) {
         if(fourWheelDriveCheckImage.equals("YES"))
        {
            
            this.fourWheelDriveCheckImage.setImage(yesPic);
        }
        else
        {
               this.fourWheelDriveCheckImage.setImage(noPic);
        }
        
    }

    public void setCdCheckImage(String cdCheckImage) {
        if(cdCheckImage.equals("YES"))
        {
            
            this.cdCheckImage.setImage(yesPic);
        }
        else
        {
               this.cdCheckImage.setImage(noPic);
        }
        
    }

    public void setTintedWindowsCheckImage(String tintedWindowsCheckImage) {
        if(tintedWindowsCheckImage.equals("YES"))
        {
            
            this.tintedWindowsCheckImage.setImage(yesPic);
        }
        else
        {
               this.tintedWindowsCheckImage.setImage(noPic);
        }
        
    }

    public void setAloyWheelCheckImage(String aloyWheelCheckImage) {
         if(aloyWheelCheckImage.equals("YES"))
        {
            
            this.aloyWheelCheckImage.setImage(yesPic);
        }
        else
        {
               this.aloyWheelCheckImage.setImage(noPic);
        }
        
    }

    public void setNavigationSystemCheckImage(String navigationSystemCheckImage) {
        if(navigationSystemCheckImage.equals("YES"))
        {
            
            this.navigationSystemCheckImage.setImage(yesPic);
        }
        else
        {
               this.navigationSystemCheckImage.setImage(noPic);
        }
        
    }

    public void setRemoteKeylessCheckImage(String remoteKeylessCheckImage) {
        if(remoteKeylessCheckImage.equals("YES"))
        {
            
            this.remoteKeylessCheckImage.setImage(yesPic);
        }
        else
        {
               this.remoteKeylessCheckImage.setImage(noPic);
        }
        
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {
            InputStream yes = new FileInputStream(new File("yesImage.png"));
               yesPic = new Image(yes);
              
        InputStream no = new FileInputStream(new File("noImage.png"));
               noPic = new Image(no);
              
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProductDescriptionPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
