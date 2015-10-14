package group6.kb_50.marketaid.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import group6.kb_50.marketaid.Comment;
import group6.kb_50.marketaid.R;

public class CommentActivity extends AppCompatActivity {

    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

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
                            Toast.makeText(getApplicationContext(), "Comment added!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    else{
                        Toast.makeText(getApplicationContext(),"Please fill in all fields",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }



}
