/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carparadise;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author sj alim
 */
public class productListTableDatahandle {
    
    
    String productId,brand,model,price;
    ImageView image ;

    public productListTableDatahandle(String productId, String brand, String model, String price, ImageView image) {
        this.productId = productId;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public ImageView getImage() {
        return image;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    

     
    
    
}
