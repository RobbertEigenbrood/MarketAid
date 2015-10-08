package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/* The Buyer product Detail Screen */

public class BuyerProductActivity extends AppCompatActivity {

    Intent imageIntent = new Intent();

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
        ImageView productImage = (ImageView) findViewById(R.id.ImageProductView);
//        final ParseImageView parseImageView = (ParseImageView) productImage;
        /* Place the image that the user clicked on */
        //productImage.setImageBitmap(bm);

        /* If the information is obtained from the server, hide the ProgressBar and show the TextViews */
        progressBar.setVisibility(View.GONE);
        //productTitle.setText(user.getUsername());
        productTitle.setVisibility(TextView.VISIBLE);
        //productDescription.setText(product.getDescription());
        productDescription.setVisibility(TextView.VISIBLE);
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
