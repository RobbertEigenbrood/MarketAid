package group6.kb_50.marketaid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class AddProductActivity extends AppCompatActivity {

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

    public void onClickAddProduct(View v){
        Firebase ref = new Firebase("https://market-aid.firebaseio.com/");

        TextView inputnameTV = (TextView) findViewById(R.id.editText);
        TextView inputpriceTV = (TextView) findViewById(R.id.editText2);
        TextView inputdescriptionTV = (TextView) findViewById(R.id.editText3);

        String inputname = inputnameTV.getText().toString();
        String inputprice = inputpriceTV.getText().toString();
        String inputdescription = inputdescriptionTV.getText().toString();

        ref.child("Products").child(inputname).child("price").setValue(inputprice);
        ref.child("Products").child(inputname).child("description").setValue(inputdescription);

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
