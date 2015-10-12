package group6.kb_50.marketaid.Seller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/**
 * Created by TSHM on 9-10-2015.
 */

public class SellerAddProductFragment extends Fragment implements View.OnClickListener {

    View view;
    Bitmap imageBitmap = null;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int RESULT_OK = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_seller_add, container, false);

        Button b = (Button) view.findViewById(R.id.AddImageButton);
        b.setOnClickListener(this);
        Button addbutton =(Button) view.findViewById(R.id.AddButton);
        addbutton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddImageButton:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
                break;
            case R.id.AddButton:
                AddProduct(v);
                break;
        }
    }

    public void AddProduct(View v){
        EditText inputnameTV = (EditText) view.findViewById(R.id.EnterProductTitleAdd);
        EditText inputpriceTV = (EditText) view.findViewById(R.id.EnterProductCategoryAdd);
        EditText inputdescriptionTV = (EditText) view.findViewById(R.id.AddProductDescriptionEdit);

        String inputname = inputnameTV.getText().toString();
        String inputprice = inputpriceTV.getText().toString();
        String inputdescription = inputdescriptionTV.getText().toString();

        Product p = new Product();
        p.setName(inputname);
        p.setCategory(inputprice);
        p.setDescription(inputdescription);
        p.setSeller(ParseUser.getCurrentUser());
        /* Check for a nullpointer. The app shuts down if the user hasn't added an image */
        if( imageBitmap != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = new ParseFile(image);
            p.setImage(file);
        }

        p.saveInBackground();
        Toast.makeText(getActivity(),"Product Added!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ParseImageView iv = (ParseImageView)view.findViewById(R.id.SampleImageAddView);
                iv.setImageBitmap(imageBitmap);
                Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
