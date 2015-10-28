package group6.kb_50.marketaid.Seller;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import group6.kb_50.marketaid.Product;
import group6.kb_50.marketaid.R;

/**
 * Created by TSHM on 9-10-2015.
 */

public class SellerAddProductFragment extends Fragment implements View.OnClickListener {
    final static String PREF_NAME = "Preferences";

    String toSave = "";
    View view;
    Bitmap imageBitmap = null;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_SEARCH_IMAGE_ACTIVITY_REQUEST_CODE = 100 + 1;
    //private static final int RESULT_OK = -1; //We can use view.RESULT_OK

    private static final int MAX_IMAGE_SIZE = 10485000;
    private Spinner category_spinner;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.seller_activity_add, container, false);

        final EditText inputnameTV = (EditText) view.findViewById(R.id.EnterProductTitleAdd);
        /* Start focus at first field */
        inputnameTV.requestFocus();
        inputnameTV.setSelection(inputnameTV.getText().length());

        final Spinner categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        categorySpinner.setFocusable(true);
        categorySpinner.setFocusableInTouchMode(true);

        final EditText inputdescriptionTV = (EditText) view.findViewById(R.id.AddProductDescriptionEdit);

        /* If the user pressed "Enter" or "Gereed", Go to next or first field */
        inputnameTV.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    categorySpinner.requestFocus();
                    categorySpinner.performClick();
                    return true;
                }
                return false;
            }
        });
        categorySpinner.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    inputdescriptionTV.requestFocus();
                    inputnameTV.setSelection(inputnameTV.getText().length());
                    return true;
                }
                return false;
            }
        });
        inputdescriptionTV.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //inputnameTV.requestFocus();
                    onClick(view.findViewById(R.id.AddButton));
                    return true;
                }
                return false;
            }
        });

        Button b = (Button) view.findViewById(R.id.AddImageButton);
        b.setOnClickListener(this);
        Button addbutton = (Button) view.findViewById(R.id.AddButton);
        addbutton.setOnClickListener(this);
        Button browseButton = (Button) view.findViewById(R.id.BrowseAddButton);
        browseButton.setOnClickListener(this);

        final ImageView imageView = (ImageView) view.findViewById(R.id.SampleImageAddView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: code here to show image in Full-screen mode
            }
        });
        setSpinnerContent(view);
        return view;
    }

    public void setSpinnerContent(View v){
        category_spinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        final View hulpjeView = v;
        switch (v.getId()) {
            case R.id.AddImageButton:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.BrowseAddButton:/* //Working section with standard interface
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), MEDIA_SEARCH_IMAGE_ACTIVITY_REQUEST_CODE);*/

                /* Working section with user's own gallery */
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, MEDIA_SEARCH_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.AddButton:
            /* "Are you sure you want to add <PRODUCT_TITLE>?"*/
                EditText inputnameTV = (EditText) view.findViewById(R.id.EnterProductTitleAdd);
                ImageView iv = (ImageView) view.findViewById(R.id.SampleImageAddView);
                Drawable drawable = (Drawable) iv.getDrawable();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (drawable != null){
                    builder.setIcon(drawable);
                }
                String message = ( getString(R.string.addToYourProductsPart1) + inputnameTV.getText().toString() + getString(R.string.addToYourProductsPart2) );
                    builder.setTitle(inputnameTV.getText().toString())
                            .setMessage( message )
                            .setPositiveButton(getString(R.string.addToYourProductsAdd), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AddProduct(hulpjeView);
                                }
                            })
                            .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            })
                            .create()
                            .show();
                break;
            default:
                // Default value here
                break;
        }
    }

    public void AddProduct(View v){
        EditText inputnameTV = (EditText) view.findViewById(R.id.EnterProductTitleAdd);
        //EditText categoryTV = (EditText) view.findViewById(R.id.EnterProductCategoryAdd);
        EditText inputdescriptionTV = (EditText) view.findViewById(R.id.AddProductDescriptionEdit);

        String inputcategory = category_spinner.getSelectedItem().toString();
        String inputname = inputnameTV.getText().toString();
        String inputdescription = inputdescriptionTV.getText().toString();

        
        final Product p = new Product();
        p.setName(inputname);
        p.setCategory(inputcategory);
        p.setDescription(inputdescription);
        p.setSeller(ParseUser.getCurrentUser());
        /* Check for a nullpointer. The app shuts down if the user hasn't added an image */
        if( imageBitmap != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = null;
            // TODO: ParseFile mag niet groter zijn dan 10 MB! (10.48)
            try {
                file = new ParseFile(image);
            }catch (IllegalArgumentException e){
                Log.e("ParseFile", "IllegalArgumentException: " + e.toString());
            }
            if(file == null){
                if( image.length > MAX_IMAGE_SIZE) {
                    Toast.makeText(getActivity(), getString(R.string.file_too_large), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            Toast.makeText(getActivity(),getString(R.string.adding_product),Toast.LENGTH_SHORT).show();
            p.setImage(file);
            p.saveInBackground();

            /* When a product is added, set a "shared" boolean to true so data will be maintained */
            SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("boolProductSaved", true);
            editor.apply();

            getActivity().finish();//TODO: should be changed to "finishing and go back to overview"
        }else{
            /* "Are you sure you don't want to add an image?"*/
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.noImageTitle)
                    .setMessage(getString(R.string.noImageText))
                    .setPositiveButton(getString(R.string.noImageContinue), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getActivity(),getString(R.string.adding_product),Toast.LENGTH_SHORT).show();
                            p.saveInBackground();

                            /* When a product is added, set a "shared" boolean to true so data will be maintained */
                            SharedPreferences settings = getActivity().getBaseContext().getSharedPreferences(PREF_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("boolProductSaved", true);
                            editor.apply();

                            getActivity().finish();//TODO: should be changed to "finishing and go back to overview"
                        }
                    })
                    .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data); //Always call overridden method first!
        try {
            if (data != null && resultCode == Activity.RESULT_OK) {

                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    ParseImageView iv = (ParseImageView) view.findViewById(R.id.SampleImageAddView);
                    iv.setImageBitmap(imageBitmap);
                    //Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                } else if (requestCode == MEDIA_SEARCH_IMAGE_ACTIVITY_REQUEST_CODE) {
                    /* //Working section with standard (documents) method. Leave for references.
                        Uri imageUri = data.getData();
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            ParseImageView iv = (ParseImageView)view.findViewById(R.id.SampleImageAddView);
                            iv.setImageBitmap(imageBitmap);
                        }catch (IOException e){
                            Log.e("IOexeption", "IOexeption when creating bitmap");
                        }
                        */

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in (Parse)ImageView after decoding the String
                    ParseImageView iv = (ParseImageView)view.findViewById(R.id.SampleImageAddView);
                    imageBitmap = (Bitmap) BitmapFactory
                            .decodeFile(imgDecodableString);
                    iv.setImageBitmap(imageBitmap);
                    /* We might want to rotate the image */
                    //iv.setRotation(90); //TODO: delete this rotation?
                    //Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                }
            } else {
                Log.e("BROWSE", "Data was null or Result was not RESULT_OK");
            }
        }catch (Exception e) {
            Log.e("BROWSE", "Exception: something went wrong. Hint: check Permission settings.");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        /* Save everything the user entered in outState. Mind that we do not need this dat when we use an @id source */
        EditText inputnameTV = (EditText) view.findViewById(R.id.EnterProductTitleAdd);
        EditText inputdescriptionTV = (EditText) view.findViewById(R.id.AddProductDescriptionEdit);
        outState.putString("inputnameTV", inputnameTV.getText().toString());
        outState.putString("inputdescriptionTV", inputdescriptionTV.getText().toString());

        /* Save the image to outState */
        ImageView iv = (ImageView) view.findViewById(R.id.SampleImageAddView);
        Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte=stream.toByteArray();
        outState.putByteArray("imageInByte", imageInByte);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null)
            return;
        EditText inputnameTV = (EditText) view.findViewById(R.id.EnterProductTitleAdd);
        EditText inputdescriptionTV = (EditText) view.findViewById(R.id.AddProductDescriptionEdit);

        /* Retrieve data from bundle */
        byte[] imageInByte = savedInstanceState.getByteArray("imageInByte");
        if( imageInByte != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(imageInByte);
            Drawable drw = Drawable.createFromStream(is, "srcName");
            ImageView iv = (ImageView) view.findViewById(R.id.SampleImageAddView);
            iv.setImageDrawable(drw);
        }else{
            Log.e("ImageInByte", "Error converting Image to Byte[]");
        }

        //inputnameTV.setText(savedInstanceState.getString("inputnameTV"));
        //inputdescriptionTV.setText(savedInstanceState.getString("inputdescriptionTV"));

    }

}
