package group6.kb_50.marketaid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.parse.ParseObject;

public class AddProductActivity extends AppCompatActivity {
    private Uri fileUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

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
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView iv = (ImageView)findViewById(R.id.imageView);
                iv.setImageBitmap(imageBitmap);
                Toast.makeText(this,"Image Saved!",Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onClickAddProduct(View v){
        TextView inputnameTV = (TextView) findViewById(R.id.editText);
        TextView inputpriceTV = (TextView) findViewById(R.id.editText2);
        TextView inputdescriptionTV = (TextView) findViewById(R.id.editText3);

        String inputname = inputnameTV.getText().toString();
        String inputprice = inputpriceTV.getText().toString();
        String inputdescription = inputdescriptionTV.getText().toString();

        ParseObject p = new ParseObject("Products");
        p.put("Name",inputname);
        p.put("Price",inputprice);
        p.put("Description",inputdescription);
        p.saveInBackground();

        Toast.makeText(this,"Product Added!",Toast.LENGTH_SHORT);
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
