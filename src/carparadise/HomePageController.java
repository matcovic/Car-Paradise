package carparadise;

import javafx.scene.paint.Color;
import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Matcovic
 */
public class HomePageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    String welcomeLabelString = "";
    ObservableList<productListTableDatahandle> oblist = FXCollections.observableArrayList();
    ObservableList<String> listOfMake = FXCollections.observableArrayList("All");
    ObservableList<String> listOfModel = FXCollections.observableArrayList("All");

    String filterBrandName = "", filterModelName = "";

    public void setFilterBrandName(String filterBrandName) {
        this.filterBrandName = filterBrandName;
    }

    public void setFilterModelName(String filterModelName) {
        this.filterModelName = filterModelName;
    }

    public String getFilterBrandName() {
        return filterBrandName;
    }

    public String getFilterModelName() {
        return filterModelName;
    }

    @FXML
    private ComboBox makeComboBox;

    @FXML
    private ComboBox modelComboBox;

    @FXML
    private TextField lowerBoundPrice;

    @FXML
    private TextField upperBoundPrice;

    @FXML
    private TableView<productListTableDatahandle> mainTable;

    @FXML
    private TableColumn<productListTableDatahandle, String> idCol;

    @FXML
    private TableColumn<productListTableDatahandle, String> brandCol;

    @FXML
    private TableColumn<productListTableDatahandle, String> ModelCol;

    @FXML
    private TableColumn<productListTableDatahandle, String> priceCol;

    @FXML
    private TableColumn<productListTableDatahandle, ImageView> pictureCol;

    @FXML
    private Label errorMessage;

    @FXML
    private Label welcomeLabel;

    public void setWelcomeLabel(String welcomeLabel) {

        this.welcomeLabelString = welcomeLabel;

    }

    AnchorPane table;

    @FXML
    void makeComboBoxButtonAction(ActionEvent event) {

        String makeName = makeComboBox.getValue().toString();
        listOfModel.clear();
        if (makeName.equals("All")) {

            listOfModel.add("All");
            modelComboBox.setItems(listOfModel);
            this.setFilterBrandName(makeName);
        } else {
            this.setFilterBrandName(makeName);
            String connect = "jdbc:sqlite:DBs/usersInfo.db";

            try {
                Connection con = DriverManager.getConnection(connect);
                String modelListQueryString = "select vehicleModelField from productList where vehicleMakeField = " + "'" + makeName + "'";
                ResultSet rs = con.createStatement().executeQuery(modelListQueryString);

                while (rs.next()) {
                    String makeModel = rs.getString("vehicleModelField");

                    boolean dupcilateCheck = false;
                    for (String nameCheck : listOfModel) {
                        if (nameCheck.equals(makeModel)) {
                            dupcilateCheck = true;
                        }
                    }
                    if (!dupcilateCheck) {
                        listOfModel.add(makeModel);
                    }
                }
                modelComboBox.setItems(listOfModel);

                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    void modelComboBoxButtonAction(ActionEvent event) {

        String temp = modelComboBox.getValue().toString();
//        System.out.println("carparadise.HomePageController.modelComboBoxButtonAction()++++"+temp);
        if (!temp.equals("") && !temp.equals("All")) {

            this.setFilterModelName(temp);

        }

    }

    @FXML
    void profileHandleButton(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/userProfile.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void logOutHandleButton(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/loginPage.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

//    stage.close();
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void sellCarHandleButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/SellYourCarPage.fxml"));

            Parent root = (Parent) loader.load();

            SellYourCarPageController second = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void filterHandleButton(ActionEvent event) {
        boolean checkProperFiltering = true;
        if (filterBrandName.equals("All") && !lowerBoundPrice.getText().equals("") && !upperBoundPrice.getText().equals("")) {
            oblist.clear();
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                ResultSet rs = con.createStatement().executeQuery("select * from productList where "
                        + "CAST(sellerPriceField AS INT) > "
                        + "'"
                        + Integer.parseInt(lowerBoundPrice.getText())
                        + "'"
                        + "AND "
                        + "CAST(sellerPriceField AS INT) < "
                        + "'"
                        + Integer.parseInt(upperBoundPrice.getText())
                        + "'"
                );

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
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            mainTable.setItems(oblist);

        }
        else if(!filterBrandName.equals("") && !lowerBoundPrice.getText().equals("") && !upperBoundPrice.getText().equals(""))
        {
          oblist.clear();
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                ResultSet rs = con.createStatement().executeQuery("select * from productList where "
                          + "vehicleMakeField= "
                        + "'"
                        + filterBrandName
                        + "'"
                        +"AND "
                        + "CAST(sellerPriceField AS INT) > "
                        + "'"
                        + Integer.parseInt(lowerBoundPrice.getText())
                        + "'"
                        + "AND "
                        + "CAST(sellerPriceField AS INT) < "
                        + "'"
                        + Integer.parseInt(upperBoundPrice.getText())
                        + "'"
                );

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
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            mainTable.setItems(oblist);
        }
        
        else if (!filterBrandName.equals("") && !filterModelName.equals("") && !lowerBoundPrice.getText().equals("") && !upperBoundPrice.getText().equals("")) {
            System.out.println("carparadise.HomePageController.filterHandleButton()");
            oblist.clear();
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                ResultSet rs = con.createStatement().executeQuery("select * from productList where "
                        + "vehicleMakeField= "
                        + "'"
                        + filterBrandName
                        + "'"
                        + "AND "
                        + "vehicleModelField = "
                        + "'"
                        + filterModelName
                        + "'"
                        + "AND "
                        + "CAST(sellerPriceField AS INT) > "
                        + "'"
                        + Integer.parseInt(lowerBoundPrice.getText())
                        + "'"
                        + "AND "
                        + "CAST(sellerPriceField AS INT) < "
                        + "'"
                        + Integer.parseInt(upperBoundPrice.getText())
                        + "'"
                );

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
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            mainTable.setItems(oblist);

        } else if (!filterBrandName.equals("") && !filterModelName.equals("") && lowerBoundPrice.getText().equals("") && !upperBoundPrice.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "set LOWEST RANGE PROREPLY");
        } else if (!filterBrandName.equals("") && !filterModelName.equals("") && !lowerBoundPrice.getText().equals("") && upperBoundPrice.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "set HIGHEST RANGE PROREPLY");
        } else if (!filterBrandName.equals("") && !filterModelName.equals("")) {
            oblist.clear();
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                ResultSet rs = con.createStatement().executeQuery("select * from productList where "
                        + "vehicleMakeField= "
                        + "'"
                        + filterBrandName
                        + "'"
                        + "AND "
                        + "vehicleModelField = "
                        + "'"
                        + filterModelName
                        + "'"
                );

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
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }

            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            mainTable.setItems(oblist);
        } else if (!filterBrandName.equals("")) {
            oblist.clear();
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                ResultSet rs = con.createStatement().executeQuery("select * from productList where "
                        + "vehicleMakeField= "
                        + "'"
                        + filterBrandName
                        + "'"
                );

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
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            mainTable.setItems(oblist);
        } else {
            checkProperFiltering = false;
            JOptionPane.showMessageDialog(null, "Invalid Filtering.");
        }

        if (checkProperFiltering) {
            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                String makeListQueryString = "select vehicleMakeField from productList";
                ResultSet rs = con.createStatement().executeQuery(makeListQueryString);
                while (rs.next()) {
                    String makeName = rs.getString("vehicleMakeField");

                    boolean dupcilateCheck = false;
                    for (String nameCheck : listOfMake) {
                        if (nameCheck.equals(makeName)) {
                            dupcilateCheck = true;
                        }

                    }
                    if (!dupcilateCheck) {
                        listOfMake.add(makeName);
                    }
                }
                makeComboBox.setItems(listOfMake);
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    public void showDetailsHandleButton(ActionEvent event) throws IOException, SQLException {

        try {
            String tempId = mainTable.getSelectionModel().getSelectedItem().productId;
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("carparadise/ProductDescriptionPage.fxml"));
            Parent root = (Parent) loader.load();
            ProductDescriptionPageController productDescription = loader.getController();

            Connection con = DriverManager.getConnection("jdbc:sqlite:DBs/usersInfo.db");
            String query = "Select * from productList where productId=" + "'" + tempId + "'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            productDescription.tempId = tempId;

            productDescription.setSellerNameLabel(rs.getString("sellerNameField"));
            productDescription.setContactNumberLabel(rs.getString("sellerContactField"));
            productDescription.setVehicleMakeLabel(rs.getString("vehicleMakeField"));
            productDescription.setVehicleModelLabel(rs.getString("vehicleModelField"));
            productDescription.setVehicleModelYearLabel(rs.getString("modelYearField"));
            productDescription.setVehicleColorLabel(rs.getString("vehicleColorField"));
            productDescription.setVehicleRegLabel(rs.getString("vehicleRegField"));
            productDescription.setVehicleMilLabel(rs.getString("vehicleMilField"));
            productDescription.setVehicleTransLabel(rs.getString("vehicleTansField"));
            productDescription.setVehicleEngineLabel(rs.getString("vehicleEngineField"));
            productDescription.setVehicleFuelLabel(rs.getString("vehicleFuelField"));
            productDescription.setVehicleCylinderLabel(rs.getString("vehicleCylinderField"));
            productDescription.setVehicleChasisLabel(rs.getString("vehicleChasisField"));
            productDescription.setVehicleBodyLabel(rs.getString("vehicleBodyField"));
            productDescription.setVehiclePriceLabel(rs.getString("sellerPriceField"));
            productDescription.setAirConditionCheckImage(rs.getString("airConditionCheckbox"));
            productDescription.setAllWheelDriveCheckImage(rs.getString("allWheelDriveCheckbox"));
            productDescription.setAloyWheelCheckImage("aloyWheelCheckbox");
            productDescription.setCdCheckImage(rs.getString("cdPlayerCheckbox"));
            productDescription.setFourWheelDriveCheckImage(rs.getString("fourWheelDriveCheckbox"));
            productDescription.setNavigationSystemCheckImage(rs.getString("navigationSytemCheckbox"));
            productDescription.setPowerLockDoorsCheckImage(rs.getString("powerLockDoorsCheckbox"));
            productDescription.setRemoteKeylessCheckImage(rs.getString("remoteKeylessEntryCheckbox"));
            productDescription.setTintedWindowsCheckImage(rs.getString("tintedWindowsCheckbox"));

            try {
                for (int i = 0; i < 4; i++) {
                    InputStream is = rs.getBinaryStream("image" + (i + 1));

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

                    Image image = new Image(photo, 536, 300, false, false);

                    if (i == 0) {
                        productDescription.setCarDescriptionImage1(image);
                    } else if (i == 1) {
                        productDescription.setCarDescriptionImage2(image);
                    } else if (i == 2) {
                        productDescription.setCarDescriptionImage3(image);
                    } else {
                        productDescription.setCarDescriptionImage4(image);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }

            Connection connectToComment = DriverManager.getConnection("jdbc:sqlite:DBs/usersComment.db");
            DatabaseMetaData dbm = connectToComment.getMetaData();
            ResultSet rs1 = dbm.getTables(null, null, tempId, null);

            if (rs1.next()) {
                ResultSet rs2 = connectToComment.createStatement().executeQuery("select * from " + "'" + tempId + "'");
                File commentFile = new File("comment.txt");
                PrintWriter writer = new PrintWriter(commentFile);
                writer.print("");
                writer.close();
                while (rs2.next()) {

                    FileWriter fileWriter = new FileWriter(commentFile, true);
                    try (PrintWriter pw = new PrintWriter(fileWriter)) {
                        pw.print(rs2.getString("Username") + ": " + rs2.getString("Comment") + "\n\n");
                        pw.close();
                    }
                }

                try (BufferedReader br = new BufferedReader(new FileReader(commentFile))) {
                    String line, text = "";
                    while ((line = br.readLine()) != null) {
                        text = text + line + "\n";
                    }
                    productDescription.setCommentBoxArea(text);
                    br.close();
                }
//                connectToComment.close();

            } else {
                Statement createTable = connectToComment.createStatement();
                String queryComment;
                queryComment = "CREATE TABLE IF NOT EXISTS "
                        + "'"
                        + tempId
                        + "'"
                        + "(Username text,"
                        + " Comment text)";

                createTable.execute(queryComment);
//                connectToComment.close();
            }
            connectToComment.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputStream in = null;

        try {

            File file = new File("userFullNameFile.txt");
            in = new FileInputStream(file);
            int c;
            while ((c = in.read()) != -1) {
                this.welcomeLabelString += (char) c;
            }

//     this.welcomeLabel.setText(welcomeLabelString);
            this.welcomeLabelString = "Welcome, " + this.welcomeLabelString;
            System.out.println("carparadise.HomePageController.initialize() hell" + this.welcomeLabelString);
            welcomeLabel.setText(welcomeLabelString);

            try {

                String connect = "jdbc:sqlite:DBs/usersInfo.db";
                Connection con = DriverManager.getConnection(connect);

                ResultSet rs = con.createStatement().executeQuery("select * from productList");

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
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
            brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
            ModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            pictureCol.setCellValueFactory(new PropertyValueFactory<>("image"));
            mainTable.setItems(oblist);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {

            String connect = "jdbc:sqlite:DBs/usersInfo.db";
            Connection con = DriverManager.getConnection(connect);

            String makeListQueryString = "select vehicleMakeField from productList";
            ResultSet rs = con.createStatement().executeQuery(makeListQueryString);
            while (rs.next()) {
                String makeName = rs.getString("vehicleMakeField");

                boolean dupcilateCheck = false;
                for (String nameCheck : listOfMake) {
                    if (nameCheck.equals(makeName)) {
                        dupcilateCheck = true;
                    }
                }
                if (!dupcilateCheck) {
                    listOfMake.add(makeName);
                }
            }
            makeComboBox.setItems(listOfMake);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
