package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;


public class SellerEditProductActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Bitmap imageBitmap = null;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_edit_product);
        handleIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_product, menu);
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
                ParseImageView iv = (ParseImageView)findViewById(R.id.view2);
                iv.setImageBitmap(imageBitmap);
                Toast.makeText(this,"Image Saved!",Toast.LENGTH_SHORT).show();

            }
        }
    }


    public void handleIntent(){
            Intent i = getIntent();
            ID = i.getStringExtra("ID");

            final EditText nametv = (EditText) findViewById(R.id.EnterProductTitleEdit);
            final EditText descriptiontv = (EditText) findViewById(R.id.EditProductDescriptionEdit);
            final EditText categorytv = (EditText) findViewById(R.id.EnterProductCategoryEdit);
            final ParseImageView imageview = (ParseImageView) findViewById(R.id.view2);


            ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
            query.getInBackground(ID, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    String name = object.get("Name").toString();
                    String description = object.get("Description").toString();
                    String category = object.get("Category").toString();
                    ParseFile file = object.getParseFile("Image");

                    nametv.setText(name);
                    descriptiontv.setText(description);
                    categorytv.setText(category);
                    imageview.setParseFile(file);
                    imageview.loadInBackground();
                }
            });
    }


    public void onClickEditProduct(View v){
        ParseQuery<Product> query = ParseQuery.getQuery("Products");

        final EditText name_et = (EditText) findViewById(R.id.EnterProductTitleEdit);
        final EditText description_et = (EditText) findViewById(R.id.EditProductDescriptionEdit);
        final EditText category_et = (EditText) findViewById(R.id.EnterProductCategoryEdit);
        final ParseImageView imageview = (ParseImageView) findViewById(R.id.view2);

        final String inputname = name_et.getText().toString();
        final String inputcategory = category_et.getText().toString();
        final String inputdescription = description_et.getText().toString();

        // Retrieve the object by id
        query.getInBackground(ID, new GetCallback<Product>() {
            public void done(Product p, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    p.put("Name",inputname);
                    p.put("Category",inputcategory);
                    p.put("Description",inputdescription);

                    //p.setName(inputname);
                    //p.setCategory(inputcategory);
                    //p.setDescription(inputdescription);
                    if (imageBitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        ParseFile file = new ParseFile(image);
                        //p.setImage(file);
                        p.put("Image",file);
                    }

                    p.saveInBackground();
                    Toast.makeText(getBaseContext(), "Product Edited!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onClickRemoveProduct(View v){
        ParseQuery<Product> query = ParseQuery.getQuery("Products");
        query.getInBackground(ID, new GetCallback<Product>() {
            public void done(Product p, ParseException e) {
                if (e == null) {
                    p.deleteInBackground();
                    Toast.makeText(getBaseContext(),"Product deleted with title:" +  p.getName() + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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