package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import group6.kb_50.marketaid.Comment;
import group6.kb_50.marketaid.R;

public class CommentActivity extends AppCompatActivity {

    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        EditText editCommentName = (EditText) findViewById(R.id.editCommentName);
        EditText editCommentText = (EditText) findViewById(R.id.editCommentText);
        try {
            editCommentName.setText(ParseUser.getCurrentUser().getUsername());
            editCommentText.requestFocus();
        }catch (Exception e){
            Log.e("Parse", "Error when writing current user to EditText (no user logged in?)");
        }

        Intent intent = getIntent();
        ID = intent.getStringExtra("Product");
    }

    public void onClickPost(View v) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        // Retrieve the object by id
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            public void done(ParseObject p, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.

                        EditText nameEditText = (EditText) findViewById(R.id.editCommentName);
                        EditText commentEditText = (EditText) findViewById(R.id.editCommentText);
                        if((nameEditText.length()!=0)&&(commentEditText.length()!=0)) {
                            Comment comment = new Comment();
                            comment.setUser(nameEditText.getText().toString());
                            comment.setComment(commentEditText.getText().toString());
                            comment.setSeller(p);
                            comment.saveInBackground();
                            Toast.makeText(getApplicationContext(), getString(R.string.comment_added), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    else{
                        Toast.makeText(getApplicationContext(), getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }



}
