package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

public class BuyerMainActivity extends AppCompatActivity {

    private ParseQueryAdapter mainAdapter;
    private CustomAdapterBuyer customAdapterBuyer;
    private GridView gridView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        storeDatabase();
                        fillList();

                    }
                }
        );

        fillList();


    }

    public void fillList() {
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

        swipeRefreshLayout.setRefreshing(false);
    }

    private void storeDatabase(){

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.findInBackground(new FindCallback<Product>() {
            public void done(List<Product> productList, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(productList);
                    Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}