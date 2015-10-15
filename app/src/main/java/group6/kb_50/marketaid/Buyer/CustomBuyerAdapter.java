package group6.kb_50.marketaid.Buyer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

import group6.kb_50.marketaid.Comment;
import group6.kb_50.marketaid.R;

/**
 * Created by robbe on 13-10-2015.
 */
public class CustomBuyerAdapter extends ParseQueryAdapter<ParseObject>{

    public CustomBuyerAdapter(final Context context, final List<ParseUser> users){
        super(context,new QueryFactory<ParseObject>() {
            public ParseQuery create(){
                ParseQuery query = new ParseQuery("Products");
                query.whereContainedIn("Seller", users);
                return query;
            }
        });


    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listview_layout, null);
        }

        super.getItemView(object, v, parent);

        // Add and download the image
        ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile imageFile = object.getParseFile("Image");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }
        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.text1);
        titleTextView.setText(object.getString("Name"));
        return v;
    }
}
