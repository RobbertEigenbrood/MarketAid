package group6.kb_50.marketaid.Buyer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import group6.kb_50.marketaid.Comment;
import group6.kb_50.marketaid.R;

/**
 * Created by robbe on 13-10-2015.
 */
public class CustomCommentAdapter extends ParseQueryAdapter<Comment>{

    public CustomCommentAdapter(final Context context,final ParseObject product){
        super(context,new ParseQueryAdapter.QueryFactory<Comment>() {
            public ParseQuery<Comment> create(){

                ParseQuery query = new ParseQuery("Comment");
                query.whereEqualTo("Product",product);
                return query;
            }

        });


    }

    @Override
    public View getItemView(Comment comment, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_comments, null);
        }

        super.getItemView(comment, v, parent);


        TextView commentTextView = (TextView) v.findViewById(R.id.CommentText);
        commentTextView.setText(comment.getComment());
        TextView userTextView = (TextView) v.findViewById(R.id.CommentName);
        userTextView.setText(comment.getUser());
        TextView dateTextView = (TextView) v.findViewById(R.id.CommentDate);
        dateTextView.setText(comment.getCreatedAt().toString());
        return v;
    }
}
