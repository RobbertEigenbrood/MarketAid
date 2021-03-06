package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

public class BuyerMainActivity extends AppCompatActivity {

    private SearchView searchView;
    private CustomSearchAdapter customSearchAdapter;
    private CustomCategoryAdapter customCategoryAdapter;
    private CustomBuyerAdapter customAdapterBuyer;
    private GridView gridView;
    private Spinner category_spinner;

    public int position;

    private static final String DEBUG_TAG = "Parse";

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_activity_main);
        /* Change the title of this screen */
        this.setTitle(getString(R.string.BuyerOverview));

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


        //TODO: GridView disappears when loading the first time
        setSearchViewListener();
        setSpinnerContent();
    }

    public void setSpinnerContent(){
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category_filter_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
        category_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v == category_spinner) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    findViewById(R.id.container).requestFocus();
                }
                return false;
            }
        });
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int pos, long id) {
                position = (int) pos;
                ParseQuery<ParseUser> pq = ParseUser.getQuery();
                pq.whereEqualTo("Present", true);
                pq.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        customCategoryAdapter = new CustomCategoryAdapter(getBaseContext(), objects, category_spinner.getItemAtPosition(pos).toString());
                        gridView.setAdapter(customCategoryAdapter);

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //another call

            }

        });
    }

    public void setSearchViewListener(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ParseQuery<ParseUser> pq = ParseUser.getQuery();
                pq.whereEqualTo("Present", true);
                pq.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        customSearchAdapter = new CustomSearchAdapter(getBaseContext(), objects, searchView, category_spinner.getItemAtPosition(position).toString());
                        gridView.setAdapter(customSearchAdapter);

                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ParseQuery<ParseUser> pq = ParseUser.getQuery();
                pq.whereEqualTo("Present", true);
                pq.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        customSearchAdapter = new CustomSearchAdapter(getBaseContext(), objects, searchView, category_spinner.getItemAtPosition(position).toString());
                        gridView.setAdapter(customSearchAdapter);

                    }
                });
                return true;
            }
        });

    }

    public void fillList() {

        gridView = (GridView) findViewById(R.id.GridViewBuyer);


        ParseQuery<ParseUser> pq = ParseUser.getQuery();
        pq.whereEqualTo("Present", true);
        pq.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                customAdapterBuyer = new CustomBuyerAdapter(getBaseContext(), objects);
                gridView.setAdapter(customAdapterBuyer);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Product item = (Product) parent.getItemAtPosition(position);
                Intent intent = new Intent(BuyerMainActivity.this, BuyerProductActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.icon);
                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);
                intent.putExtra("ID", item.getID());
                Bundle scaleBundle = ActivityOptionsCompat.makeScaleUpAnimation(
                        v, 0, 0, v.getWidth(), v.getHeight()).toBundle();

                if(Build.VERSION.SDK_INT >= 16) {
                    startActivity(intent, scaleBundle);
                }
                else {
                    startActivity(intent);
                }
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
                    //Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                    Log.d(DEBUG_TAG, "Refreshed");
                } else {

                }
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left_activity,R.anim.slide_out_to_right_activity);
    }
}