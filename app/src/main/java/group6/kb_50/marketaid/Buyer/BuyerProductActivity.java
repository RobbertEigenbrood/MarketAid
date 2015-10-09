package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

import group6.kb_50.marketaid.GPSWrapper;
import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/* The Buyer product Detail Screen */

public class BuyerProductActivity extends AppCompatActivity {
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_product);

        /* Display the ProgressBar */
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBuyerProduct);
        progressBar.setVisibility(View.VISIBLE);

        /* Get the image that the user clicked on (somehow) */
        //imageIntent = getIntent();

        /* Hide the TextViews (and show the progressbar) until data is received from server */
        final TextView productTitle = (TextView) findViewById(R.id.TitleProductText);
        productTitle.setVisibility(TextView.GONE);
        final TextView productDescription = (TextView) findViewById(R.id.DescriptionProductText);
        productDescription.setVisibility(TextView.GONE);
//        final ParseImageView parseImageView = (ParseImageView) productImage;
        /* Place the image that the user clicked on */
        //productImage.setImageBitmap(bm);

        /* If the information is obtained from the server, hide the ProgressBar and show the TextViews */
        progressBar.setVisibility(View.GONE);
        //productTitle.setText(user.getUsername());
        productTitle.setVisibility(TextView.VISIBLE);
        //productDescription.setText(product.getDescription());
        productDescription.setVisibility(TextView.VISIBLE);

        handleIntent();
    }


    public void handleIntent() {
        Intent i = getIntent();
        ID = i.getStringExtra("ID");

        final TextView nametv = (TextView) findViewById(R.id.TitleProductText);
        final TextView descriptiontv = (TextView) findViewById(R.id.DescriptionProductText);
        final ParseImageView imageview = (ParseImageView) findViewById(R.id.view);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                String name = object.get("Name").toString();
                String description = object.get("Description").toString();
                ParseFile file = object.getParseFile("Image");

                nametv.setText(name);
                descriptiontv.setText(description);
                imageview.setParseFile(file);
                imageview.loadInBackground();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buyer_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickFindLocation(View view) {
        /* Go to or show location of seller */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        // Retrieve the object by id
        final GPSWrapper mLocation = new GPSWrapper(this);
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            public void done(ParseObject p, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.

                    p.getParseUser("Seller").fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject seller, ParseException e) {
                            ParseGeoPoint sellerGeoPoint = (ParseGeoPoint) seller.get("LatLong");

                            double ownLat = mLocation.getCurrentLatDouble();
                            double ownLong = mLocation.getCurrentLongDouble();

                            double sellerLat = sellerGeoPoint.getLatitude();
                            double sellerLong = sellerGeoPoint.getLongitude();

                            Uri googlemapsUri = Uri.parse("http://maps.google.com/maps?saddr=" + ownLat + "," + ownLong + "&daddr=" + sellerLat + "," + sellerLong);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,googlemapsUri);
                            startActivity(intent);

                        }
                    });



                }
            }
        });


    }
}
