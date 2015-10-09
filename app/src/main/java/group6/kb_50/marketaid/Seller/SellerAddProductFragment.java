package group6.kb_50.marketaid.Seller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group6.kb_50.marketaid.R;

/**
 * Created by TSHM on 9-10-2015.
 */
public class SellerAddProductFragment extends Fragment {
    public SellerAddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_seller_add, container, false);

    }
}