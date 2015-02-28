package com.cs646.ted.assignment3;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfessorDetailFragment extends Fragment {

    //unit test remove later
    private static final String DUMMY_JSON_OBJECT = "{\"id\":2,\"office\":\"GMCS407B\",\"phone\":\"619-594-6191\",\"email\":\"beck@cs.sdsu.edu\",\"rating\":{\"average\":5.0,\"totalRatings\":12},\"firstName\":\"Dr.Leland\",\"lastName\":\"Beck\"}";

    public static final String ARG_SEL_PROFESSOR_ID  = "idofprofessorselected",
                               ARG_PROFESSOR_GET_URL = "progfurl",
                               ARG_RATING_POST_URL   = "ratingpurl",
                               ARG_COMMENT_POST_URL  = "commentpurl";

    public static final String FIRST_NAME = "firstName",
                               LAST_NAME  = "lastName",
                               OFFICE     = "office",
                               EMAIL      = "email",
                               PHONE      = "phone",
                               RATING     = "rating",
                               AVERAGE    = "average",
                               TOTAL      = "totalRatings";


    private int mProfessorId;
    private String mProfessorURL, mRatingURL, mCommentURL;
    private boolean mPopulated;
    private ProgressDialog mWaitDialog;
    private JSONObject mProfessorObject;
    private TextView mLastName, mFirstName, mOffice, mPhone, mEmail, mAverageRating;
    private RatingBar mRating;

    public static ProfessorDetailFragment newInstance(int idOfProfSelected, String professorURL,
                                                      String ratingURL, String commentURL) {
        ProfessorDetailFragment fragment = new ProfessorDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SEL_PROFESSOR_ID, idOfProfSelected);
        args.putString(ARG_PROFESSOR_GET_URL, professorURL);
        args.putString(ARG_RATING_POST_URL, ratingURL);
        args.putString(ARG_COMMENT_POST_URL, commentURL);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfessorDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfessorId  = getArguments().getInt(ARG_SEL_PROFESSOR_ID);
            mProfessorURL = getArguments().getString(ARG_PROFESSOR_GET_URL);
            mRatingURL    = getArguments().getString(ARG_RATING_POST_URL);
            mCommentURL   = getArguments().getString(ARG_COMMENT_POST_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirstName      = (TextView)view.findViewById(R.id.first_name_value);
        mLastName       = (TextView)view.findViewById(R.id.last_name_value);
        mOffice         = (TextView)view.findViewById(R.id.office_value);
        mEmail          = (TextView)view.findViewById(R.id.email_value);
        mPhone          = (TextView)view.findViewById(R.id.phone_value);
        mAverageRating  = (TextView)view.findViewById(R.id.average_rating);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FetchProfessorDetails fetchProfessorDetails = new FetchProfessorDetails();
        fetchProfessorDetails.execute();

        if(!mPopulated) {
            mWaitDialog = ProgressDialog.show(getActivity(), getString(R.string.loading_title),
                    getString(R.string.please_wait));
        }
    }

    private class FetchProfessorDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
                if (!mPopulated) {
                    HttpClient httpClient = new DefaultHttpClient();

                    String specificURL = mProfessorURL + Integer.toString(mProfessorId);

                    HttpGet httpGet = new HttpGet(specificURL);
                    HttpResponse response;

                    try {
                        response = httpClient.execute(httpGet);

                        //Log.wtf("JSON", EntityUtils.toString(response.getEntity(), "UTF-8"));

                        mProfessorObject = new JSONObject(DUMMY_JSON_OBJECT);
                        mPopulated = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mWaitDialog.dismiss();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                placeValues(mProfessorObject);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        private void placeValues(JSONObject obj) throws JSONException{
            mFirstName.setText((String)mProfessorObject.get(FIRST_NAME));
            mLastName.setText((String)mProfessorObject.get(LAST_NAME));
            mOffice.setText((String)mProfessorObject.get(OFFICE));
            mPhone.setText((String)mProfessorObject.get(PHONE));
            mEmail.setText((String) mProfessorObject.get(EMAIL));

            Double average =(Double)((JSONObject)mProfessorObject.get(RATING)).get(AVERAGE);
            Integer total  = (Integer)((JSONObject)mProfessorObject.get(RATING)).get(TOTAL);

            mAverageRating.setText("Average " + Double.toString(average) + " of " +
                Integer.toString(total) + " ratings");
        }
    }
}
