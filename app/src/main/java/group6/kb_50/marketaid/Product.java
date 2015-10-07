package group6.kb_50.marketaid;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Bas on 7-10-2015.
 */
@ParseClassName("Products")
public class Product extends ParseObject {
    private String name;
    private String price;
    private String description;
    private ParseFile image;
    private ParseUser Seller;

    public Product(){

    }
    public String getName() {
        return getString("Name");
    }

    public void setName(String name) {
        put("Name",name);
    }

    public String getPrice() {
        return getString("Price");
    }

    public void setPrice(String price) {
        put("Price",price);
    }

    public String getDescription() {
        return getString("Description");
    }

    public void setDescription(String description) {
        put("Description",description);
    }

    public ParseFile getImage() {
        return getParseFile("Image");
    }

    public void setImage(ParseFile image) {
        put("Image",image);
    }


    public ParseUser getSeller() {
        return getParseUser("Seller");
    }

    public void setSeller(ParseUser seller) {
        put("Seller",seller);
    }



}
