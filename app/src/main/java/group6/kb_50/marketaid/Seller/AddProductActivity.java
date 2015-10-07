package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;


public class AddProductActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Bitmap imageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

    public void onClickAddImage(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ImageView iv = (ImageView)findViewById(R.id.imageView);
                iv.setImageBitmap(imageBitmap);
                Toast.makeText(this,"Image Saved!",Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onClickAddProduct(View v){
        TextView inputnameTV = (TextView) findViewById(R.id.EnterProductTitleEdit);
        TextView inputpriceTV = (TextView) findViewById(R.id.EnterProductCategoryEdit);
        TextView inputdescriptionTV = (TextView) findViewById(R.id.AddProductDescriptionEdit);

        String inputname = inputnameTV.getText().toString();
        String inputprice = inputpriceTV.getText().toString();
        String inputdescription = inputdescriptionTV.getText().toString();

        Product p = new Product();
        p.setName(inputname);
        p.setPrice(inputprice);
        p.setDescription(inputdescription);
        p.setSeller(ParseUser.getCurrentUser());
        /* Check for a nullpointer. The app shuts down if the user hasn't added an image */
        if( imageBitmap != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = new ParseFile(image);
            p.setImage(file);
        }

        p.saveInBackground();
        Toast.makeText(this,ParseUser.getCurrentUser().toString(),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Product Added!",Toast.LENGTH_SHORT).show();
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
}
