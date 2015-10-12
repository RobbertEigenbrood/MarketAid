package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/**
 * Created by TSHM on 9-10-2015.
 */
public class SellerMainFragment extends Fragment {

    private ParseQueryAdapter mainAdapter;
    private CustomAdapterSeller customAdapterSeller;
    private GridView gridView;
    View view;

    public SellerMainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_seller_main, container, false);
        fillList();
        return view;

    }

    private void fillList() {
        mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(),Product.class);
        mainAdapter.setTextKey("Name");
        mainAdapter.setImageKey("Image");

        customAdapterSeller = new CustomAdapterSeller(getActivity());
        gridView = (GridView) view.findViewById(R.id.GridViewSeller);
        gridView.setAdapter(customAdapterSeller);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Product item = (Product) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), SellerEditProductActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.icon);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("ID", item.getID());
                //Start details activity
                startActivity(intent);
            }
        });

        mainAdapter.loadObjects();
    }
}
