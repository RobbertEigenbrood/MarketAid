package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

public class BuyerMainActivity extends AppCompatActivity {

    private ParseQueryAdapter mainAdapter;
    private CustomAdapterBuyer customAdapterBuyer;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_activity_main);
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