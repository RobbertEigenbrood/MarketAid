package group6.kb_50.marketaid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ShowProductsActivity extends AppCompatActivity {

    public ArrayList<String> products = new ArrayList<String>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);
        fillList();
    }

    public void fillList() {

        listView = (ListView) findViewById(R.id.listView);


        ParseQuery<ParseObject> pq = ParseQuery.getQuery("Products");
        pq.whereEqualTo("Seller", ParseUser.getCurrentUser());
        pq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    String toaststring = list.get(i).getString("Name");
                    String toaststringprijs = list.get(i).getString("Price");
                    String toaststringdescription = list.get(i).getString("Description");
                    String total = toaststring + " - " + toaststringprijs + " - " + toaststringdescription;
                    products.add(total);


                }
                ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,products);
                listView.setAdapter(list_adapter);
            }


        });



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
