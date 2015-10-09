package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/* The Buyer product Detail Screen */

public class BuyerProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_product);

        /* Display the ProgressBar */
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBuyerProduct);
        progressBar.setVisibility(View.VISIBLE);

        /* Hide the TextViews (and show the progressbar) until data is received from server */
        TextView productTitle = (TextView) findViewById(R.id.TitleProductText);
        productTitle.setVisibility(TextView.GONE);
        TextView productDescription = (TextView) findViewById(R.id.DescriptionProductText);
        productDescription.setVisibility(TextView.GONE);

        /* Get the image and corresponding data that the user clicked on */
        //handleIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent();
    }

    public void handleIntent(){
        Intent i = getIntent();
        String ID = i.getStringExtra("ID");

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

                 /* If the information is obtained from the server, hide the ProgressBar and show the TextViews */
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarBuyerProduct);
                progressBar.setVisibility(View.GONE);
                TextView productTitle = (TextView) findViewById(R.id.TitleProductText);
                productTitle.setVisibility(TextView.VISIBLE);
                TextView productDescription = (TextView) findViewById(R.id.DescriptionProductText);
                productDescription.setVisibility(TextView.VISIBLE);
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

    public void onClickFindLocation(View view){
        /* Go to or show location of seller */
        ParseUser mUser = ParseUser.getCurrentUser();
    }
}