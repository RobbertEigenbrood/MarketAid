package group6.kb_50.marketaid.Buyer;

import android.os.Bundle;
import android.app.ListFragment;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class CommentFragment extends ListFragment {

    private CustomCommentAdapter customCommentAdapter;

    static String ID;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getString("ID");
        }
        getComments();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private void getComments()
    {ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            public void done(ParseObject product, ParseException e) {
                if (e == null) {
                    customCommentAdapter = new CustomCommentAdapter(getActivity(), product);
                    setListAdapter(customCommentAdapter);
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        getComments();
    }

}
