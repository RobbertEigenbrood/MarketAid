package group6.kb_50.marketaid.Buyer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import group6.kb_50.marketaid.Seller.CustomAdapterSeller;
import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

public class BuyerMainActivity extends AppCompatActivity {


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private ParseQueryAdapter mainAdapter;
    private CustomAdapterBuyer customAdapterBuyer;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);

        fillList();
    }

    public void fillList() {
        mainAdapter = new ParseQueryAdapter<ParseObject>(this, Product.class);
        mainAdapter.setTextKey("Name");
        mainAdapter.setImageKey("Image");

        customAdapterBuyer = new CustomAdapterBuyer(this);
        gridView = (GridView) findViewById(R.id.GridViewBuyer);
        gridView.setAdapter(customAdapterBuyer);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Product item = (Product) parent.getItemAtPosition(position);
                Intent intent = new Intent(BuyerMainActivity.this, BuyerProductActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.icon);
                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);
                intent.putExtra("ID", item.getID());
                startActivity(intent);

            }
        });

        mainAdapter.loadObjects();
    }


}