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

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/**
 * Created by TSHM on 9-10-2015.
 */
public class SellerFirstRunFragment extends Fragment {

    View view;

    public SellerFirstRunFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.seller_fragment_firsttimeuse, container, false);
        return view;

    }
}