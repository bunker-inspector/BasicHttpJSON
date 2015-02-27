package com.cs646.ted.assignment3;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class ProfessorListFragment extends Fragment {

    private static final String PARAM_PROF_LIST = "paramproflist";

    private String mListURL;

//    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

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

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListURL != null){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(mListURL);
            HttpResponse response;

            try{
                response = httpClient.execute(httpGet);

                Log.wtf("JSON", EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professor, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
//        mListView.setOnItemClickListener(this);

        return view;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (null != mListener) {
//            // Notify the active callbacks interface (the activity, if the
//            // fragment is attached to one) that an item has been selected.
//        }
//    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

//    public interface OnFragmentInteractionListener {
//        public void onFragmentInteraction(String id);
//    }
}
