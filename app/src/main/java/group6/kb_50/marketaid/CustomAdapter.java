package group6.kb_50.marketaid;

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

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

	public CustomAdapter(Context context) {
		// Use the QueryFactory to construct a PQA that will only show
		// Todos marked as high-pri
		super(context, new QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseQuery query = new ParseQuery("Products");
				query.whereEqualTo("Seller", ParseUser.getCurrentUser());
				return query;
			}
		});
	}

	// Customize the layout by overriding getItemView
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
