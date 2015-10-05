package group6.kb_50.marketaid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import bolts.Task;

public class ShowProductsActivity extends AppCompatActivity {

    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);
        tv = (TextView) findViewById(R.id.textViewproducts);
    }



    public void getProducts(){
        ParseQuery<ParseObject> pq = ParseQuery.getQuery("Products");
        pq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                for(int i = 0; i < list.size();i++){
                    String toaststring = list.get(i).getString("Name");
                    String toaststringprijs = list.get(i).getString("Price");
                    String toaststringdescription = list.get(i).getString("Description");
                    Toast.makeText(getBaseContext(),toaststring + " - " + toaststringprijs + " - " + toaststringdescription,Toast.LENGTH_SHORT).show();
            }
        }

        });

    }

    public void onClickButton(View v){
        getProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_products, menu);
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
}
