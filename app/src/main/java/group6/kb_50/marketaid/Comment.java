package group6.kb_50.marketaid;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;


@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment(){
    }

    public String getID() {
        return getObjectId();
    }

    public String getComment() {
        return getString("Comment");
    }

    public void setComment(String comment) {
        put("Comment",comment);
    }

    public String getUser() {
        return getString("User");
    }

    public void setUser(String user) {
        put("User",user);
    }

    public ParseObject getProduct() {
        return getParseObject("Product");
    }

    public void setSeller(ParseObject product) {
        put("Product",product);
    }

}
