package com.cs646.ted.assignment3;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfessorListFragment extends ListFragment {

    //UNIT TEST REMOVE ME LATER
    private static final String DUMMY_JSON_ARRAY = "[{\"firstName\":\"Ted\",\"id\":43,\"lastName\":\"Kassen\"},{\"firstName\":\"Cameron\",\"id\":21,\"lastName\":\"Nouri\"},{\"firstName\":\"Marisa\",\"id\":306,\"lastName\":\"Till\"}]";

    private static final String PARAM_PROF_LIST = "paramproflist",
            STATE_POPULATED = "statepopulated",
            EXTRA_PROF_SELECTED = "extraprofselected",
            PACKAGE_NAME = "com.cs646.ted.assignment3",
            PROF_DETAIL_ACTIVITY =
                    "com.cs646.ted.assignment3.ProfessorDetailActivity";


    private String mListURL;
    private ProgressDialog mWaitDialog;
    private static JSONArray mProfessorArray;
    private ListView mListView;
    static boolean mPopulated = false;

//    private OnFragmentInteractionListener mListener;


    public static ProfessorListFragment newInstance(String listURL) {
        ProfessorListFragment fragment = new ProfessorListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_PROF_LIST, listURL);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfessorListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListURL = getArguments().getString(PARAM_PROF_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FetchItemsTask fetchItemsTask = new FetchItemsTask();
        fetchItemsTask.execute();

        if (!mPopulated) {
            mWaitDialog = ProgressDialog.show(getActivity(), getString(R.string.loading_title),
                    getString(R.string.please_wait));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent go = new Intent();
        go.setClassName(PACKAGE_NAME, PROF_DETAIL_ACTIVITY);

        try {
            int idOfSelectedProfessor =
                    ((Integer) ((JSONObject) mProfessorArray.get(position)).get("id"));
            go.putExtra(EXTRA_PROF_SELECTED, idOfSelectedProfessor);
            startActivity(go);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //Fetches JSONArray from URL
    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if (!mPopulated) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(mListURL);
                HttpResponse response;

                try {
                    response = httpClient.execute(httpGet);

                    Log.wtf("JSON", EntityUtils.toString(response.getEntity(), "UTF-8"));

                    mProfessorArray = new JSONArray(DUMMY_JSON_ARRAY);
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
            ArrayList<String> professorArrayList =
                    convertProfJSONArrayToProfArrayList(mProfessorArray);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    professorArrayList);
            setListAdapter(adapter);
        }
    }

    public ArrayList<String> convertProfJSONArrayToProfArrayList(JSONArray arr) {
        ArrayList<String> result = new ArrayList<String>();

        try {
            for (int i = 0; i < arr.length(); i++) {
                String newString = Integer.toString((Integer) (((JSONObject) arr.get(i)).get("id")));
                newString += ": " + ((JSONObject) arr.get(i)).get("firstName");
                newString += " " + ((JSONObject) arr.get(i)).get("lastName");
                result.add(newString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
