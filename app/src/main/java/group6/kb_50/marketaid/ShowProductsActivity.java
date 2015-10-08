package group6.kb_50.marketaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;



public class ShowProductsActivity extends AppCompatActivity {
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Product item = (Product) parent.getItemAtPosition(position);

                Intent intent = new Intent(ShowProductsActivity.this, ShowProductsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.icon);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("ID", item.getID());
                Toast.makeText(getBaseContext(),item.getID(),Toast.LENGTH_SHORT).show();
                //Start details activity
                startActivity(intent);
            }
        });


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
