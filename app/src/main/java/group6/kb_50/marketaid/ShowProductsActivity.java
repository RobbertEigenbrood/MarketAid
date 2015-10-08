package group6.kb_50.marketaid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;


public class ShowProductsActivity extends AppCompatActivity {

    private ArrayList<Product> products;
    private GridView gridView;
    private ParseQueryAdapter<ParseObject> mainAdapter;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);
        fillList();
    }

    public void fillList() {
        mainAdapter = new ParseQueryAdapter<ParseObject>(this,Product.class);
        mainAdapter.setTextKey("Name");
        mainAdapter.setImageKey("Image");


        customAdapter = new CustomAdapter(this);
        gridView = (GridView) findViewById(R.id.listView);
        gridView.setAdapter(customAdapter);
        mainAdapter.loadObjects();
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
